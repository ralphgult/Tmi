package tm.ui.tmi;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oohla.android.utils.StringUtil;
import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tm.manager.DpSpPxSwitch;
import tm.manager.PersonManager;
import tm.ui.mine.HeadBigActivity;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

public class GoodsChangeActivity extends Activity implements View.OnClickListener {
    private ImageView mBack_iv;
    private TextView mTitle_tv;
    private RelativeLayout mIntr_rv;
    private RelativeLayout mPrice_rv;
    private RelativeLayout mCount_rv;
    private RelativeLayout mName_rv;
    private RelativeLayout mOldPri_rv;
    private TextView mIntr_tv;
    private TextView mPrice_tv;
    private TextView mCount_tv;
    private TextView mDelete_tv;
    private TextView mSave_tv;
    private TextView mName_tv;
    private TextView mOldPri_tv;
    private LinearLayout mUpdate_ly;
    private TextView mAdd_tv;
    private ImageView mImage_iv1;
    private ImageView mImage_iv2;
    private ImageView mImage_iv3;
    private ImageView mImage_iv4;
    private ImageView mImage_iv5;
    private ImageView mImage_iv6;
    private ImageView mImage_iv7;
    private ImageView mImage_iv8;
    private ImageView mImgDel_iv1;
    private ImageView mImgDel_iv2;
    private ImageView mImgDel_iv3;
    private ImageView mImgDel_iv4;
    private ImageView mImgDel_iv5;
    private ImageView mImgDel_iv6;
    private ImageView mImgDel_iv7;
    private ImageView mImgDel_iv8;
    private LinearLayout mImgs_line2;

