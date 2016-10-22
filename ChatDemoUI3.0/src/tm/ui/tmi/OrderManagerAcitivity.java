package tm.ui.tmi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.ui.tmi.adapter.OrderAdapter;
import tm.utils.ViewUtil;

public class OrderManagerAcitivity extends Activity implements View.OnClickListener {
    private ImageView back;
    private ListView orderList;
    private TextView doing;
    private TextView finish;
    private TextView closed;
    private LinearLayout tosend;
    private LinearLayout topay;
    private LinearLayout sended;
    private LinearLayout toback;
    private TextView tosendCount;
    private TextView topayCount;
    private TextView sendedCount;
    private TextView tobackCount;
    private TextView tosendText;
    private TextView topayText;
    private TextView sendedText;
    private TextView tobackText;
    private LinearLayout kind;
    private List<Map<String, String>> list;
    private List<TextView> titleList;
    private List<LinearLayout> kindList;
    private List<TextView> countList;
    private List<TextView> textList;

    private OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        init();
        initViewsAndListeners();
    }

    private void init() {
        titleList = new ArrayList<TextView>();
        kindList = new ArrayList<LinearLayout>();
        countList = new ArrayList<TextView>();
        textList = new ArrayList<TextView>();
    }

    private void initViewsAndListeners() {
        back = (ImageView) findViewById(R.id.order_manager_back);
        orderList = (ListView) findViewById(R.id.order_manager_lv);

        doing = (TextView) findViewById(R.id.order_manager_doing);
        titleList.add(doing);
        finish = (TextView) findViewById(R.id.order_manager_finish);
        titleList.add(finish);
        closed = (TextView) findViewById(R.id.order_manager_close);
        titleList.add(closed);

        tosend = (LinearLayout) findViewById(R.id.order_manager_tosend);
        kindList.add(tosend);
        topay = (LinearLayout) findViewById(R.id.order_manager_topay);
        kindList.add(topay);
        sended = (LinearLayout) findViewById(R.id.order_manager_sended);
        kindList.add(sended);
        toback = (LinearLayout) findViewById(R.id.order_manager_toback);
        kindList.add(toback);

        tosendCount = (TextView) findViewById(R.id.order_manager_tosend_count);
        countList.add(tosendCount);
        topayCount = (TextView) findViewById(R.id.order_manager_topay_count);
        countList.add(topayCount);
        sendedCount = (TextView) findViewById(R.id.order_manager_sended_count);
        countList.add(sendedCount);
        tobackCount = (TextView) findViewById(R.id.order_manager_toback_count);
        countList.add(tobackCount);

        tosendText = (TextView) findViewById(R.id.order_manager_tosend_text);
        textList.add(tosendText);
        topayText = (TextView) findViewById(R.id.order_manager_topay_text);
        textList.add(topayText);
        sendedText = (TextView) findViewById(R.id.order_manager_sended_text);
        textList.add(sendedText);
        tobackText = (TextView) findViewById(R.id.order_manager_toback_text);
        textList.add(tobackText);

        kind = (LinearLayout) findViewById(R.id.order_manager_kind);

        mAdapter = new OrderAdapter(this, 0);
        mAdapter.resetData(getDatas());
        orderList.setAdapter(mAdapter);

        back.setOnClickListener(this);
        doing.setOnClickListener(this);
        finish.setOnClickListener(this);
        closed.setOnClickListener(this);
        tosend.setOnClickListener(this);
        topay.setOnClickListener(this);
        sended.setOnClickListener(this);
        toback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_manager_back:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.order_manager_doing:
            case R.id.order_manager_finish:
            case R.id.order_manager_close:
                for (TextView view : titleList) {
                    if (view.getId() == v.getId()) {
                        view.setTextColor(Color.parseColor("#a161fb"));
                    } else {
                        view.setTextColor(Color.parseColor("#4e4e4e"));
                    }
                }
                if (v.getId() != R.id.order_manager_doing) {
                    kind.setVisibility(View.GONE);
                } else {
                    kind.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.order_manager_tosend:
            case R.id.order_manager_topay:
            case R.id.order_manager_sended:
            case R.id.order_manager_toback:
                for (int i = 0; i < kindList.size(); i++) {
                    if (kindList.get(i).getId() == v.getId()) {
                        countList.get(i).setTextColor(Color.parseColor("#a161fb"));
                        textList.get(i).setTextColor(Color.parseColor("#a161fb"));
                        mAdapter.resetData(getDatas());
                        mAdapter.notify();
                    } else {
                        countList.get(i).setTextColor(Color.parseColor("#4e4e4e"));
                        textList.get(i).setTextColor(Color.parseColor("#4e4e4e"));
                    }
                }

                break;
        }
    }

    public List<Map<String, String>> getDatas() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<>();
        map.put("comName", "西域三农");
        map.put("name", "冰粽礼盒，60g*8");
        map.put("price", "79.0");
        map.put("num", "1");
        map.put("reason", "货物与实物不符");
        list.add(map);
        map = new HashMap<>();
        map.put("comName", "西域三农");
        map.put("name", "冰粽礼盒，60g*8");
        map.put("price", "79.0");
        map.put("num", "1");
        map.put("reason", "宝贝破损");
        list.add(map);
        return list;
    }
}
