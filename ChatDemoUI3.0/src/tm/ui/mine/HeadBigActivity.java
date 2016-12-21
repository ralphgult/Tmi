package tm.ui.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.xbh.tmi.R;

import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.widget.RKCloudChatTouchImageView;

public class HeadBigActivity extends Activity {
    private RKCloudChatTouchImageView head_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_big);
        head_iv = (RKCloudChatTouchImageView) findViewById(R.id.head_big_iv);
        String path = getIntent().getExtras().getString("path");
        if (!TextUtils.isEmpty(path)) {
            imageLoaders.loadImage(head_iv,path);
        }else if(!TextUtils.isEmpty(getIntent().getExtras().getString("filePath"))){
            path = getIntent().getExtras().getString("filePath");
            head_iv.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }

    private ImageLoaders imageLoaders = new ImageLoaders(this, new imageLoader());
    private class imageLoader implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ViewUtil.backToOtherActivity(HeadBigActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
