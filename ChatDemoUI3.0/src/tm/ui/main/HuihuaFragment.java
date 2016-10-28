package tm.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import tm.entity.ResourcesBean;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.WelcomeListAdapter;
import tm.ui.home.BenditeseActivity;
import tm.ui.home.GeranActivity;
import tm.ui.home.SearchActivity;
import tm.ui.main.adapter.BdtsAdapter;
import tm.utils.ConstantsHandler;
import tm.widget.StationaryGridView;
import tm.widget.pulltorefresh.PullToRefreshBase;
import tm.widget.pulltorefresh.PullToRefreshListView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class HuihuaFragment extends Fragment implements View.OnClickListener {


    private ImageView btn_1;
    private ImageView btn_2;
    private  ImageView btn_3;
    private BdtsAdapter gridViewAdapter;
    private StationaryGridView huihua_gridview;
    private EditText btn_search;
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
    private  double lat;
    private  double lng;
    static BDLocation lastLocation = null;
    private int type=0;
    private int stype=2;
    private int rjtype=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.huihua_fragment_tm, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1://个人
                btn_2.setImageResource(R.drawable.tm_qiye_norm);
                btn_1.setImageResource(R.drawable.tm_geren_pressed);
                btn_3.setImageResource(R.drawable.tm_sannong_normal);
                txt3.setText("本地特色");
                type=0;
                rjtype=1;
                if(stype==1){
                    huihua_gridview.setVisibility(View.VISIBLE);
                    refreshListView.setVisibility(View.GONE);
//                    LoadData1();
                }else if(stype==2){
                    LoadData1();
                }else if(stype==3){
                    LoadData2();
                }
                break;
            case R.id.btn_2://企业
                btn_2.setImageResource(R.drawable.tm_qiye_pressed);
                btn_1.setImageResource(R.drawable.tm_geren_normal);
                btn_3.setImageResource(R.drawable.tm_sannong_normal);
                txt3.setText("资讯");
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
                btn_2.setImageResource(R.drawable.tm_qiye_norm);
                btn_1.setImageResource(R.drawable.tm_geren_normal);
                btn_3.setImageResource(R.drawable.tm_sannong_pressed);
                txt3.setText("扶植农业");
                type=2;
                rjtype=3;
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
                }else if(type==1){
                    huihua_gridview.setVisibility(View.GONE);
                    refreshListView.setVisibility(View.VISIBLE);
                    LoadData3();
                }else if(type==2){
                    huihua_gridview.setVisibility(View.GONE);
                    refreshListView.setVisibility(View.VISIBLE);
                    LoadData3();
                }
                currentClick=3;
                break;
            case R.id.btn_search:
//                Toast.makeText(getContext(),"该功能正在开发中。。。",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadView();
        LoadData1();

    }
    /**
     * 接口推荐数据
     */
    public void LoadData1() {
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) getContext().getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        //获取Location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            //不为空,显示地理位置经纬度
            lat = location.getLatitude();//获取纬度
            lng = location.getLongitude();//获取经度
        }
        Log.e("info","lat=11="+lat);
        Log.e("info","lng=11="+lng);
        lat=34.19912;
        lng=108.891236;
        SharedPreferences sharedPre=getContext().getSharedPreferences("config",getContext().MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
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
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) getContext().getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        //获取Location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            //不为空,显示地理位置经纬度
            lat = location.getLatitude();//获取纬度
            lng = location.getLongitude();//获取经度
        }
        Log.e("info","lat=22="+lat);
        Log.e("info","lng=22="+lng);
        lat=34.19912;
        lng=108.891236;
        SharedPreferences sharedPre=getContext().getSharedPreferences("config",getContext().MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
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
     * 接口附近数据
     */
    public void LoadData3() {
        Log.e("info","企业====");
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) getContext().getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        //获取Location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            //不为空,显示地理位置经纬度
            lat = location.getLatitude();//获取纬度
            lng = location.getLongitude();//获取经度
        }
        Log.e("info","lat=22="+lat);
        Log.e("info","lng=22="+lng);
        lat=34.19912;
        lng=108.891236;
        SharedPreferences sharedPre=getContext().getSharedPreferences("config",getContext().MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        NetFactory.instance().commonHttpCilent(handler, getContext(),
                Config.URL_GET_NEARLY_USERS, list);

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
//            int pageitem = obj.getInt("pageitem");
//            if(pageitem>0){
                JSONArray objList = obj.getJSONArray("rows");
                for (int i = 0; i < objList.length(); i++) {
                    JSONObject jo = objList.getJSONObject(i);
                    HashMap<String,String> map_temp =new HashMap<String,String>();
                    map_temp.put("userid", jo.get("userId")+"");
                    map_temp.put("name", jo.get("companyName")+"");
                    map_temp.put("photo", jo.get("companyLogo")+"");
                    map_temp.put("desc", jo.get("companyIntroduction")+"");
                    map_temp.put("distance", jo.get("jl")+"公里");
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
        };
    };
    /**
     *  初始化组件
     */
    private void LoadView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ((LinearLayout) getView().findViewById(R.id.ll)).setPadding(0,
//                    SysUtils.getStatusHeight(getActivity()), 0, 0);
//        }
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
//                startActivity(new Intent(getActivity(), BenditeseActivity.class));
                Intent intent = new Intent(getActivity(), BenditeseActivity.class);
                intent.putExtra("position",position );
                Log.e("info","position=1="+position);
                startActivity(intent);
            }
        });
        btn_1 = (ImageView) getView().findViewById(R.id.btn_1);
        btn_2 = (ImageView) getView().findViewById(R.id.btn_2);
        btn_3 = (ImageView) getView().findViewById(R.id.btn_3);
        btn_search = (EditText)getView().findViewById(R.id.btn_search);


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