    private List<String> mImgPathList;
    private List<ImageView> mImgIvList;
    private List<ImageView> mImgDelList;
    private int mType;
    private static final int PHOTO = 2;
    private InputDialog dialog;
    private TextView mClickTextView;
    private List<String> mImgIdList;
    private boolean mIsUpdate;
    private String imagePath;
    private ProgressDialog mPd;
    private ImageLoaders imageLoaders;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    mPd.dismiss();
                    if (mIsUpdate) {
                        Toast.makeText(GoodsChangeActivity.this, "商品已保存", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GoodsChangeActivity.this, "商品已添加", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("add", "1");
                    ViewUtil.backToActivityForResult(GoodsChangeActivity.this, 1, intent);
                    break;
                case 2001:

                    mImgPathList.remove(msg.arg1);
                    mImgIdList.remove(msg.arg1);
                    showImageList();
                    break;
                case 3001:
                    mImgIdList.add(String.valueOf(msg.arg1));
                    setImageData();
                    break;
                case 4001:
                    Toast.makeText(GoodsChangeActivity.this, "删除商品成功", Toast.LENGTH_SHORT).show();
                    ViewUtil.backToActivityForResult(GoodsChangeActivity.this, 1, null);
                    break;
                case 5001:
                    mPd = ProgressDialog.show(GoodsChangeActivity.this, "添加商品", "添加商品中，请稍后...");
                    break;
                default:
                    Toast.makeText(GoodsChangeActivity.this, "系统繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private int widthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_change);
        mType = getIntent().getExtras().getInt("type");
        mIsUpdate = getIntent().getExtras().getBoolean("isUpdate");
        mImgPathList = new ArrayList<>();
        mImgIdList = new ArrayList<>();
        mImgIvList = new ArrayList<>();
        mImgDelList = new ArrayList<>();
        WindowManager manager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        int widthWindow = manager.getDefaultDisplay().getWidth();
        int dimenBig = DpSpPxSwitch.dp2px(this, 10);
        int dimenSmall = DpSpPxSwitch.dp2px(this, 5);
        widthView = (widthWindow - (dimenBig * 2) - (dimenSmall * 3)) / 4;
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        initViews();
        if (mIsUpdate) {
            setData();
        } else {
            setImageData();
        }
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if (!TextUtils.isEmpty(bundle.getString("imgs"))) {
            String imgPaths = bundle.getString("imgs");
            String imgIds = bundle.getString("imgIds");
            if (imgPaths.contains(",")) {
                String[] paths = imgPaths.split(",");
                for (String s : paths) {
                    mImgPathList.add(s);
                }
                for (String s : imgIds.split(",")) {
                    mImgIdList.add(s);
                }
            } else {
                mImgPathList.add(imgPaths);
                mImgIdList.add(imgIds);
            }
        }
        setImageData();
        showImageList();
        mName_tv.setText(bundle.getString("goodName"));
        mIntr_tv.setText(bundle.getString("goodProfiles"));
        mOldPri_tv.setText(bundle.getString("originalPrice"));
        mPrice_tv.setText(bundle.getString("currentPrice"));
        mCount_tv.setText(bundle.getString("count"));
    }

    public void initViews() {
        mBack_iv = (ImageView) findViewById(R.id.goods_detil_back_iv);
        mTitle_tv = (TextView) findViewById(R.id.goods_detil_title);
        mImage_iv1 = (ImageView) findViewById(R.id.goods_detil_change_imgs_1);
        mImage_iv2 = (ImageView) findViewById(R.id.goods_detil_change_imgs_2);
        mImage_iv3 = (ImageView) findViewById(R.id.goods_detil_change_imgs_3);
        mImage_iv4 = (ImageView) findViewById(R.id.goods_detil_change_imgs_4);
        mImage_iv5 = (ImageView) findViewById(R.id.goods_detil_change_imgs_5);
        mImage_iv6 = (ImageView) findViewById(R.id.goods_detil_change_imgs_6);
        mImage_iv7 = (ImageView) findViewById(R.id.goods_detil_change_imgs_7);
        mImage_iv8 = (ImageView) findViewById(R.id.goods_detil_change_imgs_8);
        mImgIvList.add(mImage_iv1);
        mImgIvList.add(mImage_iv2);
        mImgIvList.add(mImage_iv3);
        mImgIvList.add(mImage_iv4);
        mImgIvList.add(mImage_iv5);
        mImgIvList.add(mImage_iv6);
        mImgIvList.add(mImage_iv7);
        mImgIvList.add(mImage_iv8);
        for (ImageView view : mImgIvList) {
            setImageWidthHeight(view);
        }
        mImgDel_iv1 = (ImageView) findViewById(R.id.goods_detil_change_img_del_1);
        mImgDel_iv2 = (ImageView) findViewById(R.id.goods_detil_change_img_del_2);
        mImgDel_iv3 = (ImageView) findViewById(R.id.goods_detil_change_img_del_3);
        mImgDel_iv4 = (ImageView) findViewById(R.id.goods_detil_change_img_del_4);
        mImgDel_iv5 = (ImageView) findViewById(R.id.goods_detil_change_img_del_5);
        mImgDel_iv6 = (ImageView) findViewById(R.id.goods_detil_change_img_del_6);
        mImgDel_iv7 = (ImageView) findViewById(R.id.goods_detil_change_img_del_7);
        mImgDel_iv8 = (ImageView) findViewById(R.id.goods_detil_change_img_del_8);
        mImgDelList.add(mImgDel_iv1);
        mImgDelList.add(mImgDel_iv2);
        mImgDelList.add(mImgDel_iv3);
        mImgDelList.add(mImgDel_iv4);
        mImgDelList.add(mImgDel_iv5);
        mImgDelList.add(mImgDel_iv6);
        mImgDelList.add(mImgDel_iv7);
        mImgDelList.add(mImgDel_iv8);
        mImgs_line2 = (LinearLayout) findViewById(R.id.goods_detil_change_imgs_line_2);
        mIntr_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_intr_rv);
        mPrice_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_price_rv);
        mCount_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_count_rv);
        mName_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_name_rv);
        mOldPri_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_oldpri_rv);
        mIntr_tv = (TextView) findViewById(R.id.goods_detil_change_intr_text_tv);
        mPrice_tv = (TextView) findViewById(R.id.goods_detil_change_price_text_tv);
        mCount_tv = (TextView) findViewById(R.id.goods_detil_change_count_text_tv);
        mName_tv = (TextView) findViewById(R.id.goods_detil_change_name_text_tv);
        mOldPri_tv = (TextView) findViewById(R.id.goods_detil_change_oldpri_text_tv);
        mDelete_tv = (TextView) findViewById(R.id.goods_detil_delete);
        mSave_tv = (TextView) findViewById(R.id.goods_detil_mange_save);
        mUpdate_ly = (LinearLayout) findViewById(R.id.goods_detil_bottom);
        mAdd_tv = (TextView) findViewById(R.id.goods_detil_mange_add);

        if (mIsUpdate) {
            mUpdate_ly.setVisibility(View.VISIBLE);
            mAdd_tv.setVisibility(View.GONE);
            mTitle_tv.setText("修改商品");
        } else {
            mUpdate_ly.setVisibility(View.GONE);
            mAdd_tv.setVisibility(View.VISIBLE);
            mTitle_tv.setText("添加商品");
        }

        mBack_iv.setOnClickListener(this);
        mIntr_rv.setOnClickListener(this);
        mPrice_rv.setOnClickListener(this);
        mName_rv.setOnClickListener(this);
        mOldPri_rv.setOnClickListener(this);
        mCount_rv.setOnClickListener(this);
        mDelete_tv.setOnClickListener(this);
        mSave_tv.setOnClickListener(this);
        mAdd_tv.setOnClickListener(this);
        mImage_iv1.setOnClickListener(this);
        mImage_iv2.setOnClickListener(this);
        mImage_iv3.setOnClickListener(this);
        mImage_iv4.setOnClickListener(this);
        mImage_iv5.setOnClickListener(this);
        mImage_iv6.setOnClickListener(this);
        mImage_iv7.setOnClickListener(this);
        mImage_iv8.setOnClickListener(this);
        mImgDel_iv1.setOnClickListener(this);
        mImgDel_iv2.setOnClickListener(this);
        mImgDel_iv3.setOnClickListener(this);
        mImgDel_iv4.setOnClickListener(this);
        mImgDel_iv5.setOnClickListener(this);
        mImgDel_iv6.setOnClickListener(this);
        mImgDel_iv7.setOnClickListener(this);
        mImgDel_iv8.setOnClickListener(this);

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
            addImage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_detil_back_iv:
                ViewUtil.backToOtherActivity(this);
                break;
            case R.id.goods_detil_change_intr_rv:
                mClickTextView = mIntr_tv;
                createDialog("请输入商品描述");
                break;
            case R.id.goods_detil_change_price_rv:
                mClickTextView = mPrice_tv;
                createDialog("请输入商品现价");
                break;
            case R.id.goods_detil_change_count_rv:
                mClickTextView = mCount_tv;
                createDialog("请输入商品库存");
                break;
            case R.id.goods_detil_change_name_rv:
                mClickTextView = mName_tv;
                createDialog("请输入商品名称");
                break;
            case R.id.goods_detil_change_oldpri_rv:
                mClickTextView = mOldPri_tv;
                createDialog("请输入商品原价");
                break;
            case R.id.goods_detil_delete:
                new Thread() {
                    @Override
                    public void run() {
                        PersonManager.deleteGoods(getIntent().getExtras().getString("goodsId"), mHandler);
                    }
                }.start();
                break;
            case R.id.goods_detil_mange_save:
                if (checkContent()) {
                    mPd = ProgressDialog.show(this, "修改商品", "修改商品中，请稍后...");
                    new Thread() {
                        @Override
                        public void run() {
                            updateGoods();
                        }
                    }.start();
                }
                break;
            case R.id.goods_detil_mange_add:
                if (checkContent()) {
                    new Thread() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(5001);
                            addGoods();
                        }
                    }.start();
                }
                break;
            case R.id.goods_detil_change_imgs_1:
            case R.id.goods_detil_change_imgs_2:
            case R.id.goods_detil_change_imgs_3:
            case R.id.goods_detil_change_imgs_4:
            case R.id.goods_detil_change_imgs_5:
            case R.id.goods_detil_change_imgs_6:
            case R.id.goods_detil_change_imgs_7:
            case R.id.goods_detil_change_imgs_8:
                int index = mImgIvList.indexOf(v);
                if (index < mImgPathList.size() - 1) {
                    Bundle bundle = new Bundle();
                    String imgPath = mImgPathList.get(index);
                    if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
                        String []strs = new String [mImgPathList.size()];
                        int indexs =0;
                        for(int i=0;i<mImgPathList.size();i++){
                            strs[i] = mImgPathList.get(i);
                            if(strs[i].toString().equals("0")){
                                indexs = i;
                            }
                        }
                        if (indexs != 0) {
                            String []ary = new String[strs.length - 1];
                            System.arraycopy(strs, 0, ary, 0, indexs);
                            System.arraycopy(strs, indexs + 1, ary, indexs, ary.length - indexs);
                            bundle.putStringArray("path",ary);
                        }else{
                            bundle.putStringArray("path",strs);
                        }
                        bundle.putInt("status", index);
                    } else {
                        bundle.putString("filePath", imgPath);
                    }
                    ViewUtil.jumpToOtherActivity(GoodsChangeActivity.this, HeadBigActivity.class, bundle);
                } else if (index == mImgPathList.size() - 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GoodsChangeActivity.this.startActivityForResult(intent, PHOTO);
                }
                break;
            case R.id.goods_detil_change_img_del_1:
            case R.id.goods_detil_change_img_del_2:
            case R.id.goods_detil_change_img_del_3:
            case R.id.goods_detil_change_img_del_4:
            case R.id.goods_detil_change_img_del_5:
            case R.id.goods_detil_change_img_del_6:
            case R.id.goods_detil_change_img_del_7:
            case R.id.goods_detil_change_img_del_8:
                int position = mImgDelList.indexOf(v);
                deleteImage(position);
                break;
        }
    }

    private void createDialog(String string) {
        if (null == dialog) {
            dialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            dialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    dialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    if (!TextUtils.isEmpty(inputText)) {
                        mClickTextView.setText(inputText);
                        dialog.closeDialog();
                    } else {
                        Toast.makeText(GoodsChangeActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        dialog.setEditTextHint(string);
        if (mClickTextView.getId() == R.id.goods_detil_change_count_text_tv) {
            dialog.getInputView().setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (mClickTextView.getId() == R.id.goods_detil_change_price_text_tv || mClickTextView.getId() == R.id.goods_detil_change_oldpri_text_tv) {
            dialog.getInputView().setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            dialog.getInputView().setInputType(InputType.TYPE_CLASS_TEXT);
        }
        dialog.showDialog();
    }


    private boolean checkContent() {
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        String aliPayAccount = sharedPre.getString("aliAccount","");
        if(StringUtil.isNullOrEmpty(aliPayAccount)){
            Toast.makeText(this, "还未设置收款支付宝账号，请到个人中心内完善资料", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (null == mImgPathList || mImgPathList.size() == 0) {
            Toast.makeText(this, "请选择商品图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mIntr_tv.getText().toString().trim())) {
            Toast.makeText(this, "请填写商品描述", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mPrice_tv.getText().toString().trim())) {
            Toast.makeText(this, "请填写商品价格", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mCount_tv.getText().toString().trim())) {
            Toast.makeText(this, "请填写商品库存", Toast.LENGTH_SHORT).show();
            return false;
        }
        String str = "^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$";
        Pattern pattern = Pattern.compile(str);
        Matcher isPrice = pattern.matcher(mPrice_tv.getText().toString().trim());
        if (!isPrice.matches()) {
            Toast.makeText(this, "请填写正确的商品价格", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addGoods() {
        String intro = mIntr_tv.getText().toString().trim();
        String price = mPrice_tv.getText().toString().trim();
        String count = mCount_tv.getText().toString().trim();
        String oldPri = mOldPri_tv.getText().toString().trim();
        String name = mName_tv.getText().toString().trim();
        PersonManager.addGoods(name, price, count, oldPri, intro, mImgPathList, mType, mHandler);
    }

    private void updateGoods() {
        String id = getIntent().getExtras().getString("goodsId");
        String intro = mIntr_tv.getText().toString().trim();
        String price = mPrice_tv.getText().toString().trim();
        String count = mCount_tv.getText().toString().trim();
        String oldPri = mOldPri_tv.getText().toString().trim();
        String name = mName_tv.getText().toString().trim();
        PersonManager.updateGoods(id, name, price, count, intro, oldPri, mHandler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void deleteImage(final int position) {
        if (mImgPathList.size() == 2) {
            Toast.makeText(this, "至少上传一张照片", Toast.LENGTH_SHORT).show();
        } else {
            if (mIsUpdate) {
                new Thread() {
                    @Override
                    public void run() {
                        PersonManager.deleteGoodsImg(mImgIdList.get(position), position, mHandler);
                    }
                }.start();
            } else {
                mImgPathList.remove(position);
                showImageList();
            }
        }


    }

    public void addImage() {
        if (mIsUpdate) {
            new Thread() {
                @Override
                public void run() {
                    PersonManager.addGoodsImage(imagePath, getIntent().getExtras().getString("goodsId"), mHandler);
                }
            }.start();
        } else {
            setImageData();
        }
    }

    private void setImageData() {
        if (mImgPathList.contains("0")) {
            mImgPathList.remove("0");
        }
        if (!TextUtils.isEmpty(imagePath)) {
            mImgPathList.add(imagePath);
        }
        if (mImgPathList.size() < 8) {
            mImgPathList.add("0");
        }
        showImageList();
    }

    @Override
    protected void onDestroy() {
        mImgPathList = null;
        super.onDestroy();
    }

    private void setImageWidthHeight(ImageView view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = widthView;
        params.height = widthView;
    }

    private void showImageList() {
        int size = mImgPathList.size();
        for (int i = 0; i < size; i++) {
            if (mImgPathList.get(i).equals("0")) {
                //添加位
                mImgIvList.get(i).setImageResource(R.drawable.em_add_new);
            } else {
                String pathStr = mImgPathList.get(i);
                if (pathStr.startsWith("http://") || pathStr.startsWith("https://")) {
                    imageLoaders.loadImage(mImgIvList.get(i), pathStr);
                } else {
                    mImgIvList.get(i).setImageBitmap(BitmapFactory.decodeFile(pathStr));
                }
            }
        }
        int sizeIv = mImgIvList.size();
        for (int i = 0; i < sizeIv; i++) {
            mImgIvList.get(i).setVisibility(i < size ? View.VISIBLE : View.INVISIBLE);
            mImgDelList.get(i).setVisibility(i < size - 1 ? View.VISIBLE : View.GONE);
        }
        if (size <= 4) {
            mImgs_line2.setVisibility(View.GONE);
        } else {
            mImgs_line2.setVisibility(View.VISIBLE);
        }
    }

    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }
}
