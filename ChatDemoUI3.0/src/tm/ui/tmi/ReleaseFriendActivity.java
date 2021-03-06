package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.ImageUtils;
import com.oohla.android.utils.StringUtil;
import com.xbh.tmi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tm.manager.PersonManager;
import tm.ui.mine.CommonSelectImgPopupWindow;
import tm.ui.tmi.adapter.ImageAdapter;
import tm.utils.ImageUtil;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ReleaseFriendActivity extends Activity implements View.OnClickListener {
    private int mType = 0;
    private ImageView mBackImg;
    private TextView mTitleTxt;
    private TextView mReleaseTxt;
    private EditText mEditText;
    private GridView mImgGridView;
    private ImageAdapter mAdapter;
    private String[] imgpaths;
    private List<String> imgPathList;
    private String content;
    private CommonSelectImgPopupWindow mPopupWindow;
    private int LOCAL_IMG_REQUEST_CODE = 1;
    private int CAMERA_REQUEST_CODE = 2;
    private String imagePath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                String text = null;
                if (mType == 6) {
                    text = "发布失物招领信息成功";
                } else if (mType == 4) {
                    text = "发布随笔成功";
                } else {
                    text = "发布资讯成功";
                }
                Toast.makeText(ReleaseFriendActivity.this, text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("publishFinish", 1);
                ViewUtil.backToActivityForResult(ReleaseFriendActivity.this, 1, intent);
            } else {
                Toast.makeText(ReleaseFriendActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_friend);
        initData();
        initView();

    }

    private void initData() {
        mAdapter = new ImageAdapter(this, true);
        imgPathList = new ArrayList<>();
        imgPathList.add("0");
        imgpaths = new String[6];
        for (int i = 0; i < imgPathList.size(); i++) {
            imgpaths[i] = imgPathList.get(i);
        }
        mAdapter.resetData(imgpaths);
    }

    public void setAdapterData(String imgPath) {
        if (imgPathList.size() < 6) {
            imgPathList.add(imgPathList.size() - 1, imgPath);
        } else {
            imgPathList.remove(imgPathList.size() - 1);
            imgPathList.add(imgPath);
        }
        imgpaths = new String[6];
        for (int i = 0; i < imgPathList.size(); i++) {
            imgpaths[i] = imgPathList.get(i);
        }
        mAdapter.resetData(imgpaths);
        SysUtils.setGridViewHight(mImgGridView);
    }

    public void delAdapterData(String imgPath) {
        imgPathList.remove(imgPath);
        if (imgPathList.size() == 6 && !imgPathList.contains("0")) {
            imgPathList.add("0");
        }
        imgpaths = new String[6];
        for (int i = 0; i < imgPathList.size(); i++) {
            imgpaths[i] = imgPathList.get(i);
        }
        mAdapter.resetData(imgpaths);
        SysUtils.setGridViewHight(mImgGridView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_back_iv://返回按钮
                ViewUtil.backToOtherActivity(ReleaseFriendActivity.this);
                break;
            case R.id.release_center_ok_tv://发表按钮
                //输入框内容获取
                content = mEditText.getText().toString();
                if (TextUtils.isEmpty(content) && (null == imgPathList || imgPathList.size() == 1)) {
                    Toast.makeText(this, "请输入内容！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //不同的接口的调用
                Thread thread = null;
                if (mType == 6) {
                    //添加失物招领信息
                    thread = new AddLostThread();
                } else {
                    //添加资讯/朋友圈
                    thread = new AddMomentThread();
                }
                thread.start();
                break;
        }
    }

    private void initView() {
        mBackImg = (ImageView) findViewById(R.id.release_back_iv);
        mTitleTxt = (TextView) findViewById(R.id.release_title_text);
        mReleaseTxt = (TextView) findViewById(R.id.release_center_ok_tv);
        mEditText = (EditText) findViewById(R.id.release_edt);
        mImgGridView = (GridView) findViewById(R.id.release_add_img_gridview);
        mType = getIntent().getExtras().getInt("ReleaseType");
        mImgGridView.setAdapter(mAdapter);
        SysUtils.setGridViewHight(mImgGridView);
        switch (mType) {
            case 1:
                mTitleTxt.setText("发布个人资讯");
                break;
            case 2:
                mTitleTxt.setText("发布企业资讯");
                break;
            case 3:
                mTitleTxt.setText("发布三农资讯");
                break;
            case 4:
                mTitleTxt.setText("发布朋友圈");
                break;
            case 6:
                mTitleTxt.setText("发布招领信息");
                break;
        }
        mBackImg.setOnClickListener(this);
        mReleaseTxt.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //获取图片路径
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == LOCAL_IMG_REQUEST_CODE) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                setAdapterData(c.getString(columnIndex));
                c.close();
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                //照相返回
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                imagePath = "/mnt/sdcard/ImageLoader/cache/images" + System.currentTimeMillis() + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
                try {
                    ImageUtil.saveBitmap(bitmap, imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPathList.remove("0");
                imgPathList.add(imagePath);
                if (imgPathList.size() < 6) {
                    imgPathList.add("0");
                }
                imgpaths = new String[6];
                for (int i = 0; i < imgPathList.size(); i++) {
                    imgpaths[i] = imgPathList.get(i);
                }
                mAdapter.resetData(imgpaths);
            }
        }
    }

    class AddMomentThread extends Thread {
        @Override
        public void run() {
            if (imgPathList.contains("0")) {
                imgPathList.remove("0");
            }
            PersonManager.publishMoment(content, imgPathList, mType, handler);

        }
    }

    class AddLostThread extends Thread {
        @Override
        public void run() {
            if (imgPathList.contains("0")) {
                imgPathList.remove("0");
            }
            PersonManager.publishLostInfo(content, imgPathList, handler);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showPopupWindow() {
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
                        ReleaseFriendActivity.this.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        break;
                    case R.id.yx_common_add_img_pupwindow_local_tv:
                        //相册
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ReleaseFriendActivity.this.startActivityForResult(intent, LOCAL_IMG_REQUEST_CODE);
                        break;
                }
            }
        };
        mPopupWindow.showAtBOTTOM(LayoutInflater.from(this).inflate(R.layout.activity_comp_center, null));
    }

}
