package tm.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

public class FarmerCenterActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private LinearLayout logo;
    private TextView fengcai;
    private RelativeLayout name;
    private RelativeLayout sign;
    private LinearLayout qr;
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);
        back = (ImageView) findViewById(R.id.farmer_center_back_iv);
        logo = (LinearLayout) findViewById(R.id.farmer_center_head_rv);
        fengcai = (TextView) findViewById(R.id.farmer_center_fengcai_tv);
        name = (RelativeLayout)findViewById(R.id.farmer_center_name_text_rv);
        sign = (RelativeLayout)findViewById(R.id.farmer_center_sign_rv);
        qr = (LinearLayout)findViewById(R.id.farmer_center_qr_ly);
        ok = (TextView) findViewById(R.id.farmer_center_ok_tv);
        qr.setOnClickListener(this);
        sign.setOnClickListener(this);
        name.setOnClickListener(this);
        fengcai.setOnClickListener(this);
        logo.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.farmer_center_back_iv:
                this.finish();
                break;
            case R.id.farmer_center_ok_tv:
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"正在开发中...",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
