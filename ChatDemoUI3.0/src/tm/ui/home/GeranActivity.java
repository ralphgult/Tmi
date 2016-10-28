package tm.ui.home;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;

/**
 * Created by Administrator on 2016/9/11.
 */
public class GeranActivity extends BaseActivity implements View.OnClickListener{


    private LinearLayout yy_top1;
    private LinearLayout yy_top2;
    private LinearLayout yy_top3;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private ImageView img_pic1;
    private ImageView img_pic2;
    private ImageView img_pic3;
    private ImageView img_pic4;
    private ImageView img_pic5;
    private ImageView img_pic6;
    private ArrayList<Map> list = new ArrayList<Map>();
    private TextView tv_title;
    private TextView tv_gxqm;
    private TextView tv_qymc;
    private TextView tv_grqianming;
    private TextView tv_qiyeshuoming;
    private TextView tv_yuanjia;
    private TextView tv_yuanjia2;
    private TextView tv_xianjia;
    private TextView tv_xianjia2;
    private TextView tv_jianjie;
    private TextView tv_jianjie2;
    private TextView tv_dianpu;
    private LinearLayout lv_geren1;
    private LinearLayout lv_geren2;
    private LinearLayout lv_geren3;
    private LinearLayout lv_geren4;
    private LinearLayout lv_qiye;
    private LinearLayout lv_dianpu;
    private LinearLayout lv_qiye_line;
    private ImageView img_pic11;
    private ImageView img_pic22;
    private ImageView addfriend;


    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_look;
    private TextView tv_num;
    private TextView tv_comment;
    private TextView tv_content;
    private LinearLayout wwww;
    private int  type=0;
    private ImageLoaders imageLoaders = new ImageLoaders(this,
            new imageLoaderListener());

    public class imageLoaderListener implements ImageLoaders.ImageLoaderListener {
        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ImageView iv = (ImageView) v;
            iv.setImageBitmap(bmp);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_zhuye_activity);
        init();
        LoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        tv_name = (TextView)findViewById(R.id.yx_monent_top_name);
        tv_time = (TextView)findViewById(R.id.yx_monent_top_time);
        tv_look = (TextView)findViewById(R.id.yx_monent_content_look);
        tv_num = (TextView)findViewById(R.id.yx_monent_bottom_like_number);
        tv_comment = (TextView)findViewById(R.id.yx_monent_bottom_comment_number);
        tv_content = (TextView)findViewById(R.id.yx_monent_content);
        wwww = (LinearLayout)findViewById(R.id.tm_botton_ly);


        tv_gxqm = (TextView)findViewById(R.id.tm_zhuye_gexing);
        tv_qymc = (TextView)findViewById(R.id.tm_zhuye_qiyename);
        tv_grqianming = (TextView)findViewById(R.id.tm_zhuye_gx);
        tv_qiyeshuoming = (TextView)findViewById(R.id.tm_zhuye_qiyeshuoming);
        lv_qiye = (LinearLayout)findViewById(R.id.tm_zhuye_qiye);
        tv_dianpu = (TextView)findViewById(R.id.tm_zhuye_geren);
        lv_geren1 = (LinearLayout)findViewById(R.id.tm_geren1);
        lv_geren2 = (LinearLayout)findViewById(R.id.tm_geren2);
        lv_geren3 = (LinearLayout)findViewById(R.id.tm_geren3);
        lv_geren4 = (LinearLayout)findViewById(R.id.tm_geren4);
        lv_dianpu = (LinearLayout)findViewById(R.id.tm_dianpu);
        lv_qiye_line = (LinearLayout)findViewById(R.id.tm_qiye_line);
        tv_yuanjia = (TextView)findViewById(R.id.tm_zhuye_yuanjia);
        tv_yuanjia2 = (TextView)findViewById(R.id.tm_zhuye_yuanjia2);
        tv_xianjia = (TextView)findViewById(R.id.tm_zhuye_xianjia);
        tv_xianjia2 = (TextView)findViewById(R.id.tm_zhuye_xianjia2);
        tv_jianjie = (TextView)findViewById(R.id.tm_zhuye_jianjie);
        tv_jianjie2 = (TextView)findViewById(R.id.tm_zhuye_jianjie2);
        img_pic11 = (ImageView) findViewById(R.id.tm_img_pic1);
        img_pic22 = (ImageView) findViewById(R.id.tm_img_pic2);
        addfriend = (ImageView) findViewById(R.id.tm_addfriend);


