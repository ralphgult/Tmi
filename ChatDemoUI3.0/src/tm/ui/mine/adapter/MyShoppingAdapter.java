package tm.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by RG on 2017/1/4.
 */

public class MyShoppingAdapter extends BaseAdapter {
    private List<Map<String, String>> mDatas;
    private Context mContext;

    public MyShoppingAdapter(Context context){
        mContext  = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
