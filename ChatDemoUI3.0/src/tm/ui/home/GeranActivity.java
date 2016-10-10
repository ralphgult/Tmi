package tm.ui.home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.TreeMap;

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
        tv_gxqm = (TextView)findViewById(R.id.tm_zhuye_gexing);
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

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_1:
//                btn_2.setImageResource(R.drawable.tm_qiye_norm);
//                btn_1.setImageResource(R.drawable.tm_geren_pressed);
//                btn_3.setImageResource(R.drawable.tm_sannong_normal);
//                break;
//            case R.id.btn_2:
//                btn_2.setImageResource(R.drawable.tm_qiye_pressed);
//                btn_1.setImageResource(R.drawable.tm_geren_normal);
//                btn_3.setImageResource(R.drawable.tm_sannong_normal);
//                break;
//            case R.id.btn_3:
//                btn_2.setImageResource(R.drawable.tm_qiye_norm);
//                btn_1.setImageResource(R.drawable.tm_geren_normal);
//                btn_3.setImageResource(R.drawable.tm_sannong_pressed);
//                break;
            case R.id.shouye_top1:
                txt1.setTextColor(Color.parseColor("#a161fb"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));

                break;
            case R.id.shouye_top2:
                txt2.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));

                break;
            case R.id.shouye_top3:
                txt3.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
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
    }private void setData(Map map) {
        list.clear();
//        String result = map.get("result").toString();
        try {
            JSONObject objects =new JSONObject(map.toString());
//            JSONObject objects = new JSONObject(result);
            JSONObject user1 = objects.getJSONObject("user1");
            String nickname = user1.getString("nickname");
            String userphoto = user1.getString("userphoto");
            if (!nickname.equals("")) {
                tv_title.setText(nickname);
            } else {
                tv_title.setText("个人主页");
            }
            tv_gxqm.setText(user1.getString("caption"));
            JSONArray news = user1.getJSONArray("news");

            for (int i = 0; i < news.length(); i++) {
                JSONObject jsonNew = news.getJSONObject(i);
                Map mapNew = new TreeMap();
                mapNew.put("id", jsonNew.getString("id"));
                mapNew.put("desc", jsonNew.getString("desc"));
                mapNew.put("name", nickname);
                mapNew.put("photo", userphoto);
                mapNew.put("view_count", jsonNew.getString("view_count"));
                mapNew.put("comment_count", jsonNew.getString("comment_count"));
                mapNew.put("photos", jsonNew.getString("photos"));
                list.add(mapNew);
            }
            JSONArray photos = user1.getJSONArray("photos");
            if (photos != null && photos.length() != 0) {
                if (photos.length() <= 3) {
//                    ll_pic1.setVisibility(View.VISIBLE);
//                    ll_pic2.setVisibility(View.GONE);
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("url"));
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
//                    ll_pic1.setVisibility(View.VISIBLE);
//                    ll_pic2.setVisibility(View.VISIBLE);
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                imageLoaders.loadImage(img_pic1, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 1:
                                imageLoaders.loadImage(img_pic2, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 2:
                                imageLoaders.loadImage(img_pic3, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 3:
                                imageLoaders.loadImage(img_pic4, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 4:
                                imageLoaders.loadImage(img_pic5, photos
                                        .getJSONObject(i).getString("url"));
                                break;
                            case 5:
                                imageLoaders.loadImage(img_pic6, photos
                                        .getJSONObject(i).getString("url"));
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
//                ll_pic1.setVisibility(View.GONE);
//                ll_pic2.setVisibility(View.GONE);
                img_pic1.setVisibility(View.GONE);
                img_pic2.setVisibility(View.GONE);
                img_pic3.setVisibility(View.GONE);
                img_pic4.setVisibility(View.GONE);
                img_pic5.setVisibility(View.GONE);
                img_pic6.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
        }
//        resetHeight(adapter, lv_person1, 1);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info","map===个人主页========"+map);
                    setData(map);
//                    setData_qy(map);
//                    setData_sn(map);
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
}
