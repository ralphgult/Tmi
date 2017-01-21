package tm.ui.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oohla.android.utils.StringUtil;
import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.MySoppingActivity;
import tm.ui.mine.ShoppingPayActivity;
import tm.utils.ImageLoaders;
import tm.widget.zxing.view.ViewfinderView;

/**
 * Created by RG on 2017/1/4.
 */

public class MyShoppingAdapter extends BaseAdapter {
    private List<Map<String, String>> mDatas;
    private Context mContext;
    private ViewHolder vh;
    private ImageLoaders imageLoaders;
    private List<String> mSeletedIds;
    private boolean mIsShopping;

    public MyShoppingAdapter(Context context, boolean isShopping) {
        mContext = context;
        imageLoaders = new ImageLoaders(mContext, new imageLoaderListener());
        mSeletedIds = new ArrayList<>();
        mIsShopping = isShopping;
    }

    /**
     * map.put("goodId", object.getString("goodId"));
     * map.put("goodName",object.getString("goodName"));
     * map.put("scId",object.getString("scId"));
     * map.put("currentPrice",object.getString("currentPrice"));
     * map.put("cartCount",object.getString("cartCount"));
     * map.put("goodImg",object.getString("goodImg"));
     * map.put("createDate",object.getString("createDate"));
     *
     * @param datas
     */
    public void resetData(List<Map<String, String>> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.shpping_item, null);
            vh.mCheck = (ImageView) view.findViewById(R.id.item_shopping_check_iv);
            vh.mImage = (ImageView) view.findViewById(R.id.item_shopping_img_iv);
            vh.mName = (TextView) view.findViewById(R.id.item_shopping_goodsname_tv);
            vh.mPrice = (TextView) view.findViewById(R.id.item_shopping_price_tv);
            vh.mCount = (TextView) view.findViewById(R.id.item_shopping_count_tv);
            vh.mCountLayout = (LinearLayout) view.findViewById(R.id.shopping_pay_count_ly);
            vh.mCountAdd = (TextView) view.findViewById(R.id.shopping_pay_add_tv);
            vh.mCountTackof = (TextView) view.findViewById(R.id.shopping_pay_takeof_tv);
            vh.mFinalCount = (TextView) view.findViewById(R.id.shopping_pay_finalcount_tv);
            vh.mDelete = (TextView) view.findViewById(R.id.item_shopping_del_tv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        if (mSeletedIds.contains(mDatas.get(position).get("scId"))) {
            vh.mCheck.setSelected(true);
        } else {
            vh.mCheck.setSelected(false);
        }
        vh.mName.setText(mDatas.get(position).get("goodName"));
        vh.mPrice.setText(mDatas.get(position).get("currentPrice"));
        vh.mCount.setText("x" + mDatas.get(position).get("cartCount"));
        imageLoaders.loadImage(vh.mImage, mDatas.get(position).get("goodImg"));
        if (mIsShopping) {
            vh.mCheck.setVisibility(View.GONE);
            vh.mCountLayout.setVisibility(View.VISIBLE);
            vh.mCount.setVisibility(View.GONE);
            vh.mFinalCount.setText(mDatas.get(position).get("cartCount"));
            vh.mCountAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int finalcount = Integer.valueOf(mDatas.get(position).get("cartCount")) + 1;
                    mDatas.get(position).put("cartCount", String.valueOf(finalcount));
                    ((ShoppingPayActivity) mContext).setTotalPrice();
                    notifyDataSetChanged();
                }
            });
            vh.mCountTackof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int finalcount = Integer.valueOf(mDatas.get(position).get("cartCount")) - 1;
                    mDatas.get(position).put("cartCount", String.valueOf(finalcount < 0 ? 0 : finalcount));
                    ((ShoppingPayActivity) mContext).setTotalPrice();
                    notifyDataSetChanged();
                }
            });
        } else {
            vh.mCheck.setVisibility(View.VISIBLE);
            vh.mCountLayout.setVisibility(View.GONE);
            vh.mCount.setVisibility(View.VISIBLE);
            vh.mDelete.setVisibility(View.VISIBLE);
            vh.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread() {
                        @Override
                        public void run() {
                            PersonManager.delShoppingList(mDatas.get(position), ((MySoppingActivity) mContext).mHandler);
                        }
                    }.start();
                }
            });
            vh.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mDatas.get(position).get("scId");
                    if (mSeletedIds.contains(id)) {
                        mSeletedIds.remove(id);
                    } else {
                        mSeletedIds.add(id);
                    }
                    notifyDataSetChanged();
                    ((MySoppingActivity) mContext).setTotalPrice();
                }
            });
        }
        return view;
    }

    class ViewHolder {
        private ImageView mCheck;
        private ImageView mImage;
        private TextView mName;
        private TextView mPrice;
        private TextView mCount;
        private LinearLayout mCountLayout;
        private TextView mCountAdd;
        private TextView mCountTackof;
        private TextView mFinalCount;
        private TextView mDelete;
    }

    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    public List<String> getmSeletedIds() {
        return mSeletedIds;
    }

    public void setmSeletedIds(List<String> seletedIds){
        mSeletedIds.clear();
        mSeletedIds.addAll(seletedIds);
        notifyDataSetChanged();
    }

    public void removeSelectedIds(){
        mSeletedIds.clear();
        notifyDataSetChanged();
    }

    public List<Map<String,String>> getSourceData(){
        return mDatas;
    }
}
