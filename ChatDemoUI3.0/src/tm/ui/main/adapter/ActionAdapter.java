package tm.ui.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xbh.tmi.R;
import java.util.List;

import tm.ui.main.ActionActivity;
import tm.ui.main.Person;

/**
 * Created by LKing on 2017/1/17.
 */
public class ActionAdapter extends BaseAdapter {
    private List<Person> list;
    private LayoutInflater layoutInflater;

    public ActionAdapter(Context context, List<Person> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.action_activity_item, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.text_spot_price = (TextView) convertView.findViewById(R.id.text_spot_price);
            viewHolder.text_purchasing_direct = (TextView) convertView.findViewById(R.id.text_purchasing_direct);
            viewHolder.text_bid_direct = (TextView) convertView.findViewById(R.id.text_bid_direct);
            viewHolder.text_time = (TextView) convertView.findViewById(R.id.text_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(list.get(position).getPath());
        viewHolder.textView.setText(list.get(position).getName());
        viewHolder.text_spot_price.setText(list.get(position).getPrice());
        viewHolder.text_purchasing_direct.setText(list.get(position).getPurch());
        viewHolder.text_bid_direct.setText(list.get(position).getBid());
        if (!list.get(position).getTime().equals("时间到")) {
            //将一个int类型的数值转换成时间格式
            long tempTime = Long.parseLong(list.get(position).getTime());
            viewHolder.text_time.setTextColor(Color.RED);
            viewHolder.text_time.setText("剩余:"+ActionActivity.formatTime(tempTime));
        } else {
            viewHolder.text_time.setTextColor(Color.GRAY);
            viewHolder.text_time.setText("拍卖时间已过");
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView text_spot_price;
        TextView text_purchasing_direct;
        TextView text_bid_direct;
        TextView text_time;
    }

    int result = 0;
    private Thread thread;

    public void start() {
        thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (list == null || result == list.size()) {
                            break;
                        }
                        sleep(1);
                        for (Person person : list) {
                            if (!"时间到".equals(person.getTime())) {
                                if ("1".equals(person.getTime())) {
                                    person.setTime("时间到");
                                    result++;
                                } else {
                                    person.setTime((Integer.parseInt(person.getTime()) - 1) + "");
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
}