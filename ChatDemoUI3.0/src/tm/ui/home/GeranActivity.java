package tm.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;
import com.xbh.tmi.ui.ChatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.db.dao.FriendDao;
import tm.entity.DianpuBean;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.home.Adapter.DianpuAdapter;
import tm.ui.mine.HeadBigActivity;
import tm.ui.tmi.FosterAgriculturalActivity;
import tm.ui.tmi.GoodsChangeActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.widget.StationaryGridView;

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
    private TextView tv_qysm;
    private TextView tv_grqianming;
    private TextView tv_qiyeshuoming;
    private TextView tv_dianpu;
    private LinearLayout lv_geren1;
    private LinearLayout lv_geren2;
    private LinearLayout lv_geren3;
    private LinearLayout lv_geren4;
    private LinearLayout lv_qiye;
    private LinearLayout lv_qiye_line;
    private ImageView addfriend;
    private ImageView liaotian;


    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_look;
    private TextView tv_num;
    private TextView tv_comment;
    private TextView tv_content;
    private LinearLayout wwww;
    private int  type=0;
    private  String username;
    private  String uid;
    private  String uname;
    private String picurl1;
    private String picurl2;
    private String picurl3;
    private String picurl4;
    private String picurl5;
    private String picurl6;
    private int qstype=0;//企业三农区别
    private StationaryGridView dianpu_gridview;
    private DianpuAdapter gridViewAdapter;
    private   List<DianpuBean> shangpinlist=new ArrayList<DianpuBean>();

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
        SharedPreferences sharedPre=this.getSharedPreferences("config",this.MODE_PRIVATE);
        username=sharedPre.getString("username", "");
        uid=getIntent().getStringExtra("id");
        init();
        setAdapter();
        LoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void setAdapter(){
        gridViewAdapter = new DianpuAdapter(this, R.layout.tm_dianpu_item_layout);
        dianpu_gridview.setAdapter(gridViewAdapter);
        dianpu_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    Intent intent = new Intent(GeranActivity.this, GoodsChangeActivity.class);
////                    intent.putExtra("position",position );
//                    startActivity(intent);
            }
        });
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
        tv_qysm = (TextView)findViewById(R.id.tm_zhuye_qism);

        tv_grqianming = (TextView)findViewById(R.id.tm_zhuye_gx);
        tv_qiyeshuoming = (TextView)findViewById(R.id.tm_zhuye_qiyeshuoming);
        lv_qiye = (LinearLayout)findViewById(R.id.tm_zhuye_qiye);
        tv_dianpu = (TextView)findViewById(R.id.tm_zhuye_geren);
        lv_geren1 = (LinearLayout)findViewById(R.id.tm_geren1);
        lv_geren2 = (LinearLayout)findViewById(R.id.tm_geren2);
        lv_geren3 = (LinearLayout)findViewById(R.id.tm_geren3);
        lv_geren4 = (LinearLayout)findViewById(R.id.tm_geren4);
        lv_qiye_line = (LinearLayout)findViewById(R.id.tm_qiye_line);
        addfriend = (ImageView) findViewById(R.id.tm_addfriend);
        liaotian = (ImageView) findViewById(R.id.tm_liaotian);
        //店铺
        dianpu_gridview = (StationaryGridView)findViewById(R.id.tm_dianpu);




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
        liaotian.setOnClickListener(this);

        img_pic1.setOnClickListener(this);
        img_pic2.setOnClickListener(this);
        img_pic3.setOnClickListener(this);
        img_pic4.setOnClickListener(this);
        img_pic5.setOnClickListener(this);
        img_pic6.setOnClickListener(this);


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
                qstype=2;
                txt2.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                type=1;
                LoadData2();
                break;
            case R.id.shouye_top3:
                qstype=3;
                txt3.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                type=2;
                LoadData3();
                break;
            case R.id.tm_addfriend://添加好友

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("me",username ));
                list.add(new BasicNameValuePair("my", uid));
                NetFactory.instance().commonHttpCilent(addhandler, this,
                        Config.URL_GET_ADDFRIEND, list);
                break;
            case R.id.tm_liaotian://聊天
                if(username.equals(uid)){
                    Toast.makeText(this,"不能和自己聊天!",Toast.LENGTH_SHORT).show();
                    return;
                }
                FriendDao fd=new FriendDao();
                if(fd.isExist(uname)){
                    if(!TextUtils.isEmpty(uname)){
                        Log.e("info","uname=主页="+uname);
                        startActivity(new Intent(this, ChatActivity.class).putExtra("userId",uname));
                    }else{
                        Toast.makeText(this,"用户资料不完整!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"还不是好友不能聊天!",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.img_pic1:
                Bundle bundle1 = new Bundle();
                bundle1.putString("path", picurl1);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle1);
                break;
            case R.id.img_pic2:
                Bundle bundle2 = new Bundle();
                bundle2.putString("path", picurl2);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle2);
                break;
            case R.id.img_pic3:
                Bundle bundle3 = new Bundle();
                bundle3.putString("path", picurl3);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle3);
                break;
            case R.id.img_pic4:
                Bundle bundle4 = new Bundle();
                bundle4.putString("path", picurl4);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle4);
                break;
            case R.id.img_pic5:
                Bundle bundle5 = new Bundle();
                bundle5.putString("path", picurl5);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle5);
                break;
            case R.id.img_pic6:
                Bundle bundle6 = new Bundle();
                bundle6.putString("path", picurl6);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle6);
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
        wwww.setVisibility(View.VISIBLE);
        tv_grqianming.setVisibility(View.VISIBLE);
        tv_qymc.setVisibility(View.GONE);
        lv_qiye.setVisibility(View.GONE);
        dianpu_gridview.setVisibility(View.GONE);
        lv_geren1.setVisibility(View.VISIBLE);
        lv_geren2.setVisibility(View.VISIBLE);
        lv_geren3.setVisibility(View.VISIBLE);
        lv_geren4.setVisibility(View.VISIBLE);
        lv_qiye_line.setVisibility(View.GONE);

        list.clear();
        try {
            JSONObject objects =new JSONObject(map.toString());
            uname=objects.optString("user_name");
            String nickname = objects.optString("userName");
            if (!TextUtils.isEmpty(nickname)) {
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
                                picurl1=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic1,picurl1 );
                                break;
                            case 1:
                                picurl2=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic2, picurl2);
                                break;
                            case 2:
                                picurl3=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic3, picurl3);
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
                                picurl1=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic1, picurl1);
                                break;
                            case 1:
                                picurl2=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic2, picurl2);
                                break;
                            case 2:
                                picurl3=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic3, picurl3);
                                break;
                            case 3:
                                picurl4=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic4, picurl4);
                                break;
                            case 4:
                                picurl5=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic5, picurl5);
                                break;
                            case 5:
                                picurl6=photos.getJSONObject(i).getString("mpThumbnail");
                                imageLoaders.loadImage(img_pic6, picurl6);
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
        wwww.setVisibility(View.GONE);
        tv_qymc.setVisibility(View.VISIBLE);
        if(qstype==3){
            tv_qymc.setText("三农名称");
            tv_qysm.setText("三农说明");
        }else{
            tv_qymc.setText("企业名称");
            tv_qysm.setText("企业说明");
        }
        dianpu_gridview.setVisibility(View.VISIBLE);
        tv_grqianming.setVisibility(View.GONE);
        lv_qiye_line.setVisibility(View.VISIBLE);
        lv_geren1.setVisibility(View.GONE);
        lv_geren2.setVisibility(View.GONE);
        lv_geren3.setVisibility(View.GONE);
        lv_geren4.setVisibility(View.GONE);
        lv_qiye.setVisibility(View.VISIBLE);

        list.clear();
        try {
        //企业名称
        String nickname;
        if (!TextUtils.isEmpty((CharSequence) map.get("companyName"))) {
            nickname = map.get("companyName") + "";
            tv_title.setText(nickname);
            tv_gxqm.setText(nickname);
        } else {
            if(qstype==3){
                tv_title.setText("三农");
            }else{
                tv_title.setText("企业 ");
            }

        }
        //企业签名
        tv_qiyeshuoming.setText(map.get("companyIntroduction") + "");
        tv_dianpu.setText("店铺商品");
            shangpinlist.clear();
            String top=  map.get("top")+"";
            String xq=  map.get("xq")+"";
            JSONArray news = new JSONArray(xq);
            DianpuBean db ;
            for (int i = 0; i < news.length(); i++) {
                db = new DianpuBean();
                JSONObject jsonNew = news.getJSONObject(i);
                db.mCommodityname=jsonNew.optString("goodsName");
                db.mCommodityid=jsonNew.optString("goodsId");
                db.mCommodityjianjie=jsonNew.optString("doodsProfiles");
                db.mCommodityPath=jsonNew.optString("img");
                db.mCommodityxianjia=jsonNew.optString("currentPrice");
                db.mCommodityyuanjia=jsonNew.optString("originalPrice");
                shangpinlist.add(db);
            }
            gridViewAdapter.resetDato(shangpinlist);
            JSONArray     photos = new JSONArray(top);
            if (photos != null && photos.length() != 0) {
                if (photos.length() <= 3) {
                    for (int i = 0; i < photos.length(); i++) {
                        switch (i) {
                            case 0:
                                picurl1=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic1, picurl1);
                                break;
                            case 1:
                                picurl2=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic2, picurl2);
                                break;
                            case 2:
                                picurl3=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic3, picurl3);
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
                                picurl1=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic1, picurl1);
                                break;
                            case 1:
                                picurl2=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic2, picurl2);
                                break;
                            case 2:
                                picurl3=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic3,picurl3);
                                break;
                            case 3:
                                picurl4=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic4, picurl4);
                                break;
                            case 4:
                                picurl5=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic5, picurl5);
                                break;
                            case 5:
                                picurl6=photos.getJSONObject(i).getString("imgUrl");
                                imageLoaders.loadImage(img_pic6, picurl6);
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
    public List<DianpuBean> getData() {
        ArrayList<DianpuBean> list = new ArrayList<DianpuBean>();
        DianpuBean bean1 = new DianpuBean();
        bean1.mCommodityPath = "http://a3.qpic.cn/psb?/V11UnAG03VjFP8/nC1pFXU6UTL3OhS8RQ2XcLDF*SKSkNkoXtd8gqS6DPA!/b/dHMBAAAAAAAA&bo=WgBaAAAAAAADByI!&rf=viewer_4";
        list.add(0, bean1);

        DianpuBean bean2 = new DianpuBean();
        bean2.mCommodityPath = "http://a2.qpic.cn/psb?/V11UnAG03VjFP8/RcBfX9GIglDnq16kvzmsqXb1pq.fnfOusdSnr2n6dWo!/b/dG8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(1, bean2);
        return list;
    }
}
