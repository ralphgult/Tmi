package tm.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.db.dao.FriendDao;
import tm.entity.FriendBean;
import tm.entity.ResourcesBean;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.WelcomeListAdapter;
import tm.ui.home.BenditeseActivity;
import tm.ui.home.GeranActivity;
import tm.ui.home.SearchActivity;
import tm.ui.main.adapter.BdtsAdapter;
import tm.ui.tmi.FosterAgriculturalActivity;
import tm.ui.tmi.MomentsActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ViewUtil;
import tm.widget.StationaryGridView;
import tm.widget.pulltorefresh.PullToRefreshBase;
import tm.widget.pulltorefresh.PullToRefreshListView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class HuihuaFragment extends Fragment implements View.OnClickListener ,View.OnTouchListener{

    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    int status = 1;

    private ImageView btn_1;
    private ImageView btn_2;
    private  ImageView btn_3;
    private BdtsAdapter gridViewAdapter;
    private StationaryGridView huihua_gridview;
    private Button btn_search;
    LinearLayout mMoveLayout;
    LinearLayout ll_top1;
    LinearLayout ll_top2;
    LinearLayout ll_top3;
    TextView txt1;
    TextView txt2;
    TextView txt3;

    ImageView img_line1;
    ImageView img_line2;

    ListView lv_common;
    ArrayList<HashMap> list = new ArrayList<HashMap>();
    WelcomeListAdapter listAdapter;

    private PullToRefreshListView refreshListView;
    private int endItem = 0;

    private ReceiveBroadCast receiveBroadCast;

    private int currentClick=1;
    Map map = new HashMap();

    private String locationProvider;
    private  String lat;
    private  String lng;
    private String username;
    static BDLocation lastLocation = null;
    private int type=0;
    private int stype=2;
    private int rjtype=1;
    private int zxtype=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.huihua_fragment_tm, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1://个人
                status = 1;
                btn_2.setImageResource(R.drawable.tm_qiye_norm);
                btn_1.setImageResource(R.drawable.tm_geren_pressed);
                btn_3.setImageResource(R.drawable.tm_sannong_normal);
                txt3.setText("本地特色");
                btn_search.setHint("关键词搜您想搜的附近美女/帅哥");
                zxtype=1;
                type=0;
                rjtype=1;
                if(stype==1){
                    huihua_gridview.setVisibility(View.VISIBLE);
                    refreshListView.setVisibility(View.GONE);
                    gridViewAdapter.resetDato(getData());
//                    LoadData1();
                }else if(stype==2){
                    LoadData1();
                }else if(stype==3){
                    LoadData2();
                }
                break;
            case R.id.btn_2://企业
                status = 2;

                btn_2.setImageResource(R.drawable.tm_qiye_pressed);
                btn_1.setImageResource(R.drawable.tm_geren_normal);
                btn_3.setImageResource(R.drawable.tm_sannong_normal);
                txt3.setText("资讯");
                btn_search.setHint("关键词搜您想搜的企业商品/服务");
                zxtype=2;
                type=1;
                rjtype=2;
                if(stype==1){
                    huihua_gridview.setVisibility(View.GONE);
                    refreshListView.setVisibility(View.VISIBLE);
                    LoadData3();
                }else if(stype==2){
                    LoadData1();
                }else if(stype==3){
                    LoadData2();
                }
                break;
            case R.id.btn_3://三农
                status = 3;
                btn_2.setImageResource(R.drawable.tm_qiye_norm);
                btn_1.setImageResource(R.drawable.tm_geren_normal);
                btn_3.setImageResource(R.drawable.tm_sannong_pressed);
                txt3.setText("扶植农业");
                btn_search.setHint("关键词搜您想搜的三农产品/服务");
                zxtype=3;
                type=2;
                rjtype=3;
                if(stype==1){
//                    huihua_gridview.setVisibility(View.GONE);
//                    refreshListView.setVisibility(View.VISIBLE);
//                    LoadData3();
                    huihua_gridview.setVisibility(View.VISIBLE);
                    refreshListView.setVisibility(View.GONE);
                    gridViewAdapter.resetDato(getNongyeData());
                }else if(stype==2){
                    LoadData1();
                }else if(stype==3){
                    LoadData2();
                }
                break;
            case R.id.ll_top1://热荐
                txt1.setTextColor(Color.parseColor("#a161fb"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                stype=2;
                currentClick=1;
                refreshListView.setVisibility(View.VISIBLE);
                huihua_gridview.setVisibility(View.GONE);
                LoadData1();
                break;
            case R.id.ll_top2://附近
                txt2.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt3.setTextColor(Color.parseColor("#8c8c8c"));
                stype=3;
                currentClick=2;
                refreshListView.setVisibility(View.VISIBLE);
                huihua_gridview.setVisibility(View.GONE);
                LoadData2();
                break;
            case R.id.ll_top3://本地特色
                txt3.setTextColor(Color.parseColor("#a161fb"));
                txt1.setTextColor(Color.parseColor("#8c8c8c"));
                txt2.setTextColor(Color.parseColor("#8c8c8c"));
                stype=1;
                if(type==0){
                    huihua_gridview.setVisibility(View.VISIBLE);
                    refreshListView.setVisibility(View.GONE);
                    gridViewAdapter.resetDato(getData());
                }else if(type==1){
                    huihua_gridview.setVisibility(View.GONE);
                    refreshListView.setVisibility(View.VISIBLE);
                    LoadData3();
                }else if(type==2){
//                    huihua_gridview.setVisibility(View.GONE);
//                    refreshListView.setVisibility(View.VISIBLE);
//                    LoadData3();
                    huihua_gridview.setVisibility(View.VISIBLE);
                    refreshListView.setVisibility(View.GONE);
                    gridViewAdapter.resetDato(getNongyeData());
                }
                currentClick=3;
                break;
            case R.id.btn_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("type",""+type);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPre=getContext().getSharedPreferences("config",getContext().MODE_PRIVATE);
        username=sharedPre.getString("username", "");
        lat=sharedPre.getString("lat","");
        lng=sharedPre.getString("lng","");
        LoadView();
        rjtype=1;
        LoadData1();
        refreshListView.setVisibility(View.VISIBLE);
        huihua_gridview.setVisibility(View.GONE);

        gestureDetector = new GestureDetector(getActivity(),onGestureListener);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public void doResult(int action) {
        switch (action) {
            case RIGHT:
                Log.e("Lking","右滑");
                System.out.println("go right");
                if(status ==1){
                    status = 2;
                    btn_2.setImageResource(R.drawable.tm_qiye_pressed);
                    btn_1.setImageResource(R.drawable.tm_geren_normal);
                    btn_3.setImageResource(R.drawable.tm_sannong_normal);
                    txt3.setText("资讯");
                    btn_search.setHint("关键词搜您想搜的企业商品/服务");
                    zxtype=2;
                    type=1;
                    rjtype=2;
                    if(stype==1){
                        huihua_gridview.setVisibility(View.GONE);
                        refreshListView.setVisibility(View.VISIBLE);
                        LoadData3();
                    }else if(stype==2){
                        LoadData1();
                    }else if(stype==3){
                        LoadData2();
                    }
                }else if(status ==2){
                    status = 3;
                    btn_2.setImageResource(R.drawable.tm_qiye_norm);
                    btn_1.setImageResource(R.drawable.tm_geren_normal);
                    btn_3.setImageResource(R.drawable.tm_sannong_pressed);
                    txt3.setText("扶植农业");
                    btn_search.setHint("关键词搜您想搜的三农产品/服务");
                    zxtype=3;
                    type=2;
                    rjtype=3;
                    if(stype==1){
//                    huihua_gridview.setVisibility(View.GONE);
//                    refreshListView.setVisibility(View.VISIBLE);
//                    LoadData3();
                        huihua_gridview.setVisibility(View.VISIBLE);
                        refreshListView.setVisibility(View.GONE);
                        gridViewAdapter.resetDato(getNongyeData());
                    }else if(stype==2){
                        LoadData1();
                    }else if(stype==3){
                        LoadData2();
                    }
                }else if(status == 3){
                }

                break;

            case LEFT:
                Log.e("Lking","左滑");
                if(status == 1){//个人，不处理

                }else if(status == 2){//企业，滑到个人
                    status = 1;

                    btn_2.setImageResource(R.drawable.tm_qiye_norm);
                    btn_1.setImageResource(R.drawable.tm_geren_pressed);
                    btn_3.setImageResource(R.drawable.tm_sannong_normal);
                    txt3.setText("本地特色");
                    btn_search.setHint("关键词搜您想搜的附近美女/帅哥");
                    zxtype=1;
                    type=0;
                    rjtype=1;
                    if(stype==1){
                        huihua_gridview.setVisibility(View.VISIBLE);
                        refreshListView.setVisibility(View.GONE);
                        gridViewAdapter.resetDato(getData());
//                    LoadData1();
                    }else if(stype==2){
                        LoadData1();
                    }else if(stype==3){
                        LoadData2();
                    }
                }else if(status == 3 ){//三农，滑到企业
                    status = 2;
                    btn_2.setImageResource(R.drawable.tm_qiye_pressed);
                    btn_1.setImageResource(R.drawable.tm_geren_normal);
                    btn_3.setImageResource(R.drawable.tm_sannong_normal);
                    txt3.setText("资讯");
                    btn_search.setHint("关键词搜您想搜的企业商品/服务");
                    zxtype=2;
                    type=1;
                    rjtype=2;
                    if(stype==1){
                        huihua_gridview.setVisibility(View.GONE);
                        refreshListView.setVisibility(View.VISIBLE);
                        LoadData3();
                    }else if(stype==2){
                        LoadData1();
                    }else if(stype==3){
                        LoadData2();
                    }
                }
                System.out.println("go left");
                break;

        }
    }
    /**
     * 接口推荐数据
     */
    public void LoadData1() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", ""+username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        Log.e("info","type===参数=热荐==="+rjtype);
        list.add(new BasicNameValuePair("type", ""+rjtype));
        NetFactory.instance().commonHttpCilent(handler, getContext(),
                Config.URL_GET_RECOMMEND_USERS, list);

    }
    /**
     * 接口附近数据
     */
    public void LoadData2() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", ""+username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        Log.e("info","type===参数=附近==="+rjtype);
        list.add(new BasicNameValuePair("type", ""+rjtype));
        NetFactory.instance().commonHttpCilent(handler, getContext(),
                Config.URL_GET_NEARLY_USERS, list);

    }
    /**
     * 接口资讯数据
     */
    public void LoadData3() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        Log.e("info","type===参数=资讯==="+zxtype);
        list.add(new BasicNameValuePair("type", ""+zxtype));
        NetFactory.instance().commonHttpCilent(handler, getContext(),
                Config.URL_GET_ZIXUN_HOME, list);

    }
    /**
     * 适配器装数据
     */
    protected void setData(Map map) {
        if (endItem == 0) {
            list.clear();
        }
        try {
            JSONObject obj =new JSONObject(map.toString());
            JSONArray objList = obj.getJSONArray("rows");
            for (int i = 0; i < objList.length(); i++) {
                JSONObject jo = objList.getJSONObject(i);
                HashMap<String,String> map_temp =new HashMap<String,String>();
                map_temp.put("userid", jo.get("userId")+"");
                map_temp.put("name", jo.get("companyName")+"");
                map_temp.put("photo", jo.get("companyLogo")+"");
                map_temp.put("desc", jo.get("companyIntroduction")+"");
                map_temp.put("distance", jo.get("jl")+"");
                map_temp.put("uname", jo.get("uname")+"");
                map_temp.put("video", jo.get("video")+"");
                list.add(map_temp);
            }
//                if (pageitem < 15) {
//                    refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                } else {
//                    refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//                }
//            }
        } catch (JSONException e) {
        }
        Log.e("info","mtype=热荐=11="+rjtype);
        listAdapter.setType(rjtype);
        listAdapter.setHandler(addhandler);
        listAdapter.notifyDataSetChanged();
    }

    /**
     * 接口回调
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            refreshListView.onRefreshComplete();
            Log.e("info","msg.what=111="+msg.what);
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info","map=11="+map);
                    setData(map);
                    break;
                default:
                    break;
            }
        }
    };
    Handler addhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.e("info","map=msg.what=添加好友="+msg.what);
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info","map==添加好友="+map);
                    String authid=map.get("authId")+"";
                    if(authid.endsWith("1")){
                        Toast.makeText(getContext(),"请求发送成功",Toast.LENGTH_SHORT).show();
//                        LoadFriendData();
                    }else if(authid.endsWith("2")){
                        Toast.makeText(getContext(),"已经是好友关系",Toast.LENGTH_SHORT).show();
                    }else if(authid.endsWith("3")){
                        Toast.makeText(getContext(),"添加失败，该用户不是环信用户",Toast.LENGTH_SHORT).show();
                    }else if(authid.endsWith("4")){
                        Toast.makeText(getContext(),"没有此用户",Toast.LENGTH_SHORT).show();
                    }else if(authid.endsWith("0")){
                        Toast.makeText(getContext(),"请求失败",Toast.LENGTH_SHORT).show();
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
    /**
     *  初始化组件
     */
    private void LoadView() {
        refreshListView = (PullToRefreshListView)getView().findViewById(R.id.lv_common);
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        refreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        endItem = 0;
                        if (currentClick == 1) {
                            refreshListView.setVisibility(View.VISIBLE);
                            LoadData1();
                        } else if (currentClick == 2) {
                            refreshListView.setVisibility(View.VISIBLE);
                            LoadData2();
                        }
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        endItem = Integer.parseInt(list.get(list.size() - 1).get("userid").toString());
                        if (currentClick == 1) {
                            refreshListView.setVisibility(View.VISIBLE);
                            LoadData1();
                        } else if (currentClick == 2) {
                            refreshListView.setVisibility(View.VISIBLE);
                            LoadData2();
                        }

                    }
                });
        lv_common = refreshListView.getRefreshableView();
        listAdapter = new WelcomeListAdapter(getActivity(), list);
        lv_common.setAdapter(listAdapter);
        lv_common.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(getActivity(), GeranActivity.class);
                intent.putExtra("id", listAdapter.getItemId(arg2-1)+"");
                startActivity(intent);
            }
        });
        huihua_gridview = (StationaryGridView) getView().findViewById(R.id.tm_huihua_gridview);
        gridViewAdapter = new BdtsAdapter(
                getActivity(), R.layout.tm_common_gridview_item_layout);
        huihua_gridview.setAdapter(gridViewAdapter);
        gridViewAdapter.addData(getData());
        huihua_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(type==0 && stype==1){//本地特色
                    if(position == 6){//失物招领
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 6);
                        ViewUtil.jumpToOtherActivity(HuihuaFragment.this.getActivity(), MomentsActivity.class, bundle);
                    }else if (position == 7){//拍卖共享
                        ViewUtil.jumpToOtherActivity(getActivity(), AuctionActivity.class);
                    }else{
                        Intent intent = new Intent(getActivity(), BenditeseActivity.class);
                        intent.putExtra("position",position );
                        startActivity(intent);
                    }
                }else{//扶植农业
                    //Todo 扶植农业其他接口好了，注释掉这
                    if(position>5){
                        Intent intent = new Intent(getActivity(), BenditeseActivity.class);
                        intent.putExtra("position",1 );
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), FosterAgriculturalActivity.class);
                        intent.putExtra("position",position+1);
                        startActivity(intent);
                    }

                }
            }
        });
        btn_1 = (ImageView) getView().findViewById(R.id.btn_1);
        btn_2 = (ImageView) getView().findViewById(R.id.btn_2);
        btn_3 = (ImageView) getView().findViewById(R.id.btn_3);
        btn_search = (Button)getView().findViewById(R.id.btn_search);


        ll_top1 = (LinearLayout) getView().findViewById(R.id.ll_top1);
        ll_top2 = (LinearLayout) getView().findViewById(R.id.ll_top2);
        ll_top3 = (LinearLayout) getView().findViewById(R.id.ll_top3);

        txt1 = (TextView) getView().findViewById(R.id.txt1);
        txt2 = (TextView) getView().findViewById(R.id.txt2);
        txt3=  (TextView) getView().findViewById(R.id.txt3);
        img_line1 = (ImageView) getView().findViewById(R.id.img_line1);
        img_line2 = (ImageView) getView().findViewById(R.id.img_line2);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        lv_common.setOnTouchListener(this);
        huihua_gridview.setOnTouchListener(this);
        ll_top1.setOnClickListener(this);
        ll_top2.setOnClickListener(this);
        ll_top3.setOnClickListener(this);
        txt1.setTextColor(Color.parseColor("#a161fb"));
        txt2.setTextColor(Color.parseColor("#8c8c8c"));

        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xbh.tmi");    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(receiveBroadCast, filter);
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

    /**
     *  聊天回调
     */
