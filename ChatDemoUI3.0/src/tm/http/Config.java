package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */

import android.content.Context;

import java.io.IOException;

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
    public static String URL_GET_TMIMESSAGE = ROOT_HOST_NAME + "/CBDParkingMSs/mood/information";//获取T觅资讯列表
    public static String URL_ADD_MOMENT = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addMood";//发布朋友圈
    public static String URL_MOMENT_LIKE = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addPraise";//朋友圈点赞
    public static String URL_MOMENT_COMMENT = ROOT_HOST_NAME + "/CBDParkingMSs/mc/addComment";//朋友圈评论
    public static String URL_ADD_INFOMATION = ROOT_HOST_NAME + "/CBDParkingMSs/mood/addInformation";//发布T觅资讯
    public static String URL_CHANGE_COMP_LOGO = ROOT_HOST_NAME + "/CBDParkingMSs/updateCompanyLogo";//修改企业Logo
    public static String URL_CHANGE_FARM_LOGO = ROOT_HOST_NAME + "/CBDParkingMSs/updateFarmLogo";//修改三农Logo
    public static String URL_CHANGE_FACEWALL = ROOT_HOST_NAME + "/CBDParkingMSs/faceScore/addFaceScore";//修改颜值表/企业风采/三农风采图片
    public static String URL_DEL_FACEWALL = ROOT_HOST_NAME + "/CBDParkingMSs/faceScore/deleteFaceScore";//修改颜值表/企业风采/三农风采图片
    public static String URL_GET_GOODS_LIST = ROOT_HOST_NAME + "/CBDParkingMSs/goods/myGoods";//获取商品列表
    public static String URL_ADD_GOODS = ROOT_HOST_NAME + "/CBDParkingMSs/goods/addGoods";//添加商品
    public static String URL_UPDATE_GOODS = ROOT_HOST_NAME + "/CBDParkingMSs/goods/updateGoods";//修改商品信息
    public static String URL_DEL_GOODS_IMG = ROOT_HOST_NAME + "/CBDParkingMSs/goods/deleteImg";//删除商品图片
    public static String URL_ADD_GOODS_IMG = ROOT_HOST_NAME + "/CBDParkingMSs/goods/uploadGoodsImg";//添加商品图片
    public static String URL_DELETE_GOODS = ROOT_HOST_NAME + "/CBDParkingMSs/goods/deleteGoods";//添加商品图片
    public static String URL_GET_GOODS = ROOT_HOST_NAME + "/CBDParkingMSs/goods/details";//获取商品详情
    public static String URL_FOSTER_AGRICULTURAL = ROOT_HOST_NAME + "/CBDParkingMSs/sf/details";//扶植农业接口
    public static String URL_FRIENDS = ROOT_HOST_NAME + "/CBDParkingMSs/friends/myFriends";//好友列表接口
    public static String URL_DEL_EFRIENDS = ROOT_HOST_NAME + "/CBDParkingMSs/friends/deleteFriends";//删除好友接口

    public static String URL_GET_ADDRESS = ROOT_HOST_NAME + "/CBDParkingMSs/ra/myAddress";//获取收货地址（某条收货地址）
    public static String URL_GET_ALL_ADDRESS = ROOT_HOST_NAME + "/CBDParkingMSs/ra/updateIsDefault";//获取所有收货地址
    public static String URL_ADD_ADDRESS = ROOT_HOST_NAME + "/CBDParkingMSs/ra/addReceiptAddress";//添加/修改收货地址
//    public static String URL_EDT_ADDRESS = ROOT_HOST_NAME + "/CBDParkingMSs/ra/delReceiptAddress";//删除收货地址(文档中没看到这个接口)

    public static String RUL_ADD_SHOPPINGCAR = ROOT_HOST_NAME + "/CBDParkingMSs/sc/addShoppingCart";//添加购物车
    public static String RUL_ADD_SHOPPINGCAR_LIST = ROOT_HOST_NAME + "/CBDParkingMSs/sc/myShoppingCart";//购物车列表
    public static String RUL_DEL_SHOPPINGCAR = ROOT_HOST_NAME + "/CBDParkingMSs/sc/deleteShoppingCart";//删除购物车商品

    public static void init(Context context) throws IOException {
    }
}
