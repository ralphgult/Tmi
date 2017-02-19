package tm.ui.tmi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.ui.tmi.GoodsChangeActivity;
import tm.ui.tmi.GoodsManagerAcitivity;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;

/**
 * Created by RalphGult on 2016/10/22.
 */

public class GoodsAdapter extends BaseAdapter {
    private List<Map<String, String>> dataList;
    private int mType;
    private Context mContext;
    private ViewHolder vh;
    private boolean mIsChoice;
    private List<String> choiceList;
    private ImageLoaders loaders;

    public GoodsAdapter(Context context, int type){
        mContext = context;
        mIsChoice = false;
        choiceList = new ArrayList<>();
        dataList = new ArrayList<>();
        mType = type;
        loaders = new ImageLoaders(mContext,new loaderLisener());
    }

    class loaderLisener implements ImageLoaders.ImageLoaderListener{
        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }

    public void setIsChoice(boolean isChoice){
        mIsChoice = isChoice;
        notifyDataSetChanged();
    }
    public List<String> getChoiceList(){
        return choiceList;
    }
    public List<Map<String, String>> getDataList(){
        return dataList;
    }
    public void resetData(List<Map<String, String>> datas){
        dataList.clear();
        dataList.addAll(datas);
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
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = View.inflate(mContext, R.layout.goods_item_layout,null);
            vh = new ViewHolder();
            vh.head = (ImageView) view.findViewById(R.id.goods_item_head);
            vh.name = (TextView) view.findViewById(R.id.goods_item_name);
            vh.price = (TextView) view.findViewById(R.id.goods_item_price);
            vh.num = (TextView) view.findViewById(R.id.goods_item_num);
            vh.time = (TextView) view.findViewById(R.id.goods_item_time);
            vh.see = (TextView) view.findViewById(R.id.goods_item_see);
            vh.share = (TextView) view.findViewById(R.id.goods_item_share);
            vh.choice = (CheckBox) view.findViewById(R.id.goods_item_choice);
            view.setTag(vh);
        }else{
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        loaders.loadImage(vh.head, dataList.get(position).get("imgs").split(",")[0]);
        vh.name.setText(dataList.get(position).get("goodName"));
        vh.price.setText(dataList.get(position).get("price"));
        vh.num.setText("销量：" + dataList.get(position).get("sales") + "  库存：" + dataList.get(position).get("count"));
        vh.price.setText("￥" + dataList.get(position).get("currentPrice"));
        vh.time.setText("添加时间：" + dataList.get(position).get("createDate"));

        if(mIsChoice){
            vh.choice.setVisibility(View.VISIBLE);
        }else{
            vh.choice.setVisibility(View.GONE);
        }
        vh.choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    choiceList.remove(dataList.get(position).get("goodsId"));
                } else {
                    choiceList.add(dataList.get(position).get("goodsId"));
                }
            }
        });
        vh.see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", dataList.get(position).get("goodsId"));
                bundle.putString("goodName", dataList.get(position).get("goodName"));
                bundle.putString("imgs", dataList.get(position).get("imgs"));
                bundle.putString("imgIds", dataList.get(position).get("imgIds"));
                bundle.putString("createDate", dataList.get(position).get("createDate"));
                bundle.putString("currentPrice", dataList.get(position).get("currentPrice"));
                bundle.putString("originalPrice",  dataList.get(position).get("originalPrice"));
                bundle.putString("sales", dataList.get(position).get("sales"));
                bundle.putString("count", dataList.get(position).get("count"));
                bundle.putString("goodProfiles",dataList.get(position).get("goodProfiles"));
                bundle.putInt("type", mType);
                bundle.putBoolean("isUpdate",true);
                ViewUtil.jumpToOherActivityForResult((GoodsManagerAcitivity)mContext, GoodsChangeActivity.class,bundle,1);
            }
        });
        return view;
    }

    private class ViewHolder{
        ImageView head;
        TextView name;
        TextView price;
        TextView num;
        TextView time;
        TextView see;
        TextView share;
        CheckBox choice;
    }
}
