package tm.ui.tmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tm.manager.PersonManager;
import tm.ui.BigBackGroundActivity;
import tm.ui.mine.HeadBigActivity;
import tm.utils.ImageLoaders;
import tm.utils.SysUtils;
import tm.utils.ViewUtil;
import tm.utils.dialog.DialogFactory;
import tm.utils.dialog.InputDialog;

/**
 * Created by RalphGult on 9/29/2016.
 */

public class MomentListAdapter extends BaseAdapter {
    private final String mUserId;
    private List<Map<String, String>> dataList = null;
    private ViewHolder vh;
    private Context mContext;
    private boolean mIsMoment;
    private ImageAdapter mImageAdapter;
    private InputDialog mDialog;

    private ImageLoaders imageLoaders = new ImageLoaders(mContext, new imageLoaderListener());
    private Handler mHandler;

    public class imageLoaderListener implements ImageLoaders.ImageLoaderListener {

        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ((ImageView) v).setImageBitmap(bmp);
        }
    }

    public MomentListAdapter(Context context, boolean isMoment, Handler handler) {
        mContext = context;
        mIsMoment = isMoment;
        mHandler = handler;
        dataList = new ArrayList<>();
        mImageAdapter = new ImageAdapter(mContext, false);
        SharedPreferences sharedPre = mContext.getSharedPreferences("config", mContext.MODE_PRIVATE);
        mUserId = sharedPre.getString("username","无Id");
    }

    public void resetData(List<Map<String, String>> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.yx_monent_list_item, null);
            vh = new ViewHolder();
            vh.head_iv = (ImageView) view.findViewById(R.id.moment_item_list_iv);
            vh.name_tv = (TextView) view.findViewById(R.id.yx_monment_name);
            vh.time_tv = (TextView) view.findViewById(R.id.yx_monment_time);
            vh.content_tv = (TextView) view.findViewById(R.id.yx_monment_content);
            vh.pic_gv = (GridView) view.findViewById(R.id.yx_monment_image_gv);
            vh.pick_single = (ImageView) view.findViewById(R.id.yx_moment_single_imgae_iv);
            vh.count = (LinearLayout) view.findViewById(R.id.yx_moment_count);
            vh.count_text = (TextView) view.findViewById(R.id.yx_monment_count_text);
            vh.like = (LinearLayout) view.findViewById(R.id.yx_moment_like);
            vh.like_img = (ImageView) view.findViewById(R.id.yx_monment_like_img);
            vh.like_text = (TextView) view.findViewById(R.id.yx_monment_like_test);
            vh.comment = (LinearLayout) view.findViewById(R.id.yx_moment_comment);
            vh.comment_text = (TextView) view.findViewById(R.id.yx_monment_comment_test);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        imageLoaders.loadImage(vh.head_iv, dataList.get(position).get("headImg"));
        vh.name_tv.setText(dataList.get(position).get("name"));
        vh.time_tv.setText(dataList.get(position).get("time"));
        vh.content_tv.setText(dataList.get(position).get("content"));
        vh.content_tv.setMaxLines(3);
        vh.count_text.setText(dataList.get(position).get("count"));
        vh.like_text.setText(dataList.get(position).get("like"));
        String likes = dataList.get(position).get("likelist");
        vh.like_img.setImageResource(likes.contains(mUserId) ? R.drawable.tm_zan_p : R.drawable.tm_zan);
        vh.comment_text.setText(dataList.get(position).get("comment"));
        vh.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        PersonManager.momentLike(dataList.get(position).get("momentId"),mHandler,position);
                    }
                }.start();
            }
        });
        vh.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(position);
            }
        });
        if (TextUtils.isEmpty(dataList.get(position).get("pics"))) {
            vh.pic_gv.setVisibility(View.GONE);
            vh.pick_single.setVisibility(View.GONE);
        } else {
            final String pics = dataList.get(position).get("pics");
            if (pics.contains(",")) {
                vh.pic_gv.setVisibility(View.VISIBLE);
                vh.pick_single.setVisibility(View.GONE);
                mImageAdapter.resetData(pics.split(","));
                vh.pic_gv.setAdapter(mImageAdapter);
                SysUtils.setGridViewHight(vh.pic_gv);
            } else {
                vh.pic_gv.setVisibility(View.GONE);
                vh.pick_single.setVisibility(View.VISIBLE);
                imageLoaders.loadImage(vh.pick_single, pics);
                vh.pick_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        String []str = {pics};
                        bundle.putStringArray("path",str);
                        bundle.putInt("status",0);
//                        ViewUtil.jumpToOtherActivity((Activity)mContext, HeadBigActivity.class, bundle);
                        ViewUtil.jumpToOtherActivity((Activity)mContext, BigBackGroundActivity.class, bundle);
                    }
                });
            }
        }
        return view;
    }

    static class ViewHolder {
        ImageView head_iv;
        TextView name_tv;
        TextView time_tv;
        TextView content_tv;
        GridView pic_gv;
        ImageView pick_single;
        LinearLayout count;
        TextView count_text;
        LinearLayout like;
        ImageView like_img;
        TextView like_text;
        LinearLayout comment;
        TextView comment_text;

    }



    public void setLike(int position){
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        String likes = dataList.get(position).get("likelist");
        Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(likes) || !likes.contains(mUserId)) {
            String count = dataList.get(position).get("like");
            dataList.get(position).put("like",Integer.valueOf(count) + 1 + "");
            dataList.get(position).put("likelist", likes + "," + sharedPre.getString("username", ""));
        }else{
            String count = dataList.get(position).get("like");
            dataList.get(position).put("like",Integer.valueOf(count) - 1 + "");
            if (likes.contains(",")) {
                int index = likes.indexOf(sharedPre.getString("username", ""));
                if (index == 0) {
                    dataList.get(position).put("likelist", likes.replace(sharedPre.getString("username","") + ",", ""));
                }else{
                    dataList.get(position).put("likelist", likes.replace("," + sharedPre.getString("username",""), ""));
                }
            }else{
                dataList.get(position).put("likelist", "");
            }
        }
        notifyDataSetChanged();
    }

    public void setComment(String reply,int position){
        String comment = dataList.get(position).get("reply");
        String newComment = null;
        try {
            JSONObject object = new JSONObject();
            object.put("userId",mUserId);
            object.put("userName","");
            object.put("replyId",0);
            object.put("comment",reply);
            newComment = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataList.get(position).put("reply",comment.substring(0,comment.length() - 1) + "," + newComment + "]");
        String commentCount = dataList.get(position).get("comment");
        dataList.get(position).put("comment",Integer.valueOf(commentCount) + 1 + "");
        notifyDataSetChanged();
    }

    public void createDialog(final int position){
        if(mDialog == null){
            mDialog = (InputDialog)DialogFactory.createDialog(mContext,DialogFactory.DIALOG_TYPE_INPUT);
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
                                PersonManager.momentComment(dataList.get(position).get("momentId"),inputText,mHandler,position);
                            }
                        }.start();
                    }else{
                        Toast.makeText(mContext, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.closeDialog();
                }
            });
        }
        mDialog.showDialog();
    }
}
