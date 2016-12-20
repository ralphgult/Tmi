package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xbh.tmi.R;
import org.json.JSONArray;
import org.json.JSONObject;
import tm.manager.PersonManager;
import tm.ui.mine.HeadBigActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;

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
    private ImageLoaders imageLoaders;
    String []strimgs = new String[4];
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
                    Log.e("Lking--->","str = "+json);
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
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject mark = (JSONObject)jsonArray.get(i);
                            strimgs[i] = mark.getString("imgUrl");
                            Log.e("Lking--->","img = "+strimgs[i]);
                        }
                        imageLoaders.loadImage(mImg01,strimgs[0]);
                        imageLoaders.loadImage(mImg02,strimgs[1]);
                        imageLoaders.loadImage(mImg03,strimgs[2]);
                        imageLoaders.loadImage(mImg04,strimgs[3]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        new Thread(){
            @Override
            public void run() {
                //根据不同的position加载不同数据
                PersonManager.sendFosterInfo(position,mHandler);
            }
        }.start();
        setLister();
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
    private void setLister(){
        mImg01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[0];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path",str);
                startActivity(intent);
            }
        });
        mImg02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[1];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path",str);
                startActivity(intent);
            }
        });
        mImg03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[2];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path",str);
                startActivity(intent);
            }
        });
        mImg04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[3];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path",str);
                startActivity(intent);
            }
        });
    }

    class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }
}