//    private void setView() {
//        if (AppCfg.type.equals("1")) {
//            btn_1.setBackgroundResource(R.drawable.shadow);
//            btn_2.setBackground(null);
//            btn_3.setBackground(null);
//        } else if (AppCfg.type.equals("2")) {
//            btn_2.setBackgroundResource(R.drawable.shadow);
//            btn_1.setBackground(null);
//            btn_3.setBackground(null);
//        } else if (AppCfg.type.equals("3")) {
//            btn_3.setBackgroundResource(R.drawable.shadow);
//            btn_2.setBackground(null);
//            btn_1.setBackground(null);
//        }
//    }

    public List<ResourcesBean> getData() {
        ArrayList<ResourcesBean> list = new ArrayList<ResourcesBean>();
        ResourcesBean bean1 = new ResourcesBean();
        bean1.mImagePath = "http://a3.qpic.cn/psb?/V11UnAG03VjFP8/nC1pFXU6UTL3OhS8RQ2XcLDF*SKSkNkoXtd8gqS6DPA!/b/dHMBAAAAAAAA&bo=WgBaAAAAAAADByI!&rf=viewer_4";
        list.add(0, bean1);

        ResourcesBean bean2 = new ResourcesBean();
        bean2.mImagePath = "http://a2.qpic.cn/psb?/V11UnAG03VjFP8/RcBfX9GIglDnq16kvzmsqXb1pq.fnfOusdSnr2n6dWo!/b/dG8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(1, bean2);

        ResourcesBean bean3 = new ResourcesBean();
        bean3.mImagePath = "http://a2.qpic.cn/psb?/V11UnAG03VjFP8/RLhZuemvox40Eqm7DasQFt36q3yXxAEYG3BQ7v.Scyc!/b/dOUAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(2, bean3);

        ResourcesBean bean4 = new ResourcesBean();
        bean4.mImagePath = "http://a2.qpic.cn/psb?/V11UnAG03VjFP8/jE0YZ1N.mC9jYKlrjnSa2*.MQZ0FFF3Y8mgJtnvEYkA!/b/dHIBAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(3, bean4);

        ResourcesBean bean5 = new ResourcesBean();
        bean5.mImagePath = "http://a3.qpic.cn/psb?/V11UnAG03VjFP8/AZOrSemdpEvVx4zkEVPYnBRVilVAVtyjXPZdCRf2dPM!/b/dHABAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(4, bean5);

        ResourcesBean bean6 = new ResourcesBean();
        bean6.mImagePath = "http://a3.qpic.cn/psb?/V11UnAG03VjFP8/580yySv8aTkLMZTrTjV7lSpHijwv5bJksi43B5UyR58!/b/dAoBAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(5, bean6);

        ResourcesBean bean7 = new ResourcesBean();
        bean7.mImagePath = "http://a1.qpic.cn/psb?/V11UnAG03VjFP8/yPk*Ul7jteCs1ycUTQbPQLfXsp1cvy3YzOXmgiU60No!/b/dOQAAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(6, bean7);

        ResourcesBean bean8 = new ResourcesBean();
        bean8.mImagePath = "http://a3.qpic.cn/psb?/V11UnAG03VjFP8/B8FiDAGC*gLhxTP3TMPhEWCpPtvwnUHdeEakE4xa6ik!/b/dAoBAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4";
        list.add(7, bean8);
        return list;
    }
}
