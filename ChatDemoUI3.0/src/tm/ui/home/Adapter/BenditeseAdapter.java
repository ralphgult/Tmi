package tm.ui.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbh.tmi.R;
import com.xbh.tmi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tm.db.dao.FriendDao;
import tm.utils.ImageLoaders;

/**
 * Created by meixi on 2016/9/5.
 */
public class BenditeseAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap> list;
    Handler handler;


    private ImageLoaders imageLoaders = new ImageLoaders(context,
            new imageLoaderListener());

    public class imageLoaderListener implements ImageLoaders.ImageLoaderListener {
        @Override
        public void onImageLoad(View v, Bitmap bmp, String url) {
            ImageView iv = (ImageView) v;
            iv.setImageBitmap(bmp);
        }
    }

    public BenditeseAdapter(Context context, ArrayList<HashMap> list) {
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

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        ViewHolder holder = null;
        final Map map = list.get(index);
        SharedPreferences sharedPre=context.getSharedPreferences("config",context.MODE_PRIVATE);
        final String username=sharedPre.getString("username", "");
        String phone=sharedPre.getString("phone", "");
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.tm_benditese_item_ly,parent, false);
            holder.img_pic = (ImageView) view.findViewById(R.id.img_pic);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            holder.tv_distances = (TextView) view.findViewById(R.id.tv_distances);
            holder.img_add = (ImageView) view.findViewById(R.id.img_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_title.setText(map.get("name")+"");
        holder.tv_desc.setText(map.get("desc")+"");
        if(!TextUtils.isEmpty(map.get("distance").toString())){
            double i = Double.parseDouble(map.get("distance").toString());
            if(i>10000){
                holder.tv_distances.setText("未共享位置");
            }else{
                holder.tv_distances.setText(map.get("distance")+"公里");
            }
        }else{
            holder.tv_distances.setText("未共享位置");
        }

        imageLoaders.loadImage(holder.img_pic, map.get("photo")+"");
        holder.img_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(username.equals(map.get("userid")+"")){
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
        return view;
    }

    class ViewHolder {
        public ImageView img_pic;
        public TextView tv_title;
        public TextView tv_desc;
        public TextView tv_distances;
        public ImageView img_add;
    }

}

