package tm.ui.main;

/**
 * Created by Administrator on 2017/1/17.
 */
public class Person {
    private String mPath;//商品图片地址
    private String mName;//商品名称
    private String mPrice;//商品当前价
    private String mPurch;//商品直购价
    private String mBid;//商品出价次数
    private String mTime;//商品剩余时间
    private String mId;//商品id

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public String getPurch() {
        return mPurch;
    }

    public void setPurch(String purch) {
        this.mPurch = purch;
    }

    public String getBid() {
        return mBid;
    }

    public void setBid(String bid) {
        this.mBid = bid;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