//    Handler callBack=new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case ConstantsHandler.EXECUTE_SUCCESS://进入聊天的回调函数
//                    Map map = (Map) msg.obj;
//                    String result = map.get("result").toString();
//                    try {
//                        JSONObject jo = new JSONObject(result);
//                        int pageitem = jo.getInt("pageitem");
//                        if (pageitem > 0) {
//                            JSONArray ja = jo.getJSONArray("item");
//                            if (ja.length()>0) {
//                                JSONObject tmp = ja.getJSONObject(0);
//                                EaseUser eu = new EaseUser(tmp.getString("userid")
//                                        + "tmisousou");
//                                eu.setNick(tmp.getString("name"));
//                                eu.setInitialLetter(PingYinUtil.getFirstSpell(tmp
//                                        .getString("name")));
//                                eu.setAvatar(tmp.getString("photo"));
//                                RobotUser ru = new RobotUser(eu.getUsername());
//                                ru.setAvatar(eu.getAvatar());
//                                ru.setNick(eu.getNick());
//                                DemoHelper.getInstance().saveRobot(ru);
//                                if(tmp.getString("userid").equals(AppCfg._instance.getCurrentUser().getUid()+"")){
//                                    AlertMsgL("不能跟自己聊天");
//                                    return;
//                                }
//                                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,eu.getUsername()));
//                            }
//                        }
//                    } catch (JSONException e) {
//                    }
//                    break;
//
//                case 100://进入聊天的回调函数
//                    try {
//                        String userid =msg.obj+"";
//                        EaseUser eu= DemoHelper.getInstance().getUserInfo(userid+"tmisousou");
//                        if(eu!=null){
//                            if(userid.equals(AppCfg._instance.getCurrentUser().getUid()+"")){
//                                AlertMsgL("不能跟自己聊天");
//                                return;
//                            }
//                            startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,userid+"tmisousou"));
//                        }else{
//                            List<NameValuePair> list = new ArrayList<NameValuePair>();
//                            list.add(new BasicNameValuePair("userid", userid));
//                            NetFactory.instance().commonHttpCilent(callBack, getActivity(),
//                                    Config.URL_BATCH_GET_USERS, list);
//                        }
//                    } catch (Exception e) {
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        };
//    };
    class ReceiveBroadCast extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            //得到广播中得到的数据，并显示出来
            String message = intent.getStringExtra("data");
