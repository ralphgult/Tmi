package tm.ui.mine;

import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oohla.android.utils.NetworkUtil;
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
import tm.ui.BigBackGroundActivity;
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.ImageUtil;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;


public class PersonCenterActivity extends Activity implements View.OnClickListener {
    private ImageView back;
    private LinearLayout head;
    private TextView yanzhi;
    private RelativeLayout mPersonSignRv;//设置个人签名布局
    private TextView mPersonSignTv;//设置个人签名显示
    private RelativeLayout mPersonNameRv;//设置昵称布局
    private TextView mPersonNameTv;//设置昵称显示
    private RelativeLayout mPersonAccountRv;//设置账号布局
    private TextView mPersonAccountTv;//设置账号显示
    private RelativeLayout mPersonResidenceRv;//设置居住地布局
    private TextView mPersonResidenceTv;//设置居住地显示

    private RelativeLayout mPersonJobRv;//设置职业布局
    private TextView mPersonJobTv;//设置职业显示
    private RelativeLayout mPersonAgeRv;//设置年龄布局
    private TextView mPersonAgeTv;//设置年龄显示
    private RelativeLayout mPersonHeightRv;//设置身高布局
    private TextView mPersonHeightTv;//设置身高显示
    private RelativeLayout mPersonAcademyRv;//设置毕业院校布局
    private TextView mPersonAcademyTv;//设置毕业院校显示
    private RelativeLayout mPersonHobbyRv;//设置兴趣爱好布局
    private TextView mPersonHobbyTv;//设置兴趣爱好显示
    private RelativeLayout mPersonWishRv;//设置我的心愿布局
    private TextView mPersonWishTv;//设置我的心愿显示
    private RelativeLayout mPersonIncomeRv;//设置年收入布局
    private TextView mPersonIncomeTv;//设置年收入显示


    private TextView ok;
    private FaceWallAdapter mAdapter;
    private ImageLoaders imageLoaders;
    private ImageView head_iv;

