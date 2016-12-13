package com.xbh.tmi.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hyphenate.chat.EMClient;
import com.xbh.tmi.DemoHelper;
import com.xbh.tmi.R;

import tm.ui.welcome.GuideActivity;
import tm.utils.ViewUtil;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;
    private SharedPreferences sharedPre;
    private boolean hasOpened;

    private static final int sleepTime = 2000;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    static BDLocation lastLocation = null;

    @Override
    protected void onCreate(Bundle arg0) {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// open gps
        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        setContentView(R.layout.em_activity_splash);
        super.onCreate(arg0);
        //获取SharedPreferences对象
        sharedPre=this.getSharedPreferences("config", this.MODE_PRIVATE);
        hasOpened = sharedPre.getBoolean("hasOpened",false);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {

                try {
                    if (DemoHelper.getInstance().isLoggedIn()) {
                        long start = System.currentTimeMillis();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        long costTime = System.currentTimeMillis() - start;
                        //wait
                        if (sleepTime - costTime > 0) {
                            Thread.sleep(sleepTime - costTime);
                        }
                        //enter main screen
                        ViewUtil.jumpToOtherActivity(SplashActivity.this,MainActivity.class,true);
                        finish();
                    } else if(!hasOpened){
                        Thread.sleep(sleepTime);
                        ViewUtil.jumpToOtherActivity(SplashActivity.this,GuideActivity.class,true);
                    }else{
                        Thread.sleep(sleepTime);
                        ViewUtil.jumpToOtherActivity(SplashActivity.this,LoginActivity.class,true);
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
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            lastLocation = location;
            saveLoginInfo(SplashActivity.this,lastLocation.getLatitude(),lastLocation.getLongitude());

        }
    }
    /**
     * 使用SharedPreferences保存用户登录信息
     * @param context
     */
    public static void saveLoginInfo(Context context, double lat , double lng){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("lat", String.valueOf(lat));
        editor.putString("lng", String.valueOf(lng));
        //提交
        editor.commit();
    }
    @Override
    protected void onDestroy() {
        if (mLocationClient != null)
            mLocationClient.stop();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
        super.onResume();
    }
    @Override
    protected void onPause() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }
}
