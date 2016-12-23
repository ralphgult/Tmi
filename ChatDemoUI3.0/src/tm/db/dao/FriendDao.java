package tm.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;

import tm.db.TmDB;
import tm.db.table.FriendTable;
import tm.entity.FriendBean;
import tm.utils.ViewUtil;

/**
 * Created by meixi on 2016/12/20.
 */

public class FriendDao {

    private SQLiteDatabase mDb;

    /**
     * 构造方法
     */
    public FriendDao() {
        mDb = TmDB.getInstance(ViewUtil.getCurrentUserId()).getSqlDateBase();
    }
    /**
     * 批量添加数据
     *
     * @param userList 保存数据的List集合
     * @return 是否保存成功
     */
    public boolean insertUserInfoList(List<FriendBean> userList) {
        boolean result = false;
        if (null != userList && userList.size() > 0) {
            mDb.beginTransaction();
            try {
                for (FriendBean userBean : userList) {
                    ContentValues cv = getContextValues(userBean);
                    if (isExist(userBean.mUsername+"")) {
                        result = mDb.update(FriendTable.TABLE_NAME, cv, " " + FriendTable.USERNAME + " =? ", new String[]{String.valueOf(userBean.mUsername)}) > 0;
                    } else {
                        result = mDb.replace(FriendTable.TABLE_NAME, null, cv) > 0;
                    }
                }
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                result = false;
            } finally {
                mDb.endTransaction();
            }
        }
        return result;
    }
    /**
     * 判断当前用户是否存在
     *
     * @param userid
     * @return
     */
    private boolean isExist(String userid) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = mDb.query(FriendTable.TABLE_NAME, null, " " + FriendTable.USERNAME + " =?", new String[]{userid}, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 根据用户编号userId获取用户信息
     *
     * @param userId
     * @return
     */
    public FriendBean getLocalUserInfoByUserId(String userId) {
        FriendBean userInfoBean = null;
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("select * from " + FriendTable.TABLE_NAME + " where " + FriendTable.USERNAME + "=?" + "COLLATE NOCASE",
                    new String[]{userId});
            if (cursor.moveToFirst()) {
                userInfoBean = new FriendBean();
                userInfoBean.mUserID = cursor.getInt(cursor.getColumnIndex(FriendTable.USERID));
                userInfoBean.mUsername = cursor.getString(cursor.getColumnIndex(FriendTable.USERNAME));
                userInfoBean.mphoto = cursor.getString(cursor.getColumnIndex(FriendTable.PHOTO));
                userInfoBean.mNickname = cursor.getString(cursor.getColumnIndex(FriendTable.NICKNAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return userInfoBean;
    }
    /**
     * 获取列表JSON
     * @return
     */
    public List<EaseUser> getFriendList(){
        List<EaseUser> albumJsonList = null;
        Cursor cursor = null;
        FriendBean userInfoBean = null;
        try {
            cursor = mDb.query(FriendTable.TABLE_NAME,null,null,null,null,null,null);
            if(null != cursor && cursor.getCount() > 0){
                albumJsonList = new ArrayList<>();
                int albumJsonIndex1 = cursor.getColumnIndex(FriendTable.PHOTO);
                int albumJsonIndex2 = cursor.getColumnIndex(FriendTable.NICKNAME);
                int albumJsonIndex3 = cursor.getColumnIndex(FriendTable.USERNAME);
                int albumJsonIndex4 = cursor.getColumnIndex(FriendTable.USERID);
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    EaseUser euser=new EaseUser(cursor.getString(albumJsonIndex3));
                    euser.setNick(cursor.getString(albumJsonIndex2));
                    euser.setAvatar(cursor.getString(albumJsonIndex1));
//                    userInfoBean = new FriendBean();
//                    userInfoBean.mphoto = cursor.getString(albumJsonIndex1);
//                    userInfoBean.mNickname = cursor.getString(albumJsonIndex2);
//                    userInfoBean.mUsername = cursor.getString(albumJsonIndex3);
//                    userInfoBean.mUserID = cursor.getInt(albumJsonIndex4);
                    albumJsonList.add(euser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != cursor){
                cursor.close();
            }
        }
        return albumJsonList;
    }

    /**
     * 将实体类对象封装成ContentValues对象
     *
     * @param userInfoBean
     * @return
     */
    private ContentValues getContextValues(FriendBean userInfoBean) {
        ContentValues cv = new ContentValues();
        cv.put(FriendTable.USERNAME, userInfoBean.mUsername);
        cv.put(FriendTable.USERID, userInfoBean.mUserID);
        cv.put(FriendTable.PHOTO, userInfoBean.mphoto);
        cv.put(FriendTable.NICKNAME, userInfoBean.mNickname);
        return cv;
    }
}
