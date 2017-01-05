package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tm.alipay.AlipayAPI;
import tm.manager.PersonManager;
import tm.ui.mine.MySoppingActivity;
import tm.ui.tmi.adapter.GoodsImageAdapter;
import tm.utils.ViewUtil;

public class GoodsDetilActivity extends Activity implements View.OnClickListener {

    private GridView mHeadGrall;
    private TextView mPrice_tv;
    private TextView mOldPri_tv;
    private TextView mIntr_tv;
    private TextView mShop_tv;
    private TextView mChat_tv;
    private TextView mCar_tv;
    private TextView mBuy_tv;
    private GridView mPic_gv;
    private ImageView mBack_tv;
    private TextView mShopCar_tv;

    private String mGoodsId;
    private Handler mHandler;
    private GoodsImageAdapter mHeadAdapter;
    private GoodsImageAdapter mPicAdapter;
    private String mName;
    private String mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detil);
        init();
        iniViews();
        getGoodsFromServer();
    }

    private void init() {
        mGoodsId = getIntent().getExtras().getString("id");
        mHeadAdapter = new GoodsImageAdapter(this, this.getWindowManager().getDefaultDisplay().getWidth());
        mPicAdapter = new GoodsImageAdapter(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject object = (JSONObject) msg.obj;
                switch (msg.what) {
                    case 1001:
                        try {
                            mName = object.optString("goodName");
                            mPrice = object.optString("currentPrice");
                            mPrice_tv.setText("￥" + mPrice);
                            mOldPri_tv.setText("￥" + object.optString("originalPrice"));
                            mIntr_tv.setText(object.optString("goodProfiles"));
                            List<String> headImgPaths = new ArrayList<>();
                            headImgPaths.add(object.optString("goodImg"));
                            mHeadAdapter.resetData(headImgPaths);
                            mHeadGrall.setAdapter(mHeadAdapter);
                            horizontal_layout(headImgPaths);
                            List<String> picPaths = null;
                            JSONArray array = object.getJSONArray("imgs");
                            if (null == array && array.length() > 0) {
                                int size = array.length();
                                picPaths = new ArrayList<>(size);
                                for (int i = 0; i < size; i++) {
                                    picPaths.add(array.getJSONObject(i).getString("goodImg"));
                                }
                                mPicAdapter.resetData(picPaths);
                                mPic_gv.setAdapter(mPicAdapter);
                            } else {
                                mPic_gv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2001:
                        Toast.makeText(GoodsDetilActivity.this, "商品已添加至购物车", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(GoodsDetilActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void iniViews() {
        mHeadGrall = (GridView) findViewById(R.id.goods_gall);
        mPrice_tv = (TextView) findViewById(R.id.goods_price_tv);
        mOldPri_tv = (TextView) findViewById(R.id.goods_old_price_tv);
        mIntr_tv = (TextView) findViewById(R.id.goods_intor_tv);
        mPic_gv = (GridView) findViewById(R.id.goods_pic_gv);
        mShop_tv = (TextView) findViewById(R.id.goods_shop);
        mChat_tv = (TextView) findViewById(R.id.goods_chat_tv);
        mCar_tv = (TextView) findViewById(R.id.goods_shopcar_tv);
        mBuy_tv = (TextView) findViewById(R.id.goods_buy_tv);
        mBack_tv = (ImageView) findViewById(R.id.goods_back_iv);
        mShopCar_tv = (TextView) findViewById(R.id.goods_add_tv);
        mChat_tv.setOnClickListener(this);
        mCar_tv.setOnClickListener(this);
        mBuy_tv.setOnClickListener(this);
        mBack_tv.setOnClickListener(this);
        mShopCar_tv.setOnClickListener(this);
    }

    private void getGoodsFromServer() {
        new Thread() {
            @Override
            public void run() {
                PersonManager.getGoods(mGoodsId, mHandler);
            }
        }.start();
    }

    public void horizontal_layout(List<String> list) {
        int size = list.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (110 * size * density);
        int itemWidth = (int) (100 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mHeadGrall.setLayoutParams(params);// 设置GirdView布局参数
        mHeadGrall.setColumnWidth(itemWidth);// 列表项宽
        mHeadGrall.setHorizontalSpacing(10);// 列表项水平间距
        mHeadGrall.setStretchMode(GridView.NO_STRETCH);
        mHeadGrall.setNumColumns(size);//总长度
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_chat_tv:
                //TODO 跳转聊天
                break;
            case R.id.goods_shopcar_tv:
                new Thread() {
                    @Override
                    public void run() {
                        PersonManager.addShopCar(mGoodsId, mHandler);
                    }
                }.start();
                break;
            case R.id.goods_buy_tv:
                new Thread() {
                    @Override
                    public void run() {
                        AlipayAPI.pay(GoodsDetilActivity.this, mName, mIntr_tv.getText().toString(), mPrice);
                    }
                }.start();
                break;
            case R.id.goods_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.goods_add_tv:
                ViewUtil.jumpToOtherActivity(this, MySoppingActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
