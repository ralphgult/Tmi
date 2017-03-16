package tm.ui.tmi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tm.alipay.AlipayAPI;
import tm.alipay.PayResult;
import tm.manager.PersonManager;
import tm.ui.mine.MySoppingActivity;
import tm.ui.tmi.adapter.GoodsImageAdapter;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.widget.StationaryGridView;

public class GoodsDetilActivity extends Activity implements View.OnClickListener {
    private static final int SDK_PAY_FLAG = 1;
    private ImageView mHeadGrall;
    private TextView mPrice_tv;
    private TextView mOldPri_tv;
    private TextView mName_tv;
    private TextView mIntr_tv;
    private TextView mShop_tv;
    private TextView mChat_tv;
    private TextView mCar_tv;
    private TextView mBuy_tv;
    private StationaryGridView mPic_gv;
    private ImageView mBack_tv;
    private TextView mShopCar_tv;
    private ScrollView mRoot;

    private String mGoodsId;
    private Handler mHandler;
    private GoodsImageAdapter mHeadAdapter;
    private GoodsImageAdapter mPicAdapter;
    private String mName;
    private String mPrice;
    private ImageLoaders mLoader;
    private String mDetailStr;

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
        mHeadAdapter = new GoodsImageAdapter(this);
        mPicAdapter = new GoodsImageAdapter(this);
        mLoader = new ImageLoaders(this,new ImageLoaderLinstener());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject object = (JSONObject) msg.obj;
                switch (msg.what) {
                    case 1001:
                        try {
                            mName = object.optString("goodName");
                            mPrice = object.optString("currentPrice");
                            mPrice_tv.setText("商品价格：￥" + mPrice);
                            mOldPri_tv.setText("原价:￥" + object.optString("originalPrice"));
                            mName_tv.setText("商品名称："+mName);
                            mDetailStr = object.optString("goodProfiles");
                            mIntr_tv.setText("商品描述："+mDetailStr);

                            mLoader.loadImage(mHeadGrall, object.optString("goodImg"));
                            JSONArray array = object.getJSONArray("imgs");
                            if (null != array && array.length() > 0) {
                                int size = array.length();
                                List<String> picPaths = new ArrayList<>(size);
                                for (int i = 0; i < size; i++) {
                                    picPaths.add(array.getJSONObject(i).getString("goodImg"));
                                }
                                mPicAdapter.resetData(picPaths);
                                mPic_gv.setAdapter(mPicAdapter);
                                mPic_gv.setVisibility(View.VISIBLE);
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
        mHeadGrall = (ImageView) findViewById(R.id.goods_gall);
        mName_tv = (TextView) findViewById(R.id.goods_name_tv);
        mPrice_tv = (TextView) findViewById(R.id.goods_price_tv);
        mOldPri_tv = (TextView) findViewById(R.id.goods_old_price_tv);
        mIntr_tv = (TextView) findViewById(R.id.goods_intor_tv);
        mPic_gv = (StationaryGridView) findViewById(R.id.goods_pic_gv);
        mShop_tv = (TextView) findViewById(R.id.goods_shop);
        mChat_tv = (TextView) findViewById(R.id.goods_chat_tv);
        mCar_tv = (TextView) findViewById(R.id.goods_shopcar_tv);
        mBuy_tv = (TextView) findViewById(R.id.goods_buy_tv);
        mBack_tv = (ImageView) findViewById(R.id.goods_back_iv);
        mShopCar_tv = (TextView) findViewById(R.id.goods_add_tv);
        mRoot = (ScrollView) findViewById(R.id.goods_root);
        mRoot.scrollTo(0,0);
        mChat_tv.setOnClickListener(this);
        mCar_tv.setOnClickListener(this);
        mBuy_tv.setOnClickListener(this);
        mBack_tv.setOnClickListener(this);
        mShopCar_tv.setOnClickListener(this);
        ViewGroup.LayoutParams params = mPic_gv.getLayoutParams();
        params.width = getWindowManager().getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mPic_gv.setLayoutParams(params);
    }

    private void getGoodsFromServer() {
        new Thread() {
            @Override
            public void run() {
                PersonManager.getGoods(mGoodsId, mHandler);
            }
        }.start();
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
                new AliPayThread().start();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        AlipayAPI.pay(GoodsDetilActivity.this, mName,mDetailStr, mPrice);
//                    }
//                }.start();
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
    class ImageLoaderLinstener implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }


    private Handler mHandlershop=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    System.out.println("支付返回 = "+(String) msg.obj);
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(GoodsDetilActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(GoodsDetilActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(GoodsDetilActivity.this,
                                    "支付失败" + resultStatus+resultInfo, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    break;
                }
            }
        };
    };
    /**
     * 支付宝支付异步任务
     *
     * @author Simon
     */
    private class AliPayThread extends Thread {
        @Override
        public void run() {
            String result = AlipayAPI.pay(GoodsDetilActivity.this, mName,mDetailStr, mPrice);
//            String result = AlipayAPI.pay(ShoppingPayActivity.this, "测试的商品",
//                    "测试商品的详细描述", "0.01");
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandlershop.sendMessage(msg);
        }
    }
}
