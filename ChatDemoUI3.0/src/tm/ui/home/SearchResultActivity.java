package tm.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.home.Adapter.SearchResultAdapter;
import tm.utils.ConstantsHandler;

/**
 * Created by Administrator on 2016/10/12.
 */
public class SearchResultActivity extends BaseActivity {

    private TextView search;
    private String searchin;
    private  double lat;
    private  double lng;
    private ListView mListView;
    ArrayList<HashMap> list = new ArrayList<HashMap>();
    SearchResultAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_search_result_aty);
        searchin=getIntent().getStringExtra("companyName");
        init();
        LoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        mListView=(ListView)findViewById(R.id.list);
        listAdapter = new SearchResultAdapter(this, list);
        mListView.setAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }
    /**
     * 搜索数据
     */
    public void LoadData() {
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager)this.getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        //获取Location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            //不为空,显示地理位置经纬度
            lat = location.getLatitude();//获取纬度
            lng = location.getLongitude();//获取经度
        }
        Log.e("info","lat=22="+lat);
        Log.e("info","lng=22="+lng);
        SharedPreferences sharedPre=this.getSharedPreferences("config",this.MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", ""+username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        list.add(new BasicNameValuePair("companyName", searchin));
        list.add(new BasicNameValuePair("type", "2"));
        NetFactory.instance().commonHttpCilent(handler, this,
                Config.URL_GET_SEARCH_HOME, list);

    }
    /**
     * 接口回调
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
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
     * 适配器装数据
     */
    protected void setData(Map map) {
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
                map_temp.put("distance", jo.get("jl")+"公里之内");
                list.add(map_temp);
            }
        } catch (JSONException e) {
        }
        listAdapter.notifyDataSetChanged();
    }
}
