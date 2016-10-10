package com.hyphenate.tmdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;

import tm.ui.tmi.TmiNoticeActivity;


/**
 * Created by Administrator on 2016/8/21.
 */
public class TmFragment extends Fragment implements View.OnClickListener {
    private ImageView tm_title_personal_iv;//个人按钮
    private ImageView tm_title_comp_iv;//企业按钮
    private ImageView tm_title_farmer_iv;//三农按钮

    private LinearLayout tm_person_ly;//个人——页面
    private TextView tm_btn_person_notice_tv;//个人——资讯
    private TextView tm_btn_person_moments_tv;//个人——朋友圈
    private TextView tm_btn_person_local_tv;//个人——附近的人
    private TextView tm_btn_person_scan_tv;//个人——扫一扫
    private TextView tm_btn_person_myscore_tv;//个人——我的积分

    private LinearLayout tm_comp_ly;//企业——页面
    private TextView tm_btn_comp_notice_tv;//企业——资讯
    private TextView tm_btn_comp_goods_tv;//企业——商品
    private TextView tm_btn_comp_order_tv;//企业——订单

    private LinearLayout tm_farmer_ly;//三农——页面
    private TextView tm_btn_farmer_notice_tv;//三农——资讯
    private TextView tm_btn_farmer_goods_tv;//三农——商品
    private TextView tm_btn_farmer_order_tv;//三农——订单

    ImageView[] tabs;
    LinearLayout[] layouts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tm_fragment_tm, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    private void initViews() {
        tm_title_personal_iv = (ImageView) getActivity().findViewById(R.id.tm_top_person_iv);
        tm_title_comp_iv = (ImageView) getActivity().findViewById(R.id.tm_top_comp_iv);
        tm_title_farmer_iv = (ImageView) getActivity().findViewById(R.id.tm_top_farmer_iv);

        tm_person_ly = (LinearLayout) getActivity().findViewById(R.id.tm_person_ly);
        tm_btn_person_notice_tv = (TextView) getActivity().findViewById(R.id.tm_person_notice_tv);
        tm_btn_person_moments_tv = (TextView) getActivity().findViewById(R.id.tm_person_moment_tv);
        tm_btn_person_local_tv = (TextView) getActivity().findViewById(R.id.tm_person_local_tv);
        tm_btn_person_scan_tv = (TextView) getActivity().findViewById(R.id.tm_person_scan_tv);
        tm_btn_person_myscore_tv = (TextView) getActivity().findViewById(R.id.tm_person_myscore_tv);

        tm_comp_ly = (LinearLayout) getActivity().findViewById(R.id.tm_comp_ly);
        tm_btn_comp_notice_tv = (TextView) getActivity().findViewById(R.id.tm_comp_notice_tv);
        tm_btn_comp_goods_tv = (TextView) getActivity().findViewById(R.id.tm_comp_goods_tv);
        tm_btn_comp_order_tv = (TextView) getActivity().findViewById(R.id.tm_comp_order_tv);

        tm_farmer_ly = (LinearLayout) getActivity().findViewById(R.id.tm_farmer_ly);
        tm_btn_farmer_notice_tv = (TextView) getActivity().findViewById(R.id.tm_farmer_notice_tv);
        tm_btn_farmer_goods_tv = (TextView) getActivity().findViewById(R.id.tm_farmer_goods_tv);
        tm_btn_farmer_order_tv = (TextView) getActivity().findViewById(R.id.tm_farmer_order_tv);

        tm_title_personal_iv.setOnClickListener(this);
        tm_title_comp_iv.setOnClickListener(this);
        tm_title_farmer_iv.setOnClickListener(this);

        tm_btn_person_notice_tv.setOnClickListener(this);
        tm_btn_person_moments_tv.setOnClickListener(this);
        tm_btn_person_local_tv.setOnClickListener(this);
        tm_btn_person_scan_tv.setOnClickListener(this);
        tm_btn_person_myscore_tv.setOnClickListener(this);

        tm_btn_comp_notice_tv.setOnClickListener(this);
        tm_btn_comp_goods_tv.setOnClickListener(this);
        tm_btn_comp_order_tv.setOnClickListener(this);

        tm_btn_farmer_notice_tv.setOnClickListener(this);
        tm_title_comp_iv.setOnClickListener(this);
        tm_title_farmer_iv.setOnClickListener(this);
        tm_title_farmer_iv.setOnClickListener(this);

        tabs = new ImageView[]{tm_title_personal_iv, tm_title_comp_iv, tm_title_farmer_iv};
        layouts = new LinearLayout[]{tm_person_ly, tm_comp_ly, tm_farmer_ly};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tm_top_person_iv:
            case R.id.tm_top_comp_iv:
            case R.id.tm_top_farmer_iv:
                for(int i = 0; i < 3; i++){
                    if(tabs[i].getId() == v.getId()){
                        changeLayout(i);
                        return;
                    }
                }
                break;
//            case R.id.tm_person_notice_tv:
//            case R.id.tm_comp_notice_tv:
//            case R.id.tm_farmer_notice_tv:
//                Intent intentNotice = new Intent(this.getActivity(), TmiNoticeActivity.class);
//                this.startActivity(intentNotice);
//                break;
            default:
                Toast.makeText(this.getActivity(),"正在开发中...",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void changeLayout(int inedx) {
        switch (inedx){
            case 0:
                tabs[0].setImageResource(R.drawable.tm_geren_pressed);
                tabs[1].setImageResource(R.drawable.tm_qiye_norm);
                tabs[2].setImageResource(R.drawable.tm_sannong_normal);
                break;
            case 1:
                tabs[0].setImageResource(R.drawable.tm_geren_normal);
                tabs[1].setImageResource(R.drawable.tm_qiye_pressed);
                tabs[2].setImageResource(R.drawable.tm_sannong_normal);
                break;
            case 2:
                tabs[0].setImageResource(R.drawable.tm_geren_normal);
                tabs[1].setImageResource(R.drawable.tm_qiye_norm);
                tabs[2].setImageResource(R.drawable.tm_sannong_pressed);
                break;
        }
        for(int i = 0; i < 3; i++){
            layouts[i].setVisibility(i == inedx ? View.VISIBLE : View.GONE);
        }
    }

}