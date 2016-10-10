package tm.utils.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import tm.manager.DpSpPxSwitch;


public class PartentDialog {
    private View mView;
    private AlertDialog mDialog;
    private String mActionFlag;
    private int mWidth = 300;
    private boolean isFrist = true;
    private Context mContext;

    public PartentDialog(Context context, int layoutId) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(layoutId, null);
        mDialog = new Builder(context).create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setView(new EditText(context));
    }

    private void changedWindowView() {
        mDialog.show();
        Window w = mDialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = DpSpPxSwitch.dp2px(mContext, getPopWindowWidth() == 0 ? mWidth : getPopWindowWidth());
        w.setGravity(Gravity.CENTER);
        w.setContentView(mView);
    }

    public View getRootView() {
        return mView;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 重写可修改弹出窗口的width
     * 传入的参数应该以dp
     *
     * @return
     */
    public int getPopWindowWidth() {
        return 0;
    }

    public void showDialog() {
        if (isFrist) {
            changedWindowView();
            isFrist = false;
        } else {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
        }
    }

    public void closeDialog() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    public TextView getTitleTv() {
        return null;
    }

    public View getHintView() {
        return null;
    }

    /**
     * 设置对话框动作性质
     *
     * @param flag
     */
    public void setActionFlag(String flag) {
        mActionFlag = flag;
    }

    /**
     * 获取对话框动作性质
     *
     * @return
     */
    public String getActionFlag() {
        return mActionFlag;
    }

    /**
     * 设置弹窗标题
     *
     * @param title
     */
    public void setTitleStr(String title) {
        if (null != getTitleTv()) {
            getTitleTv().setText(title);
        }
    }

    /**
     * 设置控件hint文字
     *
     * @param text
     */
    public void setHintTextForView(String text) {
        if (null != getHintView()) {
            if (getHintView() instanceof EditText) {
                ((EditText) getHintView()).setHint(text);
            } else if (getHintView() instanceof TextView) {
                ((TextView) getHintView()).setHint(text);
            }
        }
    }

    /**
     * 获取dialog是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return mDialog.isShowing();
    }
}
