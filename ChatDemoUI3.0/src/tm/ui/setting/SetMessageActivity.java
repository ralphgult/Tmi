package tm.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.xbh.tmi.R;

import tm.utils.ViewUtil;

public class SetMessageActivity extends Activity implements View.OnClickListener{
    private ImageView back;
    private EaseSwitchButton mSound;
    private EaseSwitchButton mStatusbar;
    private EaseSwitchButton mRing;
    private EaseSwitchButton mViberate;
    private boolean isSwitchSound = true;
    private boolean isSwitchStatusbar = true;
    private boolean isSwitchRing = true;
    private boolean isSwitchViberate = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_message);
        initViews();
    }
    private void initViews(){
        back = (ImageView) findViewById(R.id.setmsg_back_iv);
        back.setOnClickListener(this);
        mSound = (EaseSwitchButton) findViewById(R.id.sound_swt);
        mSound.closeSwitch();
        mSound.setOnClickListener(this);
        mStatusbar = (EaseSwitchButton) findViewById(R.id.statusbar_swt);
        mStatusbar.closeSwitch();
        mStatusbar.setOnClickListener(this);
        mRing = (EaseSwitchButton) findViewById(R.id.ring_swt);
        mRing.closeSwitch();
        mRing.setOnClickListener(this);
        mViberate = (EaseSwitchButton) findViewById(R.id.viberate_swt);
        mViberate.closeSwitch();
        mViberate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setmsg_back_iv:
                ViewUtil.backToOtherActivity(SetMessageActivity.this);
                break;
            case R.id.sound_swt:
                if(isSwitchSound){//打开
                    mSound.openSwitch();
                    isSwitchSound = false;
                }else{//关闭
                    mSound.closeSwitch();
                    isSwitchSound = true;
                }
                break;
            case R.id.statusbar_swt:
                if(isSwitchStatusbar){//打开
                    mStatusbar.openSwitch();
                    isSwitchStatusbar = false;
                }else{//关闭
                    mStatusbar.closeSwitch();
                    isSwitchStatusbar = true;
                }
                break;
            case R.id.ring_swt:
                if(isSwitchRing){//打开
                    mRing.openSwitch();
                    isSwitchRing = false;
                }else{//关闭
                    mRing.closeSwitch();
                    isSwitchRing = true;
                }
                break;
            case R.id.viberate_swt:
                if(isSwitchViberate){//打开
                    mViberate.openSwitch();
                    isSwitchViberate = false;
                }else{//关闭
                    mViberate.closeSwitch();
                    isSwitchViberate = true;
                }
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ViewUtil.backToOtherActivity(SetMessageActivity.this);
        }
        return true;
    }
}
