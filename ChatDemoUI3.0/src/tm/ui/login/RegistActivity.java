package tm.ui.login;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import tm.http.Config;
import tm.http.NetFactory;
import tm.utils.ViewUtil;

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
    private EditText nickname_edt;
    private String phone;
    private String nickName;
    private String pwd;
    private EventHandler eh;
    private int i = 59;
    private boolean isCheck;
    private int recLen = 60;
    private CountDownTimer mTimer;
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
                        ViewUtil.backToOtherActivity(RegistActivity.this);
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
                        break;
                    case GET_SMS_FAIL:
                        switch (msg.arg1) {
                            case 468:
                                Toast.makeText(RegistActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                break;
                            case 462:
                            case 463:
                            case 464:
                            case 465:
                            case 476:
                            case 477:
                            case 478:
                                Toast.makeText(RegistActivity.this, "发送验证码次数达到上限", Toast.LENGTH_SHORT).show();
                                break;
                            case 467:
                                Toast.makeText(RegistActivity.this, "校验验证码请求频繁", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                if(!isCheck) {
                                    Toast.makeText(RegistActivity.this, "验证码获取失败,请重新获取", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegistActivity.this, "校验验证码失败", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        break;
                }
            }
        }
    };
    private Dialog mProgressDialog;
    private AnimationDrawable loadingAnim;

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
                        params.add(new BasicNameValuePair("userName", phone));
                        params.add(new BasicNameValuePair("userPassword", pwd));
                        params.add(new BasicNameValuePair("nickname",nickName));
                        NetFactory.instance().commonHttpCilent(handler, RegistActivity.this,
                                Config.URL_REDGIST, params);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        handler.sendEmptyMessage(GET_SMS);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    try {
                        String res   = data.toString();
                        Log.e("Lking--->","验证码返回值 = "+res);
                        JSONObject object = new JSONObject(res.substring(res.indexOf(":") + 1,res.length()));
                        Message msg = new Message();
                        msg.what = GET_SMS_FAIL;
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
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getSms_tv.setText(recLen+"秒后可点击");
                recLen--;
            }

            @Override
            public void onFinish() {
                getSms_tv.setTextColor(Color.parseColor("#ae7efc"));
                getSms_tv.setClickable(true);
                getSms_tv.setText("发送验证码");
                recLen = 60;
            }
        };
    }

    private void initView() {
        back_tv = (ImageView) findViewById(R.id.regist_back);
        confirm_iv = (TextView) findViewById(R.id.regist_confirm);
        phone_edt = (EditText) findViewById(R.id.regist_phone_edt);
        sms_edt = (EditText) findViewById(R.id.regist_sms_edt);
        pwd_edt = (EditText) findViewById(R.id.regist_pwd_edt);
        getSms_tv = (TextView) findViewById(R.id.regist_getsms_tv);
        pwdConfirm_edt = (EditText) findViewById(R.id.regist_pwd_confirm_edt);
        nickname_edt = (EditText) findViewById(R.id.regist_nickname_edt);
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
                Log.e("LKing--->","完成按钮点击事件");
                phone = phone_edt.getText().toString().trim();
                nickName = nickname_edt.getText().toString().trim();
                String ver = sms_edt.getText().toString().trim();
                pwd = pwd_edt.getText().toString().trim();
                String pwdConfirm = pwdConfirm_edt.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)) {
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ver)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
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
            case R.id.regist_getsms_tv:
                Log.e("LKing--->","发送验证码点击事件");
                phone = phone_edt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    getSms_tv.setTextColor(getResources().getColor(R.color.getsms_wait_color));
                    getSms_tv.setClickable(false);
                    SMSSDK.getVerificationCode("+86", phone);
                    mTimer.start();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        SMSSDK.unregisterEventHandler(eh);
        mTimer.cancel();
        mTimer = null;
        super.onStop();
    }
}
