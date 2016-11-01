package tm.ui.welcome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;
import com.xbh.tmi.DemoHelper;
import com.xbh.tmi.ui.LoginActivity;
import com.xbh.tmi.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import tm.utils.ViewUtil;

/**
 * GuideActivity 导航界面
 *
 * @author RalphGult
 */

public class GuideActivity extends Activity {
    ViewPager mViewPager;
    //导航页下方的指示标志定义
    private ImageView circleOne = null;
    private ImageView circleTwo = null;
    private ImageView circleThree = null;
    private ImageView[] cricles;
    Button btnGetin = null;
    //导航页图片资源
    public int[] guides = new int[]{R.drawable.start_first,
            R.drawable.start_second, R.drawable.start_third};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取SharedPreferences对象
        SharedPreferences sharedPre=this.getSharedPreferences("config", this.MODE_PRIVATE);
        //获取Editor对象
        sharedPre.edit().putBoolean("hasOpened", true).commit();

        setContentView(R.layout.yx_activity_guide_layout);
        mViewPager = (ViewPager) findViewById(R.id.yx_aty_guide_viewpager);
        initView();
        initWithPageGuideMode();
    }

    private void initView() {
        circleOne = (ImageView) findViewById(R.id.yx_aty_guide_cricle_fst_iv);
        circleTwo = (ImageView) findViewById(R.id.yx_aty_guide_cricle_sec_iv);
        circleThree = (ImageView) findViewById(R.id.yx_aty_guide_cricle_thr_iv);
        cricles = new ImageView[]{circleOne, circleTwo, circleThree};
        btnGetin = (Button) findViewById(R.id.aty_guide_getin_btn);
        btnGetin.setVisibility(View.GONE);
        //设置最后一个页面上button的监听
        btnGetin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    ViewUtil.jumpToOtherActivity(GuideActivity.this, MainActivity.class, true);
                } else {
                    ViewUtil.jumpToOtherActivity(GuideActivity.this, LoginActivity.class, true);
                }
            }
        });
    }

    /**
     * 新建TextView的方法
     *
     * @return TextView 创建的TextView
     */
    private View createView() {
        TextView textView = new TextView(GuideActivity.this);
        textView.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        textView.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        return textView;
    }

    /**
     * 程序导航页效果
     */
    public void initWithPageGuideMode() {
        //添加最右侧的空白界面
        List<View> mList = new ArrayList<View>();

        for (int index : guides) {
            View view = createView();
            view.setBackgroundResource(index);
            mList.add(view);
        }

        //ViewPager最重要的设置Adapter，这和ListView一样的原理
        MViewPageAdapter adapter = new MViewPageAdapter(mList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(adapter);

    }

    /**
     * 内部类，继承PagerAdapter
     */
    class MViewPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<View> mViewList;

        public MViewPageAdapter(List<View> views) {
            mViewList = views;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mViewList.get(position), 0);
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mViewList.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position <= mViewList.size() - 1) {
                for (int i = 0; i < mViewList.size(); i++) {
                    if (!cricles[position].equals(cricles[i])) {
                        btnGetin.setVisibility(View.GONE);
                        cricles[i].setBackgroundResource(R.drawable.yx_welcome_selector_point_5_dp_gray_shap);
                    } else {
                        cricles[i].setBackgroundResource(R.drawable.yx_welcome_selector_point_5_dp_white_shap);
                    }
                }
                if (position == mViewList.size() - 1) {
                    btnGetin.setVisibility(View.VISIBLE);
                }
            }

        }

    }
}
