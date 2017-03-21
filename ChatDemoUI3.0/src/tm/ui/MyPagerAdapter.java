package tm.ui;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

public class MyPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews;

    public MyPagerAdapter(List<ImageView> list) {
        super();
        imageViews = list;
        Log.e("Lking","img.size = "+imageViews.size());
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("Lking","po = "+position);

        // TODO Auto-generated method stub
        // 因为实际只有几个页面但是我们要无限循环，所以取模计算出当前的是第几个页面
        int i = position % imageViews.size();
        // 预防负值
        position = Math.abs(i);
        if(imageViews.size() == 1){
            position = 0;
        }
        Log.e("Lking","position = "+position);

        ImageView imageView = imageViews.get(position);
        ViewParent parent = imageView.getParent();
        // remove掉View之前已经加到一个父控件中，否则报异常
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }
}

