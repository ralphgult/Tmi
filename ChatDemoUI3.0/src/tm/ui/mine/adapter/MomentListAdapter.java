package tm.ui.mine.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.redpacketui.recyclerview.widget.RecyclerView;
import com.hyphenate.tmdemo.R;

import java.util.List;
import java.util.Map;

import tm.entity.ResourcesBean;
import tm.ui.main.adapter.BdtsAdapter;
import tm.widget.lvdapter.viewholder.CustomCommonViewHolder;

/**
 * Created by RalphGult on 9/29/2016.
 */

public class MomentListAdapter extends BaseAdapter {
    private List<Map<String, String>> dataList = null;
    private ArrayMap<Integer,List<String>> picList = null;
    private ViewHolder vh;
    private Context mContext;

    public MomentListAdapter(Context context) {
        mContext = context;
    }

    public void resetData(List<Map<String, String>> datas,ArrayMap<Integer,List<String>> pics) {
        dataList = datas;
        picList = pics;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            vh.head_iv = (ImageView) view.findViewById(R.id.yx_monent_top_head_img);
            vh.name_tv = (TextView) view.findViewById(R.id.yx_monent_top_name);
            vh.time_tv = (TextView) view.findViewById(R.id.yx_monent_top_time);
//            vh.content_tv = (TextView) view.findViewById(R.id.yx_monent_content_text);
//            vh.pic_gv = (GridView) view.findViewById(R.id.yx_monent_content_gv);
        }else{
            view = convertView;
            vh = (ViewHolder)view.getTag();
        }
        vh.head_iv.setImageURI(Uri.parse(dataList.get(position).get("headImg")));
        vh.name_tv.setText(dataList.get(position).get("name"));
        vh.time_tv.setText(dataList.get(position).get("time"));
        vh.content_tv.setText(dataList.get(position).get("content"));
        if(null != picList.get(position)){
            vh.pic_gv.setVisibility(View.VISIBLE);
        }
        return view;
    }

    static class ViewHolder {
        ImageView head_iv;
        TextView name_tv;
        TextView time_tv;
        TextView content_tv;
        GridView pic_gv;

    }
}
