package tm.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.xbh.tmi.Constant;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
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
    private ImageLoaders loaders;
    //调用系统相册-选择图片
    private static final int CHANGEHEAD = 1;
    private static final int WALLFACE = 2;
    private String imagePath;
    private String[] pathList;
    private FaceWallAdapter mAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info", "map==" + map);
                    String authId = map.get("state") + "";
                    if (authId.equals("1")) {
                        try {
                            String str = "{\"faceScore\":" + map.get("faceScore") + "}";
                            JSONObject object = new JSONObject(str);
                            JSONArray array = object.getJSONArray("faceScore");
                            List<String> list = new ArrayList<>();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    object = array.getJSONObject(i);
                                    list.add(object.getString("url"));
                                }
                                if (list.size() < 8) {
                                    list.add("0");
                                }
                            } else {
                                list.add("0");
                            }
                            pathList = new String[list.size()];
                            for (int i = 0; i < list.size(); i++) {
                                pathList[i] = list.get(i);
                            }
                            mAdapter.resetData(pathList);
                            imgGri_gv.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantsHandler.EXECUTE_FAIL:
                    Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(CompCenterActivity.this,"上传企业Logo成功",Toast.LENGTH_SHORT).show();
                    headImage_iv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case 1002:
                    Toast.makeText(CompCenterActivity.this,"系统繁忙，请稍后再试...",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Handler handlerText = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text = null;
            switch (msg.arg1){
                case 0:
                    text = "名称";
                    if(msg.what == 1001) nameText_tv.setText((String)msg.obj);
                    break;
                case 1:
                    text = "说明";
                    if(msg.what == 1001) introText_tv.setText((String)msg.obj);
                    break;
            }
            if (msg.what == 1001) {
                Toast.makeText(CompCenterActivity.this, "修改企业" + text + "成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_center);
        personManager = new PersonManager();
        mAdapter = new FaceWallAdapter(this);
        loaders = new ImageLoaders(this,new imageListener());
        initViews();
    }

    class imageListener implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }
    private void initViews() {
        back_iv = (ImageView) findViewById(R.id.comp_center_back_iv);
        confirm_tv = (TextView) findViewById(R.id.comp_center_ok_tv);
        head_ly = (LinearLayout) findViewById(R.id.comp_center_head_rv);
        headImage_iv = (ImageView) findViewById(R.id.comp_center_head_iv);
        imgGri_gv = (GridView) findViewById(R.id.comp_center_pics_gv);
        name_rv = (RelativeLayout) findViewById(R.id.comp_center_name_rv);
        nameText_tv = (TextView) findViewById(R.id.comp_center_name_tv);
        nameText_tv.setText(getIntent().getExtras().getString("compname"));
        intro_rv = (RelativeLayout) findViewById(R.id.comp_center_intur_rv);
        introText_tv = (TextView) findViewById(R.id.comp_center_sign_tv);
        introText_tv.setText(getIntent().getExtras().getString("compinter"));
        qr_ly = (LinearLayout) findViewById(R.id.comp_center_qr_ly);
        back_iv.setOnClickListener(this);
        confirm_tv.setOnClickListener(this);
        head_ly.setOnClickListener(this);
        name_rv.setOnClickListener(this);
        intro_rv.setOnClickListener(this);
        qr_ly.setOnClickListener(this);
        String url = getIntent().getExtras().getString("comphead");
        if (!TextUtils.isEmpty(url)) {
            loaders.loadImage(headImage_iv,url);
        }
        pathList = new String[1];
        pathList[0] = "0";
        mAdapter.resetData(pathList);
        imgGri_gv.setAdapter(mAdapter);
        imgGri_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!pathList[position].equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", pathList[position]);
                    ViewUtil.jumpToOtherActivity(CompCenterActivity.this, HeadBigActivity.class);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    CompCenterActivity.this.startActivityForResult(intent, WALLFACE);
                }
            }
        });
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
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CHANGEHEAD);
                break;
            case R.id.comp_center_name_rv:
                createDialog("请输入企业名称");
                break;
            case R.id.comp_center_intur_rv:
                createDialog("请输入企业介绍");
                break;
            case R.id.comp_center_qr_ly:
                if(!new File(Constant.QRCODE_FILE_PATH).exists()){
                    try {
                        File file = new File(Constant.QRCODE_FILE_PATH);
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                            ImageLoaders.initFile();
                        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
            if (requestCode == CHANGEHEAD) {
                uploadPhotoThread thread = new uploadPhotoThread();
                thread.start();
            }else{
                //TODO 上传风采图片
                Toast.makeText(this, "正在调试中...", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class uploadPhotoThread extends Thread {
        @Override
        public void run() {
            SharedPreferences sharedPre = CompCenterActivity.this.getSharedPreferences("config", CompCenterActivity.this.MODE_PRIVATE);
            String userId = sharedPre.getString("username", "");
            PersonManager.SubmitPost(new File(imagePath), userId, mHandler);
        }
    }
    private void createDialog(String string){
        if(null == dialog){
            dialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            dialog.setEditTextHint(string);
            dialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    dialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    if(!TextUtils.isEmpty(inputText)){
                        switch(viewId){
                            case R.id.comp_center_name_rv:
                                new Thread(){
                                    @Override
                                    public void run() {
                                        personManager.updateTexts(inputText,personManager.TYPE_COMPNAME,handlerText);
                                    }
                                }.start();
                                break;
                            case R.id.comp_center_intur_rv:
                                new Thread(){
                                    @Override
                                    public void run() {
                                        personManager.updateTexts(inputText,personManager.TYPE_COMPINTOR,handlerText);
                                    }
                                }.start();
                                break;
                        }
                        dialog.closeDialog();
                    }else{
                        Toast.makeText(CompCenterActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        dialog.showDialog();
    }
}
