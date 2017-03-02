package tm.ui;

/**
 * Created by meixi on 2016/8/30.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.xbh.tmi.R;
import com.xbh.tmi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tm.db.dao.FriendDao;
import tm.utils.ImageLoaders;
import tm.video.VideoViewActivity;


/***
 * 首页信息适配器
 *
 * @author lenovo
 */
public class WelcomeListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap> list;
    Handler handler;
    private int mtype;
    private String mid;
    private Handler mhandle;


    private ImageLoaders imageLoaders = new ImageLoaders(context,
            new imageLoaderListener());

    public class imageLoaderListener implements ImageLoaders.ImageLoaderListener {
        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ImageView iv = (ImageView) v;
            iv.setImageBitmap(bmp);
        }
    }
    public WelcomeListAdapter(Context context, ArrayList<HashMap> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return Long.parseLong(list.get(arg0).get("userid").toString());
    }
    public long getItemzhanghao(int arg0) {
        return Long.parseLong(list.get(arg0).get("uname").toString());
    }
    public String getItemName(int arg0) {
        return list.get(arg0).get("userName").toString();
    }
    public int setType(int type) {
        mtype=type;
        return mtype;
    }

    public Handler setHandler(Handler add) {
        mhandle=add;
        return mhandle;
    }
    @Override
    public View getView(int index, View view, ViewGroup parent) {
        ViewHolder holder = null;
        final Map map = list.get(index);
        SharedPreferences sharedPre=context.getSharedPreferences("config",context.MODE_PRIVATE);
        final String username=sharedPre.getString("username", "");
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.welcome_list_item,parent, false);
            holder.img_pic = (ImageView) view.findViewById(R.id.img_pic);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            holder.tv_distances = (TextView) view.findViewById(R.id.tv_distances);
            holder.img_add = (ImageView) view.findViewById(R.id.img_add);
            holder.img_guanzhu = (ImageView) view.findViewById(R.id.img_guanzhu);
            holder.img_shipin = (ImageView) view.findViewById(R.id.img_shipin);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //热荐判断视频、关注
        if(mtype==1){
            holder.img_add.setVisibility(View.VISIBLE);
            holder.img_guanzhu.setVisibility(View.VISIBLE);
            holder.img_shipin.setVisibility(View.INVISIBLE);
            if(new FriendDao().isExist(map.get("uname")+"")){
                holder.img_guanzhu.setImageResource(R.drawable.tm_guanzhu_normal);
            }else{
                holder.img_guanzhu.setImageResource(R.drawable.tm_guanzhu_pressed);
            }
        }else{
            holder.img_add.setVisibility(View.INVISIBLE);
            holder.img_guanzhu.setVisibility(View.INVISIBLE);
            holder.img_shipin.setVisibility(View.VISIBLE);
        }
        holder.tv_title.setText(map.get("name")+"");
        holder.tv_desc.setText(map.get("desc")+"");
        double i = Double.parseDouble(map.get("distance").toString());
        if(i>10000){
            holder.tv_distances.setText("未共享位置");
        }else{
            holder.tv_distances.setText(map.get("distance")+"公里");
        }

        imageLoaders.loadImage(holder.img_pic, map.get("photo")+"");
        holder.img_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(username.equalsIgnoreCase(map.get("userid")+"")){
                    Toast.makeText(context,"不能和自己聊天!",Toast.LENGTH_SHORT).show();
                    return;
                }
                FriendDao fd=new FriendDao();
                if(fd.isExist(map.get("uname")+"")){
                    context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId",map.get("uname")+""));
                }else{
                    Toast.makeText(context,"还不是好友不能聊天!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        final ViewHolder finalHolder = holder;
        holder.img_guanzhu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("info","=====add=好友===="+map.get("uname")+"");
                FriendDao fd=new FriendDao();
                if(fd.isExist(map.get("uname")+"")){
                    Toast.makeText(context,"已经是好友了!",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        EMClient.getInstance().contactManager().addContact(map.get("uname")+"", "赶紧加好友吧");
                        Toast.makeText(context,"您已关注对方，请求配对！",Toast.LENGTH_SHORT).show();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.img_shipin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(map.get("video")+"")){
                    context.startActivity(new Intent(context, VideoViewActivity.class).putExtra("video",map.get("video")+""));
                }else{
                    Toast.makeText(context,"还没有上传视频!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    class ViewHolder {
        public ImageView img_pic;
        public TextView tv_title;
        public TextView tv_desc;
        public TextView tv_distances;
        public ImageView img_add;
        public ImageView img_guanzhu;
        public ImageView img_shipin;
    }
}

