package tm.widget.lvdapter.viewholder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder类
 * Created by YJZ on 15-4-24.
 */
public class CustomCommonViewHolder {
    private View mConvertView;
    private int mPos;
    private SparseArray<View> mItemViewList;

    public CustomCommonViewHolder(Context context, int position, View convertView, ViewGroup parent, int layoutId) {
        this(context, position, convertView,  parent, layoutId, null);
    }

    public CustomCommonViewHolder(Context context, int position, View convertView, ViewGroup parent, int layoutId, AttributeListener listener) {
        mItemViewList = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, null, false);
        setCurrentPos(position);
        mConvertView.setTag(this);
        if(listener != null){
            listener.setAttributeForViewItem(mConvertView);
        }
    }

    public TextView getTextView(int viewId) {
        return getItemView(viewId);
    }

    public Button getButton(int viewId) {
        return getItemView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return getItemView(viewId);
    }

    public View getView(int viewId) {
        return getItemView(viewId);
    }

    public <T extends View> T getItemView(int itemViewId) {
        View itemView = mItemViewList.get(itemViewId);
        if (null == itemView) {
            itemView = mConvertView.findViewById(itemViewId);
            mItemViewList.put(itemViewId, itemView);
        }
        return (T) itemView;
    }

    public void setConvertView(View view) {
        mConvertView = view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public void setCurrentPos(int position) {
        mPos = position;
    }

    public int getCurrentPos() {
        return mPos;
    }

    public interface AttributeListener{
        void setAttributeForViewItem(View rootView);
    }
}
