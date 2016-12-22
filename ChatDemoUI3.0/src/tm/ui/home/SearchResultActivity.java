package tm.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private ListView mListView;
    ArrayList<HashMap> list = new ArrayList<HashMap>();
    SearchResultAdapter listAdapter;
    private  String lat;
    private  String lng;
    private String username;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_search_result_aty);
        searchin=getIntent().getStringExtra("companyName");
        type=getIntent().getStringExtra("type");
        SharedPreferences sharedPre=getSharedPreferences("config",MODE_PRIVATE);
        username=sharedPre.getString("username", "");
        lat=sharedPre.getString("lat","");
        lng=sharedPre.getString("lng","");
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(SearchResultActivity.this, GeranActivity.class);
                Log.e("info","arg2===22="+arg2);
                intent.putExtra("id", listAdapter.getItemId(arg2)+"");
                intent.putExtra("uid", listAdapter.getItemzhanghao(arg2)+"");
                startActivity(intent);
            }
        });
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

        Log.e("info","lat=22="+lat);
        Log.e("info","lng=22="+lng);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", ""+username));
        list.add(new BasicNameValuePair("wd", ""+lat));
        list.add(new BasicNameValuePair("jd", ""+lng));
        list.add(new BasicNameValuePair("page", "0"));
        list.add(new BasicNameValuePair("num", "15"));
        list.add(new BasicNameValuePair("companyName", searchin));
        if(type.equals("0")){
            list.add(new BasicNameValuePair("type", "1"));
        }else if(type.equals("1")){
            list.add(new BasicNameValuePair("type", "2"));
        }else if(type.equals("2")){
            list.add(new BasicNameValuePair("type", "3"));
        }
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
                map_temp.put("uname", jo.get("uname")+"");
                list.add(map_temp);
            }
        } catch (JSONException e) {
        }
        listAdapter.notifyDataSetChanged();
    }
}
