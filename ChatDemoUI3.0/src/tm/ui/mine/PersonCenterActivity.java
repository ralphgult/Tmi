package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;


public class PersonCenterActivity extends Activity implements View.OnClickListener{
    private ImageView back;
    private LinearLayout head;
    private TextView yanzhi;
    private RelativeLayout sign;
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        back = (ImageView) findViewById(R.id.person_center_back_iv);
        head = (LinearLayout) findViewById(R.id.person_center_head_rv);
        yanzhi = (TextView)findViewById(R.id.person_center_yanzhi_tv);
        sign = (RelativeLayout)findViewById(R.id.person_center_sign_text_rv);
        ok = (TextView)findViewById(R.id.person_center_ok_tv);
        ok.setOnClickListener(this);
        head.setOnClickListener(this);
        yanzhi.setOnClickListener(this);
        sign.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.person_center_back_iv:
                PersonCenterActivity.this.finish();
                break;
            case R.id.person_center_ok_tv:
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"正在开发中...",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
