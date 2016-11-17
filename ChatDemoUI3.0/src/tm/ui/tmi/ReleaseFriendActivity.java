package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import java.util.ArrayList;
import java.util.List;

import tm.manager.PersonManager;
import tm.ui.login.RegistActivity;
import tm.ui.tmi.adapter.ImageAdapter;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ReleaseFriendActivity extends Activity implements View.OnClickListener{
   private int isRelease = 0;
   private ImageView mBackImg;
   private TextView mTitleTxt;
   private TextView mReleaseTxt;
   private EditText mEditText;
   private GridView mImgGridView;
   private ImageAdapter mAdapter;
   private String[] imgpaths;
   private List<String> imgPathList;
   private String content;
   private Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
         if(msg.what == 1001){
            String text = null;
            if (isRelease == 4) {
               text = "发布随笔成功";
            }else{
               text = "发布资讯成功";
            }
            Toast.makeText(ReleaseFriendActivity.this, text, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("publishFinish",1);
            ViewUtil.backToActivityForResult(ReleaseFriendActivity.this,1,intent);
         }else{
            Toast.makeText(ReleaseFriendActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
         }
      }
   };

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_release_friend);
      initData();
      initView();

   }

   private void initData() {
      mAdapter = new ImageAdapter(this,true);
      imgPathList = new ArrayList<>();
      imgPathList.add("0");
      imgpaths = new String[9];
      for (int i = 0; i < imgPathList.size(); i++) {
         imgpaths[i] = imgPathList.get(i);
      }
      mAdapter.resetData(imgpaths);
   }

   public void setAdapterData(String imgPath) {
      if(imgPathList.size() < 9) {
         imgPathList.add(imgPathList.size() - 1,imgPath);
      }else {
         imgPathList.remove(imgPathList.size() - 1);
         imgPathList.add(imgPath);
      }
      imgpaths = new String[9];
      for (int i = 0; i < imgPathList.size(); i++) {
         imgpaths[i] = imgPathList.get(i);
      }
      mAdapter.resetData(imgpaths);
      SysUtils.setGridViewHight(mImgGridView);
   }

   public void delAdapterData(String imgPath) {
      imgPathList.remove(imgPath);
      if (imgPathList.size() == 8 && !imgPathList.contains("0")) {
         imgPathList.add("0");
      }
      imgpaths = new String[9];
      for (int i = 0; i < imgPathList.size(); i++) {
         imgpaths[i] = imgPathList.get(i);
      }
      mAdapter.resetData(imgpaths);
      SysUtils.setGridViewHight(mImgGridView);
   }
   @Override
   public void onClick(View v) {
      switch (v.getId()){
         case R.id.release_back_iv://返回按钮
            ViewUtil.backToOtherActivity(ReleaseFriendActivity.this);
            break;
         case R.id.release_center_ok_tv://发表按钮
            //输入框内容获取
            content = mEditText.getText().toString();
            //不同的接口的调用
            AddMomentThread thread = new AddMomentThread();
            thread.start();
            break;
      }
   }

   private void initView() {
      mBackImg = (ImageView) findViewById(R.id.release_back_iv);
      mTitleTxt = (TextView) findViewById(R.id.release_title_text);
      mReleaseTxt = (TextView) findViewById(R.id.release_center_ok_tv);
      mEditText = (EditText) findViewById(R.id.release_edt);
      mImgGridView = (GridView) findViewById(R.id.release_add_img_gridview);
      isRelease = getIntent().getExtras().getInt("ReleaseType");
      mImgGridView.setAdapter(mAdapter);
      SysUtils.setGridViewHight(mImgGridView);
      switch (isRelease) {
         case 1:
         case 2:
         case 3:
            mTitleTxt.setText("T觅资讯");
            break;
         case 4:
            mTitleTxt.setText("朋友圈");
            break;
      }
      mBackImg.setOnClickListener(this);
      mReleaseTxt.setOnClickListener(this);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      //获取图片路径
      if (resultCode == Activity.RESULT_OK && data != null) {
         Uri selectedImage = data.getData();
         String[] filePathColumns = {MediaStore.Images.Media.DATA};
         Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
         c.moveToFirst();
         int columnIndex = c.getColumnIndex(filePathColumns[0]);
         setAdapterData(c.getString(columnIndex));
         c.close();
      }
   }

   class AddMomentThread extends Thread{
      @Override
      public void run() {
         if (imgPathList.contains("0")) {
            imgPathList.remove("0");
         }
         PersonManager.publishMoment(content,imgPathList,isRelease,handler);

      }
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
         ViewUtil.backToOtherActivity(this);
      }
      return super.onKeyDown(keyCode,event);
   }
}
