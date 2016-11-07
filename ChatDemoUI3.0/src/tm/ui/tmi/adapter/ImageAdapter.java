package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.plus.model.moments.Moment;
import com.xbh.tmi.R;

import tm.ui.mine.HeadBigActivity;
import tm.ui.mine.PersonCenterActivity;
import tm.ui.tmi.MomentsActivity;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;


/**
 * Created by RalphGult on 2016/10/20.
 */

public class ImageAdapter extends BaseAdapter {
    private String[] mPicList;
    private ViewHolder vh;
    private Context mContext;
    private ImageLoaders imageLoaders;

    public ImageAdapter(Context context) {
        mContext = context;
        imageLoaders = new ImageLoaders(mContext, new imageLoaderListener());
    }

    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    public void resetData(String[] picList) {
        mPicList = picList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPicList.length;
    }

    @Override
    public Object getItem(int position) {
        return mPicList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.yx_common_item_image_only_gridview_layout, null);
            vh.pic = (ImageView) view.findViewById(R.id.image_item_iv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        imageLoaders.loadImage(vh.pic, mPicList[position]);
        vh.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("path", mPicList[position]);
                ViewUtil.jumpToOtherActivity((MomentsActivity) mContext, HeadBigActivity.class,bundle);
            }
        });
        return view;
    }

    static class ViewHolder {
        ImageView pic;
    }
}
