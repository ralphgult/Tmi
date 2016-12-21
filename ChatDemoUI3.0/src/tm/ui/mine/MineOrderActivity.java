package tm.ui.mine;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.ui.tmi.adapter.OrderAdapter;
import tm.utils.ViewUtil;

public class MineOrderActivity extends Activity implements View.OnClickListener {
    private ImageView order_back;
    private TextView tab_all;
    private TextView tab_toPay;
    private TextView tab_toSend;
    private TextView tab_toReceive;
    private TextView tab_toComment;
    private ListView orderList;
    private TextView[] tabs;
    private OrderAdapter mAdapter;
    private List<Map<String, String>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        init();
        initViews();
        addListeners();
    }

    private void addListeners() {
        order_back.setOnClickListener(this);
        tab_all.setOnClickListener(this);
        tab_toPay.setOnClickListener(this);
        tab_toSend.setOnClickListener(this);
        tab_toReceive.setOnClickListener(this);
        tab_toComment.setOnClickListener(this);
    }

    private void init() {
        tabs = new TextView[5];
        mAdapter = new OrderAdapter(this, 4);
        datas = getDatas(4);
    }

    private void initViews() {
        order_back = (ImageView) findViewById(R.id.my_order_back_iv);
        tab_all = (TextView) findViewById(R.id.my_order_all_tv);
        tabs[0] = tab_all;
        tab_toPay = (TextView) findViewById(R.id.my_order_pay_tv);
        tabs[1] = tab_toPay;
        tab_toSend = (TextView) findViewById(R.id.my_order_send_tv);
        tabs[2] = tab_toSend;
        tab_toReceive = (TextView) findViewById(R.id.my_order_rec_tv);
        tabs[3] = tab_toReceive;
        tab_toComment = (TextView) findViewById(R.id.my_order_com_tv);
        tabs[4] = tab_toComment;
        orderList = (ListView) findViewById(R.id.my_order_list_lv);
        mAdapter.resetData(datas);
        orderList.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == order_back.getId()) {
            ViewUtil.backToOtherActivity(this);
        } else {
            for (int i = 0; i < tabs.length; i++) {
                if (v.getId() == tabs[i].getId()) {
                    tabs[i].setTextColor(Color.parseColor("#a161fb"));
                    datas = getDatas(i + 4);
                    mAdapter.setType(i + 4);
                    mAdapter.resetData(datas);
                } else {
                    tabs[i].setTextColor(Color.parseColor("#4e4e4e"));
                }
            }
        }
    }

    public List<Map<String, String>> getDatas(int type) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        return list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
