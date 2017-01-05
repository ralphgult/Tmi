package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbh.tmi.R;

public class MySoppingActivity extends Activity {
    private ImageView mBack_iv;
    private TextView mTotalPri_tv;
    private Button mBuy_tv;
    private ListView mList_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sopping);
    }
    private void initView(){
        mBack_iv = (ImageView) findViewById(R.id.my_shopping_back_iv);
        mTotalPri_tv = (TextView) findViewById(R.id.xy_shopping_total_price_tv);
        mBuy_tv = (Button) findViewById(R.id.my_shopping_pay_btn);
    }
}
