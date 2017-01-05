package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import java.util.HashMap;
import java.util.Map;

import tm.manager.PersonManager;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

public class EditAddressActivity extends Activity implements View.OnClickListener, InputDialog.InputDialogListener {
    private ImageView mBack;
    private TextView mTitle;
    private TextView mSave;
    private LinearLayout mName_ly;
    private TextView mName_tv;
    private LinearLayout mPhone_ly;
    private TextView mPhone_tv;
    private EditText mContent_edt;
    private InputDialog mDialog;
    private boolean mIsName;
    private boolean mIsAdd;
    private String mAddrId;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initViews();
        initData();
    }

    private void initData() {
        mIsAdd = getIntent().getExtras().getBoolean("isAdd");
        if (!mIsAdd) {
            mTitle.setText("修改地址");
            mAddrId = getIntent().getExtras().getString("id");
            mName_tv.setText(getIntent().getExtras().getString("name"));
            mPhone_tv.setText(getIntent().getExtras().getString("phone"));
            mContent_edt.setText(getIntent().getExtras().getString("content"));
        } else {
            mTitle.setText("添加新地址");
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1001:
                        String text = null;
                        if (mIsAdd) {
                            text = "添加地址成功";
                        }else {
                            text = "修改地址成功";
                        }
                        Toast.makeText(EditAddressActivity.this, text, Toast.LENGTH_SHORT).show();
                        ViewUtil.backToOtherActivity(EditAddressActivity.this);
                        break;
                    default:
                        Toast.makeText(EditAddressActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initViews() {
        mBack = (ImageView) findViewById(R.id.edit_address_back_iv);
        mTitle = (TextView) findViewById(R.id.edit_address_title_tv);
        mSave = (TextView) findViewById(R.id.edit_address_add_tv);
        mName_ly = (LinearLayout) findViewById(R.id.edit_address_name_ly);
        mName_tv = (TextView) findViewById(R.id.edit_address_name_tv);
        mPhone_ly = (LinearLayout) findViewById(R.id.edit_address_phone_ly);
        mPhone_tv = (TextView) findViewById(R.id.edit_address_phone_tv);
        mContent_edt = (EditText) findViewById(R.id.edit_address_content_edt);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mName_ly.setOnClickListener(this);
        mPhone_ly.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_address_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.edit_address_add_tv:
                addEditAddress();
                break;
            case R.id.edit_address_name_ly:
                mIsName = true;
                createInputDialog();
                break;
            case R.id.edit_address_phone_ly:
                mIsName = false;
                createInputDialog();
                break;
        }
    }

    private void addEditAddress() {
        new Thread() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                if (mIsAdd) {
                    map.put("default", "0");
                } else {
                    map.put("id", mAddrId);
                    map.put("default", getIntent().getExtras().getString("default"));
                }
                map.put("name", mName_tv.getText().toString().trim());
                map.put("phone", mPhone_tv.getText().toString().trim());
                map.put("content", mContent_edt.getText().toString().trim());
                PersonManager.addEditAddress(map, mHandler);
            }
        }.start();
    }

    private void createInputDialog() {
        if (mDialog == null) {
            mDialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
        }
        if (!mIsName) {
            mDialog.getInputView().setInputType(InputType.TYPE_CLASS_PHONE);
        }else {
            mDialog.getInputView().setInputType(InputType.TYPE_CLASS_TEXT);
        }
        mDialog.setHintTextForView(mIsName ? "请输入收货人姓名" : "请输入电话号码");
        mDialog.setInputDialogListener(this);
        mDialog.showDialog();
    }

    @Override
    public void inputDialogCancle() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.closeDialog();
        }
    }

    @Override
    public void inputDialogSubmit(String inputText) {
        TextView tv = mIsName ? mName_tv : mPhone_tv;
        tv.setText(inputText);
        mDialog.closeDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
