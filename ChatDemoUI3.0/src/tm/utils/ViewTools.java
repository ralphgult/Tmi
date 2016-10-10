package tm.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wyw on 2015/12/5.
 */
public class ViewTools {

    private static PowerManager.WakeLock mWakeLock = null;
    private static final String TAG = ViewTools.class.getSimpleName();
    /**
     * EditText文字输入监听
     * @param edt
     * @param iv
     */
    public static void addTextChangedListener(final EditText edt,final ImageView iv){
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt.setText("");
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt.getText().toString().length()>0){
                    iv.setVisibility(View.VISIBLE);
                }else{
                    iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    /**
     * 隐藏软键盘
     * @param ac
     * @param et
     * @param manager
     */
    public static void hideKeyboard(Context ac, EditText et, InputMethodManager manager) {
        manager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     * @param ac
     * @param et
     * @param manager
     */
    public static void showKeyboard(Context ac, EditText et, InputMethodManager manager) {
        manager.showSoftInput(et, InputMethodManager.SHOW_FORCED);
    }







    /**
     * 获得处理连续点击事件内部类对象的方法
     * @return 处理连续点击事件的内部类
     */
    public static ClickProcess getClickProcess(){
        ClickProcess entity = new ClickProcess();
        return entity;
    }

    /**
     * 处理连续点击事件的内部类
     */
    public static class ClickProcess{
        private int clickCount = 0;
        public boolean isJump;
        /**
         * 通过点击次数判断是否应该触发跳转操作
         */
        public void isJumpForClick(){
            if(clickCount == 0){
                clickCount++;
                isJump = true;
            }
        }

        /**
         * 重置点击次数
         */
        public void resetClickCount(){
            clickCount = 0;
        }
    }





    /**
     * 检查表中某列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExist(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result ;
    }

    /**
     * 文件修改时间
     */
    public static String FileTimes(File mFile) {
        long time = mFile.lastModified();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = sDateFormat.format(new Date(time + 0));
        return strTime;
    }

    /**
     * 替换BS端的换行符
     **/
    public static CharSequence getReplaceStr(CharSequence oldStr) {
        CharSequence newStr = null;
        if(oldStr.toString().contains("[br]")){
            newStr = oldStr.toString().replace("[br]", "\n");
        }else{
            newStr = oldStr;
        }
        return newStr;
    }

    /**
     * 替换移动端的换行符
     **/
    public static String getAPPReplaceStr(String oldStr) {
        String newStr = null;
        if(oldStr.contains("\n")){
            newStr = oldStr.replace("\n", "[br]");
        }else if(oldStr.contains("\r")){
            newStr = oldStr.replace("\r", "[br]");
        }
        else{
            newStr = oldStr;
        }
        return newStr;
    }
}
