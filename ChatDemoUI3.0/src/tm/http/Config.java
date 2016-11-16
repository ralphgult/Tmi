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
//    public static String ROOT_HOST_NAME = "http://192.168.1.101:8080";
//        public static String ROOT_HOST_NAME="http://192.168.1.110:8080";

    public static String URL_LOGIN_USER = ROOT_HOST_NAME + "/CBDParkingMSs/login"; // 用户登录
    public static String URL_GET_RECOMMEND_USERS = ROOT_HOST_NAME + "/CBDParkingMSs/recommend";//获取推荐列表接口
    public static String URL_GET_NEARLY_USERS = ROOT_HOST_NAME + "/CBDParkingMSs/near";//获取附近列表接口
    public static String URL_GET_TS_USERS = ROOT_HOST_NAME + "/CBDParkingMSs/ts";//获取本地特色列表接口
    public static String URL_GET_USER_HOME = ROOT_HOST_NAME + "/CBDParkingMSs/mood/personal";//获取个人首页接口
    public static String URL_GET_QIYE_HOME = ROOT_HOST_NAME + "/CBDParkingMSs/goods/homepage";//获取企业首页接口
    public static String URL_GET_SEARCH_HOME = ROOT_HOST_NAME + "/CBDParkingMSs/searchRecommend";//获取热荐搜索接口
    public static String URL_GET_ADDFRIEND = ROOT_HOST_NAME + "/CBDParkingMSs/friends/addFriend";//添加好友接口
    public static String URL_GET_ZIXUN_HOME = ROOT_HOST_NAME + "/CBDParkingMSs/information";//添加主页资讯接口
    public static String URL_MOMENT = ROOT_HOST_NAME + "/CBDParkingMSs/mood/pyq";//朋友圈接口
    public static String URL_REDGIST = ROOT_HOST_NAME + "/CBDParkingMSs/regist";//注册接口
    public static String URL_FIND_PASSWORD = ROOT_HOST_NAME + "/CBDParkingMSs/updatePwd";//修改密码接口
    public static String URL_GET_USER_POFILE = ROOT_HOST_NAME + "/CBDParkingMSs/info";//我的页面获取用户信息接口
    public static String URL_GET_USRE_FACEWALL = ROOT_HOST_NAME + "/CBDParkingMSs/faceScore/faceScore";//获取用户颜值表
    public static String URL_CHANGE_SIGN = ROOT_HOST_NAME + "/CBDParkingMSs/updatemse";//修改信息
    public static String URL_UPDATE_IMAGE = ROOT_HOST_NAME + "/CBDParkingMSs/updatePhoto";//上传头像
    public static String URL_CHANGE_INFO = ROOT_HOST_NAME + "/CBDParkingMSs/updatemse";//修改信息
    public static String URL_GET_TMIMESSAGE = ROOT_HOST_NAME + "/CBDParkingMSs/mood/information";//获取T觅资讯列表
    public static String URL_ADD_MOMENT = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addMood";//发布朋友圈
    public static String URL_MOMENT_LIKE = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addPraise";//朋友圈点赞
    public static String URL_MOMENT_COMMENT = ROOT_HOST_NAME + "/CBDParkingMSs/mc/addComment";//朋友圈评论
    public static String URL_ADD_INFOMATION = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addInformation";//发布T觅资讯


    public static void init(Context context) throws IOException {
    }
}
