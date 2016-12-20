package tm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meixi on 2016/12/20.
 */

public class FriendBean implements Parcelable {


    public int  mUserID;//好友id
    public int  mUsername;//好友电话
    public String  mphoto;//好友头像
    public String  mNickname;//好友昵称


    public static final Creator<FriendBean> CREATOR = new Creator<FriendBean>() {
        @Override
        public FriendBean createFromParcel(Parcel source) {
            FriendBean mUserInfo = new FriendBean();
            mUserInfo.mUserID = source.readInt();
            mUserInfo.mUsername = source.readInt();
            mUserInfo.mphoto = source.readString();
            mUserInfo.mNickname = source.readString();
            return mUserInfo;
        }

        @Override
        public FriendBean[] newArray(int size) {
            return new FriendBean[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mUserID);
        dest.writeInt(mUsername);
        dest.writeString(mphoto);
        dest.writeString(mNickname);
    }

    public boolean copyData(FriendBean obj){
        if(null==obj){
            return false;
        }else{
            mUserID = obj.mUserID;
            mUsername = obj.mUsername;
            mphoto = obj.mphoto;
            mNickname = obj.mNickname;


            return true;
        }
    }
}
