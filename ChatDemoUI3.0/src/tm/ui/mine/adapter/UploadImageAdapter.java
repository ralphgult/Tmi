package tm.ui.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;

import tm.ui.mine.UploadAuctionActivity;
import tm.utils.ImageLoaders;

/**
 * Created by RG on 2017/1/23.
 */

public class UploadImageAdapter extends BaseAdapter {
    private String[] mPicList;
    private ViewHolder vh;
    private Context mContext;
    private ImageLoaders imageLoaders;

    public UploadImageAdapter(Context context) {
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
        mPicList = new String[picList.length];
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
            vh.del = (ImageView) view.findViewById(R.id.facewall_item_del_tv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        if (!TextUtils.isEmpty(mPicList[position])) {
            if (mPicList[position].equals("0")) {
                vh.del.setVisibility(View.GONE);
            } else {
                vh.del.setVisibility(View.VISIBLE);
                vh.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((UploadAuctionActivity) mContext).delAdapterData(mPicList[position]);
                    }
                });
            }
            if (mPicList[position].equals("0")) {
                vh.pic.setImageResource(R.drawable.em_add_new);
            } else {
                Log.e("info", "imagePath -=-=-=-=-=-=-= " + mPicList[position]);
                if (mPicList[position].startsWith("http://") || mPicList[position].startsWith("https://")) {
                    imageLoaders.loadImage(vh.pic, mPicList[position]);
                } else {
                    vh.pic.setImageBitmap(BitmapFactory.decodeFile(mPicList[position]));
                }
            }
        } else {
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
