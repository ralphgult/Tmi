package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import tm.alipay.AlipayAPI;
import tm.manager.PersonManager;
import tm.ui.mine.HeadBigActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;

/**
 * Created by Lking on 2016/12/12.
 */
public class FosterAgriculturalActivity extends Activity {
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mCommodityLayout;
    private LinearLayout mBomLayout;
    private TextView mTop;
    private TextView mName;
    private TextView mNumber;
    private TextView mIntegral;
    private TextView mTxt_02;
    private TextView mTxt_01;
    private TextView mDetail;
    private ImageView mImg01;
    private ImageView mImg02;
    private ImageView mImg03;
    private ImageView mImg04;
    private int position;//上传的第几个
    private String userId;//用户ID
    private ImageLoaders imageLoaders;
    String[] strimgs = new String[4];
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    break;
                case 1001:
                    break;
                case 2001:
                    break;
                case 2002:
                    break;
                case 3001:
                    String json = msg.obj.toString();
                    Log.e("Lking--->", "str = " + json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String top = jsonObject.getString("top");
                        mTop.setText(top);
                        String name = jsonObject.getString("goodsName");
                        mName.setText(name);
                        String goodsCount = jsonObject.getString("goodsCount");
                        mNumber.setText(goodsCount + "盒");
                        String integral = jsonObject.getString("integral");
                        mIntegral.setText(integral + "元");
                        String introduce = jsonObject.getString("introduce");
                        mDetail.setText(introduce);

                        JSONArray jsonArray = jsonObject.getJSONArray("sfi");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mark = (JSONObject) jsonArray.get(i);
                            strimgs[i] = mark.getString("imgUrl");
                            Log.e("Lking--->", "img = " + strimgs[i]);
                        }
                        imageLoaders.loadImage(mImg01, strimgs[0]);
                        imageLoaders.loadImage(mImg02, strimgs[1]);
                        imageLoaders.loadImage(mImg03, strimgs[2]);
                        imageLoaders.loadImage(mImg04, strimgs[3]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4001:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_agricultural);
        //获取9宫格传递过来的数据
        getIntentInfo();
        initView();
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        if (position > 6) {
            mTitle.setText("扶植农业");
            mCommodityLayout.setVisibility(View.GONE);
            mBomLayout.setVisibility(View.GONE);
        } else {
            mTitle.setText("商品详情");
            mCommodityLayout.setVisibility(View.VISIBLE);
            mBomLayout.setVisibility(View.VISIBLE);
            new Thread() {
                @Override
                public void run() {
                    //根据不同的position加载不同数据
                    PersonManager.sendFosterInfo(position, mHandler);
                }
            }.start();
        }
        setLister();
    }

    private void getIntentInfo() {
        if (null != getIntent().getExtras()) {
            position = getIntent().getExtras().getInt("position");
        }
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.foster_back_iv);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTop = (TextView) findViewById(R.id.foster_rebate);
        mTitle = (TextView) findViewById(R.id.foster_title_tv);
        mCommodityLayout = (LinearLayout) findViewById(R.id.foster_commodity_layout);
        mBomLayout = (LinearLayout) findViewById(R.id.foster_bom_title);
        mName = (TextView) findViewById(R.id.foster_title_name);
        mNumber = (TextView) findViewById(R.id.foster_title_number);
        mIntegral = (TextView) findViewById(R.id.foster_title_integral);
        mDetail = (TextView) findViewById(R.id.foster_details);
        mTxt_01 = (TextView) findViewById(R.id.foster_txt_01);
        mTxt_02 = (TextView) findViewById(R.id.foster_txt_02);
        mImg01 = (ImageView) findViewById(R.id.foster_img_01);
        mImg02 = (ImageView) findViewById(R.id.foster_img_02);
        mImg03 = (ImageView) findViewById(R.id.foster_img_03);
        mImg04 = (ImageView) findViewById(R.id.foster_img_04);
    }

    private void setLister() {
        mImg01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[0];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });
        mImg02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[1];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });
        mImg03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[2];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });
        mImg04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = strimgs[3];
                Intent intent = new Intent(FosterAgriculturalActivity.this, HeadBigActivity.class);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });
        mTxt_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 1://苹果
                        Toast.makeText(FosterAgriculturalActivity.this, "苹果已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                    case 2://梨
                        Toast.makeText(FosterAgriculturalActivity.this, "梨已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                    case 3://红枣
                        Toast.makeText(FosterAgriculturalActivity.this, "红枣已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                    case 4://桃子
                        Toast.makeText(FosterAgriculturalActivity.this, "桃子已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                    case 5://葡萄
                        Toast.makeText(FosterAgriculturalActivity.this, "葡萄已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                    case 6://西瓜
                        Toast.makeText(FosterAgriculturalActivity.this, "西瓜已加入购物车", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        mTxt_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 1:
                        gotoActivity("洛川苹果", mIntegral.getText().toString());
                        break;
                    case 2:
                        gotoActivity("蒲城贡梨", mIntegral.getText().toString());
                        break;
                    case 3:
                        gotoActivity("陕北红枣", mIntegral.getText().toString());
                        break;
                    case 4:
                        gotoActivity("华晨桃子", mIntegral.getText().toString());
                        break;
                    case 5:
                        gotoActivity("渭北葡萄", mIntegral.getText().toString());
                        break;
                    case 6:
                        gotoActivity("大荔西瓜", mIntegral.getText().toString());
                        break;

                }

            }
        });
    }

    private void gotoActivity(String name, String price) {
        Intent intent = new Intent(FosterAgriculturalActivity.this, FosterShopActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        startActivity(intent);
        finish();
    }

    class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }
}
