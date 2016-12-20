package tm.db;

/**
 * Created by meixi on 2016/12/20.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.xbh.tmi.DemoApplication;

import java.io.File;

import tm.db.table.FriendTable;

/**
 * 创建数据库，打开和关闭数据库以及数据库的更新升级等操作 目前数据库一直处于打开状态并未关闭
 */
public class TmDB {
    public static String DATABASE_NAME = "YouXueDb";
    /**
     * V1.0.2 DATABASE_VERSION = 2  添加字段
     * V1.0.3 DATABASE_VERSION = 2
     * V1.0.4 DATABASE_VERSION = 3  添加字段
     * V2.0 DATABASE_VERSION = 5; 添加字段UserInfoTable.USER_SP
     */
    private static final int DATABASE_VERSION = 5;
    private SQLiteDatabase mSqlLiteDb;
    private DatabaseHelper mDbHelper;
    private static TmDB mRkCloudDb;

    /*
     * 打开数据库的操作。
     * 在某些情况下我们会关闭数据库，所以我们先判断数据库是否被关闭了， 如果关闭则重新打开，否则直接使用数据库
     */
    private TmDB(Context context, String userId) {
        // 数据库存在并处于打开状态那么直接使用
        creatDbHelper(context);
    }

    private void creatDbHelper(Context context) {
        if (null != mSqlLiteDb && mSqlLiteDb.isOpen()) {
            return;
        }
        try {
            if (null == mDbHelper) {
                mDbHelper = new DatabaseHelper(context);
            }
            mSqlLiteDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            mSqlLiteDb = mDbHelper.getReadableDatabase();
        }
    }

    /**
     * 创建单例实例
     *
     * @param
     * @return
     */
    public synchronized static TmDB getInstance(String userId) {
        if (null == mRkCloudDb) {
            DATABASE_NAME = DATABASE_NAME + "_" + userId;
            mRkCloudDb = new TmDB(DemoApplication.applicationContext, userId);
        }
        return mRkCloudDb;
    }
    /**
     * 获得database操作数据库的实例
     */
    public SQLiteDatabase getSqlDateBase() {
        return mSqlLiteDb;
    }

    /**
     * 关闭数据库的操作
     */
    public void close() {
        if (mSqlLiteDb != null)
            mSqlLiteDb.close();
        if (mDbHelper != null)
            mDbHelper.close();
    }

    private void destoryDb() {
        mSqlLiteDb = null;
        mDbHelper = null;
    }

    public static void destory() {
        if (mRkCloudDb != null) {
            mRkCloudDb.close();
            mRkCloudDb.destoryDb();
            mRkCloudDb = null;
            DATABASE_NAME = "YouXueDb";
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /*
     * 创建内部类DatabaseHelper：创建数据库，穿件表，更新表功能
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private File mDbFile;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mDbFile = context.getDatabasePath(DATABASE_NAME);
        }

        @Override
        public synchronized SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase db;
            if (mDbFile.exists()) {
                db = SQLiteDatabase.openDatabase(mDbFile.toString(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                // 当前的使用的数据库的版本(为升级之前的)，目前我们只考虑数据库版本的升级不考虑回滚的情况
                int version = db.getVersion();
                if (version != DATABASE_VERSION) {
                    if (0 == version) {
                        onCreate(db);
                    } else {
                        onUpgrade(db, version, DATABASE_VERSION);
                    }
                } else {
//                    BroadcastUtil.sendBroadcastOfNotice(EventAction.COMMON_ACTION, EventType.CommonEvent.UPDATEDATA_NO);
                }
            } else {
                db = super.getWritableDatabase();
            }
            return db;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                //创建用户信息表
                db.execSQL(FriendTable.SQL_CREAT_TABLE);
                db.setVersion(DATABASE_VERSION);
                db.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
}
