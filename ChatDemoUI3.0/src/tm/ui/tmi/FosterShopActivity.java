package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import tm.alipay.AlipayAPI;
import tm.alipay.PayResult;
import tm.ui.mine.ShoppingPayActivity;

/**
 * Created by Lking on 2016/12/22.
 */

public class FosterShopActivity extends Activity {
    private static final int SDK_PAY_FLAG = 1;

    String name;
    String price;
    ImageView mBackTV;
    TextView mNameTV;
    TextView mAddTV;
    TextView mSumTV;
    TextView mDeleteTV;
    TextView mMoneyTV;
    TextView mSureTV;
    int sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_shopping_activity);

        getIntentString();
        initView();
        setLister();

    }

    private void getIntentString(){
        if(null != getIntent().getExtras()){
            name  = getIntent().getExtras().getString("name");
            price  = getIntent().getExtras().getString("price");
        }
    }

    private void initView(){
        mBackTV = (ImageView) findViewById(R.id.foster_shop_back_iv);
        mNameTV = (TextView) findViewById(R.id.foster_comm_name_tv);
        mAddTV = (TextView) findViewById(R.id.foster_comm_add_tv);
        mSumTV = (TextView) findViewById(R.id.foster_comm_sum_tv);
        mDeleteTV = (TextView) findViewById(R.id.foster_comm_delete_tv);
        mMoneyTV = (TextView) findViewById(R.id.foster_comm_money_tv);
        mSureTV = (TextView) findViewById(R.id.foster_comm_sure_tv);
        mNameTV.setText("商品名称："+name);
        mMoneyTV.setText("商品金额：" + price + "元");
    }
    private void setLister(){
        mAddTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sum = Integer.parseInt(mSumTV.getText().toString())+1;
                mSumTV.setText(sum +"");
                mMoneyTV.setText("商品金额："+(sum *Integer.parseInt(price)) + "元");
            }
        });
        mDeleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sum <=1){
                    sum = 1;
                }else{
                    sum = Integer.parseInt(mSumTV.getText().toString())-1;
                }
                mSumTV.setText(sum +"");
                mMoneyTV.setText("商品金额："+(sum *Integer.parseInt(price)) + "元");
            }
        });
        mSureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AliPayThread().start();
            }
        });
        mBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

                        //TODO 生成订单（商户账号（买的谁的商品））
                        String subject = name;//商品名字
                        String body = name+sum+"件";//商品描述
                        String total_fee = price;//价格
                        Log.e("Lking","上传订单 = "+"商品名字："+subject+"；商品描述："+body+"；商品价格："+total_fee);

                        Toast.makeText(FosterShopActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(FosterShopActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(FosterShopActivity.this,
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
            String result = AlipayAPI.pay(FosterShopActivity.this,name,name+sum+"件",price);
            Log.e("Lking","支付返回值 = "+result);
//            String result = AlipayAPI.pay(ShoppingPayActivity.this, "测试的商品",
//                    "测试商品的详细描述", "0.01");
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }


}
