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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

import static com.hyphenate.easeui.utils.EaseUserUtils.getHttpBitmap;

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
	public String mAppid = "1105353663";

	public static final String APP_ID = "wx4c49c49a71bfb7df";
	public static final String APP_SECRET = "fcef682cbaab83ea263658c7beb7ab44";
	public static BaseResp resp;
	private IWXAPI api;
	private String uid;


	String mOtherUid;

private boolean isWeiXin = false;
private boolean isOther = false;
	private String SinePath;//微博头像
	private String WeChatPath;//微信头像
	private String QQPath;//QQ头像
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1001://授权失败
						Toast.makeText(LoginActivity.this,"授权失败，请稍后再试",Toast.LENGTH_SHORT).show();
						break;
					case 1002://新浪微博授权成功，去登录
						Platform obj = (Platform)msg.obj;
						mOtherUid = obj.getDb().getUserId();
						String Username = obj.getDb().getUserName();
						//////////////////////////////////////////////////////////////////
						SinePath = obj.getDb().getUserIcon();
						new Thread(new Runnable() {
							@Override
							public void run() {
								Bitmap mSineBitmap = getHttpBitmap(SinePath);//从网络获取图片
								savePicture(mSineBitmap);//保存图片到SD卡

								SharedPreferences mSinesp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
								int age = mSinesp.getInt("head", 0);
								if(age!=0&&age!=20) {
									SharedPreferences.Editor editor = mSinesp.edit();
									editor.putInt("head", 2);
									editor.commit();
								}
							}
						}).start();




						Log.e("Lking info","微博头像 = "+SinePath);
						List<NameValuePair> Sinaparams = new ArrayList<NameValuePair>();
						Sinaparams.add(new BasicNameValuePair("userName", mOtherUid));
						Sinaparams.add(new BasicNameValuePair("userPassword", "123456"));

						String strSinaNickName = Username.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");

						Sinaparams.add(new BasicNameValuePair("nickname",strSinaNickName.trim()));
						NetFactory.instance().commonHttpCilent(mRegisHandler, LoginActivity.this,
								Config.URL_REDGIST, Sinaparams);
						break;
					case 1003://微信授权成功，去登录
					{
						try {
							String str = String.valueOf(msg.obj);
							JSONObject json = new JSONObject(str);
							mOtherUid =json.getString("openid");//注册使用的账号
							String nickname = json.getString("nickname");//注册使用的昵称
							////////////////////////////////////////////////////////////////////////////////////////////////////
							WeChatPath = json.getString("headimgurl");
							new Thread(new Runnable() {
								@Override
								public void run() {
									Bitmap mWeChatBitmap = getHttpBitmap(WeChatPath);//从网络获取图片
									savePicture(mWeChatBitmap);//保存图片到SD卡
									SharedPreferences mWeChatsp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
									int age = mWeChatsp.getInt("head", 0);
									if(age==0||age!=30){
										SharedPreferences.Editor editor = mWeChatsp.edit();
										editor.putInt("head", 3);
										editor.commit();
									}



								}
							}).start();
							Log.e("Lking info","微信头像 = "+WeChatPath);





							String strWeChatNickName = nickname.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
							//在这里进行微信登录*********************************************************************
							List<NameValuePair> mWeChatParams = new ArrayList<NameValuePair>();
							mWeChatParams.add(new BasicNameValuePair("userName", mOtherUid));
							mWeChatParams.add(new BasicNameValuePair("userPassword", "123456"));
							mWeChatParams.add(new BasicNameValuePair("nickname",strWeChatNickName.trim()));
							NetFactory.instance().commonHttpCilent(mRegisHandler, LoginActivity.this,
									Config.URL_REDGIST, mWeChatParams);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					}
					case 1004://QQ授权成功，去登录
					{
						try {
							String str = String.valueOf(msg.obj);
							JSONObject json = new JSONObject(str);
							/////////////////////////////////////////////////////////////////////////
							QQPath = json.getString("figureurl_qq_2");//QQ头像
							Log.e("Lking info","QQ头像 = "+QQPath);
							new Thread(new Runnable() {
								@Override
								public void run() {
									Bitmap mQQBitmap = getHttpBitmap(QQPath);//从网络获取图片
									savePicture(mQQBitmap);//保存图片到SD卡

									SharedPreferences mQQsp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
									int age = mQQsp.getInt("head", 0);
									if(age==0||age!=40) {
										SharedPreferences.Editor editor = mQQsp.edit();
										editor.putInt("head", 4);
										editor.commit();
									}
								}
							}).start();


							String nickname = json.getString("nickname");//注册使用的昵称
							//在这里进行微信登录*********************************************************************
							mOtherUid = mStropenID;
							List<NameValuePair> mQQParams = new ArrayList<NameValuePair>();
							mQQParams.add(new BasicNameValuePair("userName", mOtherUid.toLowerCase()));
							mQQParams.add(new BasicNameValuePair("userPassword", "123456"));

							String strQQNickName = nickname.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
							mQQParams.add(new BasicNameValuePair("nickname",strQQNickName.trim()));
							NetFactory.instance().commonHttpCilent(mRegisHandler, LoginActivity.this,
									Config.URL_REDGIST, mQQParams);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					}
					default:
						break;
				}

		}
	};
	private String mStropenID;

	private Handler mRegisHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(null != msg.obj){
				String result = ((Map) msg.obj).toString();
				JSONObject object = null;
				try {
					object = new JSONObject(result);
					int resultCode = object.getInt("authId");
					if (resultCode == 1 ||resultCode == -1) {
						//调用登录接口
						List<NameValuePair> list = new ArrayList<NameValuePair>();
						list.add(new BasicNameValuePair("userName", mOtherUid));
						list.add(new BasicNameValuePair("userPassword", "123456"));
						NetFactory.instance().commonHttpCilent(login, LoginActivity.this,
								Config.URL_LOGIN_USER, list);
					}else {
						Toast.makeText(LoginActivity.this, "系统错误，请稍后再试", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化QQSDK
//		mQQAuth = QQAuth.createInstance(mAppid, this.getApplicationContext());
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
					isWeiXin = true;
					isOther = true;
				}else{
					Toast.makeText(LoginActivity.this,"请先安装微信",Toast.LENGTH_SHORT).show();
				}

			}
		});
		loginWebo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//微博登录方法调用
				isWeiXin = false;
				isOther = true;
				thirdSinaLogin();
			}
		});
		loginQQ.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//QQ登录方法调用
				isWeiXin = false;
				isOther = true;
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
			boolean result=DemoHelper.getInstance().getCurrentUsernName().matches("[0-9]+");
			if(result){
				usernameEditText.setText(DemoHelper.getInstance().getCurrentUsernName());
			}else{
				usernameEditText.setText("");
			}

		}
	}


