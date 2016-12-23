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
package com.xbh.tmi.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.DemoHelper;
import com.xbh.tmi.R;
import com.xbh.tmi.db.DemoDBManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import tm.db.dao.FriendDao;
import tm.entity.FriendBean;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.login.PwdFindActivity;
import tm.ui.login.RegistActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ViewTools;
import tm.utils.ViewUtil;

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


	private UserInfo mInfo;
	private Tencent mTencent;
	public QQAuth mQQAuth;
	public String mAppid = "1105535997";
	public String openid ;

	public static final String APP_ID = "wx4c49c49a71bfb7df";
	public static final String APP_SECRET = "fcef682cbaab83ea263658c7beb7ab44";
	public static BaseResp resp;
	private IWXAPI api;
	private String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化QQSDK
		mQQAuth = QQAuth.createInstance(mAppid, this.getApplicationContext());
		mTencent = Tencent.createInstance(mAppid, this);
		//初始化微信SDK
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
		//Mob平台授权，初始化微博
		ShareSDK.initSDK(this);

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
				//微信登录 需要签名打包才能使用
				if(isAppInstalled(LoginActivity.this,"com.tencent.mm")){
					requestAuth();
				}else{
					Toast.makeText(LoginActivity.this,"请先安装微信",Toast.LENGTH_SHORT).show();
				}

			}
		});
		loginWebo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//微博登录方法调用
				thirdSinaLogin();
			}
		});
		loginQQ.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//QQ登录方法调用

				if(isAppInstalled(LoginActivity.this,"com.tencent.mobileqq")){
					onClickLogin();
				}else{
					Toast.makeText(LoginActivity.this,"请先安装QQ",Toast.LENGTH_SHORT).show();
				}
			}
		});
		addListeners();
		denglu=(Button) findViewById(R.id.tm_login);
		denglu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginMe();
			}
		});
		zhuce = (TextView) findViewById(R.id.tm_zhuce);
		//注册
		zhuce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewUtil.jumpToOtherActivity(LoginActivity.this,RegistActivity.class);
			}
		});
		findPwd = (TextView) findViewById(R.id.tm_wangjimima);
		findPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("name", "找回密码");
				ViewUtil.jumpToOtherActivity(LoginActivity.this,PwdFindActivity.class, bundle);
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


