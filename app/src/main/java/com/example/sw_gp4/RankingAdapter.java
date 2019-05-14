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

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> friend_names;
    private ArrayList<String> friend_percents;
    private ArrayList<Integer> friend_colors;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView frank;
        public ImageButton fcolor;
        public TextView fname;
        public TextView fpercent;
        public MyViewHolder(View v) {
            super(v);
            frank = (TextView) v.findViewById(R.id.friend_rank);
            fcolor = (ImageButton) v.findViewById(R.id.friend_color);
            fname = (TextView) v.findViewById(R.id.friend_name);
            fpercent = (TextView) v.findViewById(R.id.friend_percent);
        }
    }

    public RankingAdapter(Context mContext, ArrayList<Integer> friend_colors, ArrayList<String> friend_names,
                          ArrayList<String> friend_percents) {
        super();
        this.mContext = mContext;
        resetData(friend_colors,friend_names,friend_percents);
    }

    public void resetData( ArrayList<Integer> friend_colors, ArrayList<String> friend_names, ArrayList<String> friend_percents){
        this.friend_colors = friend_colors;
        this.friend_names = friend_names;
        this.friend_percents = friend_percents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RankingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranking_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.frank.setText(Integer.toString(position+1));
        ((GradientDrawable) holder.fcolor.getBackground()).setColor(
                ContextCompat.getColor(mContext, friend_colors.get(position)));
        holder.fname.setText(friend_names.get(position));
        holder.fpercent.setText(friend_percents.get(position)+"%");
    }

    @Override
    public int getItemCount() {
        return friend_names.size();
    }
}
