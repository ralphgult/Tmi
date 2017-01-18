package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oohla.android.utils.StringUtil;
import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.alipay.AlipayAPI;
import tm.ui.mine.adapter.MyShoppingAdapter;
import tm.utils.ImageUtil;
import tm.utils.ViewUtil;

public class ShoppingPayActivity extends Activity implements View.OnClickListener{
    private ImageView mBack;
    private ListView mList;
    private TextView mTotalPri;
    private Button mPay;

    private MyShoppingAdapter mAdapter;
    private List<Map<String, String>> mDataList;
    private List<String> mNameList;
    private List<String> mImgList;
    private List<String> mPriList;
    private List<String> mCountList;
    private List<String> mGoodIdList;
    private String mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_pay);
        init();
        initViews();
    }

    private void init() {
        mAdapter = new MyShoppingAdapter(this, true);
        Bundle bundle = getIntent().getExtras();
        mNameList = bundle.getStringArrayList("names");
        mImgList = bundle.getStringArrayList("imgs");
        mPriList = bundle.getStringArrayList("pris");
        mCountList = bundle.getStringArrayList("cartCount");
        mGoodIdList = bundle.getStringArrayList("ids");
        getListData();
        mAdapter.resetData(mDataList);
        mTotalPrice = bundle.getString("totalPrice");
    }

    private void getListData(){
        Map<String, String> map = null;
        mDataList = new ArrayList<>();
        int size = mNameList.size();
        for (int i = 0; i < size; i++) {
            map = new HashMap<>();
            map.put("goodName", mNameList.get(i));
            map.put("goodImg", mImgList.get(i));
            map.put("currentPrice", mPriList.get(i));
            map.put("cartCount", mCountList.get(i));
            map.put("goodId", mGoodIdList.get(i));
            mDataList.add(map);
        }
    }

    private void initViews() {
        mBack = (ImageView) findViewById(R.id.shopping_pay_back_iv);
        mList = (ListView) findViewById(R.id.shopping_pay_list_lv);
        mTotalPri = (TextView) findViewById(R.id.shopping_pay_total_price_tv);
        mPay = (Button) findViewById(R.id.shopping_pay_pay_btn);
        mTotalPri.setText("￥" + mTotalPrice);
        mList.setAdapter(mAdapter);
        mBack.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mGoodIdList.get(position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_pay_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.shopping_pay_pay_btn:
                new Thread(){
                    @Override
                    public void run() {
                        AlipayAPI.pay(ShoppingPayActivity.this,"全部商品", "购物车商品", mTotalPrice);
                    }
                }.start();
                break;
        }
    }

    public void setTotalPrice(){
        mDataList = mAdapter.getSourceData();
        float totalPri = 0.0f;
        for (Map<String, String> map : mDataList) {
            totalPri = totalPri + Float.valueOf(map.get("currentPrice")) * Float.valueOf(map.get("cartCount"));
        }
        mTotalPrice = String.valueOf(totalPri);
        mTotalPri.setText(mTotalPrice);
    }
}
