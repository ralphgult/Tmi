package tm.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.xbh.tmi.R;

/**
 * Created by Administrator on 2017/1/8.
 */


public class VideoViewActivity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview_activity);
        handler = new Handler();
        final ProgressDialog pd = new ProgressDialog(VideoViewActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        pd.setMessage("加载中...");
        Uri uri = Uri.parse(getIntent().getStringExtra("video"));

        VideoView videoView = (VideoView) this.findViewById(R.id.videoView);

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                },3000);
            }
        });

        //设置视频路径
        videoView.setVideoURI(uri);

//        //开始播放视频
        videoView.start();
        pd.show();
        videoView.requestFocus();
    }
        class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText( VideoViewActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
            }

        }
}
