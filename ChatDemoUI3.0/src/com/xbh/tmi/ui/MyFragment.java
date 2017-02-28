package com.xbh.tmi.ui;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.DemoHelper;
import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.mine.CompCenterActivity;
import tm.ui.mine.FarmerCenterActivity;
import tm.ui.mine.HeadBigActivity;
import tm.ui.mine.MineOrderActivity;
import tm.ui.mine.MyAddressActivity;
import tm.ui.mine.MySoppingActivity;
import tm.ui.mine.PersonCenterActivity;
import tm.ui.mine.UploadAuctionActivity;
import tm.ui.setting.SettingActivity;
import tm.utils.ConstantsHandler;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView setting_btn;
    private TextView personCenter_tv;
    private TextView compCenter_tv;
    private TextView farmerCenter_tv;

    private TextView myOrder_tv;
    private TextView myShopping_tv;
    private TextView myAddress_tv;
//    private TextView myMessage_tv;
    private TextView mUploadAuction_tv;

    private ImageView myHead_iv;
    private TextView myName_tv;
    private TextView mySigned_tv;
    private String headImgPath;
    private Map<String, String> mData;
    private ImageLoaders imageLoaders;
    private String mHeadPathBefore;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    String authId = map.get("state") + "";
                    if (authId.equals("1")) {
                        mData.putAll(map);
                        setData();
                    } else {
                        Toast.makeText(MyFragment.this.getActivity(), "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantsHandler.EXECUTE_FAIL:
                case ConstantsHandler.ConnectTimeout:
                    Toast.makeText(MyFragment.this.getActivity(), "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me_fragment_tm, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headImgPath = new String();
        mData = new HashMap<>();
        initView();
        getUserInfo();
    }

    private void setData() {
        imageLoaders = new ImageLoaders(this.getActivity(), new imageLoaderListener());
        headImgPath = mData.get("photo");
        if (!TextUtils.isEmpty(mHeadPathBefore) && !headImgPath.equals(mHeadPathBefore)) {
            //获取SharedPreferences对象
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE);
            //获取Editor对象
            SharedPreferences.Editor editor=sharedPre.edit();
            editor.putString("phone", headImgPath);
            Log.e("Lking","设置之后的photo = "+ headImgPath);
            editor.commit();
        }
        mHeadPathBefore = headImgPath;
        if (!TextUtils.isEmpty(headImgPath)) {
            imageLoaders.loadImage(myHead_iv, headImgPath);
        }
        myName_tv.setText(TextUtils.isEmpty(mData.get("userName")) ? DemoHelper.getInstance().getCurrentUsernName() : mData.get("userName"));
        if (!TextUtils.isEmpty(mData.get("caption"))) {
            mySigned_tv.setText(mData.get("caption"));
        }
    }

    class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle;
        switch (v.getId()) {
            case R.id.fgt_mine_setting_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(), SettingActivity.class);
                break;
            case R.id.mine_person_center_tv:
                bundle = new Bundle();
                Log.e("Lking","个人信息"+mData.toString());
                bundle.putString("headPath",headImgPath);
                bundle.putString("signed", mData.get("caption"));
                bundle.putString("name",myName_tv.getText().toString());
                String dom = mData.get("domicile");
                if(TextUtils.isEmpty(dom)){
                    dom = "请设置居住地";
                }
                bundle.putString("domicile",dom);

                String occ = mData.get("occupation");
                if(TextUtils.isEmpty(occ)){
                    occ = "请设置职业";
                }
                bundle.putString("occupation",occ);

                String age = mData.get("age");
                if(TextUtils.isEmpty(age)){
                    age = "请设置年龄";
                }
                bundle.putString("age",age);

                String height = mData.get("height");
                if(TextUtils.isEmpty(height)){
                    height = "请设置身高";
                }
                bundle.putString("height",height);

                String school = mData.get("school");
                if(TextUtils.isEmpty(school)){
                    school = "请设置毕业院校";
                }
                bundle.putString("school",school);

                String hobby = mData.get("hobby");
                if(TextUtils.isEmpty(hobby)){
                    hobby = "请设置爱好";
                }
                bundle.putString("hobby",hobby);

                String wish = mData.get("wish");
                if(TextUtils.isEmpty(wish)){
                    wish = "请设置我的心愿";
                }
                bundle.putString("wish",wish);

                String income = mData.get("income");
                if(TextUtils.isEmpty(income)){
                    income = "请设置我的心愿";
                }
                bundle.putString("income",income);
                ViewUtil.jumpToOtherActivity(this.getActivity(), PersonCenterActivity.class,bundle);
                break;
            case R.id.mine_comp_center_tv:
                bundle = new Bundle();
                bundle.putString("comphead",mData.get("companyLogo"));
                bundle.putString("compname",mData.get("companyName"));
                bundle.putString("compinter", mData.get("companyIntroduction"));
                ViewUtil.jumpToOtherActivity(this.getActivity(), CompCenterActivity.class,bundle);
                break;
            case R.id.mine_farmer_center_tv:
                bundle = new Bundle();
                bundle.putString("farmhead",mData.get("farmLogo"));
                bundle.putString("farmname",mData.get("farmName"));
                bundle.putString("farminter", mData.get("farmIntroduction"));
                ViewUtil.jumpToOtherActivity(this.getActivity(), FarmerCenterActivity.class,bundle);
                break;
            case R.id.mine_my_order_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(), MineOrderActivity.class);
                break;
            case R.id.mine_my_shopping_tv:
