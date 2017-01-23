package tm.ui.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.EditAddressActivity;
import tm.ui.mine.MyAddressActivity;
import tm.utils.ViewUtil;

/**
 * Created by RalphGult on 2016/10/20.
 */

public class AddressAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Map<String, String>> dataList;
    private ViewHolder vh;

    public AddressAdapter(Context context) {
        dataList = new ArrayList<>();
        mContext = context;
    }

    public void resetData(List<Map<String, String>> datas) {
        dataList.clear();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.addr_item_layout, null);
            vh = new ViewHolder();
            vh.name = (TextView) view.findViewById(R.id.addr_name);
            vh.phone = (TextView) view.findViewById(R.id.addr_phone);
            vh.addr = (TextView) view.findViewById(R.id.addr_addr);
            vh.isDefault = (ImageView) view.findViewById(R.id.addr_btn_default);
            vh.divide = view.findViewById(R.id.address_item_divide);
            vh.defaultText = (TextView) view.findViewById(R.id.address_item_default_text);
            vh.dele = (TextView) view.findViewById(R.id.addr_btn_delete);
            vh.edit = (TextView) view.findViewById(R.id.addr_btn_change);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        vh.divide.setVisibility(position == dataList.size() - 1 ? View.GONE : View.VISIBLE);
        vh.name.setText(dataList.get(position).get("name"));
        vh.phone.setText(dataList.get(position).get("phone"));
        vh.addr.setText(dataList.get(position).get("addr"));
        final String str = dataList.get(position).get("default");
        vh.isDefault.setSelected(str.equals("1"));
        vh.defaultText.setText(str.equals("1") ? "默认地址" : "设为默认");
        int color = str.equals("1") ? Color.parseColor("#a161fb") : mContext.getResources().getColor(R.color.gray_normal);
        vh.defaultText.setTextColor(color);
        vh.isDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!str.equals("1")) {
                    new Thread() {
                        @Override
                        public void run() {
                            PersonManager.setAddressDefault(dataList.get(position).get("id"), ((MyAddressActivity) mContext).mHandler);
                        }
                    }.start();
                }
            }
        });
        vh.dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyAddressActivity) mContext).deleteAddress(dataList.get(position).get("id"));
            }
        });
        vh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = dataList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", map.get("id"));
                bundle.putString("name", map.get("name"));
                bundle.putString("phone", map.get("phone"));
                bundle.putString("content", map.get("addr"));
                bundle.putString("default", map.get("default"));
                bundle.putBoolean("isAdd", false);
                ViewUtil.jumpToOtherActivity((MyAddressActivity) mContext, EditAddressActivity.class, bundle);
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView name;
        TextView phone;
        TextView addr;
        ImageView isDefault;
        View divide;
        TextView defaultText;
        TextView dele;
        TextView edit;
    }
}
