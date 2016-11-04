package tm.ui.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.xbh.tmi.R;

import tm.ui.mine.HeadBigActivity;
import tm.ui.mine.PersonCenterActivity;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;

/**
 * Created by RalphGult on 2016/11/4.
 */

public class FaceWallAdapter extends BaseAdapter {
    private String[] mPicList;
    private ViewHolder vh;
    private Context mContext;
    private ImageLoaders imageLoaders;

    public FaceWallAdapter(Context context){
        mContext = context;
        imageLoaders = new ImageLoaders(mContext,new imageLoaderListener());
    }
    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }
    public void resetData(String[] picList){
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
            view = View.inflate(mContext, R.layout.item_facewall_layout, null);
            vh.pic = (ImageView) view.findViewById(R.id.facewall_item_iv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        if (!mPicList[position].equals("0")) {
            imageLoaders.loadImage(vh.pic,mPicList[position]);
        }else{
            vh.pic.setImageResource(R.drawable.em_add);
        }
        vh.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPicList[position].equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path",mPicList[position]);
                    ViewUtil.jumpToOtherActivity((PersonCenterActivity)mContext, HeadBigActivity.class);
                }else{
                    Toast.makeText(mContext,"添加图片",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    static class ViewHolder {
        ImageView pic;
    }
}
