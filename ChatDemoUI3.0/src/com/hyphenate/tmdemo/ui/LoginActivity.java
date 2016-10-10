/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.tmdemo.ui;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.tmdemo.DemoApplication;
import com.hyphenate.tmdemo.DemoHelper;
import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.db.DemoDBManager;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.entity.UserBean;
import tm.http.AppCfg;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.login.PwdFindActivity;
import tm.ui.login.RegistActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ViewTools;

import android.os.Handler;

/**
 * 登录界面
 * 
 */
public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private TextView zhuce;
	private TextView findPwd;
	private Button denglu;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;
	private ImageView clearAccount;
	private ImageView clearPassword;
	private ImageView loginWechat;
	private ImageView loginWebo;
	private ImageView loginQQ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enter the main activity if already logged in
		if (DemoHelper.getInstance().isLoggedIn()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.em_activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		clearAccount = (ImageView) findViewById(R.id.tm_aty_clear_account_iv);
		clearPassword = (ImageView) findViewById(R.id.tm_aty_clear_password_iv);
		loginWechat = (ImageView) findViewById(R.id.login_wechat_iv);
		loginWebo = (ImageView) findViewById(R.id.login_webo_iv);
		loginQQ = (ImageView) findViewById(R.id.login_qq_iv);
		loginWechat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this,"正在审核中...",Toast.LENGTH_SHORT).show();
			}
		});
		loginWebo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this,"正在审核中...",Toast.LENGTH_SHORT).show();
			}
		});
		loginQQ.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this,"正在审核中...",Toast.LENGTH_SHORT).show();
			}
		});
		addListeners();
		denglu=(Button) findViewById(R.id.tm_login);
		denglu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginMe();
