package tm.widget.lvdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;

import tm.widget.lvdapter.viewholder.CustomCommonViewHolder;
import tm.widget.lvdapter.viewholder.ViewHolderFactory;

public abstract class CommonAdapter<T> extends BaseAdapter {
	private static final String TAG = CommonAdapter.class.getSimpleName();
	private Context mContext;
	private List<T> mDatas;
	private int mItemLayoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		mContext = context;
		if (null == datas) {
			this.mDatas = new ArrayList<T>();
		} else {
			this.mDatas = datas;
		}

		this.mItemLayoutId = layoutId;
	}

	public CommonAdapter(Context context, int layoutId) {
		this(context, null, layoutId);
	}

	/**
	 * 获取List数据集
	 * 
	 * @return
	 */
	public List<T> getSourceList() {
		return mDatas;
	}

	/**
	 * 添加数据
	 * 
	 * @param datas
	 */
	public void addData(List<T> datas) {
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

	/**
	 * 重置数据，即原有数据源数据清空，重新加入新的数据
	 * 
	 * @param datas
	 */
	public void resetDato(List<T> datas) {
		mDatas.clear();
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

	/**
	 * 删除指定位置的数据
	 * 
	 * @param position
	 */
	public void removeData(int position) {
		mDatas.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CustomCommonViewHolder holder = ViewHolderFactory.getViewHolder(
				mContext, position, convertView, parent, mItemLayoutId, getAttributeListener());
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public CustomCommonViewHolder.AttributeListener getAttributeListener(){
		return null;
	}

	public abstract void convert(CustomCommonViewHolder holder, T contactBean,
			int position);
}
