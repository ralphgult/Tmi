package tm.ui.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.tmdemo.DemoHelper;
import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.LoginActivity;

import tm.system.App;


public class SettingActivity extends Activity implements View.OnClickListener{
    private ImageView back_btn;
    private LinearLayout logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.tm_mine_setting_layout);
        initView();
    }
    private void initView(){
        back_btn = (ImageView) findViewById(R.id.setting_back_iv);
        logout_btn = (LinearLayout) findViewById(R.id.setting_btn_logout);
        back_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setting_back_iv:
                this.finish();
                break;
            case R.id.setting_btn_logout:
                EMClient.getInstance().logout(false);
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                App.getInstance().exit();
                break;
        }
    }
}
