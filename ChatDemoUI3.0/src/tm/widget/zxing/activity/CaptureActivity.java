package tm.widget.zxing.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tm.http.Config;
import tm.http.NetFactory;
import tm.ui.mine.FarmerCenterActivity;
import tm.utils.ConstantsHandler;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;
import tm.utils.dialog.ConfirmDialog;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.RemindDialog;
import tm.widget.zxing.camera.CameraManager;
import tm.widget.zxing.decoding.CaptureActivityHandler;
import tm.widget.zxing.decoding.InactivityTimer;
import tm.widget.zxing.view.ViewfinderView;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private RemindDialog confirmDialog;
    private ImageView back_btn;
    private String resultString;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsHandler.EXECUTE_SUCCESS:
                    Map map = (Map) msg.obj;
                    Log.e("info", "map==" + map);
                    String authId = map.get("authId") + "";
                    if (authId.equals("1")) {
                        Toast.makeText(CaptureActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        confirmDialog.closeDialog();
                        ViewUtil.backToOtherActivity(CaptureActivity.this);
                    } else if(authId.equals("2")) {
                        Toast.makeText(CaptureActivity.this, "已经是好友关系", Toast.LENGTH_SHORT).show();
                        confirmDialog.closeDialog();
                        ViewUtil.backToOtherActivity(CaptureActivity.this);
                    }else{
                        Toast.makeText(CaptureActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(CaptureActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_zixing_camera_layout);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        back_btn = (ImageView) findViewById(R.id.scan_back_iv);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.backToOtherActivity(CaptureActivity.this);
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        //quit the scan view
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        resultString = result.getText();
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "扫描失败，请重试", Toast.LENGTH_SHORT).show();
        } else {
            createDialog("是否添加该用户为好友");
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void createDialog(String text) {
        confirmDialog = (RemindDialog) DialogFactory.createDialog(this, DialogFactory.DIALOG_TYPE_REMIND);
        confirmDialog.setGroupName(text);
        confirmDialog.setPhotoDialogListener(new RemindDialog.RemindDialogListener() {
            @Override
            public void invita() {
                confirmDialog.closeDialog();
            }

            @Override
            public void remind() {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                SharedPreferences sharedPre = CaptureActivity.this.getSharedPreferences("config", CaptureActivity.this.MODE_PRIVATE);
                String userId = sharedPre.getString("username", "");
                if (!TextUtils.isEmpty(userId)) {
                    list.add(new BasicNameValuePair("me", userId));
                    list.add(new BasicNameValuePair("my", resultString));
                } else {
                    Toast.makeText(CaptureActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    return;
                }
                NetFactory.instance().commonHttpCilent(mHandler, CaptureActivity.this,
                        Config.URL_GET_ADDFRIEND, list);
            }
        });
        confirmDialog.showDialog();
    }
}