        tv_title = (TextView)findViewById(R.id.tm_shouye_top_tv);
        img_pic1 = (ImageView) findViewById(R.id.img_pic1);
        img_pic2 = (ImageView) findViewById(R.id.img_pic2);
        img_pic3 = (ImageView) findViewById(R.id.img_pic3);
        img_pic4 = (ImageView) findViewById(R.id.img_pic4);
        img_pic5 = (ImageView) findViewById(R.id.img_pic5);
        img_pic6 = (ImageView) findViewById(R.id.img_pic6);
        yy_top1 = (LinearLayout)findViewById(R.id.shouye_top1);
        yy_top2 = (LinearLayout)findViewById(R.id.shouye_top2);
        yy_top3 = (LinearLayout)findViewById(R.id.shouye_top3);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3=  (TextView)findViewById(R.id.txt3);
        txt1.setTextColor(Color.parseColor("#a161fb"));
        txt2.setTextColor(Color.parseColor("#8c8c8c"));
        txt3.setTextColor(Color.parseColor("#8c8c8c"));
        yy_top1.setOnClickListener(this);
        yy_top2.setOnClickListener(this);
        yy_top3.setOnClickListener(this);
        addfriend.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shouye_top1:
                txt1.setTextColor(Color.parseColor("#a161fb"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                type=0;
                LoadData();
                break;
            case R.id.shouye_top2:
                txt2.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                type=1;
                LoadData2();
                break;
            case R.id.shouye_top3:
                txt3.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                type=2;
                LoadData3();
                break;
            case R.id.tm_addfriend://添加好友
                SharedPreferences sharedPre=this.getSharedPreferences("config",this.MODE_PRIVATE);
                String username=sharedPre.getString("username", "");
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("me",username ));
                list.add(new BasicNameValuePair("my", getIntent().getStringExtra(
                        "id")));
//                list.add(new BasicNameValuePair("my", "36"));
                NetFactory.instance().commonHttpCilent(addhandler, this,
                        Config.URL_GET_ADDFRIEND, list);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }
    private void setData(Map map) {
        tv_grqianming.setVisibility(View.VISIBLE);
        tv_qymc.setVisibility(View.GONE);
        lv_qiye.setVisibility(View.GONE);
        lv_dianpu.setVisibility(View.GONE);
        lv_geren1.setVisibility(View.VISIBLE);
        lv_geren2.setVisibility(View.VISIBLE);
        lv_geren3.setVisibility(View.VISIBLE);
        lv_geren4.setVisibility(View.VISIBLE);
        lv_qiye_line.setVisibility(View.GONE);
//        tv_dianpu.setText("个人动态");
//        tv_title.setText("王鹏飞");
//        tv_gxqm.setText("求有销售团队的企业老板资源");
//        tv_name.setText("王鹏飞");
//        tv_time.setText("10.2  20:35");
//        tv_look.setText("2");
//        tv_num.setText("5");
//        tv_comment.setText("4");
//        tv_content.setText("天下事抬不过一个理字，为什么这么多人觉得口才胜过事实。");

        list.clear();
        try {
            JSONObject objects =new JSONObject(map.toString());
            JSONArray objList = objects.getJSONArray("rows");
              String nickname = objects.getString("userName");
            if (!nickname.equals("")) {
                tv_title.setText(nickname);
            } else {
                tv_title.setText("个人主页");
            }
            tv_gxqm.setText(objects.getString("caption"));
            JSONArray news = objects.getJSONArray("rows");

            for (int i = 0; i < news.length(); i++) {
                JSONObject jsonNew = news.getJSONObject(i);
                tv_name.setText(jsonNew.optString("userName"));
                tv_time.setText(jsonNew.optString("create_date"));
                tv_look.setText(jsonNew.optString("seeCount"));
                tv_num.setText(jsonNew.optString("mpsCount"));
                tv_comment.setText(jsonNew.optString("mcCount"));
                tv_content.setText(jsonNew.optString("mood_content"));
            }
            JSONArray photos = objects.getJSONArray("top");
            if (photos != null && photos.length() != 0) {
                if (photos.length() <= 3) {
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            default:
                                break;
                        }
                    }
                    if (photos.length() == 1) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.INVISIBLE);
                        img_pic3.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 2) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.VISIBLE);
                        img_pic3.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 3) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.VISIBLE);
                        img_pic3.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 3:
                                imageLoaders.loadImage(img_pic4, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 4:
                                imageLoaders.loadImage(img_pic5, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            case 5:
                                imageLoaders.loadImage(img_pic6, photos
                                        .getJSONObject(i).getString("mpThumbnail"));
                                break;
                            default:
                                break;
                        }
                    }
                    img_pic1.setVisibility(View.VISIBLE);
                    img_pic2.setVisibility(View.VISIBLE);
                    img_pic3.setVisibility(View.VISIBLE);
                    if (photos.length() == 4) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.INVISIBLE);
                        img_pic6.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 5) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.VISIBLE);
                        img_pic6.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 6) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.VISIBLE);
                        img_pic6.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                img_pic1.setVisibility(View.GONE);
                img_pic2.setVisibility(View.GONE);
                img_pic3.setVisibility(View.GONE);
                img_pic4.setVisibility(View.GONE);
                img_pic5.setVisibility(View.GONE);
                img_pic6.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
        }
    }
    private void setData2(Map map) {
        tv_qymc.setVisibility(View.VISIBLE);
        lv_dianpu.setVisibility(View.VISIBLE);
        tv_grqianming.setVisibility(View.GONE);
        lv_qiye_line.setVisibility(View.VISIBLE);
        lv_geren1.setVisibility(View.GONE);
        lv_geren2.setVisibility(View.GONE);
        lv_geren3.setVisibility(View.GONE);
        lv_geren4.setVisibility(View.GONE);
        lv_qiye.setVisibility(View.VISIBLE);

//        tv_gxqm.setText("西域美农");
//        tv_title.setText("西域美农");
//        tv_qiyeshuoming.setText("一切皆有可能");
//        tv_dianpu.setText("店铺商品");
//        tv_yuanjia.setText("￥:50.2");
//        tv_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        tv_yuanjia2.setText("￥:85.4");
//        tv_yuanjia2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        list.clear();
        //企业名称
        String nickname=map.get("companyName") + "";
        if (!nickname.equals("")) {
            tv_title.setText(nickname);
            tv_gxqm.setText(nickname);
        } else {
            tv_title.setText("企业主页");
        }
        //企业签名
        tv_qiyeshuoming.setText(map.get("companyIntroduction") + "");
        tv_dianpu.setText("店铺商品");
        tv_yuanjia.setText(map.get("originalPrice1") + "");
        tv_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_xianjia.setText(map.get("currentPrice1") + "");
        tv_yuanjia2.setText(map.get("originalPrice2") + "");
        tv_yuanjia2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_xianjia2.setText(map.get("currentPrice2") + "");
        tv_jianjie.setText(map.get("doodsProfiles1") + "");
        tv_jianjie2.setText(map.get("doodsProfiles2") + "");
        imageLoaders.loadImage(img_pic11, map.get("img1") + "");
        imageLoaders.loadImage(img_pic22, map.get("img2") + "");
        String top=  map.get("top")+"";
        Log.e("info","top==="+top);
        try {
            JSONArray     photos = new JSONArray(top);
            if (photos != null && photos.length() != 0) {
                if (photos.length() <= 3) {
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            default:
                                break;
                        }
                    }
                    if (photos.length() == 1) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.INVISIBLE);
                        img_pic3.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 2) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.VISIBLE);
                        img_pic3.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 3) {
                        img_pic1.setVisibility(View.VISIBLE);
                        img_pic2.setVisibility(View.VISIBLE);
                        img_pic3.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 3:
                                imageLoaders.loadImage(img_pic4, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 4:
                                imageLoaders.loadImage(img_pic5, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            case 5:
                                imageLoaders.loadImage(img_pic6, photos
                                        .getJSONObject(i).getString("imgUrl"));
                                break;
                            default:
                                break;
                        }
                    }
                    img_pic1.setVisibility(View.VISIBLE);
                    img_pic2.setVisibility(View.VISIBLE);
                    img_pic3.setVisibility(View.VISIBLE);
                    if (photos.length() == 4) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.INVISIBLE);
                        img_pic6.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 5) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.VISIBLE);
                        img_pic6.setVisibility(View.INVISIBLE);
                    } else if (photos.length() == 6) {
                        img_pic4.setVisibility(View.VISIBLE);
                        img_pic5.setVisibility(View.VISIBLE);
                        img_pic6.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                img_pic1.setVisibility(View.GONE);
                img_pic2.setVisibility(View.GONE);
                img_pic3.setVisibility(View.GONE);
                img_pic4.setVisibility(View.GONE);
                img_pic5.setVisibility(View.GONE);
                img_pic6.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.e("info","type--------"+type);
            Log.e("info","map===个人主页====msg.what===="+msg.what);
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info","map===个人主页========"+map);
                        if(type==0){
                            setData(map);
                        }else if(type==1){
                            setData2(map);
                        }else if(type == 2){
                            setData2(map);
                        }
                    break;
                case ConstantsHandler.EXECUTE_FAIL:
                    break;
                case ConstantsHandler.ConnectTimeout:
//                    AlertMsgL("超时");
                    break;
                default:
                    break;
            }
        }

    };
    Handler addhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    String authid=map.get("authId")+"";
                        if(authid.endsWith("1")){
                            Toast.makeText(GeranActivity.this,"添加好友成功",Toast.LENGTH_SHORT).show();
                        }else if(authid.endsWith("2")){
                            Toast.makeText(GeranActivity.this,"已经是好友关系",Toast.LENGTH_SHORT).show();
                        }else if(authid.endsWith("3")){
                            Toast.makeText(GeranActivity.this,"添加失败，该用户不是环信用户",Toast.LENGTH_SHORT).show();
                        }else if(authid.endsWith("4")){
                            Toast.makeText(GeranActivity.this,"没有此用户",Toast.LENGTH_SHORT).show();
                        }
                    break;
                case ConstantsHandler.EXECUTE_FAIL:
                    break;
                case ConstantsHandler.ConnectTimeout:
//                    AlertMsgL("超时");
                    break;
                default:
                    break;
            }
        }

    };

    public void LoadData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", getIntent().getStringExtra(
                "id")));
        NetFactory.instance().commonHttpCilent(handler, this,
                Config.URL_GET_USER_HOME, list);
    }
    public void LoadData2() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", getIntent().getStringExtra(
                "id")));
//        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("type", "1"));
        NetFactory.instance().commonHttpCilent(handler, this,
                Config.URL_GET_QIYE_HOME, list);
    }
    public void LoadData3() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", getIntent().getStringExtra(
                "id")));
        list.add(new BasicNameValuePair("type", "2"));
        NetFactory.instance().commonHttpCilent(handler, this,
                Config.URL_GET_QIYE_HOME, list);
    }
}
