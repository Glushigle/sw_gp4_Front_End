package com.example.sw_gp4;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FriendTaskAdapter extends RecyclerView.Adapter<FriendTaskAdapter.FtViewHolder> {

    private Context mContext;
    private ArrayList<FriendTask.Task> tasks;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class FtViewHolder extends RecyclerView.ViewHolder {
        public TextView frank;
        public ImageButton fcolor;
        public TextView fname;
        public TextView fpercent;
        public FtViewHolder(View v) {
            super(v);
            frank = (TextView) v.findViewById(R.id.friend_rank);
            fcolor = (ImageButton) v.findViewById(R.id.friend_color);
            fname = (TextView) v.findViewById(R.id.friend_name);
            fpercent = (TextView) v.findViewById(R.id.friend_percent);
        }
    }

    public FriendTaskAdapter(Context mContext, ArrayList<FriendTask.Task> tasks) {
        super();
        this.mContext = mContext;
        resetData(tasks);
    }

    public void resetData( ArrayList<FriendTask.Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendTaskAdapter.FtViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranking_item, parent, false);
        FtViewHolder vh = new FtViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(FtViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.frank.setText(Integer.toString(position+1));
        //((GradientDrawable) holder.fcolor.getBackground()).setColor(
         //       ContextCompat.getColor(mContext, friend_colors.get(position)));
        //holder.fname.setText(friend_names.get(position));
        //holder.fpercent.setText(friend_percents.get(position)+"%");
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
