package com.amigosphire_poc.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amigosphire_poc.R;

import java.util.List;
import java.util.Map;

/**
 * Created by skaribasappa on 1/28/17.
 */

public class MyWhiteListAdapter extends RecyclerView.Adapter<MyWhiteListAdapter.MyViewHolder>{

    private List<WhiteListHolder>  mWhiteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mPhoneNumber;

        public MyViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.whitelistname);
            mPhoneNumber = (TextView) view.findViewById(R.id.whitelistnumber);
        }
    }

    public MyWhiteListAdapter(List<WhiteListHolder> whiteList) {
        this.mWhiteList = whiteList;
    }

    @Override
    public MyWhiteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.whitelist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyWhiteListAdapter.MyViewHolder holder, int position) {
        WhiteListHolder whiteListItem = mWhiteList.get(position);
        holder.mName.setText(whiteListItem.getName());
        holder.mPhoneNumber.setText(whiteListItem.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mWhiteList.size();
    }
}
