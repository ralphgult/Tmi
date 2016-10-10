package tm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wyw on 2015/12/3.
 */
public class ResourcesBean implements Parcelable {

    public int mID;  //资源编号
    public String mResourcesIntroductionPath;  //资源介绍路径
    public String mResourcesUsePath;    //资源使用路径
    public String mImagePath;    //资源图标路径
    public String mResourcesName;    //资源名称
    public int mDownloadNumber;        //资源下载次数
    public int mResourcesScore;     //资源评分
    public int mIsMyResources;     //是否是我的资源

    public static final Creator<ResourcesBean> CREATOR = new Creator<ResourcesBean>() {
        @Override
        public ResourcesBean createFromParcel(Parcel source) {
            ResourcesBean resourcesBean = new ResourcesBean();
            resourcesBean.mID = source.readInt();
            resourcesBean.mResourcesIntroductionPath = source.readString();
            resourcesBean.mResourcesUsePath = source.readString();
            resourcesBean.mImagePath = source.readString();
            resourcesBean.mResourcesName = source.readString();
            resourcesBean.mDownloadNumber = source.readInt();
            resourcesBean.mResourcesScore = source.readInt();
            return resourcesBean;
        }

        @Override
        public ResourcesBean[] newArray(int size) {
            return new ResourcesBean[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeString(mResourcesIntroductionPath);
        dest.writeString(mResourcesUsePath);
        dest.writeString(mImagePath);
        dest.writeString(mResourcesName);
        dest.writeInt(mDownloadNumber);
        dest.writeInt(mResourcesScore);
    }

    public boolean copyData(ResourcesBean obj) {
        if (null == obj) {
            return false;
        } else {
            mID = obj.mID;
            mResourcesIntroductionPath = obj.mResourcesIntroductionPath;
            mResourcesUsePath = obj.mResourcesUsePath;
            mResourcesName = obj.mResourcesName;
            mImagePath = obj.mImagePath;
            mDownloadNumber = obj.mDownloadNumber;
            mResourcesScore = obj.mResourcesScore;
            return true;
        }
    }
}
