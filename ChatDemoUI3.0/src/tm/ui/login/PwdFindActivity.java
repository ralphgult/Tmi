package tm.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;
import com.xbh.tmi.ui.LoginActivity;

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
import tm.utils.ViewUtil;

public class PwdFindActivity extends BaseActivity implements View.OnClickListener{
    private final int FINDPWD_SUCESS = 1;
    private final int GET_SMS = 12;
    private int Get_SMS_FAIL = 13;

    private EditText phone_edt;
    private EditText sms_edt;
    private TextView getSms_tv;
    private EditText pwd_edt;
    private ImageView back_tv;
    private TextView confirm_iv;
    private TextView mAlter;
    private EditText pwdConfirm_edt;
    private String phone;
    private int timeCount;
    private EventHandler eh;
    private boolean isCheck;

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
                        ViewUtil.backToOtherActivity(PwdFindActivity.this, LoginActivity.class);
                        DemoApplication.getInstance().exit();
                    } else {
                        Toast.makeText(PwdFindActivity.this, "系统错误，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == GET_SMS) {
                Toast.makeText(PwdFindActivity.this, "验证码已发送，请注意查看信息", Toast.LENGTH_SHORT).show();

            } else if (msg.what == Get_SMS_FAIL) {
                switch (msg.arg1) {
                    case 468:
                        Toast.makeText(PwdFindActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case 462:
                    case 463:
                    case 464:
                    case 465:
                    case 476:
                    case 477:
                    case 478:
                        Toast.makeText(PwdFindActivity.this, "发送验证码次数达到上限", Toast.LENGTH_SHORT).show();
                        getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                        getSms_tv.setClickable(true);
                        break;
                    case 467:
                        Toast.makeText(PwdFindActivity.this, "校验验证码请求频繁", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if(!isCheck) {
                            Toast.makeText(PwdFindActivity.this, "验证码获取失败,请重新获取", Toast.LENGTH_SHORT).show();
                            getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                            getSms_tv.setClickable(true);
                        }else{
                            Toast.makeText(PwdFindActivity.this, "校验验证码失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
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
        DemoApplication.getInstance().addActivity(this);
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
                                Config.URL_FIND_PASSWORD, params);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        handler.sendEmptyMessage(GET_SMS);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    try {
                        String res   = data.toString();
                        JSONObject object = new JSONObject(res.substring(res.indexOf(":") + 1,res.length()));
                        Message msg = new Message();
                        msg.what = Get_SMS_FAIL;
                        msg.arg1 = object.getInt("status");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        mAlter = (TextView) findViewById(R.id.alter_pwd_title);
        pwdConfirm_edt = (EditText) findViewById(R.id.findpwd_pwd_confirm_edt);
        getSms_tv.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        confirm_iv.setOnClickListener(this);

        if(!TextUtils.isEmpty(this.getIntent().getExtras().toString())){
            Bundle bundle = this.getIntent().getExtras();
            String name = bundle.getString("name");
            mAlter.setText(name);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findpwd_back:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.findpwd_confirm:
                phone = phone_edt.getText().toString().trim();
                String ver = sms_edt.getText().toString().trim();
                String pwd = pwd_edt.getText().toString().trim();
                String pwdConfirm = pwdConfirm_edt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ver)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwdConfirm)) {
                    Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(this, "两次密码数据不一致，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    isCheck = true;
                    SMSSDK.submitVerificationCode("+86", phone, ver);
                }
                break;
            case R.id.findpwd_getsms_tv:
                phone = phone_edt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("info","phone number ======== " + phone);
                    SMSSDK.getVerificationCode("+86", phone);
                    getSms_tv.setTextColor(getResources().getColor(R.color.getsms_wait_color));
                    getSms_tv.setClickable(false);
                    CountDownTimer timer = new CountDownTimer(60000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                            getSms_tv.setClickable(true);
                        }
                    };
                    timer.start();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        SMSSDK.unregisterEventHandler(eh);
        super.onStop();
    }
}
