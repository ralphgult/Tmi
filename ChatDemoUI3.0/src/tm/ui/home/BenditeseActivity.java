package tm.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;

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
import tm.ui.home.Adapter.BenditeseAdapter;
import tm.utils.ConstantsHandler;

/**
 * Created by meixi on 2016/9/5.
 */
public class BenditeseActivity extends BaseActivity {
    public static final String TAG = "GroupsActivity";
    private ListView mListView;
    private BenditeseAdapter benditeseAdapter;
    ArrayList<HashMap> list = new ArrayList<HashMap>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_bendi_lv);
        init();
        position=getIntent().getExtras().getInt("position");
        Log.e("info","position=222="+position);
        LoadData(position+1);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(){

        mListView=(ListView)findViewById(R.id.list);
        benditeseAdapter = new BenditeseAdapter(this, list);
        mListView.setAdapter(benditeseAdapter);
    }
    /**
     * 接口推荐数据
     */
    public void LoadData(int userType) {
        SharedPreferences sharedPre=getSharedPreferences("config",this.MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userId", username));
        list.add(new BasicNameValuePair("userType", userType+""));
        list.add(new BasicNameValuePair("page", "1"));
        list.add(new BasicNameValuePair("num", "15"));
        NetFactory.instance().commonHttpCilent(handler, this,
                Config.URL_GET_TS_USERS, list);
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
        benditeseAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }
//    public List<ResourcesBean> getData() {
//        ArrayList<ResourcesBean> list = new ArrayList<ResourcesBean>();
//        ResourcesBean bean1 = new ResourcesBean();
//        bean1.mResourcesName="郭德纲打野";
//        list.add(0, bean1);
//        return list;
//    }

}
