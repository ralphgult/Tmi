package tm.ui.mine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.mine.adapter.FaceWallAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
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

    private InputDialog signDialog;
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
                            gv.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(PersonCenterActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initView();
        setData();
    }

    private void setData() {
        mData = new HashMap<>();
        imageLoaders = new ImageLoaders(this, new imageLoaderListener());
        imageLoaders.loadImage(head_iv, getIntent().getExtras().getString("headPath"));
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("caption"))) {
            sign_tv.setText(getIntent().getExtras().getString("caption"));
        }
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
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
        mAdapter = new FaceWallAdapter(this);
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
//                createSignDialog();
                Toast.makeText(this, "正在调试中...", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "正在调试中...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void createSignDialog() {
        if (null == signDialog) {
            signDialog = (InputDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_INPUT);
            signDialog.setTitleStr("个性签名");
            signDialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    signDialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(String inputText) {
                    final String sign = sign_tv.getText().toString().trim();
                    if (!TextUtils.isEmpty(sign)) {
                        if (!NetworkUtil.isNetworkAvailable(PersonCenterActivity.this)) {
                            Toast.makeText(PersonCenterActivity.this, "网络链接异常，请检查网络连接后重试...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        SharedPreferences sharedPre = PersonCenterActivity.this.getSharedPreferences("config", PersonCenterActivity.this.MODE_PRIVATE);
                        String userId = sharedPre.getString("username", "");
                        if (!TextUtils.isEmpty(userId)) {
                            list.add(new BasicNameValuePair("userId", userId));
                            list.add(new BasicNameValuePair("caption", sign));
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
                                                        String authId = map.get("state") + "";
                                                        if (authId.equals("1")) {
                                                            signDialog.closeDialog();
                                                            sign_tv.setText(sign);
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
