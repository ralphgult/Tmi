package tm.ui.main.adapter;

/**
 * Created by Administrator on 2015/12/7.
 */


import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xbh.tmi.R;
import tm.entity.ResourcesBean;
import tm.utils.VolleyLoadPicture;
import tm.widget.lvdapter.CommonAdapter;
import tm.widget.lvdapter.viewholder.CustomCommonViewHolder;


/**
 * Created by Administrator on 15-8-5.
 */
public class BdtsAdapter extends CommonAdapter<ResourcesBean> {

    private Context mContext;

    public BdtsAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mContext=context;

    }
    @Override
    public void convert(CustomCommonViewHolder holder, ResourcesBean contactBean, int position) {
        //初始化显示控件
        ImageView resourcespic = (ImageView) holder.getItemView(R.id.tm_image_iv);
        //填充数据
       if(null!=contactBean&&!TextUtils.isEmpty(contactBean.mImagePath)){
          //用Volley加载图片
           VolleyLoadPicture vlp = new VolleyLoadPicture(mContext, resourcespic);
           vlp.getmImageLoader().get(contactBean.mImagePath, vlp.getOne_listener());
        }

    }
}
