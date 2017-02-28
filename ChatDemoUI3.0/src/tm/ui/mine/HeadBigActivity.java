package tm.ui.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xbh.tmi.R;
import tm.utils.ImageLoaders;
import tm.utils.ViewUtil;
import tm.widget.RKCloudChatTouchImageView;
public class HeadBigActivity extends Activity implements View.OnTouchListener{
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    int status = 1;
    String [] str = {
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3957406844,3939747609&fm=23&gp=0.jpg",
            "http://a3.qpic.cn/psb?/V14fxJhp0IQ0iR/PToIp.GUEDdga1ZCO4vGBQRcZ3nYdN5582owshml65Y!/b/dB8BAAAAAAAA&bo=WgBaAAAAAAADACU!&rf=viewer_4",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487823153747&di=2f7c94cf5d513fb493bf2ad1c656ffea&imgtype=0&src=http%3A%2F%2Fwww.2cto.com%2Fuploadfile%2F2013%2F0319%2F20130319105905623.png",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487823153744&di=ef0a8e499b89530e9f39ffa4aafa6134&imgtype=0&src=http%3A%2F%2Fwww.hzhuti.com%2Fuploads%2Fallimg%2F140315%2F212Z46016-4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487823153746&di=e009a3bc743f4d9c98d2ac17a91511b6&imgtype=0&src=http%3A%2F%2Fimg13.3lian.com%2Fedu201311%2Fshouji%2Fs02%2F201311%2F1a8902d2355deb0740001924a174c37d.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487823153746&di=fa522732b15526aed07cdec8085cd7fc&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20120208%2FImg334081190.jpg"
    };

    private RKCloudChatTouchImageView head_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_head_big);
        head_iv = (RKCloudChatTouchImageView) findViewById(R.id.head_big_iv);




        String path = getIntent().getExtras().getString("path");
        if (!TextUtils.isEmpty(path)) {
            imageLoaders.loadImage(head_iv,path);
        }else if(!TextUtils.isEmpty(getIntent().getExtras().getString("filePath"))){
            path = getIntent().getExtras().getString("filePath");
            head_iv.setImageBitmap(BitmapFactory.decodeFile(path));
        }
//        TODO 替换上面的方法，将图片的数组传递过来，调用下面的方法，图片地址中不能出现特殊符号
//        imageLoaders.loadImage(head_iv,str[status]);
//        gestureDetector = new GestureDetector(this,onGestureListener);
//        head_iv.setOnTouchListener(this);



        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(HeadBigActivity.this);
            }
        });


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
        Log.e("Lking","status = "+status);
        head_iv.refreshDrawableState();
        switch (action) {
            case RIGHT:
                Log.e("Lking","右滑(减)");
                switch (status){
                    case 0:
                        Log.e("Lking","第"+"<1>"+"张图片地址 = "+str[status]);
                        imageLoaders.loadImage(head_iv,str[status]);
                        Toast.makeText(this,"已滑到第一张",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if(status<=5){
                            status--;
                        }
                        Log.e("Lking","往右滑"+status);
                        Log.e("Lking","第"+status+"张图片地址 = "+str[status]);
                        imageLoaders.loadImage(head_iv,str[status]);
                        break;
                }
                break;
            case LEFT:
                Log.e("Lking","左滑（加）");
                switch (status){
                    case 5:
                        Log.e("Lking","第6张图片地址 = "+str[status]);
                        imageLoaders.loadImage(head_iv,str[status]);
                        Toast.makeText(this,"已滑到最后一张",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if(status>=0){
                            status++;
                        }
                        Log.e("Lking","往左滑"+status);
                        Log.e("Lking","图片地址 = "+str[status]);
                        imageLoaders.loadImage(head_iv,str[status]);
                        break;
                }
                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return gestureDetector.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                Log.e("Lking","滑动的监听事件");
                return gestureDetector.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
                return gestureDetector.onTouchEvent(event);
            default:
                return false;
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
