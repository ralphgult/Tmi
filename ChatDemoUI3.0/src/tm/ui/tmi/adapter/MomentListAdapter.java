package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import java.util.List;
import java.util.Map;

import tm.utils.ImageLoaders;

/**
 * Created by RalphGult on 9/29/2016.
 */

public class MomentListAdapter extends BaseAdapter {
    private List<Map<String, String>> dataList = null;
    private ViewHolder vh;
    private Context mContext;
    private boolean mIsMoment;
    private ImageAdapter mImageAdapter;

    private ImageLoaders imageLoaders = new ImageLoaders(mContext,new imageLoaderListener());

    public class imageLoaderListener implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }

    public MomentListAdapter(Context context, boolean isMoment) {
        mContext = context;
        mIsMoment = isMoment;
        mImageAdapter = new ImageAdapter(mContext);
    }

    public void resetData(List<Map<String, String>> datas) {
        dataList = datas;
        notifyDataSetChanged();
    }
    public List<Map<String, String>> getDataList(){
        return dataList;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.yx_monent_list_item, null);
            vh = new ViewHolder();
            vh.head_iv = (ImageView) view.findViewById(R.id.moment_item_list_iv);
            vh.name_tv = (TextView) view.findViewById(R.id.yx_monment_name);
            vh.time_tv = (TextView) view.findViewById(R.id.yx_monment_time);
            vh.content_tv = (TextView) view.findViewById(R.id.yx_monment_content);
            vh.pic_gv = (GridView) view.findViewById(R.id.yx_monment_image_gv);
            vh.pick_single = (ImageView) view.findViewById(R.id.yx_moment_single_imgae_iv);
            vh.count = (LinearLayout) view.findViewById(R.id.yx_moment_count);
            vh.count_text = (TextView) view.findViewById(R.id.yx_monment_count_text);
            vh.like = (LinearLayout) view.findViewById(R.id.yx_moment_like);
            vh.like_img = (ImageView) view.findViewById(R.id.yx_monment_like_img);
            vh.like_text = (TextView) view.findViewById(R.id.yx_monment_like_test);
            vh.comment = (LinearLayout) view.findViewById(R.id.yx_moment_comment);
            vh.comment_text = (TextView) view.findViewById(R.id.yx_monment_comment_test);
            vh.bottom = (LinearLayout) view.findViewById(R.id.yx_monment_bottom_layout);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        vh.bottom.setVisibility(mIsMoment ? View.VISIBLE : View.GONE);
//        vh.head_iv.setImageURI(Uri.parse(dataList.get(position).get("headImg")));
        imageLoaders.loadImage(vh.head_iv,dataList.get(position).get("headImg"));
        vh.name_tv.setText(dataList.get(position).get("name"));
        vh.time_tv.setText(dataList.get(position).get("time"));
        vh.content_tv.setText(dataList.get(position).get("content"));
        if(mIsMoment){
            vh.count_text.setText(dataList.get(position).get("count"));
            vh.like_text.setText(dataList.get(position).get("like"));
            vh.comment_text.setText(dataList.get(position).get("comment"));
        }else{
            vh.bottom.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(dataList.get(position).get("pics"))) {
            vh.pic_gv.setVisibility(View.GONE);
            vh.pick_single.setVisibility(View.GONE);
        }else{
            String pics = dataList.get(position).get("pics");
            if(pics.contains(",")){
                vh.pic_gv.setVisibility(View.VISIBLE);
                vh.pick_single.setVisibility(View.GONE);
                mImageAdapter.resetData(pics.split(","));
                vh.pic_gv.setAdapter(mImageAdapter);
                setGridViewHight(vh.pic_gv);
            }else{
                vh.pic_gv.setVisibility(View.GONE);
                vh.pick_single.setVisibility(View.VISIBLE);
                vh.pick_single.setImageURI(Uri.parse(pics));
            }
        }
        return view;
    }

    static class ViewHolder {
        ImageView head_iv;
        TextView name_tv;
        TextView time_tv;
        TextView content_tv;
        GridView pic_gv;
        ImageView pick_single;
        LinearLayout count;
        TextView count_text;
        LinearLayout like;
        ImageView like_img;
        TextView like_text;
        LinearLayout comment;
        TextView comment_text;
        LinearLayout bottom;

    }
    /**
     * 设置图片显示GridView高度的方法
     *
     * @param view gridView的view对象
     */
    private void setGridViewHight(GridView view) {
        ListAdapter adapter = view.getAdapter();
        if (adapter.isEmpty()) {
            //适配器为空
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int totalHeight = 0;
        int size = adapter.getCount();
        for (int i = 1; i <= size; i = i + 3) {
            View listItem = adapter.getView(0, null, view);
            listItem.measure(0, 0);
            totalHeight = totalHeight + listItem.getMeasuredHeight() + 4;
        }
        params.height = totalHeight - 4;
        view.setLayoutParams(params);
    }
}
