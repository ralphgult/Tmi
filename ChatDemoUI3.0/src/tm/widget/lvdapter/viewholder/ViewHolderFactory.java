package tm.widget.lvdapter.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 15-6-19.
 */
public class ViewHolderFactory {

     public static CustomCommonViewHolder getViewHolder(Context context, int position, View convertView, ViewGroup parent, int layoutId){
         return getViewHolder(context, position, convertView, parent, layoutId, null);
    }

    public static CustomCommonViewHolder getViewHolder(Context context, int position, View convertView,
                                                       ViewGroup parent, int layoutId, CustomCommonViewHolder.AttributeListener listener){
        CustomCommonViewHolder holder = null;
        if(null == convertView){
            holder = new CustomCommonViewHolder(context, position, convertView, parent, layoutId, listener);
        }else{
            holder = (CustomCommonViewHolder) convertView.getTag();
            holder.setCurrentPos(position);
        }
        return holder;
    }


}
