package tm.ui;

/**
 * Created by meixi on 2016/8/30.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.tmdemo.R;

import tm.utils.ImageLoaders;


/***
 * 首页信息适配器
 *
 * @author lenovo
 */
public class WelcomeListAdapter extends BaseAdapter {

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

//    public WelcomeListAdapter(Context context, ArrayList<HashMap> list,Handler handler) {
//        this.context = context;
//        this.list = list;
//        this.handler =handler;
//    }
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
    public String getItemName(int arg0) {
        return list.get(arg0).get("userName").toString();
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        ViewHolder holder = null;
        final Map map = list.get(index);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.welcome_list_item,parent, false);
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
        Log.e("info","map.get(\"name\")==="+map.get("name"));
        holder.tv_desc.setText(map.get("desc")+"");
        holder.tv_distances.setText(map.get("distance")+"");
        imageLoaders.loadImage(holder.img_pic, map.get("photo")+"");
        holder.img_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                if(handler!=null){
//                    Message msg =new Message();
//                    msg.what=100;
//                    msg.obj=map.get("userid").toString();
//                    handler.sendMessage(msg);
//                }
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

