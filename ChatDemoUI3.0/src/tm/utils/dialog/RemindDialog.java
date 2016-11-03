package tm.utils.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.xbh.tmi.R;

import tm.manager.DpSpPxSwitch;


/**
 * Created by Administrator on 2016/3/1.
 */
public class RemindDialog extends PartentDialog {

    private TextView mGroupName;
    private TextView mConfirmTv;
    private TextView mConfirmTv2;
    private RemindDialogListener mListener;
    private Context mContext;

    public RemindDialog(Context context, int layoutId) {
        super(context, layoutId);
        mContext = context;
        initViews();
    }

    private void initViews() {
        mGroupName = (TextView) getRootView().findViewById(
                R.id.yx_common_remid_dialog_tv);
        mConfirmTv = (TextView)getRootView().findViewById(
                R.id.commom_remid_dialog_submit_tv);
        mConfirmTv2 = (TextView)getRootView().findViewById(
                R.id.commom_remid_dialog_submit2_tv);

        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.invita();
            }
        });
        mConfirmTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.remind();
            }
        });
    }

    public void setTextForConfirmTv(String text){
        mConfirmTv.setText(text);
    }

    public void setGroupName(String groupName){
        mGroupName.setText(groupName);
    }

    public void setPhotoDialogListener(RemindDialogListener listener){
        mListener = listener;
    }

    public interface RemindDialogListener{
        void invita();
        void remind();
    }

    @Override
    public int getPopWindowWidth() {
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        return DpSpPxSwitch.px2dp(mContext, (int) (d.widthPixels * 0.9));
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }
}