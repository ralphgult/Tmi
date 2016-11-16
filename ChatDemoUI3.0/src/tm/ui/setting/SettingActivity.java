package tm.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.util.OtherUtils;
import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.LoginActivity;

import tm.ui.login.PwdFindActivity;
import tm.utils.ViewTools;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.RemindDialog;


public class SettingActivity extends Activity implements View.OnClickListener{
    private ImageView back_btn;
    private LinearLayout logout_btn;
    private LinearLayout setMessage_btn;
    private LinearLayout cleanMsg_btn;
    private LinearLayout modifyPwd_btn;
    private LinearLayout cleanCache_btn;
    private LinearLayout feedback_btn;
    private LinearLayout about_btn;
    private RemindDialog cleanMsgDialog;
    private RemindDialog cleanCacheDialog;
    private RemindDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApplication.getInstance().addActivity(this);
        setContentView(R.layout.tm_mine_setting_layout);
        initView();
    }
    private void initView(){
        back_btn = (ImageView) findViewById(R.id.setting_back_iv);
        logout_btn = (LinearLayout) findViewById(R.id.setting_btn_logout);
        setMessage_btn = (LinearLayout) findViewById(R.id.setting_btn_msgsetting);
        cleanMsg_btn = (LinearLayout) findViewById(R.id.setting_btn_cleanmsg);
        modifyPwd_btn = (LinearLayout) findViewById(R.id.setting_btn_modifypwd);
        cleanCache_btn = (LinearLayout) findViewById(R.id.setting_btn_cleancache);
        feedback_btn = (LinearLayout) findViewById(R.id.setting_btn_feedback);
        about_btn = (LinearLayout) findViewById(R.id.setting_btn_about);
        back_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        setMessage_btn.setOnClickListener(this);
        modifyPwd_btn.setOnClickListener(this);
        cleanCache_btn.setOnClickListener(this);
        cleanMsg_btn.setOnClickListener(this);
        feedback_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setting_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.setting_btn_logout:
                createLayoutDialog();
                break;
            case R.id.setting_btn_msgsetting:
                ViewUtil.jumpToOtherActivity(this,SetMessageActivity.class);
                break;
            case R.id.setting_btn_cleanmsg:
                createCleanMsgDialog();
                break;
            case R.id.setting_btn_cleancache:
                createCleanCacheDialog();
                break;
            case R.id.setting_btn_modifypwd:
                Bundle bundle=new Bundle();
                bundle.putString("name", "修改密码");
                ViewUtil.jumpToOtherActivity(this, PwdFindActivity.class);
                break;
            case R.id.setting_btn_feedback:
                ViewUtil.jumpToOtherActivity(this,FeedbackActivity.class);
                break;
            case R.id.setting_btn_about:
                ViewUtil.jumpToOtherActivity(this,AboutAcitivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ViewUtil.backToOtherActivity(this);
        }
        return true;
    }
    public void createLayoutDialog(){
        if(null == alertDialog){
            alertDialog = (RemindDialog) DialogFactory.createDialog(this,DialogFactory.DIALOG_TYPE_REMIND);
            alertDialog.setGroupName("是否退出登录");
            alertDialog.setPhotoDialogListener(new RemindDialog.RemindDialogListener() {
                @Override
                public void invita() {
                    alertDialog.closeDialog();
                }

                @Override
                public void remind() {
                    EMClient.getInstance().logout(false);
                    ViewUtil.backToOtherActivity(SettingActivity.this,LoginActivity.class);
                    DemoApplication.getInstance().exit();
                    alertDialog.closeDialog();
                }
            });
        }
        alertDialog.showDialog();
    }
    public void createCleanMsgDialog(){
        if(null == cleanMsgDialog){
            cleanMsgDialog = (RemindDialog) DialogFactory.createDialog(this,DialogFactory.DIALOG_TYPE_REMIND);
            cleanMsgDialog.setGroupName("是否清除所有聊天消息");
            cleanMsgDialog.setPhotoDialogListener(new RemindDialog.RemindDialogListener() {
                @Override
                public void invita() {
                    cleanMsgDialog.closeDialog();
                }

                @Override
                public void remind() {
                    //TODO 清除聊天信息
                    Toast.makeText(SettingActivity.this, "聊天消息已清除", Toast.LENGTH_SHORT).show();
                    cleanMsgDialog.closeDialog();
                }
            });
        }
        cleanMsgDialog.showDialog();
    }
    public void createCleanCacheDialog(){
        if(null == cleanCacheDialog){
            cleanCacheDialog = (RemindDialog) DialogFactory.createDialog(this,DialogFactory.DIALOG_TYPE_REMIND);
            cleanCacheDialog.setGroupName("是否清除应用缓存");
            cleanCacheDialog.setPhotoDialogListener(new RemindDialog.RemindDialogListener() {
                @Override
                public void invita() {
                    cleanCacheDialog.closeDialog();
                }

                @Override
                public void remind() {
                    //TODO 清除应用缓存
                    Toast.makeText(SettingActivity.this, "应用缓存已清除", Toast.LENGTH_SHORT).show();
                    cleanCacheDialog.closeDialog();
                }
            });
        }
        cleanCacheDialog.showDialog();
    }
}
