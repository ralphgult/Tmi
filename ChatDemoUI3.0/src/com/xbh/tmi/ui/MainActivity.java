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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.redpacketui.RedPacketConstant;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xbh.tmi.Constant;
import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.DemoHelper;
import com.xbh.tmi.R;
import com.xbh.tmi.db.DemoDBManager;
import com.xbh.tmi.db.InviteMessgeDao;
import com.xbh.tmi.db.UserDao;
import com.xbh.tmi.runtimepermissions.PermissionsManager;
import com.xbh.tmi.runtimepermissions.PermissionsResultAction;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.manager.PersonManager;
import tm.ui.main.HuihuaFragment;
import tm.ui.mine.PersonCenterActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageUtil;
import tm.utils.SysUtils;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {

	protected static final String TAG = "MainActivity";
	private TextView unreadLabel;

	private ImageView[] mTabs;
	private TmFragment  tmFragment;
	private MyFragment myFragment;
	private HuihuaFragment huihuaFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	// user logged into another device
	public boolean isConflict = false;
	// user account was removed
	private boolean isCurrentAccountRemoved = false;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			}
		}
	};

	/**
	 * check if current user account was remove
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApplication.getInstance().addActivity(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String packageName = getPackageName();
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
		        Intent intent = new Intent();
		        intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
		        intent.setData(Uri.parse("package:" + packageName));
		        startActivity(intent);
		    }
		}
		
		//make sure activity will not in background if user is logged into another device or removed
		if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
		    DemoHelper.getInstance().logout(false,null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.em_activity_main);
		// runtime permission for android 6.0, just require all permissions here for simple


		SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
		int age = sp.getInt("head", 0);
		final String headPath = "/mnt/sdcard/" + "head"+".jpg";
		switch (age){
			case 2://微博
				new Thread() {
					@Override
					public void run() {
						SharedPreferences sharedPre = MainActivity.this.getSharedPreferences("config", MainActivity.this.MODE_PRIVATE);
						String userId = sharedPre.getString("username", "");
						PersonManager.SubmitPost(new File(headPath), userId, 1, mHandler);

						SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("head", 20);
						editor.commit();
					}
				}.start();
				break;
			case 3://微信
				new Thread() {
					@Override
					public void run() {
						SharedPreferences sharedPre = MainActivity.this.getSharedPreferences("config", MainActivity.this.MODE_PRIVATE);
						String userId = sharedPre.getString("username", "");
						PersonManager.SubmitPost(new File(headPath), userId, 1, mHandler);

						SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("head", 30);
						editor.commit();
				}
				}.start();
				break;
			case 4://QQ
				new Thread() {
					@Override
					public void run() {
						SharedPreferences sharedPre = MainActivity.this.getSharedPreferences("config", MainActivity.this.MODE_PRIVATE);
						String userId = sharedPre.getString("username", "");
						PersonManager.SubmitPost(new File(headPath), userId, 1, mHandler);

						SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("head", 40);
						editor.commit();
					}
				}.start();
				break;
			default:
				Log.e("Lking","已经设置过了，不在重新设置");
				break;
		}






		requestPermissions();

		initView();

		//umeng api
		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		inviteMessgeDao = new InviteMessgeDao(this);
		UserDao userDao = new UserDao(this);
		conversationListFragment = new ConversationListFragment();
		tmFragment=new TmFragment();
		myFragment=new MyFragment();
		huihuaFragment=new HuihuaFragment();
		fragments = new Fragment[] { huihuaFragment,conversationListFragment, tmFragment ,myFragment};

		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, huihuaFragment).commit();


		//register broadcast receiver to receive the change of group from DemoHelper
		registerBroadcastReceiver();
		
		
		EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
		//debug purpose only
        registerInternalDebugReceiver();
	}

	@TargetApi(23)
	private void requestPermissions() {
		PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
			@Override
			public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDenied(String permission) {
				//Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * init views
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		mTabs = new ImageView[4];
		mTabs[0] = (ImageView) findViewById(R.id.btn_huihua);
		mTabs[1] = (ImageView) findViewById(R.id.btn_miliao);
		mTabs[2] = (ImageView) findViewById(R.id.btn_tm);
		mTabs[3] = (ImageView) findViewById(R.id.btn_me);
		// select first tab
		mTabs[0].setSelected(true);
	}

	/**
	 * on tab clicked
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_huihua:
			index = 0;
			break;
		case R.id.btn_miliao:
			index = 1;
			break;
		case R.id.btn_tm:
			index = 2;
			break;
		case R.id.btn_me:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
				trx.replace(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// set current tab selected
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	EMMessageListener messageListener = new EMMessageListener() {
		
		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// notify new message
		    for (EMMessage message : messages) {
		        DemoHelper.getInstance().getNotifier().onNewMsg(message);
		    }
			refreshUIWithMessage();
		}
		
		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
			//red packet code : 处理红包回执透传消息
			for (EMMessage message : messages) {
				EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
				final String action = cmdMsgBody.action();//获取自定义action
				if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
					RedPacketUtil.receiveRedPacketAckMessage(message);
				}
			}
			//end of red packet code
			refreshUIWithMessage();
		}
		
		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages) {
		}
		
		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message) {
		}
		
		@Override
		public void onMessageChanged(EMMessage message, Object change) {}
	};

	private void refreshUIWithMessage() {
		runOnUiThread(new Runnable() {
			public void run() {	// refresh unread count
				updateUnreadLabel();

				if (currentTabIndex == 1) {
					// refresh conversation list
					if (conversationListFragment != null) {
						conversationListFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	public void back(View view) {
		super.back(view);
	}
	
	private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
		intentFilter.addAction(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                if (currentTabIndex == 1) {
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
//				else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
				//red packet code : 处理红包回执透传消息
//				if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//					if (conversationListFragment != null){
//						conversationListFragment.refresh();
//					}
//				}
				//end of red packet code
			}
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }
	
	public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
					if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
							username.equalsIgnoreCase(ChatActivity.activityInstance.toChatUsername)) {
					    String st10 = getResources().getString(R.string.have_you_removed);
					    Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
					    .show();
					    ChatActivity.activityInstance.finish();
					}
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onContactAgreed(String username) {}
        @Override
        public void onContactRefused(String username) {}
	}
	
	private void unregisterBroadcastReceiver(){
	    broadcastManager.unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		
		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		unregisterBroadcastReceiver();

		try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
		
	}

	/**
	 * update unread message count
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		int countT = getUnreadMessagesCount();
		if (count > 0 || countT > 0 ) {
			if(count == 0){
				count = 1;
			}
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
	}
	}

	public int getUnreadMessagesCount(){
		return DemoDBManager.getInstance().getUnreadNotifyCount();
	}
//	/**
//	 * update the total unread count
//	 */
//	public void updateUnreadAddressLable() {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				int count = getUnreadAddressCountTotal();
//				if (count > 0) {
//					unreadAddressLable.setVisibility(View.VISIBLE);
//				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
//				}
//			}
//		});
//
//	}

	/**
	 * get unread event notification count, including application, accepted, etc
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
		return unreadAddressCountTotal;
	}

	/**
	 * get unread message count
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
		for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
			if(conversation.getType() == EMConversationType.ChatRoom)
			chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal-chatroomUnreadMsgCount;
	}

	private InviteMessgeDao inviteMessgeDao;

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("info", "refresh=2222=");
//		conversationListFragment.refresh();
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
//			updateUnreadAddressLable();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.pushActivity(this);

		EMClient.getInstance().chatManager().addMessageListener(messageListener);
	}

	@Override
	protected void onStop() {
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}



	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

	/**
	 * show the dialog when user logged into another device
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHelper.getInstance().logout(false,null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * show the dialog if user account is removed
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHelper.getInstance().logout(false,null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}
	
	/**
	 * debug purpose only, you can ignore this
	 */
	private void registerInternalDebugReceiver() {
	    internalDebugReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                
                            }
                        });
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {}
                    
                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

	@Override 
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				moveTaskToBack(false);
//				finish();
//				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
