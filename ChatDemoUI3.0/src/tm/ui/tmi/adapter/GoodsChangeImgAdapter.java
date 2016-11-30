package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;

import tm.manager.PersonManager;
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.ui.tmi.GoodsChangeActivity;
import tm.utils.ImageLoaders;

/**
 * Created by RalphGult on 2016/11/26.
 */

public class GoodsChangeImgAdapter extends BaseAdapter {
    private List<String> mPicList;
    private ViewHolder vh;
    private Context mContext;
    private ImageLoaders imageLoaders;

    public GoodsChangeImgAdapter(Context context) {
        mContext = context;
        mPicList = new ArrayList<>();
        imageLoaders = new ImageLoaders(mContext, new imageLoaderListener());
    }

    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    public void resetData(List<String> picList) {
        mPicList.clear();
        mPicList.addAll(picList);
        if(mPicList.size() < 8){
            mPicList.add("0");
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicList.get(position);
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
        if (null != mPicList.get(position)) {
            vh.pic.setVisibility(View.VISIBLE);
            if (!mPicList.get(position).equals("0")) {
                vh.del.setVisibility(View.VISIBLE);
                imageLoaders.loadImage(vh.pic,mPicList.get(position));
            }else{
                vh.del.setVisibility(View.GONE);
                vh.pic.setImageResource(R.drawable.em_add);
            }
        }else{
            vh.pic.setVisibility(View.GONE);
            vh.del.setVisibility(View.GONE);
        }
        vh.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoodsChangeActivity) mContext).deleteImage(position);
            }
        });
        return view;
    }

    static class ViewHolder {
        ImageView pic;
        ImageView del;
    }

    public List<String> getmPicList() {
        return mPicList;
    }

}
