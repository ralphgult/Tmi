package tm.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.xbh.tmi.R;

import tm.utils.ViewUtil;

public class FeedbackActivity extends Activity {
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
    }
    private void initViews(){
        back = (ImageView) findViewById(R.id.feedback_back_iv);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(FeedbackActivity.this);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ViewUtil.backToOtherActivity(this);
        }
        return true;
    }
}
