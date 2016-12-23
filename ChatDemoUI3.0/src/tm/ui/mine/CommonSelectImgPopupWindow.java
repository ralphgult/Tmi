package tm.ui.mine;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xbh.tmi.R;

import tm.widget.BasePopupWindow;


/**
 * Created by RalphGult on 2015/12/15.
 */
public class CommonSelectImgPopupWindow extends BasePopupWindow implements View.OnClickListener{
    private TextView chooseImgCamera = null;
    private TextView chooseImgLocal = null;
    private TextView chooseImgCancle = null;
    private Context mContext = null;
    public View.OnClickListener mOnclickListener;
    public CommonSelectImgPopupWindow(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.yx_common_add_img_pupwindow_layout;
    }

    @Override
    public void initWidgetByRootView(View conentView) {
        chooseImgCamera = (TextView)conentView.findViewById(R.id.yx_common_add_img_pupwindow_camera_tv);
        chooseImgLocal = (TextView)conentView.findViewById(R.id.yx_common_add_img_pupwindow_local_tv);
        chooseImgCancle = (TextView)conentView.findViewById(R.id.yx_common_add_img_pupwindow_cancle_tv);
        chooseImgCamera.setOnClickListener(this);
        chooseImgLocal.setOnClickListener(this);
        chooseImgCancle.setOnClickListener(this);

    }
    /**
     * 从底部显示popupwindow
     * @param v
     */
    public void showAtBOTTOM(View v){
        if(!this.isShowing()){
            showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            CommonSelectImgPopupWindow.this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        mOnclickListener.onClick(v);
        dismiss();
    }
}
