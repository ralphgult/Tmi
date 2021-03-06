package tm.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
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
import tm.ui.BigBackGroundActivity;
import tm.ui.home.Adapter.DianpuAdapter;
import tm.ui.mine.HeadBigActivity;
import tm.ui.tmi.GoodsDetilActivity;
import tm.ui.tmi.MomentsActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.widget.StationaryGridView;

/**
 * Created by Administrator on 2016/9/11.
 */
public class GeranActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{
    String [] strPath = new String[6];
    int photoLength;
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    int status = 1;
    private ScrollView mHuaScrollView;

    private LinearLayout yy_top1;
    private LinearLayout yy_top2;
    private LinearLayout yy_top3;
    private LinearLayout tm_geren;
    private LinearLayout tm_geren_info;
    private TextView juzhudi;
    private TextView zhiye;
    private TextView age;
    private TextView shenggao;
    private TextView yuanxiao;
    private TextView aihao;
    private TextView xinyuan;
    private TextView shouru;


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
    private ImageView top_head_img;//个人动态头像
    private LinearLayout pic_ly;
    private LinearLayout ll_pic2;


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

        mHuaScrollView = (ScrollView)findViewById(R.id.huadong_view);
        mHuaScrollView.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,onGestureListener);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    if(e1 == null || e2 == null){
                        return false;
                    }else{
                        float x = e2.getX() - e1.getX();
                        float y = e2.getY() - e1.getY();

                        if (x > 0 && (Math.abs(x) > 200)&&(Math.abs(y) < 200) ) {
                            Log.e("Lking "," y " +y);
                            doResult(RIGHT);
                        } else if (x < 0 && (Math.abs(x) > 200)&&(Math.abs(y) < 200)) {
                            Log.e("Lking "," y " +y);
                            doResult(LEFT);
                        }
                        return true;
                    }


                }
            };

    public void doResult(int action) {
        switch (action) {
            case LEFT:
//            case RIGHT:
                Log.e("Lking","左滑");
                System.out.println("go right");
                if(status ==1){
                    status = 2;

                    qstype=2;
                    txt2.setTextColor(Color.parseColor("#a161fb"));
                    txt1.setTextColor(Color.parseColor("#8c8c8c"));
                    txt3.setTextColor(Color.parseColor("#8c8c8c"));
                    type=1;
                    LoadData2();

                }else if(status ==2){
                    status = 3;

                    qstype=3;
                    txt3.setTextColor(Color.parseColor("#a161fb"));
                    txt1.setTextColor(Color.parseColor("#8c8c8c"));
                    txt2.setTextColor(Color.parseColor("#8c8c8c"));
                    type=2;
                    LoadData3();

                }else if(status == 3){
                }

                break;

            case RIGHT:
                Log.e("Lking","右滑");
                if(status == 1){//个人，不处理

                }else if(status == 2){//企业，滑到个人
                    status = 1;

                    txt1.setTextColor(Color.parseColor("#a161fb"));
                    txt2.setTextColor(Color.parseColor("#8c8c8c"));
                    txt3.setTextColor(Color.parseColor("#8c8c8c"));
                    type=0;
                    LoadData();

                }else if(status == 3 ){//三农，滑到企业
                    status = 2;

                    qstype=2;
                    txt2.setTextColor(Color.parseColor("#a161fb"));
                    txt1.setTextColor(Color.parseColor("#8c8c8c"));
                    txt3.setTextColor(Color.parseColor("#8c8c8c"));
                    type=1;
                    LoadData2();

                }
                System.out.println("go left");
                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.e("Lking","滑动的监听事件");
                return gestureDetector.onTouchEvent(event);
            default:
                Log.e("Lking","滑动的事件");
                return gestureDetector.onTouchEvent(event);
        }
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

                    Intent intent = new Intent(GeranActivity.this, GoodsDetilActivity.class);
                    Log.e("info","position=="+position);
                    intent.putExtra("id",gridViewAdapter.getSourceList().get(position).mCommodityid );
                    startActivity(intent);
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
        pic_ly = (LinearLayout)findViewById(R.id.ll_pic);

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

        ll_pic2= (LinearLayout)findViewById(R.id.ll_pic2);

        top_head_img = (ImageView) findViewById(R.id.yx_monent_top_head_img);//个人头像
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
        tm_geren = (LinearLayout)findViewById(R.id.tm_geren_ly);
        //个人详情
        tm_geren_info = (LinearLayout)findViewById(R.id.tm_geren_info_ly);
        juzhudi=(TextView)findViewById(R.id.tm_geren_juzhudi_tv);
        zhiye=(TextView)findViewById(R.id.tm_geren_zhiye_tv);
        age=(TextView)findViewById(R.id.tm_geren_age_tv);
        shenggao=(TextView)findViewById(R.id.tm_geren_height_tv);
        yuanxiao=(TextView)findViewById(R.id.tm_geren_daxue_tv);
        aihao=(TextView)findViewById(R.id.tm_geren_aihao_tv);
        xinyuan=(TextView)findViewById(R.id.tm_geren_xinyuan_tv);
        shouru=(TextView)findViewById(R.id.tm_geren_shouru_tv);


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
        tm_geren.setOnClickListener(this);

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
                status = 1;

                txt1.setTextColor(Color.parseColor("#a161fb"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                type=0;
                LoadData();
                break;
            case R.id.shouye_top2:
                status = 2;

                qstype=2;
                txt2.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                type=1;
                LoadData2();
                break;
            case R.id.shouye_top3:
                status = 3;

                qstype=3;
                txt3.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                type=2;
                LoadData3();
                break;
            case R.id.tm_geren_ly:
                Bundle bund = new Bundle();
                bund.putInt("type", 5);
                bund.putString("id", uid);
                bund.putString("name", uname);
                ViewUtil.jumpToOtherActivity(this, MomentsActivity.class, bund);
                break;
            case R.id.tm_addfriend://添加好友
                FriendDao fd2=new FriendDao();
                if(fd2.isExist(uname)){
                    Toast.makeText(GeranActivity.this,"已经是好友了!",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        EMClient.getInstance().contactManager().addContact(uname, "赶紧加好友吧");
                        Toast.makeText(GeranActivity.this,"发送请求成功,等待对方验证",Toast.LENGTH_SHORT).show();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }

//                List<NameValuePair> list = new ArrayList<NameValuePair>();
//                list.add(new BasicNameValuePair("me",username ));
//                list.add(new BasicNameValuePair("my", uid));
//                NetFactory.instance().commonHttpCilent(addhandler, this,
//                        Config.URL_GET_ADDFRIEND, list);
                break;
            case R.id.tm_liaotian://聊天
                if(username.equalsIgnoreCase(uid)){
                    Toast.makeText(this,"不能和自己聊天!",Toast.LENGTH_SHORT).show();
                    return;
                }
                FriendDao fd=new FriendDao();
                if(fd.isExist(uname)){
                    if(!TextUtils.isEmpty(uname)){
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
                addPath();
                String []str1 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str1[i] = strPath[i];
                }
                bundle1.putStringArray("path",str1);
                bundle1.putInt("status",0);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle1);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle1);
                break;
            case R.id.img_pic2:
                Bundle bundle2 = new Bundle();
                addPath();
                String []str2 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str2[i] = strPath[i];
                }
                bundle2.putStringArray("path", str2);
                bundle2.putInt("status",1);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle2);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle2);
                break;
            case R.id.img_pic3:
                Bundle bundle3 = new Bundle();
                addPath();
                String []str3 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str3[i] = strPath[i];
                }
                bundle3.putStringArray("path", str3);
                bundle3.putInt("status",2);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle3);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle3);
                break;
            case R.id.img_pic4:
                Bundle bundle4 = new Bundle();
                addPath();
                String []str4 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str4[i] = strPath[i];
                }
                bundle4.putStringArray("path", str4);
                bundle4.putInt("status",3);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle4);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle4);
                break;
            case R.id.img_pic5:
                Bundle bundle5 = new Bundle();
                addPath();
                String []str5 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str5[i] = strPath[i];
                }
                bundle5.putStringArray("path", str5);
                bundle5.putInt("status",4);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle5);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle5);
                break;
            case R.id.img_pic6:
                Bundle bundle6 = new Bundle();
                addPath();
                String []str6 = new String[photoLength];
                for(int i=0;i<photoLength;i++){
                    str6[i] = strPath[i];
                }
                bundle6.putStringArray("path", str6);
                bundle6.putInt("status",5);
