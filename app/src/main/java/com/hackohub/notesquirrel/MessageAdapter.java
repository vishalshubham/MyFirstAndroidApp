package com.hackohub.notesquirrel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vishal on 05/07/2015.
 */
public class MessageAdapter extends BaseAdapter implements ListAdapter {

    private List<Message> messages;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public String getTitle(int position){
        return messages.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_messgae_item_layout, null);

        Message message = messages.get(position);
        String title = message.getTitle();
        String dateTime = message.getDatetime();
        TextView titleView = (TextView)view.findViewById(R.id.list_message_title);
        TextView dateTimeView = (TextView)view.findViewById(R.id.list_message_datetime);
        titleView.setText(title);
        dateTimeView.setText(dateTime);
        return view;
    }
}
