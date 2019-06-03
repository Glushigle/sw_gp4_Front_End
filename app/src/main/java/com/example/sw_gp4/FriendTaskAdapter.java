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
    private ArrayList<Task> tasks;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class FtViewHolder extends RecyclerView.ViewHolder {
        public TextView month;
        public TextView day;
        public View divider;
        public TextView title;
        public TextView info;
        public FtViewHolder(View v) {
            super(v);
            month = (TextView) v.findViewById(R.id.tasklist_month);
            day = (TextView) v.findViewById(R.id.tasklist_day);
            divider = (View) v.findViewById(R.id.tasklist_divider);
            title = (TextView) v.findViewById(R.id.tasklist_title);
            info = (TextView) v.findViewById(R.id.tasklist_info);
        }
    }

    public FriendTaskAdapter(Context mContext, ArrayList<Task> tasks) {
        super();
        this.mContext = mContext;
        resetData(tasks);
    }

    public void resetData( ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendTaskAdapter.FtViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendtask_item, parent, false);
        FtViewHolder vh = new FtViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(FtViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Task task =  tasks.get(position);
        holder.month.setText(String.valueOf(task.finish_time[1])+"月");
        holder.day.setText(String.valueOf(task.finish_time[2]));
        holder.divider.setBackgroundColor(task.color);
        holder.title.setText(task.title);
        holder.info.setText(task.info);

        float alpha = (float) ((task.status==1)? 1.0 : 0.5);
        holder.month.setAlpha(alpha);
        holder.title.setAlpha(alpha);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
