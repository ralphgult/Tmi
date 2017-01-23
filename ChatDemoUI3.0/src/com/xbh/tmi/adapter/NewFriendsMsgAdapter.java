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
package com.xbh.tmi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hyphenate.chat.EMClient;
import com.xbh.tmi.R;
import com.xbh.tmi.db.InviteMessgeDao;
import com.xbh.tmi.domain.InviteMessage;
import com.xbh.tmi.domain.InviteMessage.InviteMesageStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tm.db.dao.FriendDao;
import tm.entity.FriendBean;
import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.home.GeranActivity;
import tm.utils.ConstantsHandler;
import tm.utils.VolleyLoadPicture;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;
	private  String username;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		SharedPreferences sharedPre=context.getSharedPreferences("config",context.MODE_PRIVATE);
		username=sharedPre.getString("username", "");
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.agree = (Button) convertView.findViewById(R.id.agree);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String str1 = context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
		String str2 = context.getResources().getString(R.string.agree);
		
		String str3 = context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
		String str4 = context.getResources().getString(R.string.Apply_to_the_group_of);
		String str5 = context.getResources().getString(R.string.Has_agreed_to);
		String str6 = context.getResources().getString(R.string.Has_refused_to);
		
		String str7 = context.getResources().getString(R.string.refuse);
		String str8 = context.getResources().getString(R.string.invite_join_group);
        String str9 = context.getResources().getString(R.string.accept_join_group);
		String str10 = context.getResources().getString(R.string.refuse_join_group);
		
		final InviteMessage msg = getItem(position);
		if (msg != null) {
		    
		    holder.agree.setVisibility(View.INVISIBLE);
		    
			if(msg.getGroupId() != null){ // show group name
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else{
				holder.groupContainer.setVisibility(View.GONE);
			}
			
			holder.reason.setText(msg.getReason());
			FriendBean fb=new FriendDao().getLocalUserInfoByUserId(msg.getFrom());
			if(fb!=null){
				if(TextUtils.isEmpty(fb.mNickname)){
					holder.name.setText(msg.getFrom());
				}else{
					holder.name.setText(fb.mNickname);
				}
				if(!TextUtils.isEmpty(fb.mphoto)){
					VolleyLoadPicture vlp = new VolleyLoadPicture(context, holder.avator);
					vlp.getmImageLoader().get(fb.mphoto, vlp.getOne_listener());
				}

			}else{
				holder.name.setText(msg.getFrom());
			}



			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText(str1);
			} else if (msg.getStatus() == InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMesageStatus.BEAPPLYED ||
			        msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
			    holder.agree.setVisibility(View.VISIBLE);
                holder.agree.setEnabled(true);
                holder.agree.setBackgroundResource(android.R.drawable.btn_default);
                holder.agree.setText(str2);
			    
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
				holder.status.setBackgroundResource(android.R.drawable.btn_default);
				holder.status.setText(str7);
				if(msg.getStatus() == InviteMesageStatus.BEINVITEED){
					if (msg.getReason() == null) {
						// use default text
						holder.reason.setText(str3);
					}
				}else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //application to join group
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText(str4 + msg.getGroupName());
					}
				} else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
				    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str8 + msg.getGroupName());
                    }
				}
				
				// set click listener
                holder.agree.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // accept invitation
                        acceptInvitation(holder.agree, holder.status, msg);
                    }
                });
				holder.status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// decline invitation
					    refuseInvitation(holder.agree, holder.status, msg);
					}
				});
			} else if (msg.getStatus() == InviteMesageStatus.AGREED) {
				holder.status.setText(str5);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMesageStatus.REFUSED){
				holder.status.setText(str6);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMesageStatus.GROUPINVITATION_ACCEPTED){
			    String str = msg.getGroupInviter() + str9 + msg.getGroupName();
                holder.status.setText(str);
                holder.status.setBackgroundDrawable(null);
                holder.status.setEnabled(false);
            } else if(msg.getStatus() == InviteMesageStatus.GROUPINVITATION_DECLINED){
                String str = msg.getGroupInviter() + str10 + msg.getGroupName();
                holder.status.setText(str);
                holder.status.setBackgroundDrawable(null);
                holder.status.setEnabled(false);
            }
		}

		return convertView;
	}

	/**
	 * accept invitation
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// call api
				try {
					if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//accept be friends
						EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
						accept(msg.getFrom());
					} else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //accept application to join group
						EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
					} else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
					    EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
					}
                    msg.setStatus(InviteMesageStatus.AGREED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							buttonAgree.setText(str2);
							buttonAgree.setBackgroundDrawable(null);
							buttonAgree.setEnabled(false);
							
							buttonRefuse.setVisibility(View.INVISIBLE);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}
	
	/**
     * decline invitation
     * 
     * @param button
     * @param username
     */
    private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(R.string.Has_refused_to);
        final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//decline the invitation
                        EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //decline application to join group
                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
                    }
                    msg.setStatus(InviteMesageStatus.REFUSED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                            buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);

                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), 1).show();
                        }
                    });

                }
            }
        }).start();
    }

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
        Button agree;
		Button status;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}
	private void accept(String uidd){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("me",username ));
		list.add(new BasicNameValuePair("myFriendName", uidd));
		NetFactory.instance().commonHttpCilent(addhandler, context,
				Config.URL_GET_ADDFRIEND, list);
	}
	Handler addhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case ConstantsHandler.EXECUTE_SUCCESS:
					Map map = (Map) msg.obj;
					String authid=map.get("authId")+"";
					if(authid.endsWith("1")){
						Toast.makeText(context,"添加好友成功",Toast.LENGTH_SHORT).show();
						LoadFriendData();
					}else if(authid.endsWith("2")){
						Toast.makeText(context,"已经是好友关系",Toast.LENGTH_SHORT).show();
					}else if(authid.endsWith("3")){
						Toast.makeText(context,"添加失败，该用户不是环信用户",Toast.LENGTH_SHORT).show();
					}else if(authid.endsWith("4")){
						Toast.makeText(context,"没有此用户",Toast.LENGTH_SHORT).show();
					}
					break;
				case ConstantsHandler.EXECUTE_FAIL:
					break;
				case ConstantsHandler.ConnectTimeout:
//                    AlertMsgL("超时");
					break;
				default:
					break;
			}
		}

	};
	/**
	 * 好友列表
	 */
	public void LoadFriendData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", username));
		NetFactory.instance().commonHttpCilent(friendhandler, getContext(),
				Config.URL_FRIENDS, list);
	}
	/**
	 * 接口回调
	 */
	Handler friendhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.e("info","msg.what=111="+msg.what);
			switch (msg.what) {
				case ConstantsHandler.EXECUTE_SUCCESS:
					Map map = (Map) msg.obj;
					Log.e("info","map=好友="+map);
					setFriendData(map);
					//插库
					break;
				default:
					break;
			}
		}
	};
	protected void setFriendData(Map map) {
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
