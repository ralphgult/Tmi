package tm.ui.tmi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xbh.tmi.R;


/**
 * Created by RalphGult on 2016/10/20.
 */

public class ImageAdapter extends BaseAdapter {
    private String[] mPicList;
    private ViewHolder vh;
    private Context mContext;

    public ImageAdapter(Context context){
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        vh.pic.setImageURI(Uri.parse(mPicList[position]));
        return view;
    }
    static class ViewHolder {
        ImageView pic;
    }
}
