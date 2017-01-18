package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.adapter.MyShoppingAdapter;
import tm.ui.tmi.GoodsDetilActivity;
import tm.utils.ViewUtil;

public class MySoppingActivity extends Activity implements View.OnClickListener {
    private ImageView mBack_iv;
    private TextView mTotalPri_tv;
    private Button mBuy_tv;
    private TextView mEdit_tv;
    private ListView mList_lv;

    private List<Map<String, String>> mDataList;
    private MyShoppingAdapter mAdapter;
    public Handler mHandler;
    private float mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sopping);
        init();
        initView();
        getShoppingList();
    }

    private void init() {
        mDataList = new ArrayList<>();
        mAdapter = new MyShoppingAdapter(this, false);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1001:
                        try {
                            JSONArray array = (JSONArray) msg.obj;
                            if (null != array && array.length() > 0) {
                                int size = array.length();
                                mDataList = new ArrayList<>(size);
                                Map<String, String> map = null;
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    map = new HashMap<>();
                                    map.put("goodId", object.getString("goodId"));
                                    map.put("goodName", object.getString("goodName"));
                                    map.put("scId", object.getString("scId"));
                                    map.put("currentPrice", object.getString("currentPrice"));
                                    map.put("cartCount", String.valueOf(object.getInt("cartCount")));
                                    map.put("goodImg", object.getString("goodImg"));
                                    map.put("createDate", object.getString("createDate"));
                                    mDataList.add(map);
                                }
                                mAdapter.resetData(mDataList);
                                mList_lv.setAdapter(mAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2001:
                        Toast.makeText(MySoppingActivity.this, "商品已删除", Toast.LENGTH_SHORT).show();
                        mDataList.remove((Map<String, String>) msg.obj);
                        mAdapter.resetData(mDataList);
                        break;
                    default:
                        Toast.makeText(MySoppingActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initView() {
        mBack_iv = (ImageView) findViewById(R.id.my_shopping_back_iv);
        mTotalPri_tv = (TextView) findViewById(R.id.xy_shopping_total_price_tv);
        mBuy_tv = (Button) findViewById(R.id.my_shopping_pay_btn);
        mList_lv = (ListView) findViewById(R.id.my_shopping_list_lv);
        mEdit_tv = (TextView) findViewById(R.id.my_shopping_edit_tv);
        mBack_iv.setOnClickListener(this);
        mBuy_tv.setOnClickListener(this);
        mEdit_tv.setOnClickListener(this);
        mList_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mDataList.get(position).get("goodId"));
                ViewUtil.jumpToOtherActivity(MySoppingActivity.this, GoodsDetilActivity.class, bundle);
            }
        });
    }

    private void getShoppingList() {
        new Thread() {
            @Override
            public void run() {
                PersonManager.getShoppingList(mHandler);
            }
        }.start();
    }

    public void setTotalPrice() {
        mTotalPrice = 0.00f;
        List<String> idList = mAdapter.getmSeletedIds();
        for (Map<String, String> map : mDataList) {
            if (idList.contains(map.get("scId"))) {
                float price = Float.valueOf(map.get("currentPrice")) * Integer.valueOf(map.get("cartCount"));
                mTotalPrice = mTotalPrice + price;
            }
        }
        mTotalPri_tv.setText("￥" + String.valueOf(mTotalPrice));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_shopping_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.my_shopping_pay_btn:
                List<String> ids = mAdapter.getmSeletedIds();
                ArrayList<String> mNameList = new ArrayList<>();
                ArrayList<String> mImgList = new ArrayList<>();
                ArrayList<String> mPriList = new ArrayList<>();
                ArrayList<String> mCountList = new ArrayList<>();
                ArrayList<String> mGoodIdList = new ArrayList<>();
                for (Map<String, String> map : mDataList) {
                    if (ids.contains(map.get("scId"))) {
                        mNameList.add(map.get("goodName"));
                        mImgList.add(map.get("goodImg"));
                        mPriList.add(map.get("currentPrice"));
                        mCountList.add(map.get("cartCount"));
                        mGoodIdList.add(map.get("goodId"));
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("names", mNameList);
                bundle.putStringArrayList("imgs", mImgList);
                bundle.putStringArrayList("pris", mPriList);
                bundle.putStringArrayList("cartCount", mCountList);
                bundle.putStringArrayList("ids", mGoodIdList);
                ViewUtil.jumpToOtherActivity(this, ShoppingPayActivity.class, bundle);
                break;
        }
    }
}