    private GridView gv;
    private int CHANGE_FACE_WALL = 0;
    private int CHANGE_HEAD = 1;
    private static final int CHANGEHEAD_LOCAL = 1;
    private static final int WALLFACE_LOCAL = 2;
    private static final int CHANGEHEAD_CAMERA = 3;
    private static final int WALLFACE_CAMERA = 4;
    private CommonSelectImgPopupWindow mPopupWindow;
    private InputDialog signDialog;
    private List<String> imgPath;
    private List<Integer> idList;
    private Map<String, String> mData;
    private Handler mHandler;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        init();
        initView();
        setData();
    }

    private void init() {
        mHandler = new Handler() {
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
                                    if (imgPath.size() < 6) {
                                        imgPath.add("0");
                                    }
                                } else {
                                    imgPath.add("0");
                                }
                                mAdapter.resetData(imgPath, idList);
                                gv.setAdapter(mAdapter);
                                SysUtils.setGridViewHight(gv);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1001:
                        Toast.makeText(PersonCenterActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();
                        head_iv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                        break;
                    case 3001:
                    case 4001:
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
                        Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void setData() {
        mData = new HashMap<>();
        imgPath = new ArrayList<>();
        idList = new ArrayList<>();
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        imageLoaders.loadImage(head_iv, getIntent().getExtras().getString("headPath"));
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("signed"))) {
            mPersonSignTv.setText(getIntent().getExtras().getString("signed"));
        }
        mPersonNameTv.setText(getIntent().getExtras().getString("name"));

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        //账号
        mPersonAccountTv.setText(userId);
        //居住地
        mPersonResidenceTv.setText(getIntent().getExtras().getString("domicile"));
        //职业
        mPersonJobTv.setText(getIntent().getExtras().getString("occupation"));
        //年龄
        mPersonAgeTv.setText(getIntent().getExtras().getString("age"));
        //身高
        mPersonHeightTv.setText(getIntent().getExtras().getString("height") + "cm");
        //毕业院校
        mPersonAcademyTv.setText(getIntent().getExtras().getString("school"));
        //爱好
        mPersonHobbyTv.setText(getIntent().getExtras().getString("hobby"));
        //我的心愿
        mPersonWishTv.setText(getIntent().getExtras().getString("wish"));
        //年收入
        mPersonIncomeTv.setText(getIntent().getExtras().getString("income") + "元");


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
        mPersonSignRv = (RelativeLayout) findViewById(R.id.person_sign_rv);
        mPersonNameRv = (RelativeLayout) findViewById(R.id.person_name_rv);
        mPersonNameTv = (TextView) findViewById(R.id.person_name_tv);
        mPersonAccountRv = (RelativeLayout) findViewById(R.id.person_account_rv);
        mPersonAccountTv = (TextView) findViewById(R.id.person_account_tv);

        mPersonResidenceRv = (RelativeLayout) findViewById(R.id.person_residence_rv);
        mPersonResidenceTv = (TextView) findViewById(R.id.person_residence_tv);
        mPersonJobRv = (RelativeLayout) findViewById(R.id.person_job_rv);
        mPersonJobTv = (TextView) findViewById(R.id.person_job_tv);
        mPersonAgeRv = (RelativeLayout) findViewById(R.id.person_age_rv);
        mPersonAgeTv = (TextView) findViewById(R.id.person_age_tv);
        mPersonHeightRv = (RelativeLayout) findViewById(R.id.person_height_rv);
        mPersonHeightTv = (TextView) findViewById(R.id.person_height_tv);
        mPersonAcademyRv = (RelativeLayout) findViewById(R.id.person_academy_rv);
        mPersonAcademyTv = (TextView) findViewById(R.id.person_academy_tv);
        mPersonHobbyRv = (RelativeLayout) findViewById(R.id.person_hobby_rv);
        mPersonHobbyTv = (TextView) findViewById(R.id.person_hobby_tv);
        mPersonWishRv = (RelativeLayout) findViewById(R.id.person_wish_rv);
        mPersonWishTv = (TextView) findViewById(R.id.person_wish_tv);
        mPersonIncomeRv = (RelativeLayout) findViewById(R.id.person_income_rv);
        mPersonIncomeTv = (TextView) findViewById(R.id.person_income_tv);

        mPersonSignTv = (TextView) findViewById(R.id.person_sign_tv);
        ok = (TextView) findViewById(R.id.person_center_ok_tv);
        head_iv = (ImageView) findViewById(R.id.person_center_head_iv);
        gv = (GridView) findViewById(R.id.person_center_pics_gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!imgPath.get(position).equals("0")) {
                    Bundle bundle = new Bundle();
                    String []path = new String[imgPath.size()];
                    int indexs =0;
                    for(int i=0;i<imgPath.size();i++){
                        path[i] = imgPath.get(i);
                        if(path[i].toString().equals("0")){
                            indexs = i;
                        }
                    }
                    if (indexs != 0) {
                        String []ary = new String[path.length - 1];
                        System.arraycopy(path, 0, ary, 0, indexs);
                        System.arraycopy(path, indexs + 1, ary, indexs, ary.length - indexs);
                        bundle.putStringArray("path",ary);
                    }else{
                        bundle.putStringArray("path",path);
                    }
                    bundle.putInt("status",position);
//                    ViewUtil.jumpToOtherActivity(PersonCenterActivity.this, HeadBigActivity.class, bundle);
                    ViewUtil.jumpToOtherActivity(PersonCenterActivity.this, BigBackGroundActivity.class, bundle);
                } else {
                    showPopupWindow(CHANGE_FACE_WALL);
                }
            }
        });
        mAdapter = new FaceWallAdapter(this);
        mAdapter.setHandler(mHandler);
        ok.setOnClickListener(this);
        head.setOnClickListener(this);
        yanzhi.setOnClickListener(this);
        mPersonSignRv.setOnClickListener(this);
        mPersonNameRv.setOnClickListener(this);
        mPersonAccountRv.setOnClickListener(this);
        mPersonResidenceRv.setOnClickListener(this);
        mPersonJobRv.setOnClickListener(this);
        mPersonAgeRv.setOnClickListener(this);
        mPersonHeightRv.setOnClickListener(this);
        mPersonAcademyRv.setOnClickListener(this);
        mPersonHobbyRv.setOnClickListener(this);
        mPersonWishRv.setOnClickListener(this);
        mPersonIncomeRv.setOnClickListener(this);
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
            case R.id.person_sign_rv:
                createDialog("请输入个性签名", 3);
                break;
            case R.id.person_name_rv:
                //设置昵称
                createDialog("设置昵称", 1);
                break;
            case R.id.person_account_rv:
                Toast.makeText(this, "账号不可修改", Toast.LENGTH_SHORT).show();
                break;
            case R.id.person_residence_rv:
                //设置居住地
                createDialog("设置居住地", 4);
                break;
            case R.id.person_job_rv:
                //设置职业
                createDialog("设置职业", 5);
                break;
            case R.id.person_age_rv:
                //设置年龄
                createDialog("设置年龄", 6);
                break;
            case R.id.person_height_rv:
                //设置身高
                createDialog("设置身高", 7);
                break;
            case R.id.person_academy_rv:
                //设置毕业院校
                createDialog("设置毕业院校", 8);
                break;
            case R.id.person_hobby_rv:
                //设置兴趣爱好
                createDialog("设置兴趣爱好", 9);
                break;
            case R.id.person_wish_rv:
                //设置我的心愿
                createDialog("设置我的心愿", 10);
                break;
            case R.id.person_income_rv:
                //设置年收入
                createDialog("设置年收入", 11);
                break;

            case R.id.person_center_head_rv:
                showPopupWindow(CHANGE_HEAD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
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
                imagePath = "/mnt/sdcard/ImageLoader/cache/images" + System.currentTimeMillis() + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
                try {
                    ImageUtil.saveBitmap(bitmap, imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == CHANGEHEAD_LOCAL || requestCode == CHANGEHEAD_CAMERA) {
                uploadPhotoThread thread = new uploadPhotoThread();
                thread.start();
            } else if (requestCode == WALLFACE_LOCAL || requestCode == WALLFACE_CAMERA) {
                new Thread() {
                    @Override
                    public void run() {
                        PersonManager.uploadImgwall(imagePath, 1, mHandler);
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
            PersonManager.SubmitPost(new File(imagePath), userId, 1, mHandler);
        }
    }

    private void createDialog(String hint, final int type) {
        if (null == signDialog) {
            signDialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
        }
        signDialog.setEditTextHint(hint);
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
                        //接口设置type
                        switch (type) {
                            case 1://设置昵称
                                list.add(new BasicNameValuePair("nickname", inputText));
                                break;
                            case 3://个性签名
                                list.add(new BasicNameValuePair("caption", inputText));
                                break;
                            case 4://设置居住地
                                list.add(new BasicNameValuePair("domicile", inputText));
                                break;
                            case 5://修改职业
                                list.add(new BasicNameValuePair("occupation", inputText));
                                break;
                            case 6://设置年龄
                                boolean result = inputText.matches("[0-9]+");
                                if (!result) {
                                    Toast.makeText(PersonCenterActivity.this, "年龄必须是纯数字", Toast.LENGTH_SHORT).show();
                                } else {
                                    list.add(new BasicNameValuePair("age", inputText));
                                }
                                break;
                            case 7://设置身高
                            {
                                boolean resulta = inputText.matches("[0-9]+");
                                if (!resulta) {
                                    Toast.makeText(PersonCenterActivity.this, "身高必须是纯数字", Toast.LENGTH_SHORT).show();
                                } else {
                                    list.add(new BasicNameValuePair("height", inputText));
                                }

                                break;
                            }
                            case 8://设置毕业院校
                                list.add(new BasicNameValuePair("school", inputText));
                                break;
                            case 9://设置爱好
                                list.add(new BasicNameValuePair("hobby", inputText));
                                break;
                            case 10://设置心愿
                                list.add(new BasicNameValuePair("wish", inputText));
                                break;
                            case 11://设置年收入
                            {
                                boolean resultb = inputText.matches("[0-9]+");
                                if (!resultb) {
                                    Toast.makeText(PersonCenterActivity.this, "年收入必须是纯数字", Toast.LENGTH_SHORT).show();
                                } else {
                                    list.add(new BasicNameValuePair("income", inputText));
                                }

                                break;
                            }
                        }
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
                                                        switch (type) {
                                                            case 1:
                                                                mPersonNameTv.setText(inputText);
                                                                break;
                                                            case 3:
                                                                mPersonSignTv.setText(inputText);
                                                                break;
                                                            case 4:
                                                                mPersonResidenceTv.setText(inputText);
                                                                break;
                                                            case 5:
                                                                mPersonJobTv.setText(inputText);
                                                                break;
                                                            case 6:
                                                                mPersonAgeTv.setText(inputText);
                                                                break;
                                                            case 7:
                                                                mPersonHeightTv.setText(inputText + "cm");
                                                                break;
                                                            case 8:
                                                                mPersonAcademyTv.setText(inputText);
                                                                break;
                                                            case 9:
                                                                mPersonHobbyTv.setText(inputText);
                                                                break;
                                                            case 10:
                                                                mPersonWishTv.setText(inputText);
                                                                break;
                                                            case 11:
                                                                mPersonIncomeTv.setText(inputText + "元");
                                                                break;
                                                        }

                                                        Toast.makeText(PersonCenterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                } else {
                    Toast.makeText(PersonCenterActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signDialog.showDialog();
    }


    private class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
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
                        PersonCenterActivity.this.startActivityForResult(intent, type == CHANGE_FACE_WALL ? WALLFACE_CAMERA : CHANGEHEAD_CAMERA);
                        break;
                    case R.id.yx_common_add_img_pupwindow_local_tv:
                        //相册
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        PersonCenterActivity.this.startActivityForResult(intent, type == CHANGE_FACE_WALL ? WALLFACE_LOCAL : CHANGEHEAD_LOCAL);
                        break;
                }
            }
        };
        mPopupWindow.showAtBOTTOM(LayoutInflater.from(this).inflate(R.layout.activity_person_center, null));
    }
}
