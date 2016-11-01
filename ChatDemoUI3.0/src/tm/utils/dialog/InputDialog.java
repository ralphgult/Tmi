package tm.utils.dialog;


import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xbh.tmi.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tm.manager.DpSpPxSwitch;

public class InputDialog extends PartentDialog {

    private EditText mInputEt;
    private TextView mCancle;
    private TextView mSubmit;
    private InputDialogListener mListener;
    private Context mContext;
    private InputMethodManager mInputMetHodManager;

    public InputDialog(Context context, int layoutId) {
        super(context, layoutId);
        mContext = context;
        mInputMetHodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initViews();
    }

    private void initViews() {
        mInputEt = (EditText) getRootView().findViewById(
                R.id.commom_input_dialog_input_tv);
        mCancle = (TextView) getRootView().findViewById(
                R.id.commom_input_dialog_cancle_tv);
        mSubmit = (TextView) getRootView().findViewById(
                R.id.commom_input_dialog_submit_tv);
        InputFilter emojiFilter = new InputFilter() {
            Pattern emoji = Pattern.compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
        InputFilter[] emojiFilters = {emojiFilter};
        mInputEt.setFilters(emojiFilters);
        mCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.inputDialogCancle();
                    mInputEt.setText("");
                }
            }
        });
        mSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.inputDialogSubmit(mInputEt.getText().toString().trim());
                    mInputEt.setText("");
                }
            }
        });

    }


    public void setEditTextHint(String hint) {
        mInputEt.setHint(hint);
    }

    public void setEditMaxlength(int length) {
        mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public EditText getInputView() {
        return mInputEt;
    }


    @Override
    public View getHintView() {
        return mInputEt;
    }

    public void setInputDialogListener(InputDialogListener listener) {
        mListener = listener;
    }

    public interface InputDialogListener {
        void inputDialogCancle();

        void inputDialogSubmit(String inputText);
    }

    @Override
    public int getPopWindowWidth() {
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        return DpSpPxSwitch.px2dp(mContext, (int) (d.widthPixels * 0.9));
    }

}
