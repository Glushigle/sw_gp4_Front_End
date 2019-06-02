package com.example.sw_gp4;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> friend_names;
    private ArrayList<Integer> friend_colors;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout fitem;
        public TextView frank;
        public ImageButton fcolor;
        public TextView fname;
        public View convertView;
        public MyViewHolder(View v) {
            super(v);
            fitem = (LinearLayout) v.findViewById(R.id.ranking_item);
            frank = (TextView) v.findViewById(R.id.friend_rank);
            fcolor = (ImageButton) v.findViewById(R.id.friend_color);
            fname = (TextView) v.findViewById(R.id.friend_name);
            convertView = v;
        }
    }

    private void markInvitation(MyViewHolder holder){
        holder.fname.setAlpha((float)0.5);
        holder.fname.setWidth(200);
        holder.fcolor.setAlpha((float)0.5);
        holder.frank.setVisibility(View.INVISIBLE);

        View convertView = holder.convertView;
        Button btn_deny = (Button)convertView.findViewById(R.id.btn_deny);
        Button btn_accept = (Button)convertView.findViewById(R.id.btn_accept);
        btn_deny.setVisibility(View.VISIBLE);
        btn_accept.setVisibility(View.VISIBLE);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "deny invitation", Toast.LENGTH_SHORT).show();
                // todo deny invitation request
                // update ui
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "accept invitation", Toast.LENGTH_SHORT).show();
                // todo accept invitation request
                //update ui
            }
        });
    }

    public RankingAdapter(Context mContext, ArrayList<Integer> friend_colors, ArrayList<String> friend_names) {
        super();
        this.mContext = mContext;
        resetData(friend_colors,friend_names);
    }

    public void resetData( ArrayList<Integer> friend_colors, ArrayList<String> friend_names){
        this.friend_colors = friend_colors;
        this.friend_names = friend_names;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.fitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点选第"+String.valueOf(position+1)+"个好友", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, FriendTask.class);
                intent.putExtra("rank", position+1);
                intent.putExtra("color", friend_colors.get(position));
                intent.putExtra("friend_username", friend_names.get(position));
                mContext.startActivity(intent);
                //mContext. // todo: can't finish another activity la QwQ
            }
        });
        holder.frank.setText(Integer.toString(position+1));
        ((GradientDrawable) holder.fcolor.getBackground()).setColor(friend_colors.get(position));
        holder.fname.setText(friend_names.get(position));

        //todo if(invitation)
        //markInvitation(holder);
    }

    @Override
    public int getItemCount() {
        return friend_names.size();
    }
}
