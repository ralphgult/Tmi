package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.alipay.AlipayAPI;
import tm.alipay.PayResult;
import tm.ui.mine.adapter.MyShoppingAdapter;
import tm.utils.ViewUtil;

public class ShoppingPayActivity extends Activity implements View.OnClickListener{
    private static final int SDK_PAY_FLAG = 1;

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
        mList.setAdapter(mAdapter);
        setTotalPrice();
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
                new AliPayThread().start();
//                new Thread(){
//                    @Override
//                    public void run() {
//                        AlipayAPI.pay(ShoppingPayActivity.this,"全部商品", "购物车商品", mTotalPrice);
//                    }
//                }.start();
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
        mTotalPri.setText("￥" + mTotalPrice);
    }

    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    System.out.println("支付返回 = "+(String) msg.obj);
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ShoppingPayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ShoppingPayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ShoppingPayActivity.this,
                                    "支付失败" + resultStatus+resultInfo, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    break;
                }
            }
        };
    };
    /**
     * 支付宝支付异步任务
     *
     * @author Simon
     */
    private class AliPayThread extends Thread {
        @Override
        public void run() {
            String result =AlipayAPI.pay(ShoppingPayActivity.this,"全部商品", "购物车商品", mTotalPrice);
//            String result = AlipayAPI.pay(ShoppingPayActivity.this, "测试的商品",
//                    "测试商品的详细描述", "0.01");
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
