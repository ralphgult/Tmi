package tm.ui.tmi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.List;
import java.util.Map;

/**
 * Created by RalphGult on 2016/11/12.
 */

public class ReplyAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<Map<String, String>> mData;
    private ViewHolder vh;

    public ReplyAdapter(Context context) {
        mContext = context;
    }

    public void resetData(List<Map<String, String>> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_reply, null);
            vh = new ViewHolder();
            vh.name = (TextView) view.findViewById(R.id.item_reply_name);
            vh.content = (TextView) view.findViewById(R.id.item_reply_content);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        if (TextUtils.isEmpty(mData.get(position).get("userName"))) {
            vh.name.setText("我:");
        }else{
            vh.name.setText(mData.get(position).get("userName") + ":");
        }
        vh.content.setText(mData.get(position).get("comment"));
        return view;
    }

    class ViewHolder {
        TextView name;
        TextView content;
    }
}
