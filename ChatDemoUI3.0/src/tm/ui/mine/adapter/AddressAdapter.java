package tm.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import java.util.List;
import java.util.Map;

import tm.ui.tmi.adapter.OrderAdapter;

/**
 * Created by RalphGult on 2016/10/20.
 */

public class AddressAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Map<String, String>> dataList;
    private ViewHolder vh;

    public AddressAdapter(Context context){
        mContext = context;
    }

    public void resetData(List<Map<String, String>> datas){
        dataList = datas;
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
        if(convertView == null){
            view = View.inflate(mContext, R.layout.addr_item_layout,null);
            vh = new ViewHolder();
            vh.name = (TextView) view.findViewById(R.id.addr_name);
            vh.phone = (TextView) view.findViewById(R.id.addr_phone);
            vh.addr = (TextView) view.findViewById(R.id.addr_addr);
            view.setTag(vh);
        }else{
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        vh.name.setText(dataList.get(position).get("name"));
        vh.phone.setText(dataList.get(position).get("phone"));
        vh.addr.setText(dataList.get(position).get("addr"));
        return view;
    }
    private class ViewHolder{
        TextView name;
        TextView phone;
        TextView addr;
    }
}
