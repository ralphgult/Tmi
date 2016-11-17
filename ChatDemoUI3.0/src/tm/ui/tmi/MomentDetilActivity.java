package tm.ui.tmi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.tmi.adapter.ImageAdapter;
import tm.ui.tmi.adapter.ReplyAdapter;
import tm.utils.ImageLoaders;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

public class MomentDetilActivity extends Activity {
    private ImageView head_iv;
    private TextView name_tv;
    private TextView time_tv;
    private TextView content_tv;
    private GridView pic_gv;
    private ImageView pick_single;
    private LinearLayout count;
    private TextView count_text;
    private LinearLayout like;
    private ImageView like_img;
    private TextView like_text;
    private LinearLayout comment;
    private TextView comment_text;
    private TextView title_tv;
    private ListView replyList;
    private ImageView back_iv;

    private int type;
    private boolean isLike;
    private boolean isFinish;
    private ImageAdapter mImgAdapter;
    private ImageLoaders imageLoaders;
    private ReplyAdapter replyAdapter;
    private List<Map<String, String>> replysList = null;
    private InputDialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2001:
                    isFinish = true;
                    if (isLike) {
                        Toast.makeText(MomentDetilActivity.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
                        like_img.setImageResource(R.drawable.tm_zan_p);
                        like_text.setText(Integer.valueOf(like_text.getText().toString()) + 1 + "");
                    }else{
                        Toast.makeText(MomentDetilActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        like_img.setImageResource(R.drawable.tm_zan);
                        like_text.setText(Integer.valueOf(like_text.getText().toString()) - 1 + "");

                    }
                    isLike = !isLike;
                    break;
                case 3001:
                    isFinish = true;
                    Toast.makeText(MomentDetilActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                    Map<String,String> map = new HashMap<>();
                    map.put("userName", "我");
                    map.put("comment", (String)msg.obj);
                    map.put("userId", sp.getString("username","无Id"));
                    map.put("replyId", 0 + "");
                    replysList.add(map);
                    replyAdapter.resetData(replysList);
                    break;
                default:
                    Toast.makeText(MomentDetilActivity.this, "系统繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detil);
        initViews();
        initData();
        setData();
    }

    private void initData() {
        imageLoaders = new ImageLoaders(this, new imageLoaderLinsters());
        type = getIntent().getExtras().getInt("type");
        if (type == 4) {
            title_tv.setText("朋友圈");
        } else {
            title_tv.setText("资讯");
        }
    }

    private void initViews() {
        title_tv = (TextView) findViewById(R.id.moment_detil_title_text);
        back_iv = (ImageView) findViewById(R.id.moment_detil_back_iv);
        head_iv = (ImageView) findViewById(R.id.moment_item_list_iv);
        name_tv = (TextView) findViewById(R.id.yx_monment_name);
        time_tv = (TextView) findViewById(R.id.yx_monment_time);
        content_tv = (TextView) findViewById(R.id.yx_monment_content);
        pic_gv = (GridView) findViewById(R.id.yx_monment_image_gv);
        pick_single = (ImageView) findViewById(R.id.yx_moment_single_imgae_iv);
        count = (LinearLayout) findViewById(R.id.yx_moment_count);
        count_text = (TextView) findViewById(R.id.yx_monment_count_text);
        like = (LinearLayout) findViewById(R.id.yx_moment_like);
        like_img = (ImageView) findViewById(R.id.yx_monment_like_img);
        like_text = (TextView) findViewById(R.id.yx_monment_like_test);
        comment = (LinearLayout) findViewById(R.id.yx_moment_comment);
        comment_text = (TextView) findViewById(R.id.yx_monment_comment_test);
        replyList = (ListView) findViewById(R.id.reply_list);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        PersonManager.momentLike(getIntent().getExtras().getString("momentId"), mHandler, 0);
                    }
                }.start();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(0);
            }
        });
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isFinish", isFinish);
                ViewUtil.backToActivityForResult(MomentDetilActivity.this,2,intent);
            }
        });
    }

    private void setData() {
        SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
        name_tv.setText(getIntent().getExtras().getString("name"));
        time_tv.setText(getIntent().getExtras().getString("time"));
        count_text.setText(getIntent().getExtras().getString("count"));
        content_tv.setText(getIntent().getExtras().getString("content"));
        like_text.setText(getIntent().getExtras().getString("like"));
        comment_text.setText(getIntent().getExtras().getString("comment"));
        imageLoaders.loadImage(head_iv, getIntent().getExtras().getString("headImg"));
        String pics = getIntent().getExtras().getString("pics");
        if (!TextUtils.isEmpty(pics)) {
            if (pics.contains(",")) {
                pic_gv.setVisibility(View.VISIBLE);
                pick_single.setVisibility(View.GONE);
                mImgAdapter = new ImageAdapter(this, false);
                mImgAdapter.resetData(pics.split(","));
                pic_gv.setAdapter(mImgAdapter);
                SysUtils.setGridViewHight(pic_gv);
            } else {
                pic_gv.setVisibility(View.GONE);
                pick_single.setVisibility(View.VISIBLE);
                imageLoaders.loadImage(pick_single, pics);
            }
        }else{
            pick_single.setVisibility(View.GONE);
            pic_gv.setVisibility(View.GONE);
        }
        String likeList = getIntent().getExtras().getString("likelist");
        isLike = likeList.contains(sharedPre.getString("username", ""));
        like_img.setImageResource(isLike ? R.drawable.tm_zan_p : R.drawable.tm_zan);
        String reply = getIntent().getExtras().getString("reply");
        if (!TextUtils.isEmpty(reply)) {
            try {
                JSONObject object = new JSONObject("{\"reply\":" + reply + "}");
                JSONArray array = object.getJSONArray("reply");
                int size = array.length();
                replysList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("userName", obj.getString("userName"));
                    map.put("comment", obj.getString("comment"));
                    map.put("userId", obj.getString("userId"));
                    map.put("replyId", obj.getString("replyId"));
                    replysList.add(map);
                }
                replyAdapter = new ReplyAdapter(this);
                replyAdapter.resetData(replysList);
                replyList.setAdapter(replyAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class imageLoaderLinsters implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("isFinish", isFinish);
            ViewUtil.backToActivityForResult(MomentDetilActivity.this,2,intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void createDialog(final int position){
        if(mDialog == null){
            mDialog = (InputDialog) DialogFactory.createDialog(this,DialogFactory.DIALOG_TYPE_INPUT);
            mDialog.setEditTextHint("请输入评论内容");
            mDialog.setInputDialogListener(new InputDialog.InputDialogListener() {
                @Override
                public void inputDialogCancle() {
                    mDialog.closeDialog();
                }

                @Override
                public void inputDialogSubmit(final String inputText) {
                    if (!TextUtils.isEmpty(inputText)) {
                        new Thread(){
                            @Override
                            public void run() {
                                PersonManager.momentComment(getIntent().getExtras().getString("momentId"),inputText,mHandler,position);
                            }
                        }.start();
                    }else{
                        Toast.makeText(MomentDetilActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.closeDialog();
                }
            });
        }
        mDialog.showDialog();
    }
}