//微信登录方法
	public void wechatLogin(View v) {
		requestAuth();
	}
	private void requestAuth() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "test_wechat_login";
		api.sendReq(req);
	}
	private void getAccessToken(String code) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("appid", LoginActivity.APP_ID.trim());
		params.addQueryStringParameter("secret", LoginActivity.APP_SECRET.trim());
		params.addQueryStringParameter("code", code.trim());
		params.addQueryStringParameter("grant_type", "authorization_code");
		httpUtils.send(HttpRequest.HttpMethod.GET, "https://api.weixin.qq.com/sns/oauth2/access_token", params , new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if(TextUtils.isEmpty(result))return;
				Log.e("info", "token = "+result);
				System.out.println("getAccessToken----->"+result);
				try {
					JSONObject json = new JSONObject(result);
					if(json.has("access_token")&&json.has("openid")){
						String access_token = json.getString("access_token");
						String openid = json.getString("openid");
						getWeChatUserInfo(access_token, openid);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}





	/**
	 * 环信登录
	 * 
	 * @param
	 */
	public void hxlogin() {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, "网络连接失败请稍后再试", Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		String	hxPassword ="tmi1q2w3e";

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

        DemoDBManager.getInstance().closeDB();

        DemoHelper.getInstance().setCurrentUserName(currentUsername);
        
		final long start = System.currentTimeMillis();
		Log.d(TAG, "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, hxPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "login: onSuccess");

				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}

			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();

				boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}

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
						Toast.makeText(getApplicationContext(), "登录失败",
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
		if(resp!=null){
			System.out.println("onResume-->code-->"+((SendAuth.Resp) resp).code);
			getAccessToken(((SendAuth.Resp) resp).code);
		}
	}
	public void LoginMe() {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			return;
		}

		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userName", currentUsername));
		list.add(new BasicNameValuePair("userPassword", currentPassword));
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
						uid=map.get("userId")+"";
						saveLoginInfo(LoginActivity.this,uid);
						LoadData();
						hxlogin();
					}else{
						Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
					}
					break;
				case ConstantsHandler.EXECUTE_FAIL:
					break;
				case ConstantsHandler.ConnectTimeout:
					Toast.makeText(LoginActivity.this,"登录超时",Toast.LENGTH_SHORT).show();
					break;
				default:
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
	public void onClickLogin() {
		if (!mQQAuth.isSessionValid()) {
			// 登录成功后返回监听
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();
				}
			};
			mTencent.loginWithOEM(this, "all", listener, "10000144", "10000144", "xxxx");
		}
	}
	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onComplete(final Object response) {
				}

				@Override
				public void onCancel() {

				}

				@Override
				public void onError(UiError arg0) {

				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);
		}
	}
	private class BaseUiListener implements IUiListener {
		@Override
		public void onComplete(Object response) {
			Log.e("lking", "返回值 = " + response.toString());
			JSONObject json = (JSONObject) response;
			try {
				openid = json.getString("openid");
				//在这里进行QQ登录操作*******************************************************************
				Log.e("lking", "登录操作");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e("lking", "openid2 = " + openid);
		}
		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {

		}

		@Override
		public void onCancel() {
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
	}
	private void getWeChatUserInfo(String token, String openid) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("access_token", token);
		params.addQueryStringParameter("openid", openid);
		httpUtils.send(HttpRequest.HttpMethod.GET, "https://api.weixin.qq.com/sns/userinfo", params , new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.e("info", "fanhuizhi = "+responseInfo.result);
				//在这里进行微信登录*********************************************************************
				try {
					JSONObject json = new JSONObject(responseInfo.result);
					String chatopenid = json.getString("openid");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/** 新浪微博授权、获取用户信息页面 */
	private void thirdSinaLogin() {
		//初始化新浪平台
		Platform pf = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
		pf.SSOSetting(true);
		//设置监听
		pf.setPlatformActionListener(new PlatformActionListener() {

			/** 新浪微博授权成功回调页面 */
			@Override
			public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
				//在这里进行微博登录操作*******************************************************************
				Log.e("info", "action = "+action);
				Log.e("info", "uid = "+platform.getDb().getUserId());
			}
			/** 取消授权 */
			@Override
			public void onCancel(Platform platform, int action) {
				Log.e("info", "Cancel = "+platform.getDb().getUserId());
			}
			/** 授权失败 */
			@Override
			public void onError(Platform platform, int action, Throwable t) {
				Log.e("info", "Error = "+platform.getDb().getUserId());
			}
		});;
		//获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
		pf.authorize();
	}




	private boolean isAppInstalled(Context context,String packagename)
	{
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		}catch (Exception e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			//System.out.println("没有安装");
			return false;
		}else{
			//System.out.println("已经安装");
			return true;
		}
	}
	/**
	 * 好友列表
	 */
	public void LoadData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", uid));
		NetFactory.instance().commonHttpCilent(handler, LoginActivity.this,
				Config.URL_FRIENDS, list);

	}
	/**
	 * 接口回调
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.e("info","msg.what=111="+msg.what);
			switch (msg.what) {
				case ConstantsHandler.EXECUTE_SUCCESS:
					Map map = (Map) msg.obj;
					Log.e("info","map=11="+map);
					setData(map);
					//插库
					break;
				default:
					break;
			}
		}
	};
	protected void setData(Map map) {
		try {
			JSONObject obj =new JSONObject(map.toString());
			JSONArray objList = obj.getJSONArray("rows");
			FriendBean mFriendBean=new FriendBean();
			FriendDao mdao =new FriendDao();
			List<FriendBean> friendlist = new ArrayList<FriendBean>();
			if(objList.length()>0){
				for (int i = 0; i < objList.length(); i++) {
					JSONObject jo = objList.getJSONObject(i);
					mFriendBean.mNickname=jo.get("nickname")+"";
					mFriendBean.mphoto=jo.get("photo")+"";
					mFriendBean.mUsername= jo.get("userName")+"";
					mFriendBean.mUserID= Integer.parseInt(jo.get("userId")+"");
					friendlist.add(mFriendBean);
					mdao.insertUserInfoList(friendlist);
				}
			}
		} catch (JSONException e) {
		}

	}
}
