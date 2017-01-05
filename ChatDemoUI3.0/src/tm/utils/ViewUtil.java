package tm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;


/**
 * Created by Administrator on 15-11-7.
 */
public class ViewUtil {

    /**
     * 跳转到另一个activity 说明:不能携带数据并且当前activity不可以设置finish
     *
     * @param ac
     * @param otherCls
     */
    public static void jumpToOtherActivity(Activity ac, Class<?> otherCls) {
        jumpToOtherActivity(ac, otherCls, false);
    }

    /**
     * 跳转到另一个activity 说明:不能携带数据但当前activity可以设置finish
     *
     * @param ac
     * @param otherCls
     * @param isFinish
     */
    public static void jumpToOtherActivity(Activity ac, Class<?> otherCls,
                                           boolean isFinish) {
        jumpToOtherActivity(ac, otherCls, null, isFinish, -1, -1);
    }

    /**
     * 跳转到另一个activity 说明:可以携带数据但当前activity不可以设置finish
     *
     * @param ac
     * @param otherCls
     * @param bundle
     */
    public static void jumpToOtherActivity(Activity ac, Class<?> otherCls,
                                           Bundle bundle) {
        jumpToOtherActivity(ac, otherCls, bundle, false, -1, -1);
    }

    /**
     * 跳转到另一个activity 说明:可以携带数据并且当前activity可以设置finish
     * 可设置activity进入/出去动画，使用默认动画请设置为-1
     *
     * @param ac
     * @param otherCls
     * @param bundle
     * @param isFinish
     */
    public static void jumpToOtherActivity(Activity ac, Class<?> otherCls,
                                           Bundle bundle, boolean isFinish, int enterAnim, int exitAnim) {
        Intent intent = new Intent(ac, otherCls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        ac.startActivity(intent);
        ac.overridePendingTransition(enterAnim == -1 ? R.anim.activity_common_right_in : enterAnim,
                exitAnim == -1 ? R.anim.activity_common_left_out : exitAnim);
        if (isFinish) {
            ac.finish();
        }
    }

    /**
     * 跳转界面可接收返回值的方法
     * @param ac
     * @param otherCls
     * @param requestCode
     */
    public static void jumpToOherActivityForResult(Activity ac, Class<?> otherCls, int requestCode){
        jumpToOherActivityForResult(ac, otherCls, null, requestCode);
    }

    public static void jumpToOherActivityForResult(Activity ac, Class<?> otherCls, Bundle bundle, int requestCode){
        Intent intent = new Intent(ac, otherCls);
        if(null != bundle){
            intent.putExtras(bundle);
        }
        ac.startActivityForResult(intent, requestCode);
        ac.overridePendingTransition(R.anim.activity_common_right_in, R.anim.activity_common_left_out);
    }

    /**
     * 返回数据给前面的activity，配合startActivityForResult方法使用的
     * @param ac
     * @param resultCode
     * @param intent
     */
    public static void backToActivityForResult(Activity ac, int resultCode, Intent intent){
        ac.setResult(resultCode, intent);
        ac.finish();
        ac.overridePendingTransition(R.anim.activity_common_left_in, R.anim.activity_common_right_out);
    }


    /**
     * 从当前界面退出到另一个界面,针对actionbar的返回键及back键
     * 可设置activity进入/出去动画，使用默认动画请设置为-1
     * @param ac
     * @param otherCls
     * @param bundle
     */
    public static void backToOtherActivity(Activity ac, Class<?> otherCls,
                                           Bundle bundle, int enterAnim, int exitAnim) {
        if (null != otherCls) {
            Intent intent = new Intent(ac, otherCls);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            ac.startActivity(intent);
        }
        ac.finish();
        ac.overridePendingTransition(enterAnim == -1 ? R.anim.activity_common_left_in : enterAnim,
                exitAnim == -1 ? R.anim.activity_common_right_out : exitAnim);
    }

    public static void backToOtherActivity(Activity ac, Class<?> otherCls, Bundle bundle) {
        backToOtherActivity(ac, otherCls, bundle, -1, -1);
    }

    public static void backToOtherActivity(Activity ac, Class<?> otherCls) {
        backToOtherActivity(ac, otherCls, null, -1, -1);
    }

    public static void backToOtherActivity(Activity ac) {
        backToOtherActivity(ac, null);
    }
    /**
     * 从sp里获取userId
     */
    public static String getCurrentUserId() {
        SharedPreferences sharedPre= DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        return sharedPre.getString("username", "");
    }
    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

}
