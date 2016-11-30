package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
    private int mType;
    private TextView momentAdd;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    if (mType == 4) {
                        phraseMomentData(msg);
                    } else {
                        phraseNoticeData(msg);
                    }
                    break;
                case 2001:
                    mAdapter.setLike(msg.arg1);
                    break;
                case 3001:
                    mAdapter.setComment((String) msg.obj, msg.arg1);
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
        mType = getIntent().getExtras().getInt("type");
        datas = new ArrayList<>();
        initViewAndListeners();
        getSourceData(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViewAndListeners() {
        back = (ImageView) findViewById(R.id.moment_back_iv);
        data_lv = (ListView) findViewById(R.id.moment_list_lv);
        nodata = (TextView) findViewById(R.id.moment_nodata);
        title = (TextView) findViewById(R.id.moment_title_text);
        momentAdd = (TextView) findViewById(R.id.moment_add_tv);
        title.setText(mType == 4 ? "朋友圈" : "T觅资讯");
        mAdapter = new MomentListAdapter(this, true, mHandler);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(MomentsActivity.this);
            }
        });
        momentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ReleaseType", mType);
                ViewUtil.jumpToOherActivityForResult(MomentsActivity.this, ReleaseFriendActivity.class, bundle, 1);
            }
        });
        data_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = mAdapter.getDataList().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("momentId", map.get("momentId"));
                bundle.putString("name", map.get("name"));
                bundle.putString("headImg", map.get("headImg"));
                bundle.putString("time", map.get("time"));
                bundle.putString("content", map.get("content"));
                bundle.putString("count", map.get("count"));
                bundle.putString("like", map.get("like"));
                bundle.putString("comment", map.get("comment"));
                bundle.putString("pics", map.get("pics"));
                bundle.putString("reply", map.get("reply"));
                bundle.putString("likelist", map.get("likelist"));
                bundle.putInt("type", mType);
                ViewUtil.jumpToOherActivityForResult(MomentsActivity.this, MomentDetilActivity.class, bundle, 10);
            }
        });

    }

    private void getSourceData(int page) {
        String url = null;
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("num", "50"));
            list.add(new BasicNameValuePair("page", String.valueOf(page)));
            if (mType == 4) {
                url = Config.URL_MOMENT;
            } else {
                url = Config.URL_GET_TMIMESSAGE;
                list.add(new BasicNameValuePair("type", getIntent().getExtras().getInt("type") + ""));
            }
        }
        NetFactory.instance().commonHttpCilent(mHandler, this, url, list);
    }

    private void phraseMomentData(Message msg) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> jsonMap = (Map<String, String>) msg.obj;
        String authId = jsonMap.get("state");
        if (authId.equals("1")) {
            try {
                String moments = "{\"rows\":" + jsonMap.get("rows") + "}";
                JSONObject object = new JSONObject(moments);
                JSONArray momentList = object.getJSONArray("rows");
                int size = momentList.length();
                for (int i = 0; i < size; i++) {
                    StringBuffer sb = new StringBuffer();
                    map = new HashMap<String, String>();
                    JSONObject obj = momentList.getJSONObject(i);
                    map.put("momentId", obj.getString("mood_id"));
                    map.put("name", obj.getString("username"));
                    map.put("headImg", obj.getString("photo"));
                    map.put("time", obj.getString("create_date"));
                    JSONArray arrayImg = obj.getJSONArray("mp");
                    if (null != arrayImg && arrayImg.length() > 0) {
                        for (int j = 0; j < arrayImg.length(); j++) {
                            sb.append(arrayImg.getJSONObject(j).getString("mpName") + ",");
                        }
                        map.put("pics", sb.substring(0, sb.length() - 1));
                    } else {
                        map.put("pics", "");
                    }
                    map.put("likelist", obj.getString("mps"));
                    map.put("like", obj.getString("countMps"));
                    map.put("comment", obj.getString("countReply"));
                    map.put("count", obj.getString("countBrowse"));
                    map.put("reply", obj.getString("reply"));
                    if (obj.has("mood_content")) {
                        map.put("content", obj.getString("mood_content"));
                        datas.add(map);
                    }
                }
                mAdapter.resetData(datas);
                data_lv.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MomentsActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
        }
    }

    private void phraseNoticeData(Message msg) {
        Map<String, String> jsonMap = (Map<String, String>) msg.obj;
        Map<String, String> map = new HashMap<>();
        String authId = jsonMap.get("state");
        if (authId.equals("1")) {
            try {
                JSONObject object = new JSONObject("{\"rows\":" + jsonMap.get("rows") + "}");
                JSONArray array = object.getJSONArray("rows");
                int size = Integer.valueOf(jsonMap.get("total"));
                for (int i = 0; i < size; i++) {
                    StringBuffer sb = new StringBuffer();
                    map = new HashMap<String, String>();
                    JSONObject obj = array.getJSONObject(i);
                    map.put("momentId", obj.getString("mood_id"));
                    map.put("name", obj.getString("user_name"));
                    map.put("headImg", obj.getString("photo"));
                    map.put("time", obj.getString("create_date"));
                    map.put("content", obj.getString("mood_content"));
                    map.put("id", obj.getString("mood_id"));
                    JSONArray arrayImg = obj.getJSONArray("mp");
                    if (null != arrayImg && arrayImg.length() > 0) {
                        for (int j = 0; j < arrayImg.length(); j++) {
                            sb.append(arrayImg.getJSONObject(j).getString("mpName") + ",");
                        }
                        map.put("pics", sb.substring(0, sb.length() - 1));
                    } else {
                        map.put("pics", "");
                    }
                    map.put("likelist", obj.getString("mps"));
                    map.put("like", obj.getString("countMps"));
                    map.put("comment", obj.getString("countReply"));
                    map.put("reply", obj.getString("reply"));
                    map.put("count", obj.getString("countBrowse"));
                    datas.add(map);
                }
                mAdapter.resetData(datas);
                data_lv.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MomentsActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if ( null != data && requestCode == 1) {
                if (resultCode == 1) {
                    int isFinish = data.getIntExtra("publishFinish", 0);
                    if (isFinish == 1) {
                        datas.clear();
                        getSourceData(0);
                    }
                }else{
                    boolean isDone = data.getBooleanExtra("isFinish",false);
                    if(isDone) {
                        datas.clear();
                        getSourceData(0);
                    }
                }
            }
    }
}
