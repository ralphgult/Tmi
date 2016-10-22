package com.hyphenate.tmdemo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.tmdemo.DemoHelper;
import com.hyphenate.tmdemo.R;

import tm.ui.welcome.GuideActivity;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;
    private SharedPreferences sharedPre;
    private boolean hasOpened;

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.em_activity_splash);
        super.onCreate(arg0);
        //获取SharedPreferences对象
        sharedPre=this.getSharedPreferences("config", this.MODE_PRIVATE);
        hasOpened = sharedPre.getBoolean("hasOpened",false);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);

//		versionText.setText(getVersion());
//		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
//		animation.setDuration(1500);
//		rootLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {

                try {
                    if (DemoHelper.getInstance().isLoggedIn()) {
                        // auto login mode, make sure all group and conversation is loaed before enter the main screen
                        long start = System.currentTimeMillis();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        long costTime = System.currentTimeMillis() - start;
                        //wait
                        if (sleepTime - costTime > 0) {
                            Thread.sleep(sleepTime - costTime);
                        }
                        //enter main screen
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } /*else if(!hasOpened){
                        Thread.sleep(sleepTime);
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));

                        finish();
                    }*/else{
                        Thread.sleep(sleepTime);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * get sdk version
     */
    private String getVersion() {
        return EMClient.getInstance().getChatConfig().getVersion();
    }
}