//            setView();
        }

    }


    public List<ResourcesBean> getData() {
        ArrayList<ResourcesBean> list = new ArrayList<ResourcesBean>();
        ResourcesBean bean1 = new ResourcesBean();
        bean1.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/G2JIXi.MaRldU5*1mXEjpcGa74dXM*A4wwxiimfeh5E!/b/dK0AAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(0, bean1);

        ResourcesBean bean2 = new ResourcesBean();
        bean2.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/PToIp.GUEDdga1ZCO4vGBQRcZ3nYdN5582owshml65Y!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(1, bean2);

        ResourcesBean bean3 = new ResourcesBean();
        bean3.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/7Zq6mS*GFtLqJTUXmCjTBPG3eXJC77zUzoC3CDpnlMg!/b/dCABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(2, bean3);

        ResourcesBean bean4 = new ResourcesBean();
        bean4.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/Lw1ur0ZrdSvd5Jyu0fsdOwLsHhEDJOR3AsTD3vNePRU!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(3, bean4);

        ResourcesBean bean5 = new ResourcesBean();
        bean5.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/IwsKRkREf5O8IDH735G4AXQFed4llmfM7FE0QPT*tuE!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(4, bean5);

        ResourcesBean bean6 = new ResourcesBean();
        bean6.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/.nwVTsdxqqde9j9cr8qs*N.q3WY4Xvv3ZBFWgxAymH8!/b/dCABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(5, bean6);

        ResourcesBean bean7 = new ResourcesBean();
        bean7.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/yhvdoaZ8xXXpvBccdSuQxm9aLW6F.MiRmNmBXjmbmQ0!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(6, bean7);

        ResourcesBean bean8 = new ResourcesBean();
        bean8.mImagePath = "http://a2.qpic.cn/psb?/V14fxJhp0IQ0iR/fRqidEeRsQFYkqIQlIzOIPhC5KW2aEE2BKUseWWx0HY!/b/dLIAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(7, bean8);
        return list;
    }
    public List<ResourcesBean> getNongyeData() {
        ArrayList<ResourcesBean> list = new ArrayList<ResourcesBean>();
        ResourcesBean bean1 = new ResourcesBean();
        bean1.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/R6ayDnevKcEowPGmxNy1MSgMYFugs74eXZ7ngTZakvU!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(0, bean1);

        ResourcesBean bean2 = new ResourcesBean();
        bean2.mImagePath = "http://a2.qpic.cn/psb?/V14fxJhp0IQ0iR/Ry4.yyi92vAcRs4*PJF1IgCnUOconjMwcZiu8D6JKVU!/b/dLIAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(1, bean2);

        ResourcesBean bean3 = new ResourcesBean();
        bean3.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/e535vixbOqkQixBAOAP*gKONPtBaxOg4L4iqMdtT4c8!/b/dPYAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(2, bean3);

        ResourcesBean bean4 = new ResourcesBean();
        bean4.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/lK9XnnunQFmYUsR9S4xQlI4Kx21yOINcUuR4KnniPbc!/b/dPYAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(3, bean4);

        ResourcesBean bean5 = new ResourcesBean();
        bean5.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/g7baWZGeJcq7CwkYWUcivVV5QvIQUXxaDXF4tZV44mE!/b/dCABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(4, bean5);

        ResourcesBean bean6 = new ResourcesBean();
        bean6.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/nrqXFjYCJUPsLDdXLPddN*Lqjbj7peGEtpOjM0qmliA!/b/dCABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(5, bean6);

        ResourcesBean bean7 = new ResourcesBean();
        bean7.mImagePath = "http://a1.qpic.cn/psb?/V14fxJhp0IQ0iR/lgEmmmjas3yvC2cP5IVe2a9Ohn6MN7f1*CFX4QjW9f8!/b/dCABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(6, bean7);

        ResourcesBean bean8 = new ResourcesBean();
        bean8.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/3af8S*Ws2.jW6Y67ak1BbowAT3ywAc8x3Be70e6L*4Q!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(7, bean8);

        ResourcesBean bean9 = new ResourcesBean();
        bean9.mImagePath = "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/cLvep2OVUHO7eP*aeHvjyA2sG9DX8P*9AUgOn0jP0.s!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(8, bean9);
        return list;
    }




    /**
     * 好友列表
     */
    public void LoadFriendData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", username));
        NetFactory.instance().commonHttpCilent(friendhandler, getContext(),
                Config.URL_FRIENDS, list);

    }
    /**
     * 接口回调
     */
    Handler friendhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.e("info","msg.what=111="+msg.what);
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info","map=好友="+map);
                    setFriendData(map);
                    //插库
                    break;
                default:
                    break;
            }
        }
    };
    protected void setFriendData(Map map) {
        try {
            JSONObject obj =new JSONObject(map.toString());
            JSONArray objList = obj.getJSONArray("rows");
            FriendBean mFriendBean=new FriendBean();
            FriendDao mdao =new FriendDao();
            List<FriendBean> friendlist = new ArrayList<FriendBean>();
            if(objList.length()>0){
                for (int i = 0; i < objList.length(); i++) {
                    JSONObject jo = objList.getJSONObject(i);
                    mFriendBean.mNickname=jo.get("nickname")+"";
                    mFriendBean.mphoto=jo.get("photo")+"";
                    mFriendBean.mUsername= jo.get("userName")+"";
                    mFriendBean.mUserID= Integer.parseInt(jo.get("userId")+"");
                    friendlist.add(mFriendBean);
                    mdao.insertUserInfoList(friendlist);
                }
            }
        } catch (JSONException e) {
        }

    }



}
