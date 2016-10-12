package tm.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import tm.http.Config;
import tm.http.NetFactory;


public class RegistActivity extends BaseActivity implements View.OnClickListener {
    private final int REGIST_SUCESS = 1;
    private final int REGIST_EXIST = -1;
    private final int GET_SMS = 12;
    private final int GET_SMS_FAIL = 13;

    private EditText phone_edt;
    private EditText sms_edt;
    private TextView getSms_tv;
    private EditText pwd_edt;
    private ImageView back_tv;
    private TextView confirm_iv;
    private EditText pwdConfirm_edt;
    private String userName;
    private String pwd;
    private EventHandler eh;
    private int i = 59;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg.obj) {
                Log.e("info", "result = " + ((Map) msg.obj).toString());
                String result = ((Map) msg.obj).toString();
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    int resultCode = object.getInt("authId");
                    if (resultCode == REGIST_SUCESS) {
                        Toast.makeText(RegistActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                        RegistActivity.this.finish();
                    } else if (resultCode == REGIST_EXIST) {
                        Toast.makeText(RegistActivity.this, "该手机号已经被注册", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistActivity.this, "系统错误，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                switch (msg.what) {
                    case GET_SMS:
                        Toast.makeText(RegistActivity.this, "验证码已发送，请注意查看信息", Toast.LENGTH_SHORT).show();
                        getSms_tv.setTextColor(getResources().getColor(R.color.getsms_wait_color));
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                                getSms_tv.setClickable(true);
                            }
                        }, 60000);
                        break;
                    case GET_SMS_FAIL:
                        Toast.makeText(RegistActivity.this, "当日获取验证码次数已用完", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);
    }

    private void init() {
        SMSSDK.initSDK(this, "15ad624441feb", "5c0ead0ca6ccbd06cb1997398e14bba5");
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        // 设置HTTP POST请求参数必须用NameValuePair对象
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("userName", userName));
                        params.add(new BasicNameValuePair("userPassword", pwd));
                        NetFactory.instance().commonHttpCilent(handler, RegistActivity.this,
                                Config.URL_REDGIST, params);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        handler.sendEmptyMessage(GET_SMS);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    handler.sendEmptyMessage(GET_SMS_FAIL);
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        back_tv = (ImageView) findViewById(R.id.regist_back);
        confirm_iv = (TextView) findViewById(R.id.regist_confirm);
        phone_edt = (EditText) findViewById(R.id.regist_phone_edt);
        sms_edt = (EditText) findViewById(R.id.regist_sms_edt);
        pwd_edt = (EditText) findViewById(R.id.regist_pwd_edt);
        getSms_tv = (TextView) findViewById(R.id.regist_getsms_tv);
        pwdConfirm_edt = (EditText) findViewById(R.id.regist_pwd_confirm_edt);
        getSms_tv.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        confirm_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_back:
                this.finish();
                break;
            case R.id.regist_confirm:
                userName = phone_edt.getText().toString().trim();
                String ver = sms_edt.getText().toString().trim();
                pwd = pwd_edt.getText().toString().trim();
                String pwdConfirm = pwdConfirm_edt.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ver)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwdConfirm)) {
                    Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(this, "两次密码数据不一致，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (userName.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("+86", userName, ver);
                }
                break;
            case R.id.regist_getsms_tv:
                userName = phone_edt.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (userName.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.getVerificationCode("+86", userName);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        SMSSDK.unregisterEventHandler(eh);
        super.onStop();
    }
}
