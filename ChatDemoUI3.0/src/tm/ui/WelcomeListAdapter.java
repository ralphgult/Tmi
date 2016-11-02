package tm.ui;

/**
 * Created by meixi on 2016/8/30.
 */
import android.content.Context;
import android.content.SharedPreferences;
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

import com.xbh.tmi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tm.http.Config;
import tm.http.NetFactory;
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
            holder.img_guanzhu.setVisibility(View.VISIBLE);
            holder.img_shipin.setVisibility(View.INVISIBLE);
        }else{
            holder.img_guanzhu.setVisibility(View.INVISIBLE);
            holder.img_shipin.setVisibility(View.VISIBLE);
        }
        holder.tv_title.setText(map.get("name")+"");
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
        holder.img_guanzhu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPre=context.getSharedPreferences("config",context.MODE_PRIVATE);
                String username=sharedPre.getString("username", "");
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("me",username ));
                list.add(new BasicNameValuePair("my", map.get("userid")+""));
                NetFactory.instance().commonHttpCilent(mhandle, context,
                        Config.URL_GET_ADDFRIEND, list);
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

