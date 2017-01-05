package tm.ui.tmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;

import tm.ui.mine.HeadBigActivity;
import tm.ui.tmi.MomentsActivity;
import tm.ui.tmi.ReleaseFriendActivity;
import tm.utils.ImageLoaders;
import tm.utils.ImageUtil;
import tm.utils.ViewUtil;
import android.provider.MediaStore.Images.Media;


/**
 * Created by RalphGult on 2016/10/20.
 */

public class ImageAdapter extends BaseAdapter {
    private String[] mPicList;
    private ViewHolder vh;
    private Context mContext;
    private ImageLoaders imageLoaders;
    private boolean mIsDel;

    public ImageAdapter(Context context,boolean isDel) {
        mContext = context;
        mIsDel = isDel;
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
            vh.del = (ImageView) view.findViewById(R.id.img_item_del_iv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(mPicList[position])) {
            vh.pic.setVisibility(View.VISIBLE);
            if(!mIsDel || mPicList[position].equals("0")){
                vh.del.setVisibility(View.GONE);
            }else{
                vh.del.setVisibility(View.VISIBLE);
            }
            if(mPicList[position].equals("0")){
                vh.pic.setImageResource(R.drawable.em_add_new);
            }else{
                if (mPicList[position].startsWith("http://")) {
                    imageLoaders.loadImage(vh.pic, mPicList[position]);
                }else if(mPicList[position].startsWith(Environment.getExternalStorageDirectory().getPath())){
                    vh.pic.setImageBitmap(ImageUtil.resizeBitmapForce(mPicList[position],110,110));
                }
            }
            vh.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPicList[position].equals("0")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                Media.EXTERNAL_CONTENT_URI);
                        ((Activity)mContext).startActivityForResult(intent, 1);
                    }else{
                        Bundle bundle = new Bundle();
                        if (mPicList[position].startsWith("http://")) {
                            bundle.putString("path", mPicList[position]);
                        }else if(mPicList[position].startsWith(Environment.getExternalStorageDirectory().getPath())){
                            bundle.putString("filePath", mPicList[position]);
                        }
                        ViewUtil.jumpToOtherActivity((Activity) mContext, HeadBigActivity.class,bundle);
                    }
                }
            });
            vh.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ReleaseFriendActivity)mContext).delAdapterData(mPicList[position]);
                }
            });
        }else{
            vh.pic.setVisibility(View.GONE);
            vh.pic.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        ImageView pic;
        ImageView del;
    }
}
