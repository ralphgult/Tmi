package tm.ui.tmi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by RalphGult on 2016/10/20.
 */

public class OrderAdapter extends BaseAdapter {
    private ViewHolder vh;
    private List<Map<String, String>> dataList;
    private Context mContext;
    private int mType;

    public OrderAdapter(Context context, int type){
        mContext = context;
        mType = type;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.order_item_layout, null);
            vh = new ViewHolder();
            vh.compName = (TextView) view.findViewById(R.id.order_item_compname);
            vh.head = (ImageView) view.findViewById(R.id.order_item_header);
            vh.name = (TextView) view.findViewById(R.id.order_item_name);
            vh.price = (TextView) view.findViewById(R.id.order_item_price);
            vh.num = (TextView) view.findViewById(R.id.order_item_num);
            vh.count = (TextView) view.findViewById(R.id.order_item_count);
            vh.total = (TextView) view.findViewById(R.id.order_item_total);
            vh.btn_left = (Button) view.findViewById(R.id.order_item_btn_left);
            vh.btn_right = (Button) view.findViewById(R.id.order_item_btn_right);
            vh.reason = (TextView) view.findViewById(R.id.order_manager_reason);
            vh.btn_three = (Button) view.findViewById(R.id.order_item_btn_third);
            view.setTag(vh);
        }else{

            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        switch (mType){
            case 0:
                vh.reason.setVisibility(View.GONE);
                vh.btn_left.setText("联系买家");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("发货");
                break;
            case 1:
                vh.reason.setVisibility(View.GONE);
                vh.btn_left.setText("联系买家");
                vh.btn_right.setVisibility(View.GONE);
                break;
            case 2:
                vh.reason.setVisibility(View.GONE);
                vh.btn_left.setText("查看物流");
                vh.btn_right.setVisibility(View.GONE);
                break;
            case 3:
                vh.reason.setVisibility(View.VISIBLE);
                vh.reason.setText("退款理由：" + dataList.get(position).get("reason"));
                vh.btn_left.setText("联系买家");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("确认退款");
                vh.btn_right.setTextColor(mContext.getResources().getColor(R.color.common_bottom_bar_normal_bg));
                vh.btn_right.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.order_gray_edge_gb));
                break;
            case 4:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("联系卖家");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("取消订单");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("付款");
                break;
            case 5:
            case 6:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("退款");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("查看物流");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("确认收货");
                break;
            case 7:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("退款");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("删除");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("评价");
                break;
        }
        vh.compName.setText(dataList.get(position).get("comName"));
        vh.head.setBackgroundResource(R.drawable.default_pic);
        vh.name.setText(dataList.get(position).get("name"));
        vh.price.setText("￥" + dataList.get(position).get("price"));
        vh.num.setText("x" + dataList.get(position).get("num"));
        vh.count.setText("共" + dataList.get(position).get("num") + "件商品  合计：");
        vh.total.setText("￥" + String.valueOf(Float.valueOf(dataList.get(position).get("price")) * Integer.valueOf(dataList.get(position).get("num"))));
        return view;
    }
    private class ViewHolder{
        TextView compName;
        ImageView head;
        TextView name;
        TextView price;
        TextView num;
        TextView count;
        TextView total;
        TextView reason;
        Button btn_left;
        Button btn_right;
        Button btn_three;
    }
}
