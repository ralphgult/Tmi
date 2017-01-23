package tm.ui.mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oohla.android.utils.NetworkUtil;
import com.xbh.tmi.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tm.manager.PersonManager;
import tm.ui.tmi.adapter.ImageAdapter;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;

/**
 * Created by Lking on 2017/1/23.
 */

public class UploadAuctionActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener{
    private ImageView mBack;
    private TextView mConfirm;
    private EditText mNameEdt;
    private EditText mPhoneEdt;
    private EditText mPristartEdt;
    private EditText mPribuyEdt;
    private EditText mTimeEdt;
    private EditText mIntorEdt;
    private GridView mImageGv;

    private ImageAdapter mAdapter;
    private List<String> imgList;
    private String[] mImageDatas;
    private CommonSelectImgPopupWindow mPopupWindow;
    private String imagePath;
    private Handler mHandler;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_upload_activity);
        //看接口写网络请求，有好几个不显示，但是要上传的，还有上传图片是上传多张的（1-5张）
        init();
        initViews();
        initData();
    }

    private void initData() {
        mImageDatas[0] = "0";
        mAdapter.resetData(mImageDatas);
        mImageGv.setAdapter(mAdapter);
        SysUtils.setGridViewHight(mImageGv);
    }

    private void init() {
        imgList = new ArrayList<>();
        mImageDatas = new String[5];
        mAdapter = new ImageAdapter(this, true);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                pd.dismiss();
                switch (msg.what){
                    case 1001:
                        Toast.makeText(UploadAuctionActivity.this, "商品已添加", Toast.LENGTH_SHORT).show();
                        ViewUtil.backToOtherActivity(UploadAuctionActivity.this);
                        break;
                    case 1002:
                        Toast.makeText(UploadAuctionActivity.this, "系统繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initViews() {
        mBack = (ImageView) findViewById(R.id.auction_upload_back_iv);
        mConfirm = (TextView) findViewById(R.id.auction_upload_ok_tv);
        mNameEdt = (EditText) findViewById(R.id.auction_name_edt);
        mPhoneEdt = (EditText) findViewById(R.id.auction_phone_edt);
        mPristartEdt = (EditText) findViewById(R.id.auction_pristart_edt);
        mPribuyEdt = (EditText) findViewById(R.id.auction_pribuy_edt);
        mTimeEdt = (EditText) findViewById(R.id.auction_time_edt);
        mIntorEdt = (EditText) findViewById(R.id.auction_intor_edt);
        mImageGv = (GridView) findViewById(R.id.auction_img_gv);
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mImageGv.setOnItemClickListener(this);
    }


    public void setAdapterData(String imgPath) {
        imgList.add(imgPath);
        int size = imgList.size();
        for (int i = 0; i < size; i++) {
            mImageDatas[i] = imgList.get(i);
        }
        if (imgList.size() < 5) {
            mImageDatas[imgList.size()] = "0";
        }
        mAdapter.resetData(mImageDatas);
        SysUtils.setGridViewHight(mImageGv);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            ViewUtil.backToOtherActivity(this);
        } else if (v instanceof TextView){
            doAddAuction();
        }
    }

    private void doAddAuction(){
        final String name = mNameEdt.getText().toString().trim();
        final String phone = mPhoneEdt.getText().toString().trim();
        final String startPri = mPristartEdt.getText().toString().trim();
        final String buyPri = mPribuyEdt.getText().toString().trim();
        final String time = mTimeEdt.getText().toString().trim();
        final String intor = mIntorEdt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入商品名称", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.length() != 11) {
            Toast.makeText(this, "请输入正确的联系电话", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(startPri)) {
            Toast.makeText(this, "请输入商品起拍价格", Toast.LENGTH_SHORT).show();
            return;
        } else if (!checkIsPrice(startPri)){
            Toast.makeText(this, "请输入正确的价格", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(buyPri)) {
            Toast.makeText(this, "请输入商品直购价格", Toast.LENGTH_SHORT).show();
            return;
        }else if (!checkIsPrice(buyPri)){
            Toast.makeText(this, "请输入正确的价格", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "请输入剩余时间", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(intor)) {
            Toast.makeText(this, "请输入商品简介", Toast.LENGTH_SHORT).show();
            return;
        }else  if(!NetworkUtil.isNetworkAvailable(this)){
            Toast.makeText(this, "无网络连接，请检查网络简介情况后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                PersonManager.addAuction(name,startPri,phone,buyPri,intor,imgList,Integer.valueOf(time),mHandler);
                pd = ProgressDialog.show(UploadAuctionActivity.this,"注册","注册中，请稍后...");
            }
        }.start();
    }

    private boolean checkIsPrice(String pri){
        String str = "^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$";
        Pattern pattern = Pattern.compile(str);
        Matcher isPrice = pattern.matcher(pri);
        return isPrice.matches();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!imgList.get(position).equals("0")) {
            Bundle bundle = new Bundle();
            bundle.putString("path", imgList.get(position));
            ViewUtil.jumpToOtherActivity(this, HeadBigActivity.class, bundle);
        } else {
            showPopupWindow();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //获取图片路径
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imagePath = c.getString(columnIndex);
                c.close();
            } else if (requestCode == 1) {
                //照相返回
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            setAdapterData(imagePath);

        }
    }

    private void showPopupWindow() {
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
                        imagePath = "/mnt/sdcard/ImageLoader/cache/imageslarge/" + System.currentTimeMillis() + ".jpg";
                        File path1 = new File(imagePath).getParentFile();
                        if (!path1.exists()) {
                            path1.mkdirs();
                        }
                        File file = new File(imagePath);
                        Uri mOutPutFileUri = Uri.fromFile(file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.yx_common_add_img_pupwindow_local_tv:
                        //相册
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                        break;
                }
            }
        };
        mPopupWindow.showAtBOTTOM(LayoutInflater.from(this).inflate(R.layout.activity_person_center, null));
    }
}
