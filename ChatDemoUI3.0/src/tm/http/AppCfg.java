package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.tmdemo.DemoApplication;

import tm.entity.UserBean;

public class AppCfg extends DemoApplication {

    public static AppCfg _instance = null;
    public Context context;

    public static String type = "1";
    public String userId = "";
    public String userName = "";
    public String password = "";
    public String realName = "";
    public String LoginType = "0"; // 0:未登录 1:用户名密码 2：第三方登陆
    public String ThirdToken = "";// 第三方token
    public String ThirdType = ""; // 1:微信登录 2：QQ登录 3：sina登录

    /***
     * 经度
     */
    public static String longitude = "";
    /***
     * 纬度
     */
    public static String latitude = "";
    public static String locAddr = "";

    private UserBean currentUser;

    public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    static {
        if (_instance == null) {
            _instance = new AppCfg();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getParameters(this);
        initUmengParameter();
    }

    private void initUmengParameter() {
//        PlatformConfig.setWeixin("wx967daebe835fbeac",
//                "5bb696d9ccd75a38c8a0bfe0675559b3");
//        // 微信 appid appsecret
//        PlatformConfig.setSinaWeibo("3186265802",
//                "6ca25ceb48f24a9aacb7b806948ca886");
        // 新浪微博 appkey appsecret
        // PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        // 易信 appkey
        // PlatformConfig.setRenren("201874","28401c0964f04a72a14c812d6132fcef","3bf66e42db1e4fa9829b955cc300b737");
        // 人人 appid appkey appsecret
//        PlatformConfig.setQQZone("1105353663",
//                "otLC5Hib63KMRZOj");
        // qq qzone appid appkey
        //PlatformConfig.setAlipay("2015111700822536");
        // alipay appid
    }

    public void getParameters(Context context) {
        this.context = context;
        try {
            Config.init(context);
        } catch (IOException e) {
        }
        SharedPreferences spf = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        userId = spf.getString("userId", "");
        userName = spf.getString("userName", "");
        password = spf.getString("password", "");
        realName = spf.getString("realName", "");
        LoginType = spf.getString("LoginType", "");
        ThirdToken = spf.getString("ThirdToken", "");
        ThirdType = spf.getString("ThirdType", "");
    }

    public void save() {
        if (context != null) {
            SharedPreferences spf = context.getSharedPreferences("userInfo",
                    Context.MODE_PRIVATE);
            Editor edit = spf.edit();
            edit.putString("userId", userId);
            edit.putString("userName", userName);
            edit.putString("password", password);
            edit.putString("realName", realName);
            edit.putString("LoginType", LoginType);
            edit.putString("ThirdToken", ThirdToken);
            edit.putString("ThirdType", ThirdType);
            edit.commit();
        }
    }

}