//				hxlogin();
			}
		});
		zhuce = (TextView) findViewById(R.id.tm_zhuce);
		//注册
		zhuce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,RegistActivity.class));
			}
		});
		findPwd = (TextView) findViewById(R.id.tm_wangjimima);
		findPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,PwdFindActivity.class));
			}
		});

		// if user changed, clear the password
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (DemoHelper.getInstance().getCurrentUsernName() != null) {
			usernameEditText.setText(DemoHelper.getInstance().getCurrentUsernName());
		}
	}

	/**
	 * login
	 * 
	 * @param
	 */
	public void hxlogin() {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();


		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d(TAG, "EMClient.getInstance().onCancel");
				progressShow = false;
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
		pd.show();

		// After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
		// close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);
        
		final long start = System.currentTimeMillis();
		// call login method
		Log.d(TAG, "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "login: onSuccess");

				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}

				// ** manually load all local groups and conversation
			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();

			    // update current user's display name for APNs
				boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}

				// get user's info (this should be get from App's server or 3rd party service)
				DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
				Log.d(TAG, "login: onProgress");
			}

			@Override
			public void onError(final int code, final String message) {
				Log.d(TAG, "login: onError: " + code);
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	
	/**
	 * register
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}
	public void LoginMe() {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			return;
		}
//		String userName = et_name.getText().toString();
//		String passWord = et_password.getText().toString();
//		if (userName.equals("")) {
//			AlertMsgS("请输入用户名");
//			et_name.findFocus();
//			return;
//		}
//		if (passWord.equals("")) {
//			AlertMsgS("请输入用户密码");
//			et_password.findFocus();
//			return;
//		}

//		progressShow = true;
//		pd = new ProgressDialog(LoginActivity.this);
//		pd.setCanceledOnTouchOutside(false);
//		pd.setOnCancelListener(new OnCancelListener() {
//
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				Log.d(this.getClass().getSimpleName(),
//						"EMClient.getInstance().onCancel");
//				progressShow = false;
//			}
//		});
//		pd.setMessage("登录中……");
//		pd.show();

		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("info","currentUsername=="+currentUsername);
		Log.e("info","currentPassword=="+currentPassword);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userName", currentUsername));
		list.add(new BasicNameValuePair("userPassword", currentPassword));
//		list.add(new BasicNameValuePair("identify", ""));
		NetFactory.instance().commonHttpCilent(login, this,
				Config.URL_LOGIN_USER, list);
	}
	Handler login = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.e("info","msg.what=="+msg.what);
			switch (msg.what) {
				case ConstantsHandler.EXECUTE_SUCCESS:
					Map map = (Map) msg.obj;
					Log.e("info","map=="+map);
					String authId = map.get("authId") + "";
					if(authId.endsWith("1")){
						Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//						UserBean user = new UserBean();
						String uid=map.get("userId")+"";
						saveLoginInfo(LoginActivity.this,uid);
//						AppCfg._instance.setCurrentUser(user);
//						AppCfg._instance.userName = currentUsername;
//						AppCfg._instance.password = currentPassword;
//						AppCfg._instance.userId=map.get("userId")+"";
//						AppCfg._instance.save();
						hxlogin();
					}else{
						Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
					}
//					try {
//						result = URLDecoder.decode(result, "utf-8");
//						JSONObject jo = new JSONObject(result);
//						AppCfg._instance.userName = currentUsername;
//						AppCfg._instance.password = currentPassword;
//						AppCfg._instance.userId=jo.get("userId")+"";
////					    AppCfg._instance.LoginType = "1";
//						AppCfg._instance.save();
//						UserBean user = new UserBean();
//						user.init(jo);
//						AppCfg._instance.setCurrentUser(user);
//						DemoApplication.currentUserNick = user.getName();
//					} catch (Exception e) {
//					}
//					MainActivity.userStateChange = true;
//					AppCfg._instance.userName = currentUsername;
//					AppCfg._instance.password = currentPassword;
//					AppCfg._instance.userId=jo.get("userid")+"";
////					AppCfg._instance.LoginType = "1";
//					AppCfg._instance.save();
//					loginHx();
					hxlogin();
					break;
				case ConstantsHandler.EXECUTE_FAIL:

//					pd.dismiss();
//					map = (Map) msg.obj;
//					String status = map.get("status").toString();
//					if (status.equals("-200")) {
////						AlertMsgL("密码错误");
//					} else if (status.equals("-300")) {
////						AlertMsgL("用户不存在");
//					}
					break;
				case ConstantsHandler.ConnectTimeout:
//					pd.dismiss();
//					AlertMsgL("登录超时");
//					Toast.makeText(this,"登录超时",Toast.LENGTH_SHORT).show();
					break;
				default:
//					pd.dismiss();
					break;
			}
		}
	};
	/**
	 * 使用SharedPreferences保存用户登录信息
	 * @param context
	 * @param username
	 */
	public static void saveLoginInfo(Context context, String username){
		//获取SharedPreferences对象
		SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
		//获取Editor对象
		SharedPreferences.Editor editor=sharedPre.edit();
		//设置参数
		editor.putString("username", username);
		//提交
		editor.commit();
	}
	public void addListeners() {
		ViewTools.addTextChangedListener(usernameEditText, clearAccount);
		usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String	firstAccount = usernameEditText.getText().toString().trim();
				if (hasFocus) {
					if (firstAccount.length() > 0) {
						clearAccount.setVisibility(View.VISIBLE);
					} else {
						clearAccount.setVisibility(View.GONE);
					}

				} else {
					clearAccount.setVisibility(View.GONE);
				}
			}
		});
		ViewTools.addTextChangedListener(passwordEditText, clearPassword);
		passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				String loginPwd = passwordEditText.getText().toString().trim();
				if (hasFocus) {
					if (loginPwd.length() > 0) {
						clearPassword.setVisibility(View.VISIBLE);
					} else {
						clearPassword.setVisibility(View.GONE);
					}
				} else {
					clearPassword.setVisibility(View.GONE);
				}
			}
		});
	}
}
