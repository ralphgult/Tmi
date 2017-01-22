package tm.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tm.manager.PersonManager;
import tm.ui.main.adapter.ActionAdapter;
import tm.utils.ImageLoaders;

/**
 * Created by Administrator on 2017/1/21.
 */

public class AuctionDetailActivity extends Activity{
    private ImageView mBackImg;
    private ImageView mDetailImgs;
    private TextView mDetailName;
    private TextView mDetailNumber;
    private TextView mDetailContact;
    private TextView mDetailPrice;
    private TextView mDetailPriceUnit;
    private TextView mDetailPriceOrig;
    private TextView mDetailPriceNum;
    private TextView mDetailTime;
    private TextView mDetailBid;
    private TextView mDetailText;
    private ImageView mDetailImg01;
    private ImageView mDetailImg02;
    private ImageView mDetailImg03;
    private ImageView mDetailImg04;
    private ImageView mDetailImg05;

    private ImageView[] ivArray = {mDetailImg01, mDetailImg02, mDetailImg03, mDetailImg04, mDetailImg05};
    private ImageLoaders imageLoaders;
    private String mShopDetailId;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3001) {
                Log.e("LKing","shopping_detail = "+String.valueOf(msg.obj));
                try{
//                    JSONObject jsonObject = new JSONObject();
//                    imageLoaders.loadImage(mDetailImgs, jsonObject.getString("auctionImg"));
//                    mDetailName.setText("商品名称:"+jsonObject.getString("name"));
//                    mDetailNumber.setText("商品编号:"+jsonObject.getString("number"));
//                    mDetailPrice.setText("当前价格:RMB "+jsonObject.getString("price"));
////                    mDetailPriceUnit.setText("加价单位:"+jsonObject.getString("number")+"元");
//                    mDetailPriceOrig.setText("直购价:RMB "+jsonObject.getString("originalPrice"));
////                    mDetailPriceNum.setText("出价"+jsonObject.getString("originalPrice")+"次");
//                    mDetailTime.setText("剩余时间:"+AuctionActivity.formatTime(Integer.parseInt(jsonObject.getString("residual"))));
//                    mDetailText.setText(jsonObject.getString("details"));
//                    JSONArray jsonArray = jsonObject.getJSONArray("auctionImgs") ;
//                    for(int i=0;i<jsonArray.length();i++){
//                        imageLoaders.loadImage(ivArray[i],String.valueOf(jsonArray.opt(i)));
//                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_detail_activity);
        getActivityIntent();
        initView();
        setListener();
        networkRequest();
    }

    private void getActivityIntent() {
        if (getIntent().getExtras()!=null) {
            mShopDetailId = getIntent().getExtras().getString("shopping_detail_id");
        }
    }

    private void initView() {
        mBackImg = (ImageView)findViewById(R.id.auction_detail_back_iv);
        mDetailImgs = (ImageView)findViewById(R.id.auction_detail_imgs);//图片显示
        mDetailName = (TextView)findViewById(R.id.auction_detail_name);//名字
        mDetailNumber = (TextView)findViewById(R.id.auction_detail_number);//编号
        mDetailContact = (TextView)findViewById(R.id.auction_contact);//联系
        mDetailPrice = (TextView)findViewById(R.id.auction_detail_price);//当前价格
        mDetailPriceUnit = (TextView)findViewById(R.id.auction_detail_unit_price);//单价
        mDetailPriceOrig = (TextView)findViewById(R.id.auction_detail_original_price);//直购价
        mDetailPriceNum = (TextView)findViewById(R.id.auction_detail_price_number);//出价次数
        mDetailTime = (TextView)findViewById(R.id.auction_detail_time);//剩余时间
        mDetailBid = (TextView)findViewById(R.id.auction_detail_bid);//手动出价
        mDetailText = (TextView)findViewById(R.id.auction_detail_txt);//拍品详情
        mDetailImg01 = (ImageView)findViewById(R.id.auction_img_01);//小图片
        mDetailImg02 = (ImageView)findViewById(R.id.auction_img_02);//小图片
        mDetailImg03 = (ImageView)findViewById(R.id.auction_img_03);//小图片
        mDetailImg04 = (ImageView)findViewById(R.id.auction_img_04);//小图片
        mDetailImg05 = (ImageView)findViewById(R.id.auction_img_05);//小图片
        imageLoaders = new ImageLoaders(AuctionDetailActivity.this,new MyImageLoaderListener());
//
    }

    private void setListener() {
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDetailContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AuctionDetailActivity.this,"联系我们",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"18792681661"));
//                startActivity(intent);
            }
        });
        mDetailBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AuctionDetailActivity.this,"手动出价一次，成功后....",Toast.LENGTH_SHORT).show();
                mDetailPriceNum.setText("出价加一次");
            }
        });
    }

    private void networkRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PersonManager.getAuctionDetail(handler,mShopDetailId);
            }
        }).start();
    }

    class MyImageLoaderListener implements ImageLoaders.ImageLoaderListener{
        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

}
