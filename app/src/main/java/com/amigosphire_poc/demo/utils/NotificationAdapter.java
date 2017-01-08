package com.amigosphire_poc.demo.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amigosphire_poc.R;

import java.util.List;

/**
 * Created by skaribasappa on 1/8/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Notifications> notificationsList;

    public NotificationAdapter(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.notification_title);
            date = (TextView) view.findViewById(R.id.notification_date);
        }
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.push_notification_details,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        Notifications notification = notificationsList.get(position);
        holder.title.setText(notification.getTitle());
        holder.date.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }
}
