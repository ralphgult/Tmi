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
import android.widget.Toast;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;

import tm.ui.tmi.GoodsChangeActivity;
import tm.utils.ImageLoaders;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

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

//        Log.e("Lking","新的list = "+picList);
//        mPicList.clear();
//        mPicList.addAll(picList);
//        modeDto = new ModeDto();
//        notifyDataSetChanged();
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

    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
                imageLoaders.loadImage(vh.pic,mPicList.get(position));
                //todo 删除的时候，第一张删除不了
//                if(position == 0){
//                    ImageLoaders img = new ImageLoaders(mContext, new imageLoaderListener());
//                    img.loadImage(vh.pic,mPicList.get(0));
//                }else{
//                    String imgPath = mPicList.get(position);
//                    //清除缓存
//                    vh.pic.refreshDrawableState();
//                    if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
//                        imageLoaders.loadImage(vh.pic, imgPath);
//                    } else {
//                        imageLoaders.clearCache();
//                        vh.pic.setImageBitmap(BitmapFactory.decodeFile(imgPath));
//                    }
//                }
            } else {
                    Log.e("Lking","加载最后一张默认添加图");
                    vh.del.setVisibility(View.GONE);
                    vh.pic.refreshDrawableState();
                    String imgPath = "http://a3.qpic.cn/psb?/V12LbzOe2y8yLa/oJb*vPALr.unNctTK6hPxNoOZCbIZ9eQwIvqqD0.1TE!/b/dIIBAAAAAAAA&bo=nQCgAAAAAAADBx8!&rf=viewer_4";
                    imageLoaders.loadImage(vh.pic, imgPath);
                }
        } else {
            vh.pic.setVisibility(View.GONE);
            vh.del.setVisibility(View.GONE);
        }

        vh.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0 && mPicList.size()>=2){
                    Toast.makeText(mContext,"控件缓存未清除，但是图片已替换，可点击放大查看",Toast.LENGTH_SHORT).show();
                }
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
