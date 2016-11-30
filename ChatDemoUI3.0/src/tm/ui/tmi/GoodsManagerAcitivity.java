package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import tm.ui.tmi.adapter.GoodsAdapter;
import tm.utils.ConstantsHandler;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

public class GoodsManagerAcitivity extends Activity {
    private ImageView mBack_iv;
    private ListView mList_lv;
    private TextView mSearch_tv;
    private TextView mSelete_tv;
    private TextView mAdd_tv;
    private InputDialog dialog;
    private GoodsAdapter mAdapter;
    private List<Map<String, String>> mAllList;
    private List<Map<String, String>> mSearchList;
    private int mType;
    private boolean mIsSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manager_acitivity);
        init();
        initViews();
        getDataFormService(0);
    }

    private void getDataFormService(int page) {
        mAllList.clear();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getSharedPreferences("config", this.MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
            list.add(new BasicNameValuePair("num", "100"));
            list.add(new BasicNameValuePair("page", String.valueOf(page)));
        }
        NetFactory.instance().commonHttpCilent(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsHandler.EXECUTE_SUCCESS:
                        phraseData(msg);
                        break;
                    default:
                        Toast.makeText(GoodsManagerAcitivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, this, Config.URL_GET_GOODS_LIST, list);
    }

    private void init() {
        mIsSelect = false;
        mType = getIntent().getExtras().getInt("type");
        mAllList = new ArrayList<>();
        mAdapter = new GoodsAdapter(this, mType);
        mAdapter.setIsChoice(mIsSelect);
    }

    private void initViews() {
        mBack_iv = (ImageView) findViewById(R.id.goods_manager_back_iv);
        mList_lv = (ListView) findViewById(R.id.goods_manager_list);
        mSearch_tv = (TextView) findViewById(R.id.goods_manager_search);
        mSelete_tv = (TextView) findViewById(R.id.goods_manager_mange_all);
        mAdd_tv = (TextView) findViewById(R.id.goods_manager_add);
        mBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(GoodsManagerAcitivity.this);
            }
        });
        mSearch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSelect) {
                    List<String> list = mAdapter.getChoiceList();
                    if (null != list && list.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        for (String s : list) {
                            sb.append(s)
                                    .append(",");
                        }
                        String ids = sb.substring(0, sb.length() - 1);
                        //TODO 调用删除商品的方法
                        Toast.makeText(GoodsManagerAcitivity.this, "删除商品", Toast.LENGTH_SHORT).show();
                        List<Map<String, String>> dataList = mAdapter.getDataList();
                        for (Map<String, String> map : dataList) {
                            if (list.contains(map.get("id"))) {
                                dataList.remove(map);
                            }
                        }
                        mAdapter.resetData(dataList);
                    } else {
                        Toast.makeText(GoodsManagerAcitivity.this, "请选择商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    createDialog("请输入商品名称");
                }
            }
        });
        mSelete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSelect) {
                    mIsSelect = false;
                    mAdapter.setIsChoice(mIsSelect);
                    mSelete_tv.setText("批量管理");
                    mSearch_tv.setText("搜索");
                } else {
                    mIsSelect = true;
                    mAdapter.setIsChoice(mIsSelect);
                    mSelete_tv.setText("取消");
                    mSearch_tv.setText("删除");
                }
            }
        });
        mAdd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("type",mType);
                bundle.putBoolean("isUpdate",false);
                ViewUtil.jumpToOherActivityForResult(GoodsManagerAcitivity.this,GoodsChangeActivity.class,bundle,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 1){
            getDataFormService(0);
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
                        mSearchList = new ArrayList<Map<String, String>>();
                        for (Map<String, String> map : mAllList) {
                            if (map.get("name").contains(inputText)) {
                                mSearchList.add(map);
                            }
                        }
                        mAdapter.resetData(mSearchList);
                        dialog.closeDialog();
                    } else {
                        Toast.makeText(GoodsManagerAcitivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        dialog.showDialog();
    }

    private void phraseData(Message msg) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> jsonMap = (Map<String, String>) msg.obj;
        String authId = jsonMap.get("state");
        if (authId.equals("1")) {
            try {
                String moments = "{\"goods\":" + jsonMap.get("goods") + "}";
                JSONObject object = new JSONObject(moments);
                JSONArray momentList = object.getJSONArray("goods");
                int size = momentList.length();
                for (int i = 0; i < size; i++) {
                    map = new HashMap<String, String>();
                    JSONObject obj = momentList.getJSONObject(i);
                    map.put("goodsId", obj.getString("goodsId"));
                    map.put("goodName", obj.getString("goodName"));
                    JSONArray array = obj.getJSONArray("img");
                    StringBuffer sbPath = new StringBuffer();
                    StringBuffer sbPid = new StringBuffer();
                    for (int j = 0; j < array.length(); j++) {
                        sbPath.append(array.getJSONObject(j).getString("goodImg"))
                                .append(",");
                        sbPid.append(array.getJSONObject(j).getString("giId"))
                                .append(",");
                    }
                    if (!TextUtils.isEmpty(sbPath.toString())) {
                        map.put("imgs", sbPath.substring(0,sbPath.length() - 1));
                        map.put("imgIds", sbPid.substring(0,sbPid.length() - 1));
                    }else{
                        map.put("imgs", "");
                        map.put("imgIds", "");
                    }
                    map.put("createDate", obj.getString("createDate"));
                    map.put("currentPrice", obj.getString("currentPrice"));
                    map.put("originalPrice",  obj.getString("originalPrice"));
                    map.put("sales", obj.getString("sales"));
                    map.put("count", obj.getString("count"));
                    if (obj.getInt("type") == mType) {
                        mAllList.add(map);
                    }
                }
                mAdapter.resetData(mAllList);
                mList_lv.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GoodsManagerAcitivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GoodsManagerAcitivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
        }
    }
}
