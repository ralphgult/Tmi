package tm.ui.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.tmdemo.Constant;
import com.hyphenate.tmdemo.R;

import tm.utils.ImageLoaders;

public class HeadBigActivity extends Activity {
    private ImageView head_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_big);
        head_iv = (ImageView) findViewById(R.id.head_big_iv);
        String path = getIntent().getExtras().getString("path");
        if (!TextUtils.isEmpty(path)) {
            imageLoaders.loadImage(head_iv,path);
        }else {
            path = getIntent().getExtras().getString("filePath");
            if(!TextUtils.isEmpty(path)){
                head_iv.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }
    }

    private ImageLoaders imageLoaders = new ImageLoaders(this, new imageLoader());
    private class imageLoader implements ImageLoaders.ImageLoaderListener{

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView)v).setImageBitmap(bmp);
        }
    }
}
