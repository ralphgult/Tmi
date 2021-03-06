package tm.ui.mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
    public Handler mHandler;

    private List<Map<String, String>> mDatas;
    private TextView add;
    private ProgressDialog pd;

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
                if (null != pd) {
                    pd.dismiss();
                }
                switch (msg.what) {
                    case 1001:
                        try {
                            JSONArray array = (JSONArray) msg.obj;
                            if (null != array && array.length() > 0) {
                                int size = array.length();
                                mDatas = new ArrayList<Map<String, String>>(size);
                                Map<String, String> map;
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    map = new HashMap<>();
                                    map.put("id", object.optString("raId"));
                                    map.put("name", object.optString("name"));
                                    map.put("phone", object.optString("phone"));
                                    map.put("addr", object.optString("address"));
                                    map.put("default", object.optString("isDefault"));
                                    mDatas.add(map);
                                }
                            } else {
                                mDatas = new ArrayList<Map<String, String>>();
                            }
                            mAdapter.resetData(mDatas);
                            addrList.setAdapter(mAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MyAddressActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2001:
                        Toast.makeText(MyAddressActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        mDatas.clear();
                        getData();
                        mAdapter.resetData(mDatas);
                        break;
                    case 3001:
                        getData();
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
                bundle.putBoolean("isAdd", true);
                ViewUtil.jumpToOherActivityForResult(MyAddressActivity.this, EditAddressActivity.class, bundle,10000);
            }
        });
        getData();
        addrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = mDatas.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", map.get("id"));
                bundle.putString("name", map.get("name"));
                bundle.putString("phone", map.get("phone"));
                bundle.putString("content", map.get("addr"));
                String str = map.get("default");
                bundle.putString("default", str.equals("1") ? str : "0");
                bundle.putBoolean("isAdd", false);
                ViewUtil.jumpToOherActivityForResult(MyAddressActivity.this, EditAddressActivity.class, bundle,10000);
            }
        });
    }

    private void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            new Thread() {
                @Override
                public void run() {
                    PersonManager.getAddresses(null, mHandler);
                }
            }.start();
        }
    }

    public void deleteAddress(final String addrId){
        pd = ProgressDialog.show(this, "删除", "删除地址中，请稍后...");
        new Thread(){
            @Override
            public void run() {
                PersonManager.deleteAddress(addrId,mHandler);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10000 && resultCode == 1000) {
            getData();
        }
    }
}
