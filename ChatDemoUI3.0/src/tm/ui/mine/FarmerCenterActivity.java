package tm.ui.mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.ta.utdid2.android.utils.NetworkUtils;
import com.xbh.tmi.Constant;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.BaseActivity;

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
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.ImageUtil;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;
import tm.utils.dialog.RemindDialog;
import tm.widget.zxing.encoding.BitmapEncodUtil;

public class FarmerCenterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private LinearLayout logo;
    private TextView fengcai;
    private RelativeLayout name;
    private RelativeLayout sign;
    private TextView name_tv;
    private TextView intor_tv;
    private LinearLayout qr;
    private TextView ok;
    private RelativeLayout vedio_rv;
    private TextView vedioHave_tv;
    private ImageLoaders loaders;
    private int CHANGE_FACE_WALL = 0;
    private int CHANGE_HEAD = 1;
    private final int CHANGEHEAD_LOCAL = 1;
    private final int WALLFACE_LOCAL = 2;
    private final int CHANGEHEAD_CAMERA = 3;
    private final int WALLFACE_CAMERA = 4;
    private final int REQUESTCODE_VEDIO = 5;
    private String imagePath;
    private List<String> imgList;
    private List<Integer> idList;
    private FaceWallAdapter mAdapter;
    private GridView gv;
    private int mType;
    private String mVedioPath;
    private ProgressDialog pd;
    private RemindDialog mDialog;
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
                            gv.setAdapter(mAdapter);
                            SysUtils.setGridViewHight(gv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(FarmerCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1001:
                    Toast.makeText(FarmerCenterActivity.this, "上传三农Logo成功", Toast.LENGTH_SHORT).show();
                    logo_iv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case 2001:
                    TextView tv = null;
                    String text = null;
                    if (msg.arg1 == 2) {
                        tv = name_tv;
                        text = "名称";
                    } else {
                        tv = intor_tv;
                        text = "说明";
                    }
                    Toast.makeText(FarmerCenterActivity.this, "修改三农" + text + "成功", Toast.LENGTH_SHORT).show();
                    tv.setText((String) msg.obj);
                    break;
                case 3001:
                case 4001:
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    SharedPreferences sharedPre = FarmerCenterActivity.this.getSharedPreferences("config", FarmerCenterActivity.this.MODE_PRIVATE);
                    String userId = sharedPre.getString("username", "");
                    if (!TextUtils.isEmpty(userId)) {
                        list.add(new BasicNameValuePair("userId", userId));
                        list.add(new BasicNameValuePair("type", "3"));
                    } else {
                        Toast.makeText(FarmerCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NetFactory.instance().commonHttpCilent(mHandler, FarmerCenterActivity.this,
                            Config.URL_GET_USRE_FACEWALL, list);
                    break;
                case 6001:
                    Toast.makeText(FarmerCenterActivity.this, "视频上传完成", Toast.LENGTH_SHORT).show();
                case 5001:
                    if (null != pd && pd.isShowing()) {
                        pd.dismiss();
                    }
                    mVedioPath = (String) msg.obj;
                    Log.e("info","vedioPath ====== " + mVedioPath);
                    vedioHave_tv.setText(TextUtils.isEmpty(mVedioPath) ? "未上传" : "重新上传");
                    break;
                default:
                    Toast.makeText(FarmerCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private ImageView logo_iv;
    private PersonManager personManager;
    private InputDialog dialog;
    private CommonSelectImgPopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);
        imgList = new ArrayList<>();
        idList = new ArrayList<>();
        personManager = new PersonManager();
        loaders = new ImageLoaders(this, new imageLoaderListener());
        mAdapter = new FaceWallAdapter(this);
        mAdapter.setHandler(mHandler);
        back = (ImageView) findViewById(R.id.farmer_center_back_iv);
        logo = (LinearLayout) findViewById(R.id.farmer_center_head_rv);
        logo_iv = (ImageView) findViewById(R.id.farmer_center_head_iv);
        fengcai = (TextView) findViewById(R.id.farmer_center_fengcai_tv);
        name = (RelativeLayout) findViewById(R.id.farmer_center_name_text_rv);
        name_tv = (TextView) findViewById(R.id.farmer_center_name_tv);
        name_tv.setText(getIntent().getExtras().getString("farmname"));
        intor_tv = (TextView) findViewById(R.id.farmer_center_sign_tv);
        intor_tv.setText(getIntent().getExtras().getString("farminter"));
        sign = (RelativeLayout) findViewById(R.id.farmer_center_sign_rv);
        gv = (GridView) findViewById(R.id.farmer_center_pics_gv);
        vedio_rv = (RelativeLayout) findViewById(R.id.farmer_center_vedio_rv);
        vedioHave_tv = (TextView) findViewById(R.id.farmer_center_ishave_vedio_text);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!imgList.get(position).equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", imgList.get(position));
                    ViewUtil.jumpToOtherActivity(FarmerCenterActivity.this, HeadBigActivity.class, bundle);
                } else {
                    showPopupWindow(CHANGE_FACE_WALL);
                }
            }
        });
        qr = (LinearLayout) findViewById(R.id.farmer_center_qr_ly);
        ok = (TextView) findViewById(R.id.farmer_center_ok_tv);
        qr.setOnClickListener(this);
        sign.setOnClickListener(this);
        name.setOnClickListener(this);
        fengcai.setOnClickListener(this);
        logo.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        vedio_rv.setOnClickListener(this);
        String url = getIntent().getExtras().getString("farmhead");
        if (!TextUtils.isEmpty(url)) {
            loaders.loadImage(logo_iv, url);
        }
        new Thread() {
            @Override
            public void run() {
                PersonManager.getVedio(2, mHandler);
            }
        }.start();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = FarmerCenterActivity.this.getSharedPreferences("config", FarmerCenterActivity.this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("type", "3"));
        } else {
            Toast.makeText(FarmerCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }
        NetFactory.instance().commonHttpCilent(mHandler, FarmerCenterActivity.this,
                Config.URL_GET_USRE_FACEWALL, list);
    }
    class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.farmer_center_back_iv:
                this.finish();
                break;
            case R.id.farmer_center_ok_tv:
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.farmer_center_head_rv:
                showPopupWindow(CHANGE_HEAD);
                break;
            case R.id.farmer_center_name_text_rv:
                createDialog("请输入三农名称");
                mType = 2;
                break;
            case R.id.farmer_center_sign_rv:
                createDialog("请输入三农说明");
                mType = 3;
                break;
            case R.id.farmer_center_qr_ly:
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
            case R.id.farmer_center_vedio_rv:
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = new File(Constant.FARM_VEDIO_PATH);
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                startActivityForResult(intent, REQUESTCODE_VEDIO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                if (requestCode == REQUESTCODE_VEDIO) {
                pd = ProgressDialog.show(this, "上传", "视频上传中，请稍后...");
                if(!NetworkUtils.isWifi(this)){
                    createReminDialog();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        PersonManager.addVedio(new File(Constant.COMP_VEDIO_PATH), 2, mHandler);
                    }
                }.start();
                return;
            }
                if (requestCode == CHANGEHEAD_LOCAL || requestCode == WALLFACE_LOCAL) {
                    //选择本地图片
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    imagePath = c.getString(columnIndex);
                    c.close();
                } else if (requestCode == CHANGEHEAD_CAMERA || requestCode == WALLFACE_CAMERA) {
                    //照相返回
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = System.currentTimeMillis() + ".jpg";
                    imagePath = "/mnt/sdcard/ImageLoader/cache/imageslarge/" + name;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImageUtil.saveBitmap(bitmap, imagePath);
                }

                if (requestCode == CHANGEHEAD_LOCAL || requestCode == CHANGEHEAD_CAMERA) {
                    uploadPhotoThread thread = new uploadPhotoThread();
                    thread.start();
                } else if (requestCode == WALLFACE_LOCAL || requestCode == WALLFACE_CAMERA) {
                    new Thread() {
                        @Override
                        public void run() {
                            PersonManager.uploadImgwall(imagePath, 3, mHandler);
                        }
                    }.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class uploadPhotoThread extends Thread {
        @Override
        public void run() {
            SharedPreferences sharedPre = FarmerCenterActivity.this.getSharedPreferences("config", FarmerCenterActivity.this.MODE_PRIVATE);
            String userId = sharedPre.getString("username", "");
            PersonManager.SubmitPost(new File(imagePath), userId, 3, mHandler);
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
                            case 2:
                                new Thread() {
                                    @Override
                                    public void run() {
                                        PersonManager.updateTexts(inputText, personManager.TYPE_FARMNAME, mHandler);
                                    }
                                }.start();
                                break;
                            case 3:
                                new Thread() {
                                    @Override
                                    public void run() {
                                        PersonManager.updateTexts(inputText, personManager.TYPE_FARMINTOR, mHandler);
                                    }
                                }.start();
                                break;
                        }
                        dialog.closeDialog();
                    } else {
                        Toast.makeText(FarmerCenterActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        dialog.showDialog();
    }
    private void showPopupWindow(final int type) {
        if (null == mPopupWindow) {
            mPopupWindow = new CommonSelectImgPopupWindow(this);
            WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            mPopupWindow.setWidth(wm.getDefaultDisplay().getWidth());
        }
        mPopupWindow.mOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.yx_common_add_img_pupwindow_camera_tv:
                        //照相
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        FarmerCenterActivity.this.startActivityForResult(intent, type == CHANGE_FACE_WALL ? WALLFACE_CAMERA : CHANGEHEAD_CAMERA);
                        break;
                    case R.id.yx_common_add_img_pupwindow_local_tv:
                        //相册
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        FarmerCenterActivity.this.startActivityForResult(intent, type == CHANGE_FACE_WALL ? WALLFACE_LOCAL : CHANGEHEAD_LOCAL);
                        break;
                }
            }
        };
        mPopupWindow.showAtBOTTOM(LayoutInflater.from(this).inflate(R.layout.activity_farmer_center, null));
    }
    private void createReminDialog(){
        if (null == mDialog) {
            mDialog = (RemindDialog) DialogFactory.createDialog(this,DialogFactory.DIALOG_TYPE_REMIND);
            mDialog.setGroupName("您正在使用流量上传，是否继续?");
            mDialog.setPhotoDialogListener(new RemindDialog.RemindDialogListener() {
                @Override
                public void invita() {
                    mDialog.closeDialog();
                }

                @Override
                public void remind() {
                    new Thread() {
                        @Override
                        public void run() {
                            PersonManager.addVedio(new File(Constant.COMP_VEDIO_PATH), 2, mHandler);
                        }
                    }.start();
                    mDialog.closeDialog();
                }
            });
        }
        mDialog.showDialog();
    }
}
