package tm.ui.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.MySoppingActivity;
import tm.ui.mine.ShoppingPayActivity;
import tm.utils.ImageLoaders;

/**
 * Created by RG on 2017/1/4.
 */

public class MyShoppingAdapter extends BaseAdapter {
    private List<Map<String, String>> mDatas;
    private Context mContext;
    private ViewHolder vh;
    private ImageLoaders imageLoaders;
    private List<String> mSeletedIds;
    private boolean mIsEdit;
    private boolean mIsShopping;

    public MyShoppingAdapter(Context context, boolean isShopping) {
        mContext = context;
        imageLoaders = new ImageLoaders(mContext, new imageLoaderListener());
        mSeletedIds = new ArrayList<>();
        mIsEdit = false;
        mIsShopping = isShopping;
    }

    /**
     * map.put("goodId", object.getString("goodId"));
     * map.put("goodName",object.getString("goodName"));
     * map.put("scId",object.getString("scId"));
     * map.put("currentPrice",object.getString("currentPrice"));
     * map.put("num",object.getString("num"));
     * map.put("goodImg",object.getString("goodImg"));
     * map.put("createDate",object.getString("createDate"));
     *
     * @param datas
     */
    public void resetData(List<Map<String, String>> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setIsEdit(boolean isEdit) {
        mIsEdit = isEdit;
        resetData(mDatas);
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
            vh.mCountLayout = (LinearLayout) view.findViewById(R.id.item_shopping_count_ly);
            vh.mCountEdit = (EditText) view.findViewById(R.id.item_shopping_count_edt);
            vh.mDelete = (TextView) view.findViewById(R.id.item_shopping_del_tv);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        vh.mName.setText(mDatas.get(position).get("goodName"));
        vh.mPrice.setText(mDatas.get(position).get("currentPrice"));
        vh.mCount.setText("x" + mDatas.get(position).get("num"));
        imageLoaders.loadImage(vh.mImage, mDatas.get(position).get("goodImg"));
        if (mIsShopping) {
            vh.mCheck.setVisibility(View.GONE);
            vh.mCountLayout.setVisibility(View.VISIBLE);
            vh.mCount.setVisibility(View.GONE);
            vh.mCountEdit.setHint(mDatas.get(position).get("num"));
            vh.mCountEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        ((ShoppingPayActivity) mContext).setTotalPrice(position,vh.mCountEdit.getText().toString().trim());
                    }
                }
            });
        } else {
            vh.mCheck.setVisibility(View.VISIBLE);
            vh.mCountLayout.setVisibility(View.GONE);
            vh.mCount.setVisibility(View.VISIBLE);
            if (mIsEdit) {
                vh.mDelete.setVisibility(View.VISIBLE);
                vh.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 删除购物车
                        new Thread() {
                            @Override
                            public void run() {
                                PersonManager.delShoppingList(mDatas.get(position), ((MySoppingActivity) mContext).mHandler);
                            }
                        }.start();
                    }
                });
            } else {
                vh.mDelete.setVisibility(View.GONE);
            }
            vh.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vh.mCheck.isSelected()) {
                        mSeletedIds.remove(mDatas.get(position).get("scId"));
                        vh.mCheck.setSelected(false);
                    } else {
                        mSeletedIds.add(mDatas.get(position).get("scId"));
                        vh.mCheck.setSelected(true);
                    }
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
        private EditText mCountEdit;
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
}
