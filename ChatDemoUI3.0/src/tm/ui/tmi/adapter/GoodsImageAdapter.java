package tm.ui.tmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;

import java.util.List;

import tm.ui.mine.HeadBigActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        hodler.mPic_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                String []strs = new String [mImgPaths.size()];
                int indexs =0;
                for(int i=0;i<mImgPaths.size();i++){
                    strs[i] = mImgPaths.get(i);
                    if(strs[i].toString().equals("0")){
                        indexs = i;
                    }
                }
                if (indexs != 0) {
                    String []ary = new String[strs.length - 1];
                    System.arraycopy(strs, 0, ary, 0, indexs);
                    System.arraycopy(strs, indexs + 1, ary, indexs, ary.length - indexs);
                    bundle.putStringArray("path",ary);
                }else{
                    bundle.putStringArray("path",strs);
                }



                bundle.putStringArray("path",strs);
                bundle.putInt("status", position);

                ViewUtil.jumpToOtherActivity(((Activity) mContext), HeadBigActivity.class, bundle);
            }
        });
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