//                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle6);
                ViewUtil.jumpToOtherActivity(this, BigBackGroundActivity.class, bundle6);
                break;
            default:
                break;
        }

    }

    private void addPath() {
        if(null != picurl1){
            strPath[0] = picurl1;
        }
        if(null != picurl2){
            strPath[1] = picurl2;
        }
        if(null != picurl3){
            strPath[2] = picurl3;
        }
        if(null != picurl4){
            strPath[3] = picurl4;
        }
        if(null != picurl5){
            strPath[4] = picurl5;
        }
        if(null != picurl6){
            strPath[5] = picurl6;
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
        tm_geren_info.setVisibility(View.VISIBLE);
        wwww.setVisibility(View.VISIBLE);
        tv_grqianming.setVisibility(View.VISIBLE);
        tv_qymc.setVisibility(View.GONE);
        lv_qiye.setVisibility(View.GONE);
        dianpu_gridview.setVisibility(View.GONE);
        lv_geren1.setVisibility(View.VISIBLE);
        lv_geren2.setVisibility(View.VISIBLE);
        lv_geren3.setVisibility(View.GONE);
        lv_geren4.setVisibility(View.VISIBLE);
        lv_qiye_line.setVisibility(View.GONE);
        tv_dianpu.setText("个人动态");
        list.clear();
        try {
//            JSONObject objects =new JSONObject(map.toString());
//            uname=objects.optString("user_name");
            //个人信息
            if(map.containsKey("domicile")){
                juzhudi.setText(map.get("domicile")+"");//居住地
            }else{
                juzhudi.setText("未设置");//居住地
            }
            if(map.containsKey("occupation")){
                zhiye.setText(map.get("occupation")+"");//职业
            }else{
                zhiye.setText("未设置");//职业
            }
            if(map.containsKey("age")){
                age.setText(map.get("age")+"");//年龄
            }else{
                age.setText("未设置");//年龄
            }
            if(map.containsKey("height")){
                shenggao.setText(map.get("height")+"cm");//身高
            }else{
                shenggao.setText("未设置");//身高
            }
            if(map.containsKey("school")){
                yuanxiao.setText(map.get("school")+"");//院校
            }else{
                yuanxiao.setText("未设置");//院校
            }
            if(map.containsKey("hobby")){
                aihao.setText(map.get("hobby")+"");//爱好
            }else{
                aihao.setText("未设置");//爱好
            }
            if(map.containsKey("wish")){
                xinyuan.setText(map.get("wish")+"");//心愿
            }else{
                xinyuan.setText("未设置");//心愿
            }
            if(map.containsKey("income")){
                shouru.setText(map.get("income")+"元");//收入
            }else{
                shouru.setText("未设置");//收入
            }

            uname=map.get("user_name")+"";
//            imageLoaders.loadImage(top_head_img,objects.optString("photo") );
            imageLoaders.loadImage(top_head_img,map.get("photo")+"" );
            String nickname = map.get("userName")+"";
//            String nickname = objects.optString("userName");
            if (!TextUtils.isEmpty(nickname)) {
                tv_title.setText(nickname);
            } else {
                tv_title.setText("个人主页");
            }
//            tv_gxqm.setText(objects.getString("caption"));
            if(map.containsKey("caption")){
                if(!TextUtils.isEmpty(map.get("caption")+"")){
                    tv_gxqm.setText(map.get("caption")+"");
                }
            }else{
                tv_gxqm.setText("");
            }
//            JSONArray news = objects.getJSONArray("rows");
            JSONArray news = new JSONArray(map.get("rows")+"");

            for (int i = 0; i < news.length(); i++) {
                JSONObject jsonNew = news.getJSONObject(i);
                tv_name.setText(jsonNew.optString("userName"));
                tv_time.setText(jsonNew.optString("create_date"));
                tv_look.setText(jsonNew.optString("seeCount"));
                tv_num.setText(jsonNew.optString("mpsCount"));
                tv_comment.setText(jsonNew.optString("mcCount"));
                tv_content.setText(jsonNew.optString("mood_content"));
            }
//            JSONArray photos = objects.getJSONArray("top");
            JSONArray photos = new JSONArray(map.get("top")+"");


            if (photos != null && photos.length() != 0) {
                photoLength = photos.length();
                img_pic1.refreshDrawableState();
                pic_ly.setVisibility(View.VISIBLE);
                if (photos.length() <= 3) {
                    ll_pic2.setVisibility(View.GONE);
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
                    ll_pic2.setVisibility(View.VISIBLE);
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
                pic_ly.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setData2(Map map) {
        tm_geren_info.setVisibility(View.GONE);
        wwww.setVisibility(View.GONE);
        tv_qymc.setVisibility(View.VISIBLE);
        if(qstype==3){
            tv_qymc.setText("三农名称:");
            tv_qysm.setText("三农说明");
        }else{
            tv_qymc.setText("企业名称:");
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
                photoLength = photos.length();
                img_pic1.refreshDrawableState();
                pic_ly.setVisibility(View.VISIBLE);
                if (photos.length() <= 3) {
                    ll_pic2.setVisibility(View.GONE);
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
                    ll_pic2.setVisibility(View.VISIBLE);
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
                pic_ly.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
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