//                Toast.makeText(this.getActivity(), "正在调试中...", Toast.LENGTH_SHORT).show();
                ViewUtil.jumpToOtherActivity(this.getActivity(),MySoppingActivity.class);
                break;
            case R.id.mine_my_address_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(),MyAddressActivity.class);
                break;
            case R.id.mine_upload_auction_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(),UploadAuctionActivity.class);
                break;
            case R.id.fgt_mine_head_iv:
                bundle = new Bundle();
                bundle.putString("path", headImgPath);
                ViewUtil.jumpToOtherActivity(this.getActivity(), HeadBigActivity.class, bundle);
                break;
        }

    }


    private void initView() {
        setting_btn = (TextView) getView().findViewById(R.id.fgt_mine_setting_tv);
        personCenter_tv = (TextView) getView().findViewById(R.id.mine_person_center_tv);
        compCenter_tv = (TextView) getView().findViewById(R.id.mine_comp_center_tv);
        farmerCenter_tv = (TextView) getView().findViewById(R.id.mine_farmer_center_tv);

        myOrder_tv = (TextView) getView().findViewById(R.id.mine_my_order_tv);
        myShopping_tv = (TextView) getView().findViewById(R.id.mine_my_shopping_tv);
        myAddress_tv = (TextView) getView().findViewById(R.id.mine_my_address_tv);
        mUploadAuction_tv = (TextView) getView().findViewById(R.id.mine_upload_auction_tv);

        myHead_iv = (ImageView) getView().findViewById(R.id.fgt_mine_head_iv);
        myName_tv = (TextView) getView().findViewById(R.id.fgt_mine_name_tv);
        mySigned_tv = (TextView) getView().findViewById(R.id.fgt_mine_singh_tv);

        setting_btn.setOnClickListener(this);
        personCenter_tv.setOnClickListener(this);
        compCenter_tv.setOnClickListener(this);
        farmerCenter_tv.setOnClickListener(this);

        myOrder_tv.setOnClickListener(this);
        myShopping_tv.setOnClickListener(this);
        myAddress_tv.setOnClickListener(this);
        mUploadAuction_tv.setOnClickListener(this);

        myHead_iv.setOnClickListener(this);


    }

    public void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        SharedPreferences sharedPre = this.getActivity().getSharedPreferences("config", this.getActivity().MODE_PRIVATE);
        String userId = sharedPre.getString("username", "");
        if (!TextUtils.isEmpty(userId)) {
            list.add(new BasicNameValuePair("userId", userId));
        }
        NetFactory.instance().commonHttpCilent(mHandler, this.getActivity(),
                Config.URL_GET_USER_POFILE, list);
    }
}

