package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import tm.alipay.AlipayAPI;

/**
 * Created by Lking on 2016/12/22.
 */

public class FosterShopActivity extends Activity {
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
                new Thread() {
                    @Override
                    public void run() {
                        AlipayAPI.pay(FosterShopActivity.this,name,name+sum+"件",price);
                    }
                }.start();
            }
        });
        mBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
