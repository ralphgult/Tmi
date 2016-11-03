package tm.utils.dialog;

import android.content.Context;

import com.xbh.tmi.R;


public class DialogFactory {
    public static final int DIALOG_TYPE_INPUT = 1;
    public static final int DIALOG_TYPE_CONFIRM = 2;
    public static final int DIALOG_TYPE_REMIND = 3;

    public static PartentDialog createDialog(Context context, int type) {
        PartentDialog dialog = null;
        switch (type) {
            case DIALOG_TYPE_INPUT:
                dialog = new InputDialog(context,
                        R.layout.yx_common_input_dialog_layout);
                break;
            case DIALOG_TYPE_CONFIRM:
                dialog = new ConfirmDialog(context,R.layout.youxue_aty_confirm_dialog_layout);
                break;
            case DIALOG_TYPE_REMIND:
                dialog = new RemindDialog(context,R.layout.yx_common_show_remind_dialog_layout);
                break;
        }
        return dialog;
    }
}
