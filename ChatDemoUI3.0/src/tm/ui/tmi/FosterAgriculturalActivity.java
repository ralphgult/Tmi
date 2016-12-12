package tm.ui.tmi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.LoginActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.manager.PersonManager;
import tm.ui.mine.CompCenterActivity;
import tm.utils.ConstantsHandler;
import tm.utils.SysUtils;

/**
 * Created by Lking on 2016/12/12.
 */

public class FosterAgriculturalActivity extends Activity {
    private ImageView mBack;
    private TextView mTop;
    private TextView mName;
    private TextView mNumber;
    private TextView mIntegral;
    private TextView mDetail;
    private ImageView mImg01;
    private ImageView mImg02;
    private ImageView mImg03;
    private ImageView mImg04;
    private int position;//上传的第几个
    private String userId;//用户ID
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    break;
                case 1001:
                    break;
                case 2001:
                    break;
                case 2002:
                    break;
                case 3001:
                    String json = msg.obj.toString();
                    try{
                        JSONObject jsonObject = new JSONObject(json);
                        String top = jsonObject.getString("top");
                        mTop.setText(top);
                        String name = jsonObject.getString("goodsName");
                        mName.setText(name);
                        String goodsCount = jsonObject.getString("goodsCount");
                        mNumber.setText(goodsCount+"盒");
                        String integral = jsonObject.getString("integral");
                        mIntegral.setText(integral+"积分");
                        String introduce = jsonObject.getString("introduce");
                        mDetail.setText(introduce);

                        JSONArray jsonArray = jsonObject.getJSONArray("sfi");
                        String []strimgs = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject mark = (JSONObject)jsonArray.get(i);
                            strimgs[i] = mark.getString("imgUrl");
                        }
                        mImg01.setImageBitmap(returnBitMap(strimgs[0]));
                        mImg02.setImageBitmap(returnBitMap(strimgs[1]));
                        mImg03.setImageBitmap(returnBitMap(strimgs[2]));
                        mImg04.setImageBitmap(returnBitMap(strimgs[3]));
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    Log.e("LKING","json="+json);
                    System.out.print(json);
                    break;
                case 4001:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_agricultural);
        //获取9宫格传递过来的数据
        getIntentInfo();
        initView();
        new Thread(){
            @Override
            public void run() {
                //根据不同的position加载不同数据
                PersonManager.sendFosterInfo(position,mHandler);
            }
        }.start();
    }

    private void getIntentInfo(){
        if(null != getIntent().getExtras()){
            position=getIntent().getExtras().getInt("position");
        }
    }

    private void initView(){
        mBack = (ImageView)findViewById(R.id.foster_back_iv);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTop = (TextView)findViewById(R.id.foster_rebate);
        mName = (TextView)findViewById(R.id.foster_title_name);
        mNumber = (TextView)findViewById(R.id.foster_title_number);
        mIntegral = (TextView)findViewById(R.id.foster_title_integral);
        mDetail = (TextView)findViewById(R.id.foster_details);
        mImg01 = (ImageView)findViewById(R.id.foster_img_01);
        mImg02 = (ImageView)findViewById(R.id.foster_img_02);
        mImg03 = (ImageView)findViewById(R.id.foster_img_03);
        mImg04 = (ImageView)findViewById(R.id.foster_img_04);
    }

    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
