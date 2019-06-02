package com.example.sw_gp4;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

public class friendsAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private ArrayList<friends.MyFriend> myFriends;

    public friendsAdapter(Context mContext, ArrayList<friends.MyFriend> myFriends) {
        this.mContext = mContext;
        resetData(myFriends);
    }

    public void resetData( ArrayList<friends.MyFriend> myFriends){
        this.myFriends = myFriends;
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        friends.MyFriend friend = myFriends.get(position);

        ((GradientDrawable) convertView.findViewById(R.id.group_color).getBackground()).setColor(friend.color);
        ((TextView) convertView.findViewById(R.id.group_name)).setText(friend.name);

        // Delete
        convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((friends) mContext).OnDeleteClicked(view, position);
            }
        });

        //todo: Mark group invitation
        //if(group.invitation)
        markInvitation(convertView);
    }

    @Override
    public int getCount() {
        if(myFriends==null) return 0;
        return myFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void markInvitation(View convertView){
        convertView.findViewById(R.id.group_name).setAlpha((float)0.5);
        convertView.findViewById(R.id.group_color).setAlpha((float)0.5);

        ((TextView)convertView.findViewById(R.id.group_name)).setWidth(450);

        Button btn_deny = (Button)convertView.findViewById(R.id.btn_deny);
        Button btn_accept = (Button)convertView.findViewById(R.id.btn_accept);
        btn_deny.setVisibility(View.VISIBLE);
        btn_accept.setVisibility(View.VISIBLE);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "已删除请求", Toast.LENGTH_SHORT).show();
                // todo deny invitation request
                // update ui
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "成功建起友谊的桥梁", Toast.LENGTH_SHORT).show();
                // todo accept invitation request
                //update ui
            }
        });
    }
}
