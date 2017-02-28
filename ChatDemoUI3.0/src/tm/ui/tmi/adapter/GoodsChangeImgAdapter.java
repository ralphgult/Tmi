package tm.ui.tmi.adapter;

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

import java.util.ArrayList;
import java.util.List;

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
    private ModeDto modeDto;

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
        mPicList = picList;
        modeDto = new ModeDto();
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
        //防止第0位重复加载
        if (position == 0 && modeDto.isHasFirstLoaded()) {
            return view;
        }
        if (position == 0) {
            modeDto.setHasFirstLoaded(true);
        }
        if (!TextUtils.isEmpty(mPicList.get(position))) {
            vh.pic.setVisibility(View.VISIBLE);
            Log.e("info", "positon =============== " + position + " &&&&&&&& path ========= " + mPicList.get(position));
            if (!mPicList.get(position).equals("0")) {
                vh.del.setVisibility(View.VISIBLE);
                String imgPath = mPicList.get(position);
                if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
                    imageLoaders.loadImage(vh.pic, imgPath);
                } else {
                    vh.pic.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                }
            } else {
                vh.del.setVisibility(View.GONE);
                vh.pic.setImageResource(R.drawable.em_add_new);            }
        } else {
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

    class ModeDto {
        private boolean hasFirstLoaded = false;
        public boolean isHasFirstLoaded() {
            return hasFirstLoaded;
        }

        public void setHasFirstLoaded(boolean hasFirstLoaded) {
            this.hasFirstLoaded = hasFirstLoaded;
        }
    }

    public List<String> getmPicList() {
        return mPicList;
    }

}
