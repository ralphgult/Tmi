package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Environment;

public class Config {
//    public static String ROOT_HOST_NAME="http://hsaiqs.xicp.net:20806";
    public static String ROOT_HOST_NAME="http://121.196.244.27:3003";
//        public static String ROOT_HOST_NAME="http://192.168.1.110:8080";

    public static String URL_LOGIN_USER=ROOT_HOST_NAME+"/CBDParkingMSs/login"; // 用户登录
    public static String URL_GET_RECOMMEND_USERS=ROOT_HOST_NAME+"/CBDParkingMSs/recommend";//获取推荐列表接口
    public static String URL_GET_NEARLY_USERS=ROOT_HOST_NAME+"/CBDParkingMSs/near";//获取附近列表接口
    public static String URL_GET_TS_USERS=ROOT_HOST_NAME+"/CBDParkingMSs/ts";//获取本地特色列表接口
    public static String URL_GET_USER_HOME=ROOT_HOST_NAME+"/CBDParkingMSs/mood/personal";//获取个人首页接口
    public static String URL_GET_QIYE_HOME=ROOT_HOST_NAME+"/CBDParkingMSs/goods/homepage";//获取企业首页接口
    public static String URL_GET_SEARCH_HOME=ROOT_HOST_NAME+"/CBDParkingMSs/searchRecommend";//获取热荐搜索接口
    public static String URL_GET_ADDFRIEND=ROOT_HOST_NAME+"/CBDParkingMSs/friends/addFriend";//添加好友接口
    public static String URL_REDGIST = ROOT_HOST_NAME+"/CBDParkingMSs/regist";
    public static String URL_FIND_PASSWORD = ROOT_HOST_NAME+"/CBDParkingMSs/updatePwd";


    public static void init(Context context) throws IOException {
    }
}
