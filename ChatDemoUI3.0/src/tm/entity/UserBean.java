package tm.entity;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int uid;//用户ID
    private String pass;//用户密码
    private String token;//用户标志
    private String province;//省
    private String city;//城市
    private String area;//区
    private String userName;//手机号
    private String nickName;//昵称
    private String photo;//头像
    private String type;//用户类型
    private String name;//姓名
    private String sex;//性别
    private String area_id;//地区编号
    private String company;//公司
    private String department;//部门
    private String position;//职位
    private String phone;//手机号
    private String email;//邮箱
    private String qq;//Qq
    private String weixin;//微信
    private String weibo;//微博
    private String identity;//身份
    private String user_address;//详细地址
    private String trade_status;//融资状态
    private String desc;//公司简介
    private String website;//网址
    private String account_money;//账户余额
    private String user_status;//用户状态（0：未认证1：已认证2：认证中3：不通过）
    private String rz_status;//用户认证状态（0：未认证1：已认证）
    private String category;//资金类型（
    // 1：银行，
    // 2：信托，
    // 3：券商，
    // 4：基金子公司，
    // 5：保险公司，
    // 6：四大资产管理公司，
    // 7：上市公司，
    // 8：私募基金，
    // 9：投资公司，
    // 10：投资管理／资产管理，
    // 11：实体企业，
    // 12：其他）
    private String file1;//认证文件（根据终端传输顺序返回，包含公司名片,个人照片,营业执照,身份证）
    private String file2;//认证文件（根据终端传输顺序返回，包含公司名片,个人照片,营业执照,身份证）
    private String file3;//认证文件（根据终端传输顺序返回，包含公司名片,个人照片,营业执照,身份证）
    private String file4;//认证文件（根据终端传输顺序返回，包含公司名片,个人照片,营业执照,身份证）

    private String userPhoto;//用户手机
    private String address;//地址
    private String lat;//纬度
    private String lng;//经度
    private String intro;//
    private List<Map> photos;
    private String keywords;
    private String shopdesc;

    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public String getShopdesc() {
        return shopdesc;
    }
    public void setShopdesc(String shopdesc) {
        this.shopdesc = shopdesc;
    }
    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public List<Map> getPhotos() {
        return photos;
    }
    public void setPhotos(List<Map> photos) {
        this.photos = photos;
    }
    public String getRz_status() {
        return rz_status;
    }
    public void setRz_status(String rz_status) {
        this.rz_status = rz_status;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getFile1() {
        return file1;
    }
    public void setFile1(String file1) {
        this.file1 = file1;
    }
    public String getFile2() {
        return file2;
    }
    public void setFile2(String file2) {
        this.file2 = file2;
    }
    public String getFile3() {
        return file3;
    }
    public void setFile3(String file3) {
        this.file3 = file3;
    }
    public String getFile4() {
        return file4;
    }
    public void setFile4(String file4) {
        this.file4 = file4;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getWeibo() {
        return weibo;
    }
    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getArea_id() {
        return area_id;
    }
    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getWeixin() {
        return weixin;
    }
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public String getUser_address() {
        return user_address;
    }
    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }
    public String getTrade_status() {
        return trade_status;
    }
    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getAccount_money() {
        return account_money;
    }
    public void setAccount_money(String account_money) {
        this.account_money = account_money;
    }
    public String getUser_status() {
        return user_status;
    }
    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void init(JSONObject jo) throws JSONException {
        this.uid = jo.getInt("uid");
        try {
            this.pass = jo.getString("pass");
        } catch (Exception e2) {
        }
        try {
            this.token = jo.getString("token");
        } catch (Exception e1) {
        }
        this.userName = jo.getString("username");
        this.name = jo.getString("name");
        this.userPhoto = jo.getString("userphoto");
        this.address = jo.getString("address");
        this.lat=jo.getString("lat");
        this.lng = jo.getString("lng");
        this.intro = jo.getString("intro");
        this.type = jo.getString("photos");
    }
}