//微信登录方法
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
		if(isOther){
			currentUsername = mOtherUid;
		}else{
			currentUsername = usernameEditText.getText().toString().trim();
		}
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
						Toast.makeText(getApplicationContext(), "即时通讯组件启动失败请重新登陆",
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
		if(isWeiXin){
			if(resp!=null){
				System.out.println("onResume-->code-->"+((SendAuth.Resp) resp).code);
				getAccessToken(((SendAuth.Resp) resp).code);
			}
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
						Log.e("Lking","photo = "+ map.get("photo"));
						saveLoginInfo(LoginActivity.this,uid,map.get("userName")+"",map.get("photo")+"",map.get("nickname")+"", map.get("aliPay") + "");
						LoadData();
						hxlogin();
					}else{
						Toast.makeText(LoginActivity.this,"账号或密码错误请重新登陆",Toast.LENGTH_SHORT).show();
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
	public static void saveLoginInfo(Context context, String username,String phone,String photo,String nickname,String aliPay){
		//获取SharedPreferences对象
		SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
		//获取Editor对象
		SharedPreferences.Editor editor=sharedPre.edit();
		//设置参数
		editor.putString("username", username);
		editor.putString("phone", phone);
		editor.putString("photo", photo);
		editor.putString("nickname", nickname);
		editor.putString("aliAccount", aliPay);
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
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", qqListener);
		}
	}

	//登录成功监听
	private IUiListener qqListener = new BaseUiListener(){
		@Override
		protected void doComplete(JSONObject values) {
			initLoginID(values);
		}
	};
	/**
	 * 获取qq信息
	 * @param jsonObject
	 */
	private void initLoginID(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt("ret")==0) {
				String token = jsonObject.getString("access_token");
				String expires = jsonObject.getString("expires_in");
				mStropenID = jsonObject.getString("openid");
				//**下面这两步设置很重要,如果没有设置,返回为空**
				mTencent.setOpenId(mStropenID);
				mTencent.setAccessToken(token, expires);
				getuserInfo();
			}
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}
	//QQ信息返回
	private void getuserInfo() {
		mInfo = new UserInfo(LoginActivity.this,mTencent.getQQToken());
		mInfo.getUserInfo(getQQinfoListener);
	}
	/**
	 * 获取用户信息
	 */
	private IUiListener getQQinfoListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			Log.e("Lking","用户信息="+response.toString());
			Message msg = new Message();
			msg.what = 1004;
			msg.obj = response;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(UiError uiError) {
			Message msg = new Message();
			msg.what = 1001;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onCancel() {

		}
	};

	private class BaseUiListener implements IUiListener {
		@Override
		public void onComplete(Object response) {
			Log.e("lking", "返回值 = " + response.toString());
			doComplete((JSONObject)response);

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
		Tencent.onActivityResultData(requestCode, resultCode, data, qqListener);
	}
	private void getWeChatUserInfo(String token, String openid) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("access_token", token);
		params.addQueryStringParameter("openid", openid);
		httpUtils.send(HttpRequest.HttpMethod.GET, "https://api.weixin.qq.com/sns/userinfo", params , new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
					Message msg = new Message();
					msg.what = 1001;
					mHandler.sendMessage(msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.e("info", "fanhuizhi = "+responseInfo.result);
				Message msg = new Message();
				msg.what = 1003;
				msg.obj = responseInfo.result;
				mHandler.sendMessage(msg);

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
				Message msg = new Message();
				msg.what = 1002;
				msg.obj = platform;
				mHandler.sendMessage(msg);
//				try{
////					Log.e("info", "uid = "+platform.getDb().getUserId());
////					Log.e("info", "Username = "+platform.getDb().getUserName());
//					String uid = platform.getDb().getUserId();
//					final String Username = platform.getDb().getUserName();
//					Log.e("Lking","Username = "+Username);
//
//					runOnUiThread(new Runnable() {
//						public void run() {
//							Toast.makeText(LoginActivity.this,Username,Toast.LENGTH_SHORT).show();
//						}
//					});
//				}catch (Exception e){
//					e.printStackTrace();
//				}
				}
			/** 取消授权 */
			@Override
			public void onCancel(Platform platform, int action) {
				Log.e("info", "Cancel = "+platform.getDb().getUserId());
			}
			/** 授权失败 */
			@Override
			public void onError(Platform platform, int action, Throwable t) {
				Message msg = new Message();
				msg.what = 1001;
				mHandler.sendMessage(msg);
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



	public Bitmap getHttpBitmap(String url)
	{
		Bitmap bitmap = null;
		try
		{
			URL pictureUrl = new URL(url);
			InputStream in = pictureUrl.openStream();
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}

	public void savePicture(Bitmap bitmap)
	{
		String pictureName = "/mnt/sdcard/" + "head"+".jpg";
		File file = new File(pictureName);
		FileOutputStream out;
		try
		{
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
