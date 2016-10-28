package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.ui.mine.adapter.AddressAdapter;
import tm.utils.ViewUtil;

public class MyAddressActivity extends Activity {
    private ImageView back;
    private ListView addrList;
    private AddressAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initViews();
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.my_address_back_iv);
        addrList = (ListView) findViewById(R.id.my_address_lsit_lv);
        mAdapter = new AddressAdapter(this);
        mAdapter.resetData(getData());
        addrList.setAdapter(mAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(MyAddressActivity.this);
            }
        });
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list  = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("name","张欧诺");
        map.put("phone","15211455121");
        map.put("addr", "陕西省 西安市 雁塔区 丈八沟街道 锦业时代");
        list.add(map);
        map = new HashMap<>();
        map.put("name","四大");
        map.put("phone","15211455121");
        map.put("addr", "陕西省 西安市 雁塔区 丈八沟街道 绿地世纪城");
        list.add(map);
        return list;
    }
}
