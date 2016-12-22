package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oohla.android.utils.NetworkUtil;
import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.adapter.AddressAdapter;
import tm.utils.ViewUtil;

public class MyAddressActivity extends Activity {
    private ImageView back;
    private ListView addrList;
    private AddressAdapter mAdapter;
    private Handler mHandler;

    private List<Map<String, String>> mDatas;
    private TextView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        init();
        initViews();
    }

    private void init() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1001:
                        try{
                            JSONArray array = (JSONArray) msg.obj;
                            if (null != array && array.length() > 0) {
                                int size = array.length();
                                mDatas = new ArrayList<Map<String, String>>(size);
                                Map<String, String> map;
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    map = new HashMap<>();
                                    map.put("id",object.optString("raId"));
                                    map.put("name", object.optString("name"));
                                    map.put("phone", object.optString("phone"));
                                    map.put("addr", object.optString("address"));
                                    map.put("default",object.optString("isDefault"));
                                }
                            }else {
                                mDatas = new ArrayList<Map<String, String>>();
                            }
                            mAdapter.resetData(mDatas);
                            addrList.setAdapter(mAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MyAddressActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(MyAddressActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.my_address_back_iv);
        add = (TextView) findViewById(R.id.my_address_add_tv);
        addrList = (ListView) findViewById(R.id.my_address_lsit_lv);
        mAdapter = new AddressAdapter(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(MyAddressActivity.this);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isAdd",true);
                ViewUtil.jumpToOtherActivity(MyAddressActivity.this, EditAddressActivity.class, bundle);
            }
        });
        getData();
    }

    private void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            new Thread(){
                @Override
                public void run() {
                    PersonManager.getAddresses(mHandler);
                }
            }.start();
        }
    }
}
