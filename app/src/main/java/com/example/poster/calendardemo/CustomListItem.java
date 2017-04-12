package com.example.poster.calendardemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by POSTER on 29.03.2017.
 */

public class CustomListItem extends BaseAdapter{
    private TextView customTime;
    private TextView itemDescription;
    private ImageView notifImage;
    private ArrayList<TimeModel> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListItem(ArrayList<TimeModel> list, Context context) {
        this.list = list;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        convertView = layoutInflater.inflate(R.layout.fragment_custom_list_item, parent, false);

        customTime = (TextView)convertView.findViewById(R.id.custom_time);
        itemDescription = (TextView)convertView.findViewById(R.id.item_description);
        notifImage = (ImageView)convertView.findViewById(R.id.notifImageV);

        if (list.get(position).getCurrentTime() != 0){
            customTime.setText(list.get(position).getCurrentTime() + ":00");
        }
        itemDescription.setText(list.get(position).getDescription());
        if (list.get(position).getIsNotif() == 0){
            notifImage.setImageResource(R.drawable.ic_notifications_none_black_24dp);
        }else {
            notifImage.setImageResource(R.drawable.ic_notifications_black_24dp);
        }
        return convertView;
    }
}
