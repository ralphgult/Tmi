package tm.ui.tmi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public void setType(int type){
        mType = type;
    }
    public void resetData(List<Map<String, String>> datas){
        dataList = datas;
        notifyDataSetChanged();
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
            vh.reason = (LinearLayout) view.findViewById(R.id.order_manager_reason_layout);
            vh.reasonText = (TextView) view.findViewById(R.id.order_manager_reason);
            vh.btn_three = (Button) view.findViewById(R.id.order_item_btn_third);
            vh.status = (TextView) view.findViewById(R.id.order_item_status);
            view.setTag(vh);
        }else{

            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        vh.status.setVisibility(View.GONE);
        switch (mType){
            case 0:
                vh.btn_left.setText("联系买家");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("发货");
                vh.reason.setVisibility(View.GONE);
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
                vh.reasonText.setText("退款理由：" + dataList.get(position).get("reason"));
                vh.btn_left.setText("联系买家");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("确认退款");
                vh.btn_right.setTextColor(mContext.getResources().getColor(R.color.common_bottom_bar_normal_bg));
                vh.btn_right.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.order_gray_edge_gb));
                break;
            case 4:
                int status = Integer.valueOf(dataList.get(position).get("status"));
                vh.status.setVisibility(View.VISIBLE);
                vh.reason.setVisibility(View.GONE);
                switch (status){
                    case 5:
                        vh.reason.setVisibility(View.GONE);
                        vh.btn_three.setVisibility(View.VISIBLE);
                        vh.btn_three.setText("联系卖家");
                        vh.btn_left.setVisibility(View.VISIBLE);
                        vh.btn_left.setText("取消订单");
                        vh.btn_right.setVisibility(View.VISIBLE);
                        vh.btn_right.setText("付款");
                        vh.status.setText("等待买家付款");

                        break;
                    case 6:
                    case 7:
                        vh.reason.setVisibility(View.GONE);
                        vh.btn_three.setVisibility(View.VISIBLE);
                        vh.btn_three.setText("退款");
                        vh.btn_left.setVisibility(View.VISIBLE);
                        vh.btn_left.setText("查看物流");
                        vh.btn_right.setVisibility(View.VISIBLE);
                        vh.btn_right.setText("确认收货");
                        if (mType == 6) {
                            vh.status.setText("等待卖家发货");
                        }else{
                            vh.status.setText("买家已发货");
                        }
                        break;
                    case 8:
                        vh.reason.setVisibility(View.GONE);
                        vh.btn_three.setVisibility(View.VISIBLE);
                        vh.btn_three.setText("退款");
                        vh.btn_left.setVisibility(View.VISIBLE);
                        vh.btn_left.setText("删除订单");
                        vh.btn_right.setVisibility(View.VISIBLE);
                        vh.btn_right.setText("评价");
                        vh.status.setText("等待买家评价");
                        break;
                    default:
                        vh.reason.setVisibility(View.GONE);
                        vh.btn_three.setVisibility(View.GONE);
                        vh.btn_left.setVisibility(View.VISIBLE);
                        vh.btn_left.setText("删除订单");
                        vh.btn_right.setVisibility(View.GONE);
                        vh.status.setText("交易关闭");
                        break;
                }
                break;
            case 5:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("联系卖家");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("取消订单");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("付款");
                vh.status.setVisibility(View.VISIBLE);
                vh.status.setText("等待买家付款");
                vh.reason.setVisibility(View.GONE);
                break;
            case 6:
            case 7:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("退款");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("查看物流");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("确认收货");
                vh.status.setVisibility(View.VISIBLE);
                vh.reason.setVisibility(View.GONE);
                if (mType == 6) {
                    vh.status.setText("等待卖家发货");
                }else{
                    vh.status.setText("买家已发货");
                }
                break;
            case 8:
                vh.btn_three.setVisibility(View.VISIBLE);
                vh.btn_three.setText("退款");
                vh.btn_left.setVisibility(View.VISIBLE);
                vh.btn_left.setText("删除订单");
                vh.btn_right.setVisibility(View.VISIBLE);
                vh.btn_right.setText("评价");
                vh.status.setVisibility(View.VISIBLE);
                vh.reason.setVisibility(View.GONE);
                vh.status.setText("等待买家评价");
                break;
        }
        vh.compName.setText(dataList.get(position).get("comName"));
        vh.head.setBackgroundResource(R.drawable.goods);
        vh.name.setText(dataList.get(position).get("name"));
        vh.price.setText("￥" + dataList.get(position).get("price"));
        vh.num.setText("x" + dataList.get(position).get("num"));
        vh.count.setText("共" + dataList.get(position).get("num") + "件商品  合计：");
        int status = Integer.valueOf(dataList.get(position).get("status"));
        if(mType != 4 && status > 8){
            vh.reason.setVisibility(View.GONE);
            vh.btn_three.setVisibility(View.GONE);
            vh.btn_left.setVisibility(View.VISIBLE);
            vh.btn_left.setText("删除订单");
            vh.btn_right.setVisibility(View.GONE);
            vh.status.setVisibility(View.VISIBLE);
            if(dataList.get(position).containsKey("reason") && !TextUtils.isEmpty(dataList.get(position).get("reason")) && mType == 3){
                vh.reason.setVisibility(View.VISIBLE);
                vh.reasonText.setText("退款理由：" + dataList.get(position).get("reason"));
            }
            vh.status.setText("交易关闭");
        }
        vh.total.setText("￥" + String.valueOf(Float.valueOf(dataList.get(position).get("price")) * Integer.valueOf(dataList.get(position).get("num"))));
        return view;
    }
    private class ViewHolder{
        TextView compName;
        TextView status;
        ImageView head;
        TextView name;
        TextView price;
        TextView num;
        TextView count;
        TextView total;
        LinearLayout reason;
        TextView reasonText;
        Button btn_left;
        Button btn_right;
        Button btn_three;
    }
}
