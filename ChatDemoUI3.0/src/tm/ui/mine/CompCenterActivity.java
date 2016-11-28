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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.xbh.tmi.Constant;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.manager.PersonManager;
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.SysUtils;
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
    private PersonManager personManager;
    private ImageLoaders loaders;
    //调用系统相册-选择图片
    private static final int CHANGEHEAD = 1;
    private static final int WALLFACE = 2;
    private String imagePath;
    private FaceWallAdapter mAdapter;
    private int mType;
    private List<String> imgList;
    private List<Integer> idList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info", "map==" + map);
                    String authId = map.get("state") + "";
                    if (authId.equals("1")) {
                        try {
                            String str = "{\"faceScore\":" + map.get("faceScore") + "}";
                            JSONObject object = new JSONObject(str);
                            JSONArray array = object.getJSONArray("faceScore");
                            imgList.clear();
                            idList.clear();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    object = array.getJSONObject(i);
                                    imgList.add(object.getString("url"));
                                    idList.add(object.getInt("fsId"));
                                }
                                if (imgList.size() < 8) {
                                    imgList.add("0");
                                }
                            } else {
                                imgList.add("0");
                            }
                            mAdapter.resetData(imgList, idList);
                            imgGri_gv.setAdapter(mAdapter);
                            SysUtils.setGridViewHight(imgGri_gv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1001:
                    Toast.makeText(CompCenterActivity.this, "上传企业Logo成功", Toast.LENGTH_SHORT).show();
                    headImage_iv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case 2001:
                    TextView tv = null;
                    String text = null;
                    if (msg.arg1 == 0) {
                        tv = nameText_tv;
                        text = "名称";
                    } else {
                        tv = introText_tv;
                        text = "说明";
                    }
                    Toast.makeText(CompCenterActivity.this, "修改企业" + text + "成功", Toast.LENGTH_SHORT).show();
                    tv.setText((String) msg.obj);
                    break;
                case 2002:
                    Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
                case 3001:
                case 4001:
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    SharedPreferences sharedPre = CompCenterActivity.this.getSharedPreferences("config", CompCenterActivity.this.MODE_PRIVATE);
                    String userId = sharedPre.getString("username", "");
                    if (!TextUtils.isEmpty(userId)) {
                        list.add(new BasicNameValuePair("userId", userId));
                        list.add(new BasicNameValuePair("type", "2"));
                    } else {
                        Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NetFactory.instance().commonHttpCilent(mHandler, CompCenterActivity.this,
                            Config.URL_GET_USRE_FACEWALL, list);
                    break;
                default:
                    Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_center);
        init();
        initViews();
    }

    private void init() {
        imgList = new ArrayList<>();
        idList = new ArrayList<>();
        personManager = new PersonManager();
        mAdapter = new FaceWallAdapter(this);
        mAdapter.setHandler(mHandler);
        loaders = new ImageLoaders(this, new imageListener());
    }

    class imageListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
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
            loaders.loadImage(headImage_iv, url);
        }
        imgGri_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!imgList.get(position).equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", imgList.get(position));
                    ViewUtil.jumpToOtherActivity(CompCenterActivity.this, HeadBigActivity.class,bundle);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    CompCenterActivity.this.startActivityForResult(intent, WALLFACE);
                }
            }
        });
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = CompCenterActivity.this.getSharedPreferences("config", CompCenterActivity.this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("type", "2"));
        } else {
            Toast.makeText(CompCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }
        NetFactory.instance().commonHttpCilent(mHandler, CompCenterActivity.this,
                Config.URL_GET_USRE_FACEWALL, list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comp_center_back_iv:
                this.finish();
                break;
            case R.id.comp_center_ok_tv:
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.comp_center_head_rv:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CHANGEHEAD);
                break;
            case R.id.comp_center_name_rv:
                createDialog("请输入企业名称");
                mType = 0;
                break;
            case R.id.comp_center_intur_rv:
                createDialog("请输入企业介绍");
                mType = 1;
                break;
            case R.id.comp_center_qr_ly:
                if (!new File(Constant.QRCODE_FILE_PATH).exists()) {
                    try {
                        File file = new File(Constant.QRCODE_FILE_PATH);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                            ImageLoaders.initFile();
                        }
                        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
                        String userId = sharedPre.getString("username", "");
                        BitmapEncodUtil.createQRCode(userId, 500);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("path", "");
                bundle.putString("filePath", Constant.QRCODE_FILE_PATH);
                ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle);
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
            } else {
                new Thread(){
                    @Override
                    public void run() {
                        PersonManager.uploadImgwall(imagePath,2,mHandler);
                    }
                }.start();
            }
        }
    }

    class uploadPhotoThread extends Thread {
        @Override
        public void run() {
            SharedPreferences sharedPre = CompCenterActivity.this.getSharedPreferences("config", CompCenterActivity.this.MODE_PRIVATE);
            String userId = sharedPre.getString("username", "");
            PersonManager.SubmitPost(new File(imagePath), userId, 2, mHandler);
        }
    }

    private void createDialog(String string) {
        if (null == dialog) {
            dialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            dialog.setEditTextHint(string);
            dialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    dialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    if (!TextUtils.isEmpty(inputText)) {
                        switch (mType) {
                            case 0:
                                new Thread() {
                                    @Override
                                    public void run() {
                                        personManager.updateTexts(inputText, personManager.TYPE_COMPNAME, mHandler);
                                    }
                                }.start();
                                break;
                            case 1:
                                new Thread() {
                                    @Override
                                    public void run() {
                                        personManager.updateTexts(inputText, personManager.TYPE_COMPINTOR, mHandler);
                                    }
                                }.start();
                                break;
                        }
                        dialog.closeDialog();
                    } else {
                        Toast.makeText(CompCenterActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        dialog.showDialog();
    }
}
