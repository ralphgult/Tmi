package tm.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LKing on 2017/1/12.
 */

public class ActionActivity extends Activity {
    ImageView mBackImg;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = { R.drawable.default_pic, R.drawable.default_pic,
            R.drawable.default_pic, R.drawable.default_pic, R.drawable.default_pic,
            R.drawable.default_pic, R.drawable.default_pic, R.drawable.default_pic,
            R.drawable.default_pic, R.drawable.default_pic, R.drawable.default_pic,
            R.drawable.default_pic };
    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_activity);
        initView();
    }
    private void initView() {
        goBackImg();
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.action_activity_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    /*** 标题的返回按钮初始化并监听点击事件 */
    private void goBackImg() {
        mBackImg = (ImageView)findViewById(R.id.auction_back_iv);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
