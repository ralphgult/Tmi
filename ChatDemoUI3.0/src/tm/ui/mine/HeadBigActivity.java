package tm.ui.mine;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.tmdemo.R;

public class HeadBigActivity extends Activity {
    private ImageView head_iv;
    private ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_big);
        head_iv = (ImageView) findViewById(R.id.head_big_iv);
        head_iv.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("path")));
}
}
