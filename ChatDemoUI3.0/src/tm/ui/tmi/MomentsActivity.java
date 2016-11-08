package tm.ui.tmi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.tmi.adapter.MomentListAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ViewUtil;

public class MomentsActivity extends Activity {
    private ImageView back;
    private ListView data_lv;
    private TextView title;
    private List<Map<String, String>> datas;
    private TextView nodata;
    private MomentListAdapter mAdapter;
    private boolean isMoment;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    String authId = map.get("state");
                    if (authId.equals("1")) {
                        try {
                            String moments = "{\"rows\":" + map.get("rows") + "}";
                            JSONObject object = new JSONObject(moments);
                            JSONArray momentList = object.getJSONArray("rows");
                            int size = momentList.length();
                            for (int i = 0; i < size; i++) {
                                StringBuffer sb = new StringBuffer();
                                map = new HashMap<String, String>();
                                JSONObject obj = momentList.getJSONObject(i);
                                map.put("name", obj.getString("username"));
                                map.put("headImg", obj.getString("photo"));
                                map.put("time", obj.getString("create_date"));
                                map.put("content", obj.getString("mood_content"));
                                JSONArray arrayImg = obj.getJSONArray("mp");
                                if (null != arrayImg && arrayImg.length() > 0) {
                                    for (int j = 0; j < arrayImg.length(); j++) {
                                        sb.append(arrayImg.getJSONObject(j).getString("mpName") + ",");
                                    }
                                    map.put("pics", sb.substring(0, sb.length() - 1));
                                }else{
                                    map.put("pics", "");
                                }
                                map.put("like", obj.getString("countMps"));
                                map.put("comment", obj.getString("countReply"));
                                map.put("count", obj.getString("countBrowse"));
                                datas.add(map);
                                Log.e("info","momentMap ========== " + map);
                            }
                            mAdapter.resetData(datas);
                            data_lv.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MomentsActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(MomentsActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        isMoment = getIntent().getExtras().getBoolean("isMoment");
        datas = new ArrayList<>();
        initViewAndListeners();
    }

    private void initViewAndListeners() {
        back = (ImageView) findViewById(R.id.moment_back_iv);
        data_lv = (ListView) findViewById(R.id.moment_list_lv);
        nodata = (TextView) findViewById(R.id.moment_nodata);
        title = (TextView) findViewById(R.id.moment_title_text);
        title.setText(isMoment ? "朋友圈" : "T觅资讯");
        mAdapter = new MomentListAdapter(this, true);
        getSourceData(0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(MomentsActivity.this);
            }
        });
        data_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.getDataList().get(position);
            }
        });
        data_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void getSourceData(int page) {
        page = 0;
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            Log.e("info", "userId ====== " + userId);
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("page", String.valueOf(page)));
            list.add(new BasicNameValuePair("num", "10"));
        }
        NetFactory.instance().commonHttpCilent(mHandler, this,
                Config.URL_MOMENT, list);
    }

//    private List<Map<String, String>> getSourceData() {
//        String downloadFloder = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download";
//        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//        Map<String, String> map = new HashMap<>();
//        map.put("headImg", downloadFloder + File.separator + "001.jpg");
//        map.put("name", "王志鹏");
//        map.put("time", "5.26  20:35");
//        map.put("content", "“天下事抬不过一个理字”，为什么这么多人觉得口才会胜过摆事实讲道理呢？");
//        map.put("pics", downloadFloder + File.separator + "003.png");
//        map.put("count", "112");
//        map.put("like", "60");
//        map.put("comment", "23");
//        dataList.add(map);
//        map = new HashMap<>();
//        map.put("headImg", downloadFloder + File.separator + "001.jpg");
//        map.put("name", "王志鹏");
//        map.put("time", "5.26  20:35");
//        map.put("content", "盖在北戴河沙滩上的教堂，据说潮起时会淹没来时的路，这个概念有点让人想起Mount");
//        map.put("pics", downloadFloder + File.separator + "001.jpg," + downloadFloder + File.separator + "002.jpeg," + downloadFloder + File.separator + "003.png");
//        map.put("count", "112");
//        map.put("like", "60");
//        map.put("comment", "23");
//        dataList.add(map);
//        return dataList;
//    }
}
