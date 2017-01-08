package tm.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import com.xbh.tmi.R;

/**
 * Created by Administrator on 2017/1/8.
 */

public class VideoViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview_activity);


        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        VideoView videoView = (VideoView) this.findViewById(R.id.videoView);

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

//        //开始播放视频
        videoView.start();

        videoView.requestFocus();
    }
        class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText( VideoViewActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
            }
        }
}
