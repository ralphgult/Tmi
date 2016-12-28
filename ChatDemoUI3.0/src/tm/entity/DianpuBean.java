package tm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meixi on 2016/12/28.
 */

public class DianpuBean implements Parcelable {

    public String mCommodityyuanjian;  //店铺原价
    public String mCommodityxianjia;    //店铺现价
    public String mCommodityPath;    //店铺图标路径
    public String mCommodityjianjie;    //店铺简介
    public String mCommodityzhongliang;    //商品计量


    public static final Creator<DianpuBean> CREATOR = new Creator<DianpuBean>() {
        @Override
        public DianpuBean createFromParcel(Parcel source) {
            DianpuBean resourcesBean = new DianpuBean();
            resourcesBean.mCommodityyuanjian = source.readString();
            resourcesBean.mCommodityxianjia = source.readString();
            resourcesBean.mCommodityPath = source.readString();
            resourcesBean.mCommodityjianjie = source.readString();
            resourcesBean.mCommodityzhongliang = source.readString();
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
        dest.writeString(mCommodityyuanjian);
        dest.writeString(mCommodityxianjia);
        dest.writeString(mCommodityPath);
        dest.writeString(mCommodityjianjie);
        dest.writeString(mCommodityzhongliang);
    }

    public boolean copyData(DianpuBean obj) {
        if (null == obj) {
            return false;
        } else {
            mCommodityyuanjian = obj.mCommodityyuanjian;
            mCommodityxianjia = obj.mCommodityxianjia;
            mCommodityPath = obj.mCommodityPath;
            mCommodityjianjie = obj.mCommodityjianjie;
            mCommodityzhongliang = obj.mCommodityzhongliang;
            return true;
        }
    }
}