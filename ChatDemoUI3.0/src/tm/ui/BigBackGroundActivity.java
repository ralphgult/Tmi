package tm.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xbh.tmi.R;


/**
 * 作者: Lking
 * 时间: 2017/3/21
 * 邮箱： 1599863094@qq.com
 * 说明：
 */
public class BigBackGroundActivity extends Activity {

    private ViewPager mViewPager;
    // 记录当前的页数
    private int mCount = 0;
    // 开始
    public static final int START = -1;
    // 停止
    public static final int STOP = -2;
    // 更新
    public static final int UPDATE = -3;
    // 接受传过来的当前页面数
    public static final int RECORD = -4;
    private List<ImageView> mList;
    private MyPagerAdapter mAdapter;
    private List<String> urlList;
    private ImageView dot1, dot2, dot3, dot0, dot4, dot5;
    private ImageView[] dots = new ImageView[6];
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case START:
                    handler.sendEmptyMessageDelayed(UPDATE, 3000);
                    break;
                case STOP:
                    handler.removeMessages(UPDATE);
                    break;
                case UPDATE:
                    Log.e("Lking","mCount = "+mCount);
                    mCount++;
                    mViewPager.setCurrentItem(mCount);
                    break;
                case RECORD:
                    mCount = msg.arg1;
                    break;

                default:
                    break;
            }

        }
    };
    private String[] paths;
    private int index;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigactivity_background);
        initView();
        init();
        mAdapter = new MyPagerAdapter(mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(status);
//        int i = Integer.MAX_VALUE / 2 % mList.size();
//        // 默认在中间，让用户看不到边界
//        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - i);
//		handler.sendEmptyMessage(START);
    }

    private void init() {
        // TODO Auto-generated method stub
        urlList = new ArrayList<String>();
        mList = new ArrayList<ImageView>();
        paths = getIntent().getExtras().getStringArray("path");
        status = getIntent().getExtras().getInt("status");
        ImageView imageView;

        if(null!=paths && paths.length>0){
            index = paths.length;
            for(int i=0;i<paths.length;i++){
                urlList.add(paths[i]);
            }
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).displayer(new RoundedBitmapDisplayer(50))
                    .displayer(new FadeInBitmapDisplayer(100)).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();

            for (int i = 0; i < paths.length; i++) {
                imageView = new ImageView(BigBackGroundActivity.this);
                imageView.setScaleType(ScaleType.FIT_XY);
                // 使用的ImageLoader网络加载图片，需先在Application和清单文件中配置在使用
                Log.e("Lking","wangluo = "+i);
                ImageLoader.getInstance().displayImage(urlList.get(i), imageView,
                        options);
                mList.add(imageView);
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                        System.out.println("==========m==" + mCount % mList.size());
                        // 这里写点击图片的操作 mCount % mList.size()这个点击的第几个图片
                    }
                });
            }
        }else if(!TextUtils.isEmpty(getIntent().getExtras().getString("filePath"))){
            String path = getIntent().getExtras().getString("filePath");
            imageView = new ImageView(BigBackGroundActivity.this);
            ImageLoader.getInstance().displayImage(path,imageView);
        }

    }

    private void initView() {
        // TODO Auto-generated method stub
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

}
