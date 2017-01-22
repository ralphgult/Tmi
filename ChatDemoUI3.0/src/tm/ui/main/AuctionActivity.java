package tm.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tm.manager.PersonManager;
import tm.ui.main.adapter.ActionAdapter;

/**
 * Created by LKing on 2017/1/12.
 */
public class AuctionActivity extends Activity {
    private String[] icon;
    private String[] iconName;
    private String[] iconPrice;
    private String[] iconPurch;
    private String[] iconBid;
    private String[] iconTime;
    private String[] iconId;
    public static List<Person> list;
    private GridView gridView;
    private ActionAdapter adapter;
    private ImageView mBackImg;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1) {
                adapter.notifyDataSetChanged();
                //每隔1毫秒更新一次界面，如果只需要精确到秒的倒计时此处改成1000即可
                handler.sendEmptyMessageDelayed(1,1000);
            }
            if(msg.what == 3001 ){

                try {
                    Log.e("LKing","obj = "+ msg.obj.toString());
                    JSONArray jsonArray = new JSONArray(msg.obj.toString());
                    int max = jsonArray.length();
                    icon = new String[max];
                    iconName = new String[max];
                    iconPrice = new String[max];
                    iconPurch = new String[max];
                    iconBid = new String[max];
                    iconTime = new String[max];
                    iconId = new String[max];
                    for(int i = 0 ; i< max ; i++){
                        JSONObject jsonObject = (JSONObject)jsonArray.opt(i);
                        icon[i]=jsonObject.getString("auctionImg");
                        iconName[i]=jsonObject.getString("name");
                        iconPrice[i]=jsonObject.getString("price");
                        iconPurch[i]=jsonObject.getString("originalPrice");
                        iconBid[i]=jsonObject.getString("number");
                        iconTime[i]=jsonObject.getString("residual");
                        iconId[i]=jsonObject.getString("id");
                        Person person = new Person();
//                        person.setPath(getResources().getDrawable(R.id.about_back_iv));
                        person.setName("粽子一盒");
                        person.setPrice("当前价：RMB 60");
                        person.setPurch("直购价：RMB 90");
                        person.setBid("出价6次");
                        person.setTime("600000");
                        person.setId(String.valueOf(i));
                        list.add(person);
                    }
                    // 数据拿到开始计时
                    adapter.start();
                    handler.sendEmptyMessageDelayed(1,1000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_activity);
        initView();
        setListener();
        networkRequest();
    }

    private void initView() {
        mBackImg = (ImageView)findViewById(R.id.auction_back_iv);
        gridView = (GridView) findViewById(R.id.action_gview);

        list = new ArrayList<>();
        adapter = new ActionAdapter(this,list);
        gridView.setAdapter(adapter);
    }

    private void setListener() {
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AuctionActivity.this,AuctionDetailActivity.class);
                intent.putExtra("shopping_detail_id",iconId[position]);
                startActivity(intent);
            }
        });

    }

    private void networkRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PersonManager.getAuctionList(handler);
            }
        }).start();
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

