package tm.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xbh.tmi.R;

/**
 * Created by LKing on 2017/1/12.
 */

public class ActionActivity extends Activity {
    ImageView mBackImg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_activity);
        initView();
    }
    private void initView() {
        goBackImg();

    }

    /*** 标题的返回按钮初始化并监听点击事件 */
    private void goBackImg() {
        mBackImg = (ImageView)findViewById(R.id.auction_back_iv);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
