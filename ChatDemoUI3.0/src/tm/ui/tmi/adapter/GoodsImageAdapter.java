package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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

    public GoodsImageAdapter(Context context) {
        mContext = context;
        mLoaders = new ImageLoaders(mContext, new loaderListener());
    }

    public GoodsImageAdapter(Context context, int imgWidth){
        mContext = context;
        mItemWidth = imgWidth;
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
        ImageView view = new ImageView(mContext);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0 == mItemWidth ? ViewGroup.LayoutParams.MATCH_PARENT : mItemWidth;
        params.width = 0 == mItemWidth ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewUtil.dp2px(mContext, 250);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mLoaders.loadImage(view, mImgPaths.get(position));
        return view;
    }

    public class loaderListener implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }
}
