package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

public class TmiNoticeActivity extends Activity implements View.OnClickListener{
    private ImageView back_iv;
    private ListView list_lv;
    private TextView nodata_tv;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmi_notice);
        back_iv = (ImageView) findViewById(R.id.tmi_notice_back_iv);
        list_lv = (ListView) findViewById(R.id.tmi_notice_list_lv);
        nodata_tv = (TextView) findViewById(R.id.tmi_notice_nodata);
        nodata_tv.setVisibility(View.VISIBLE);
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tmi_notice_back_iv:
                this.finish();
                break;
        }
    }
}
