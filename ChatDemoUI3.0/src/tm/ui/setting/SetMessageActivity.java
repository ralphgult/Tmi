package tm.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.SettingsFragment;

import tm.utils.ViewUtil;

public class SetMessageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_message);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commitAllowingStateLoss();

    }


}
