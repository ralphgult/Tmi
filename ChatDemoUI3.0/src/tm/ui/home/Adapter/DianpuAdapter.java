package tm.ui.home.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;

import tm.entity.DianpuBean;
import tm.utils.VolleyLoadPicture;
import tm.widget.lvdapter.CommonAdapter;
import tm.widget.lvdapter.viewholder.CustomCommonViewHolder;

/**
 * Created by meixi on 2016/12/28.
 */

public class DianpuAdapter extends CommonAdapter<DianpuBean> {

    private Context mContext;

    public DianpuAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mContext=context;

    }
    @Override
    public void convert(CustomCommonViewHolder holder, DianpuBean contactBean, int position) {
        //初始化显示控件
        //商品图片
        ImageView pic = (ImageView) holder.getItemView(R.id.tm_img_pic1);
        //商品简介
        TextView jianjie = (TextView) holder.getItemView(R.id.tm_zhuye_jianjie);
        //商品现价
        TextView xianjia = (TextView) holder.getItemView(R.id.tm_zhuye_xianjia);
        //商品原价
        TextView yuanjia = (TextView) holder.getItemView(R.id.tm_zhuye_yuanjia);
        if(!TextUtils.isEmpty(contactBean.mCommodityyuanjia)){
            yuanjia.setText("￥"+contactBean.mCommodityyuanjia);
            yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(!TextUtils.isEmpty(contactBean.mCommodityPath)){
            //用Volley加载图片
            VolleyLoadPicture vlp = new VolleyLoadPicture(mContext, pic);
            vlp.getmImageLoader().get(contactBean.mCommodityPath, vlp.getOne_listener());
        }
        if(!TextUtils.isEmpty(contactBean.mCommodityjianjie)){
            jianjie.setText(contactBean.mCommodityname);
        }
        if(!TextUtils.isEmpty(contactBean.mCommodityxianjia)){
            xianjia.setText("￥"+contactBean.mCommodityxianjia);
        }


    }
}