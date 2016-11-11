package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbh.tmi.R;

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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_release_friend);
      initView();

   }

   @Override
   public void onClick(View v) {
      switch (v.getId()){
         case R.id.release_back_iv://返回按钮
            ViewUtil.backToOtherActivity(ReleaseFriendActivity.this);
            break;
         case R.id.release_center_ok_tv://发表按钮
            //输入框内容获取
            String release_edt = mEditText.getText().toString();
            //不同的接口的调用
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
   }
}
