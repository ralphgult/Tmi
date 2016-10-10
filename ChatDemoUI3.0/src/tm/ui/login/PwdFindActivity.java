package tm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;
import com.hyphenate.tmdemo.ui.RegisterActivity;

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
import tm.system.App;


public class PwdFindActivity extends BaseActivity implements View.OnClickListener {
    private final int FINDPWD_SUCESS = 0;
    private final int GET_SMS = 12;
    private int Get_SMS_FAIL = 13;

    private EditText phone_edt;
    private EditText sms_edt;
    private TextView getSms_tv;
    private EditText pwd_edt;
    private ImageView back_tv;
    private TextView confirm_iv;
    private EditText pwdConfirm_edt;
    private String phone;
    private int timeCount;
    private EventHandler eh;
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
                    if (resultCode == FINDPWD_SUCESS) {
                        Toast.makeText(PwdFindActivity.this, "修改密码成功，请重新登录", Toast.LENGTH_SHORT).show();
                        PwdFindActivity.this.startActivity(new Intent(PwdFindActivity.this, RegisterActivity.class));
                        App.getInstance().exit();
                    } else {
                        Toast.makeText(PwdFindActivity.this, "系统错误，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == GET_SMS) {
                Toast.makeText(PwdFindActivity.this, "验证码已发送，请注意查看信息", Toast.LENGTH_SHORT).show();
                getSms_tv.setTextColor(getResources().getColor(R.color.getsms_wait_color));
                getSms_tv.setClickable(false);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                        getSms_tv.setClickable(true);
                    }
                }, 60000);
            } else if (msg.what == Get_SMS_FAIL) {
                Toast.makeText(PwdFindActivity.this, "{当前手机号发送短信的数量超过限额", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_find);
        init();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);
    }

    private void init() {
        App.getInstance().addActivity(this);
        timeCount = 60;
        SMSSDK.initSDK(this, "15ad624441feb", "5c0ead0ca6ccbd06cb1997398e14bba5");
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("userName", phone_edt.getText().toString().trim()));
                        params.add(new BasicNameValuePair("userPassword", pwd_edt.getText().toString().trim()));
                        NetFactory.instance().commonHttpCilent(handler, PwdFindActivity.this,
                                Config.URL_REDGIST, params);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        handler.sendEmptyMessage(GET_SMS);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        back_tv = (ImageView) findViewById(R.id.findpwd_back);
        confirm_iv = (TextView) findViewById(R.id.findpwd_confirm);
        phone_edt = (EditText) findViewById(R.id.findpwd_phone_edt);
        sms_edt = (EditText) findViewById(R.id.findpwd_sms_edt);
        pwd_edt = (EditText) findViewById(R.id.findpwd_pwd_edt);
        getSms_tv = (TextView) findViewById(R.id.findpwd_getsms_tv);
        pwdConfirm_edt = (EditText) findViewById(R.id.findpwd_pwd_confirm_edt);
        getSms_tv.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        confirm_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findpwd_back:
                this.finish();
                break;
            case R.id.findpwd_confirm:
                phone = phone_edt.getText().toString().trim();
                String ver = sms_edt.getText().toString().trim();
                String pwd = pwd_edt.getText().toString().trim();
                String pwdConfirm = pwdConfirm_edt.getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (ver.equals("")) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                } else if (pwdConfirm.equals("")) {
                    Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(this, "两次密码数据不一致，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("+86", phone, ver);
                }
                break;
            case R.id.findpwd_getsms_tv:
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("info","phone number ======== " + phone);
                    SMSSDK.getVerificationCode("+86", phone);
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
