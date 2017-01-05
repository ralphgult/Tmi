package tm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meixi on 2016/12/28.
 */

public class DianpuBean implements Parcelable {

    public String mCommodityyuanjia;  //商品原价
    public String mCommodityxianjia;    //商品现价
    public String mCommodityPath;    //商品图标路径
    public String mCommodityjianjie;    //商品简介
    public String mCommodityname;    //商品名称
    public String mCommodityid;    //商品id


    public static final Creator<DianpuBean> CREATOR = new Creator<DianpuBean>() {
        @Override
        public DianpuBean createFromParcel(Parcel source) {
            DianpuBean resourcesBean = new DianpuBean();
            resourcesBean.mCommodityyuanjia = source.readString();
            resourcesBean.mCommodityxianjia = source.readString();
            resourcesBean.mCommodityPath = source.readString();
            resourcesBean.mCommodityjianjie = source.readString();
            resourcesBean.mCommodityid = source.readString();
            resourcesBean.mCommodityname = source.readString();
            return resourcesBean;
        }

        @Override
        public DianpuBean[] newArray(int size) {
            return new DianpuBean[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCommodityyuanjia);
        dest.writeString(mCommodityxianjia);
        dest.writeString(mCommodityPath);
        dest.writeString(mCommodityjianjie);
        dest.writeString(mCommodityid);
        dest.writeString(mCommodityname);
    }

    public boolean copyData(DianpuBean obj) {
        if (null == obj) {
            return false;
        } else {
            mCommodityyuanjia = obj.mCommodityyuanjia;
            mCommodityxianjia = obj.mCommodityxianjia;
            mCommodityPath = obj.mCommodityPath;
            mCommodityjianjie = obj.mCommodityjianjie;
            mCommodityid = obj.mCommodityid;
            mCommodityname = obj.mCommodityname;
            return true;
        }
    }
}