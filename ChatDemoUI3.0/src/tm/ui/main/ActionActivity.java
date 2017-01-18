package tm.ui.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import com.xbh.tmi.R;
import java.util.ArrayList;
import java.util.List;
import tm.ui.main.adapter.ActionAdapter;
import tm.widget.pulltorefresh.ILoadingLayout;
import tm.widget.pulltorefresh.PullToRefreshBase;
import tm.widget.pulltorefresh.PullToRefreshGridView;

/**
 * Created by LKing on 2017/1/12.
 */
public class ActionActivity extends Activity {
    private int[] icon = {
            R.drawable.goods, R.drawable.goods,R.drawable.goods,
            R.drawable.goods, R.drawable.goods,R.drawable.goods,
            R.drawable.goods, R.drawable.goods,R.drawable.goods,
            R.drawable.goods, R.drawable.goods,R.drawable.goods };
    private String[] iconName = {
            "拍卖物品1:纯手工制作，无添加剂", "拍卖物品2:纯手工制作，无添加剂",
            "拍卖物品3:纯手工制作，无添加剂", "拍卖物品4:纯手工制作，无添加剂",
            "拍卖物品5:纯手工制作，无添加剂", "拍卖物品6:纯手工制作，无添加剂",
            "拍卖物品7:纯手工制作，无添加剂", "拍卖物品8:纯手工制作，无添加剂",
            "拍卖物品9:纯手工制作，无添加剂", "拍卖物品10:纯手工制作，无添加剂",
            "拍卖物品11:纯手工制作，无添加剂", "拍卖物品12:纯手工制作，无添加剂" };
    private String[] iconPrice = {
            "当前价:RMB 50", "当前价:RMB 60", "当前价:RMB 70", "当前价:RMB 80",
            "当前价:RMB 90", "当前价:RMB 100", "当前价:RMB 110","当前价:RMB 50",
            "当前价:RMB 80", "当前价:RMB 100", "当前价:RMB 30", "当前价:RMB 40" };
    private String[] iconPurch = {
            "直购价:RMB 50", "直购价:RMB 60", "直购价:RMB 70", "直购价:RMB 80",
            "直购价:RMB 90", "直购价:RMB 100", "直购价:RMB 110","直购价:RMB 50",
            "直购价:RMB 80", "直购价:RMB 100", "直购价:RMB 30", "直购价:RMB 40" };
    private String[] iconBid = {
            "出价2次", "出价2次", "出价2次", "出价2次", "出价2次", "出价2次",
            "出价2次","出价2次", "出价2次", "出价2次", "出价2次", "出价2次" };
    private String[] iconTime = {
            "5000", "60000", "70000", "80000", "90000", "40000",
            "210000","40000", "56000", "760000", "45000", "67000" };
    public static List<Person> list;
    private PullToRefreshGridView gridView;
    private ActionAdapter adapter;
    private ImageView mBackImg;
    private int mItemCount = 10;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1) {
                adapter.notifyDataSetChanged();
                //每隔1毫秒更新一次界面，如果只需要精确到秒的倒计时此处改成1000即可
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_activity);
        goBackImg();
        gridView = (PullToRefreshGridView) findViewById(R.id.action_gview);
        ILoadingLayout startLabels = gridView.getLoadingLayoutProxy();
        startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
        startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>()
        {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView)
            {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                Log.e("LKing--->","下拉过程中.....");
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
            {
                Log.e("LKing--->","上拉过程中.....");
                new GetDataTask().execute();
            }
        });
        //测试数据
        list = new ArrayList<>();
        for (int i = 0; i < icon.length; i++) {
            Person person = new Person();
            person.setPath(icon[i]);
            person.setName(iconName[i]);
            person.setPrice(iconPrice[i]);
            person.setPurch(iconPurch[i]);
            person.setBid(iconBid[i]);
            person.setTime(iconTime[i]);
            list.add(person);
        }

        // 数据拿到开始计时
        adapter = new ActionAdapter(this,list);
        gridView.setAdapter(adapter);
        adapter.start();//开始倒计时
        handler.sendEmptyMessageDelayed(1,1000);
    }

    /*** 标题的返回按钮初始化并监听点击事件 */
    private void goBackImg() {
        mBackImg = (ImageView)findViewById(R.id.auction_back_iv);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Log.e("LKing--->","松手了，进行网络请求");

                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.e("LKing--->","网络请求完成了");
            adapter.notifyDataSetChanged();
            gridView.onRefreshComplete();
        }
    }




    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strDay + "天"+strHour+"时"+strMinute + "分" + strSecond + "秒";
    }
}

