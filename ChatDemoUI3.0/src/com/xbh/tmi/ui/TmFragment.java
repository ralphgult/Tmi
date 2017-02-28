package com.xbh.tmi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import tm.ui.tmi.GoodsManagerAcitivity;
import tm.ui.tmi.MomentsActivity;
import tm.ui.tmi.OrderManagerAcitivity;
import tm.utils.ViewUtil;
import tm.widget.zxing.activity.CaptureActivity;


/**
 * Created by Administrator on 2016/8/21.
 */
public class TmFragment extends Fragment implements View.OnClickListener,View.OnTouchListener{
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    int status = 1;
    private ListView mTmiMoveTxtPer;

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

    private ImageView[] tabs;
    private LinearLayout[] layouts;
    private int type;

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
        gestureDetector = new GestureDetector(getActivity(),onGestureListener);

        tm_title_personal_iv = (ImageView) getActivity().findViewById(R.id.tm_top_person_iv);
        tm_title_comp_iv = (ImageView) getActivity().findViewById(R.id.tm_top_comp_iv);
        tm_title_farmer_iv = (ImageView) getActivity().findViewById(R.id.tm_top_farmer_iv);

        mTmiMoveTxtPer = (ListView) getActivity().findViewById(R.id.tmi_move_per_txt);
        mTmiMoveTxtPer.setOnTouchListener(this);


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

        tm_btn_farmer_order_tv.setOnClickListener(this);
        tm_btn_farmer_notice_tv.setOnClickListener(this);
        tm_btn_farmer_goods_tv.setOnClickListener(this);

        tm_btn_person_notice_tv.setOnClickListener(this);
        tm_btn_person_moments_tv.setOnClickListener(this);
        tm_btn_person_local_tv.setOnClickListener(this);
        tm_btn_person_scan_tv.setOnClickListener(this);
        tm_btn_person_myscore_tv.setOnClickListener(this);

        tm_btn_comp_notice_tv.setOnClickListener(this);
        tm_btn_comp_goods_tv.setOnClickListener(this);
        tm_btn_comp_order_tv.setOnClickListener(this);

        tabs = new ImageView[]{tm_title_personal_iv, tm_title_comp_iv, tm_title_farmer_iv};
        layouts = new LinearLayout[]{tm_person_ly, tm_comp_ly, tm_farmer_ly};


    }
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0 && (Math.abs(x) > 200)&&(Math.abs(y) < 200) ) {
                        Log.e("Lking "," y " +y);
                        doResult(RIGHT);
                    } else if (x < 0 && (Math.abs(x) > 200)&&(Math.abs(y) < 200)) {
                        Log.e("Lking "," y " +y);
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public void doResult(int action) {
        switch (action) {
            case LEFT:
                Log.e("Lking","右滑+status = "+status);
                if (status <= 2) {
                    status++;
                    changeLayout(status - 1);
                }
                Log.e("Lking","右滑完成，status = "+status);
                break;

            case RIGHT:
                Log.e("Lking","左滑+status = "+status);
                if (status > 1) {
                    status--;
                    changeLayout(status - 1);
                }
                Log.e("Lking","左滑完成，status = "+status);
                break;

        }
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.tm_top_person_iv:
            case R.id.tm_top_comp_iv:
            case R.id.tm_top_farmer_iv:
                for(int i = 0; i < 3; i++){
                    if(tabs[i].getId() == v.getId()){
                        changeLayout(i);
                        status = i+1;
                        Log.e("LKing","变换status = "+status);
                        return;
                    }
                }
                break;
            case R.id.tm_person_scan_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(), CaptureActivity.class);
                break;
            case R.id.tm_person_notice_tv:
                type = 1;
                bundle = new Bundle();
                bundle.putInt("type",type);
                ViewUtil.jumpToOtherActivity(this.getActivity(), MomentsActivity.class,bundle);
                break;
            case R.id.tm_comp_notice_tv:
                type = 2;
                bundle = new Bundle();
                bundle.putInt("type",type);
                ViewUtil.jumpToOtherActivity(this.getActivity(), MomentsActivity.class,bundle);
                break;
            case R.id.tm_farmer_notice_tv:
                type = 3;
                bundle = new Bundle();
                bundle.putInt("type",type);
                ViewUtil.jumpToOtherActivity(this.getActivity(), MomentsActivity.class,bundle);
                break;
            case R.id.tm_person_moment_tv:
                type = 4;
                bundle = new Bundle();
                bundle.putInt("type",type);
                ViewUtil.jumpToOtherActivity(this.getActivity(), MomentsActivity.class,bundle);
                break;
            case R.id.tm_comp_order_tv:
            case R.id.tm_farmer_order_tv:
                ViewUtil.jumpToOtherActivity(this.getActivity(), OrderManagerAcitivity.class);
                break;
            case R.id.tm_comp_goods_tv:
                bundle = new Bundle();
                bundle.putInt("type",1);
                ViewUtil.jumpToOtherActivity(this.getActivity(), GoodsManagerAcitivity.class,bundle);
                break;
            case R.id.tm_farmer_goods_tv:
                bundle = new Bundle();
                bundle.putInt("type",2);
                ViewUtil.jumpToOtherActivity(this.getActivity(), GoodsManagerAcitivity.class,bundle);
                break;
            default:
                Toast.makeText(this.getActivity(),"正在调试中...",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.e("Lking","滑动的监听事件");
                return gestureDetector.onTouchEvent(event);
            default:
                Log.e("Lking","滑动的事件");
                return gestureDetector.onTouchEvent(event);
        }
    }
}