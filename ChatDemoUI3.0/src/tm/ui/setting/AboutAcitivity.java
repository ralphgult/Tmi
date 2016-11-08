package tm.ui.setting;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;

import tm.utils.ViewUtil;

public class AboutAcitivity extends Activity {
    private ImageView back;
    private TextView vc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_acitivity);
        initView();
    }
    private void initView(){
        back = (ImageView) findViewById(R.id.about_back_iv);
        vc = (TextView) findViewById(R.id.version_tv);
        try {
            vc.setText("V"+ this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName+"\nT觅搜搜");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(AboutAcitivity.this);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return true;
    }
}
