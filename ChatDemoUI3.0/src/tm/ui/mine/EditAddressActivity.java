package tm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import java.util.HashMap;
import java.util.Map;

import tm.manager.PersonManager;
import tm.utils.ViewUtil;

public class EditAddressActivity extends Activity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTitle;
    private TextView mSave;
    private EditText mName_tv;
    private EditText mPhone_tv;
    private EditText mContent_edt;
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
                        } else {
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
        mName_tv = (EditText) findViewById(R.id.edit_address_name_tv);
        mPhone_tv = (EditText) findViewById(R.id.edit_address_phone_tv);
        mContent_edt = (EditText) findViewById(R.id.edit_address_content_edt);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
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
        }
    }

    private void addEditAddress() {
        final String name = mName_tv.getText().toString().trim();
        final String phone = mPhone_tv.getText().toString().trim();
        final String content = mContent_edt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(EditAddressActivity.this, "收件人不能为空", Toast.LENGTH_SHORT).show();
        } else if (phone.length() != 11 && !isMobile(phone)) {
            Toast.makeText(EditAddressActivity.this, "请输入正确的收件人手机号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(content)) {
            Toast.makeText(EditAddressActivity.this, "收件人详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else {
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
                    map.put("name", name);
                    map.put("phone", phone);
                    map.put("content", content);
                    PersonManager.addEditAddress(map, mHandler);
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    public static boolean isMobile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String exp = "^1[34578][0-9]{9}$";
        if (str.matches(exp)) {
            return true;
        }
        return false;
    }
}
