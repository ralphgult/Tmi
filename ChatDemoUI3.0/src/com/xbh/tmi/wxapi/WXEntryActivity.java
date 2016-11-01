package com.xbh.tmi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbh.tmi.ui.LoginActivity;

/**
 * Created by Administrator on 2016/11/1.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

   private IWXAPI api;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      api = WXAPIFactory.createWXAPI(this, LoginActivity.APP_ID, true);
      api.handleIntent(getIntent(), this);
   }

   @Override
   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      setIntent(intent);
      api.handleIntent(intent, this);
   }

   //微信发送请求到你的应用
   @Override
   public void onReq(BaseReq req) {
   }

   //应用请求微信的响应结果将通过onResp回调
   @Override
   public void onResp(BaseResp resp) {
      if(resp.errCode==BaseResp.ErrCode.ERR_OK){//用户同意
         LoginActivity.resp = resp;
      }
      finish();
   }



}
