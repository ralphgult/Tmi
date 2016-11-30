package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oohla.android.utils.NetworkUtil;
import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tm.manager.PersonManager;
import tm.ui.mine.HeadBigActivity;
import tm.ui.tmi.adapter.GoodsChangeImgAdapter;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

public class GoodsChangeActivity extends Activity implements View.OnClickListener {
    private ImageView mBack_iv;
    private GridView mPhoto_gv;
    private RelativeLayout mIntr_rv;
    private RelativeLayout mPrice_rv;
    private RelativeLayout mCount_rv;
    private TextView mIntr_tv;
    private TextView mPrice_tv;
    private TextView mCount_tv;
    private TextView mDelete_tv;
    private TextView mSave_tv;

    private List<String> mImgPathList;
    private int mType;
    private GoodsChangeImgAdapter mAdapter;
    private static final int PHOTO = 2;
    private InputDialog dialog;
    private TextView mClickTextView;
    private List<String> mImgIdList;
    private boolean mIsUpdate;
    private String imagePath;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Intent intent = new Intent();
                    intent.putExtra("add", "1");
                    ViewUtil.backToActivityForResult(GoodsChangeActivity.this, 1, intent);
                    break;
                case 2001:
                    mImgPathList.remove(msg.arg1);
                    mImgIdList.remove(msg.arg1);
                    mAdapter.resetData(mImgPathList);
                    break;
                case 3001:
                    mImgPathList.add(imagePath);
                    mAdapter.resetData(mImgPathList);
                    break;
                default:
                    Toast.makeText(GoodsChangeActivity.this, "系统繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_change);
        mAdapter = new GoodsChangeImgAdapter(this);
        mType = getIntent().getExtras().getInt("type");
        mIsUpdate = getIntent().getExtras().getBoolean("isUpdate");
        mImgPathList = new ArrayList<>();
        mImgIdList = new ArrayList<>();
        initViews();
        if (mIsUpdate) {
            setData();
        } else {
            mAdapter.resetData(mImgPathList);
            mPhoto_gv.setAdapter(mAdapter);
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
            mAdapter.resetData(mImgPathList);
            mPhoto_gv.setAdapter(mAdapter);
        }
        mIntr_tv.setText(bundle.getString("goodName"));
        mPrice_tv.setText(bundle.getString("currentPrice"));
        mCount_tv.setText(bundle.getString("count"));
    }

    public void initViews() {
        mBack_iv = (ImageView) findViewById(R.id.goods_detil_back_iv);
        mPhoto_gv = (GridView) findViewById(R.id.goods_detil_gv);
        mIntr_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_intr_rv);
        mPrice_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_price_rv);
        mCount_rv = (RelativeLayout) findViewById(R.id.goods_detil_change_count_rv);
        mIntr_tv = (TextView) findViewById(R.id.goods_detil_change_intr_text_tv);
        mPrice_tv = (TextView) findViewById(R.id.goods_detil_change_price_text_tv);
        mCount_tv = (TextView) findViewById(R.id.goods_detil_change_count_text_tv);
        mDelete_tv = (TextView) findViewById(R.id.goods_detil_delete);
        mSave_tv = (TextView) findViewById(R.id.goods_detil_mange_save);

        mPhoto_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getmPicList().get(position) == "0") {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GoodsChangeActivity.this.startActivityForResult(intent, PHOTO);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", mImgPathList.get(position));
                    ViewUtil.jumpToOtherActivity(GoodsChangeActivity.this, HeadBigActivity.class, bundle);
                }
            }
        });
        mBack_iv.setOnClickListener(this);
        mIntr_rv.setOnClickListener(this);
        mPrice_rv.setOnClickListener(this);
        mCount_rv.setOnClickListener(this);
        mDelete_tv.setOnClickListener(this);
        mSave_tv.setOnClickListener(this);
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
                createDialog("请输入商品价格");
                break;
            case R.id.goods_detil_change_count_rv:
                mClickTextView = mCount_tv;
                createDialog("请输入商品库存");
                break;
            case R.id.goods_detil_delete:

                break;
            case R.id.goods_detil_mange_save:
                if (!mIsUpdate) {
                    if (checkContent()) {
                        new Thread() {
                            @Override
                            public void run() {
                                addGoods();
                            }
                        }.start();
                    }
                } else {
                    if (checkContent()) {
                        new Thread() {
                            @Override
                            public void run() {
                                updateGoods();
                            }
                        }.start();
                    }
                }
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
        } else if (mClickTextView.getId() == R.id.goods_detil_change_price_text_tv) {
            dialog.getInputView().setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            dialog.getInputView().setInputType(InputType.TYPE_CLASS_TEXT);
        }
        dialog.showDialog();
    }


    private boolean checkContent() {
        if (mImgPathList.get(0) == "0") {
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
        String name = mIntr_tv.getText().toString().trim();
        String price = mPrice_tv.getText().toString().trim();
        String count = mCount_tv.getText().toString().trim();
        PersonManager.addGoods(name, price, count, mImgPathList, mType, mHandler);
    }

    private void updateGoods() {
        String id = getIntent().getExtras().getString("goodsId");
        String name = mIntr_tv.getText().toString().trim();
        String price = mPrice_tv.getText().toString().trim();
        String count = mCount_tv.getText().toString().trim();
        PersonManager.updateGoods(id, name, price, count, mHandler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewUtil.backToOtherActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void deleteImage(final int position) {
        if (mIsUpdate) {
            new Thread() {
                @Override
                public void run() {
                    PersonManager.deleteGoodsImg(mImgIdList.get(position), position, mHandler);
                }
            }.start();
        } else {
            mImgPathList.remove(position);
            mImgIdList.remove(position);
            mAdapter.resetData(mImgPathList);
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
            mImgPathList.add(imagePath);
            mAdapter.resetData(mImgPathList);
        }
    }
}
