package tm.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import tm.ui.setting.SettingActivity;
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
    private TextView myMessage_tv;
    private ImageView myHead_iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me_fragment_tm, container, false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.fgt_mine_setting_tv:
                intent = new Intent(this.getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_person_center_tv:
                intent = new Intent(this.getActivity(), PersonCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_comp_center_tv:
                intent = new Intent(this.getActivity(), CompCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_farmer_center_tv:
                intent = new Intent(this.getActivity(), FarmerCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_my_order_tv:
                intent = new Intent(this.getActivity(), MineOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_my_shopping_tv:
                intent = new Intent(this.getActivity(), MySoppingActivity.class);
                startActivity(intent);
                break;
//            case R.id.mine_my_address_tv:
//                intent = new Intent(this.getActivity(), MyAddressActivity.class);
//                startActivity(intent);
//                break;
            case R.id.mine_my_message_tv:
                intent = new Intent(this.getActivity(), MyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.fgt_mine_head_iv:
                Bundle bundle = new Bundle();
                bundle.putString("path","");
                ViewUtil.jumpToOtherActivity(this.getActivity(),HeadBigActivity.class,bundle);
                break;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        setting_btn = (TextView) getView().findViewById(R.id.fgt_mine_setting_tv);
        personCenter_tv = (TextView) getView().findViewById(R.id.mine_person_center_tv);
        compCenter_tv = (TextView) getView().findViewById(R.id.mine_comp_center_tv);
        farmerCenter_tv = (TextView) getView().findViewById(R.id.mine_farmer_center_tv);

        myOrder_tv = (TextView) getView().findViewById(R.id.mine_my_order_tv);
        myShopping_tv = (TextView) getView().findViewById(R.id.mine_my_shopping_tv);
        myAddress_tv = (TextView) getView().findViewById(R.id.mine_my_address_tv);
        myMessage_tv = (TextView) getView().findViewById(R.id.mine_my_message_tv);
        myHead_iv = (ImageView) getView().findViewById(R.id.fgt_mine_head_iv);

        setting_btn.setOnClickListener(this);
        personCenter_tv.setOnClickListener(this);
        compCenter_tv.setOnClickListener(this);
        farmerCenter_tv.setOnClickListener(this);

        myOrder_tv.setOnClickListener(this);
        myShopping_tv.setOnClickListener(this);
        myAddress_tv.setOnClickListener(this);
        myMessage_tv.setOnClickListener(this);
        myHead_iv.setOnClickListener(this);


    }
}

