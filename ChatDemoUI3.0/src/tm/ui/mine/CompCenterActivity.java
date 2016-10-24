package tm.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.hyphenate.tmdemo.Constant;
import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;

import java.io.File;

import tm.manager.PersonManager;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;
import tm.widget.zxing.encoding.BitmapEncodUtil;

public class CompCenterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back_iv;
    private TextView confirm_tv;
    private LinearLayout head_ly;
    private ImageView headImage_iv;
    private GridView imgGri_gv;
    private RelativeLayout name_rv;
    private TextView nameText_tv;
    private RelativeLayout intro_rv;
    private TextView introText_tv;
    private LinearLayout qr_ly;
    private InputDialog dialog;
    int viewId = 0;
    private PersonManager personManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_center);
        personManager = new PersonManager();
        initViews();
    }

    private void initViews() {
        back_iv = (ImageView) findViewById(R.id.comp_center_back_iv);
        confirm_tv = (TextView) findViewById(R.id.comp_center_ok_tv);
        head_ly = (LinearLayout) findViewById(R.id.comp_center_head_rv);
        headImage_iv = (ImageView) findViewById(R.id.comp_center_head_iv);
        imgGri_gv = (GridView) findViewById(R.id.comp_center_pics_gv);
        name_rv = (RelativeLayout) findViewById(R.id.comp_center_name_rv);
        nameText_tv = (TextView) findViewById(R.id.comp_center_name_tv);
        intro_rv = (RelativeLayout) findViewById(R.id.comp_center_intur_rv);
        introText_tv = (TextView) findViewById(R.id.comp_center_sign_tv);
        qr_ly = (LinearLayout) findViewById(R.id.comp_center_qr_ly);
        back_iv.setOnClickListener(this);
        confirm_tv.setOnClickListener(this);
        head_ly.setOnClickListener(this);
        name_rv.setOnClickListener(this);
        intro_rv.setOnClickListener(this);
        qr_ly.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        viewId = v.getId();
        switch (v.getId()){
            case R.id.comp_center_back_iv:
                this.finish();
                break;
            case R.id.comp_center_ok_tv:
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.comp_center_head_rv:
                Toast.makeText(this,"正在开发中...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.comp_center_name_rv:
            case R.id.comp_center_intur_rv:
                createDialog();
                break;
            case R.id.comp_center_qr_ly:
                if(!new File(Constant.QRCODE_FILE_PATH).exists()){
                    try {
                        SharedPreferences sharedPre=this.getSharedPreferences("config", this.MODE_PRIVATE);
                        String userId = sharedPre.getString("username","");
                        BitmapEncodUtil.createQRCode(userId,500);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("path","");
                bundle.putString("filePath", Constant.QRCODE_FILE_PATH);
                ViewUtil.jumpToOtherActivity(this,HeadBigActivity.class,bundle);
                break;
        }

    }
    private void createDialog(){
        if(null == dialog){
            dialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            dialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    dialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    switch(viewId){
                        case R.id.comp_center_name_rv:
                            nameText_tv.setText(inputText);
                            new Thread(){
                                @Override
                                public void run() {
                                    personManager.updateTexts(inputText,personManager.TYPE_COMPNAME);
                                }
                            }.start();
                            break;
                        case R.id.comp_center_intur_rv:
                            introText_tv.setText(inputText);
                            new Thread(){
                                @Override
                                public void run() {
                                    personManager.updateTexts(inputText,personManager.TYPE_COMPINTOR);
                                }
                            }.start();
                            break;
                    }

                    dialog.closeDialog();
                }
            });
        }
        dialog.showDialog();
    }
}
