package tm.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xbh.tmi.R;

/**
 * Created by Administrator on 15-11-3.
 */
public abstract class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(final Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(getLayoutId(), null);
        if(null == conentView){
            throw new RuntimeException("The dialogLayoutId is invalid!");
        }
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //this.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        if(getAnimatioStyleId() == -1){
            this.setAnimationStyle(R.style.SingleWheelViewPopuWindowAnimationPreview);
        }else{
            this.setAnimationStyle(getAnimatioStyleId());
        }

        initWidgetByRootView(conentView);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, -this.getWidth() + 50, 0);  //parent.getLayoutParams().width / 2, 18
        } else {
            this.dismiss();
        }
    }

    /**
     * 在自定义位置显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, x, y);
        } else {
            this.dismiss();
        }
    }

    /**
     * 设置popupWindow的宽度
     * @return
     */
    public void setPopWindowWidth(int width){
        this.setWidth(width);
    }

    /**
     * 设置popupWindow高度
     * @return
     */
    public void setPopWindowHeight(int height){
        this.setHeight(height);
    }

    /**
     * 设置popupWindow动画
     * @return
     */
    public int getAnimatioStyleId(){
        return -1;
    }

    /**
     * 获取popupWindow自定义布局的layoutID
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化自定义布局内的控件
     * @param conentView
     */
    public abstract void initWidgetByRootView(View conentView);
}
