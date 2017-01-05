package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;

import java.util.List;

import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;

/**
 * Created by RG on 2017/1/4.
 */

public class GoodsImageAdapter extends BaseAdapter {
    private List<String> mImgPaths;
    private Context mContext;
    private ImageLoaders mLoaders;
    private int mItemWidth;
    private ViewHodler hodler;

    public GoodsImageAdapter(Context context) {
        mContext = context;
        mLoaders = new ImageLoaders(mContext, new loaderListener());
    }

    public void resetData(List<String> paths) {
        mImgPaths = paths;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (null == convertView) {
            view = View.inflate(mContext, R.layout.goods_pic_item, null);
            hodler = new ViewHodler();
            hodler.mPic_iv = (ImageView) view.findViewById(R.id.goods_pic_iv);
            view.setTag(hodler);
        } else {
            view = convertView;
            hodler = (ViewHodler) view.getTag();
        }
        mLoaders.loadImage(hodler.mPic_iv, mImgPaths.get(position));
        return view;
    }

    public class loaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    class ViewHodler {
        private ImageView mPic_iv;
    }
}
