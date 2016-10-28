package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.ui.tmi.adapter.MomentListAdapter;
import tm.utils.ViewUtil;

public class MomentsActivity extends Activity {
    private ImageView back;
    private ListView data_lv;
    private TextView title;
    private List<Map<String, String>> datas;
    private TextView nodata;
    private MomentListAdapter mAdapter;
    private boolean isMoment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        isMoment = getIntent().getExtras().getBoolean("isMoment");
        initViewAndListeners();
    }

    private void initViewAndListeners() {
        back = (ImageView) findViewById(R.id.moment_back_iv);
        data_lv = (ListView) findViewById(R.id.moment_list_lv);
        nodata = (TextView) findViewById(R.id.moment_nodata);
        title = (TextView) findViewById(R.id.moment_title_text);
        title.setText(isMoment ? "朋友圈" : "T觅资讯");
        mAdapter = new MomentListAdapter(this,true);
        mAdapter.resetData(getSourceData());
        data_lv.setAdapter(mAdapter);
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
    }

    private List<Map<String, String>> getSourceData(){
        String downloadFloder = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download";
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<>();
        map.put("headImg",downloadFloder + File.separator + "001.jpg");
        map.put("name", "王志鹏");
        map.put("time", "5.26  20:35");
        map.put("content", "“天下事抬不过一个理字”，为什么这么多人觉得口才会胜过摆事实讲道理呢？");
        map.put("pics", downloadFloder + File.separator + "003.png");
        map.put("count", "112");
        map.put("like", "60");
        map.put("comment", "23");
        dataList.add(map);
        map = new HashMap<>();
        map.put("headImg",downloadFloder + File.separator + "001.jpg");
        map.put("name", "王志鹏");
        map.put("time", "5.26  20:35");
        map.put("content", "盖在北戴河沙滩上的教堂，据说潮起时会淹没来时的路，这个概念有点让人想起Mount");
        map.put("pics", downloadFloder + File.separator + "001.jpg," + downloadFloder + File.separator + "002.jpeg," + downloadFloder + File.separator + "003.png");
        map.put("count", "112");
        map.put("like", "60");
        map.put("comment", "23");
        dataList.add(map);
        return dataList;
    }
}
