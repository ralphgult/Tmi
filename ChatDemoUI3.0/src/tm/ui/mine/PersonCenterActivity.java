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

import com.oohla.android.utils.NetworkUtil;
import com.ta.utdid2.android.utils.SystemUtils;
import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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


public class PersonCenterActivity extends Activity implements View.OnClickListener {
    private ImageView back;
    private LinearLayout head;
    private TextView yanzhi;
    private RelativeLayout sign;
    private TextView ok;
    private FaceWallAdapter mAdapter;
    private String[] pathList;
    private ImageLoaders imageLoaders;
    private ImageView head_iv;
    private TextView sign_tv;
    private GridView gv;
    //调用系统相册-选择图片
    private static final int CHANGEHEAD = 1;
    private static final int WALLFACE = 2;
    private InputDialog signDialog;
    private List<String> imgPath;
    private List<Integer> idList;
    private Map<String, String> mData;
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
                            imgPath.clear();
                            idList.clear();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    object = array.getJSONObject(i);
                                    imgPath.add(object.getString("url"));
                                    idList.add(object.getInt("fsId"));
                                }
                                if (imgPath.size() < 8) {
                                    imgPath.add("0");
                                }
                            } else {
                                imgPath.add("0");
                            }
                            mAdapter.resetData(imgPath,idList);
                            gv.setAdapter(mAdapter);
                            SysUtils.setGridViewHight(gv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantsHandler.EXECUTE_FAIL:
                    Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(PersonCenterActivity.this,"上传头像成功",Toast.LENGTH_SHORT).show();
                    head_iv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case 1002:
                    Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                case 3001:
                    List<NameValuePair> list2 = new ArrayList<NameValuePair>();
                    SharedPreferences sharedPre2 = PersonCenterActivity.this.getSharedPreferences("config", PersonCenterActivity.this.MODE_PRIVATE);
                    String userId2 = sharedPre2.getString("username", "");
                    if (!TextUtils.isEmpty(userId2)) {
                        list2.add(new BasicNameValuePair("userId", userId2));
                        list2.add(new BasicNameValuePair("type", "1"));
                    } else {
                        Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NetFactory.instance().commonHttpCilent(mHandler, PersonCenterActivity.this,
                            Config.URL_GET_USRE_FACEWALL, list2);
                    break;
                default:
                    Toast.makeText(PersonCenterActivity.this, "颜值图片上传失败，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initView();
        setData();
    }

    private void setData() {
        mData = new HashMap<>();
        imgPath = new ArrayList<>();
        idList = new ArrayList<>();
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        imageLoaders.loadImage(head_iv, getIntent().getExtras().getString("headPath"));
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("signed"))) {
            sign_tv.setText(getIntent().getExtras().getString("signed"));
        }
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("type", "1"));
        } else {
            Toast.makeText(this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }
        NetFactory.instance().commonHttpCilent(mHandler, this,
                Config.URL_GET_USRE_FACEWALL, list);
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.person_center_back_iv);
        head = (LinearLayout) findViewById(R.id.person_center_head_rv);
        yanzhi = (TextView) findViewById(R.id.person_center_yanzhi_tv);
        sign = (RelativeLayout) findViewById(R.id.person_center_sign_text_rv);
        sign_tv = (TextView) findViewById(R.id.person_center_sign_text_tv);
        ok = (TextView) findViewById(R.id.person_center_ok_tv);
        head_iv = (ImageView) findViewById(R.id.person_center_head_iv);
        gv = (GridView) findViewById(R.id.person_center_pics_gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!imgPath.get(position).equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", imgPath.get(position));
                    ViewUtil.jumpToOtherActivity(PersonCenterActivity.this, HeadBigActivity.class,bundle);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    PersonCenterActivity.this.startActivityForResult(intent, WALLFACE);
                }
            }
        });
        mAdapter = new FaceWallAdapter(this);
        mAdapter.setHandler(mHandler);
        ok.setOnClickListener(this);
        head.setOnClickListener(this);
        yanzhi.setOnClickListener(this);
        sign.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_center_back_iv:
                PersonCenterActivity.this.finish();
                break;
            case R.id.person_center_ok_tv:
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.person_center_sign_text_rv:
                createSignDialog();
                break;
            case R.id.person_center_head_rv:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CHANGEHEAD);
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
               new Thread(){
                   @Override
                   public void run() {
                       PersonManager.uploadImgwall(imagePath,1,mHandler);
                   }
               }.start();
            }
        }
    }

    class uploadPhotoThread extends Thread {
        @Override
        public void run() {
            SharedPreferences sharedPre = PersonCenterActivity.this.getSharedPreferences("config", PersonCenterActivity.this.MODE_PRIVATE);
            String userId = sharedPre.getString("username", "");
            PersonManager.SubmitPost(new File(imagePath), userId, 1,mHandler);
        }
    }

    private void createSignDialog() {
        if (null == signDialog) {
            signDialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            signDialog.setEditTextHint("请输入个性签名");
            signDialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    signDialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    if (!TextUtils.isEmpty(inputText)) {
                        if (!NetworkUtil.isNetworkAvailable(PersonCenterActivity.this)) {
                            Toast.makeText(PersonCenterActivity.this, "网络链接异常，请检查网络连接后重试...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        SharedPreferences sharedPre = PersonCenterActivity.this.getSharedPreferences("config", PersonCenterActivity.this.MODE_PRIVATE);
                        String userId = sharedPre.getString("username", "");
                        if (!TextUtils.isEmpty(userId)) {
                            list.add(new BasicNameValuePair("userId", userId));
                            list.add(new BasicNameValuePair("caption", inputText));
                        } else {
                            Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        NetFactory
                                .instance()
                                .commonHttpCilent(
                                        new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                switch (msg.what) {
                                                    case ConstantsHandler.EXECUTE_SUCCESS:
                                                        Map map = (Map) msg.obj;
                                                        Log.e("info", "map==" + map);
                                                        String authId = map.get("authId") + "";
                                                        if (authId.equals("1")) {
                                                            signDialog.closeDialog();
                                                            sign_tv.setText(inputText);
                                                        } else {
                                                            Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                                                        }
                                                        break;
                                                    case ConstantsHandler.EXECUTE_FAIL:
                                                    case ConstantsHandler.ConnectTimeout:
                                                        Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                            }
                                        }, PersonCenterActivity.this,
                                        Config.URL_CHANGE_SIGN, list);
                    }else{
                        Toast.makeText(PersonCenterActivity.this, "个性签名不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        signDialog.showDialog();
    }

    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }
}
