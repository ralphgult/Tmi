package tm.utils.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xbh.tmi.R;


/**
 * Created by Administrator on 2016/3/14.
 */
public class ConfirmDialog extends PartentDialog {

    private TextView mTipText;    //提示文字
    private TextView mConfirmTextTv;   //确定按钮
    private Context mContext ;
    private ConfirmDialogListener mListener;

    public ConfirmDialog(Context context, int layoutId) {
        super(context, layoutId);
        this.mContext = context;
        initView();
    }


    private void initView(){
        mTipText = (TextView)getRootView().findViewById(R.id.yx_aty_confirm_dialog_tip_text);
        mConfirmTextTv = (TextView)getRootView().findViewById(R.id.yx_aty_confirm_dialog_confirm_tv);
        mConfirmTextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener){
                    mListener.confirmDialogSubmit();
                }
            }
        });
    }

    /**
     * 设置提示文字
     * @param content
     */
    public void setmTipText(String content){
        mTipText.setText(content);
    }

    public void setConfirmDialogListener(ConfirmDialogListener listener){
        mListener = listener;
    }

    public interface ConfirmDialogListener{
        void confirmDialogSubmit();
    }

}
