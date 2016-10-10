package tm.system;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by RalphGult on 8/29/2016.
 */

public class App extends Application {
    private static List<Activity> mList = new LinkedList<Activity>();

    public static App getInstance(){
        return new App();
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // 关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
