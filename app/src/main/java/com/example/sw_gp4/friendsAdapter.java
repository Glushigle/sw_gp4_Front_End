package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONObject;

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

        // 因为invitation会reset这些，只好再处理一次type
        ((GradientDrawable) convertView.findViewById(R.id.group_color).getBackground()).setColor(friend.color);
        ((TextView) convertView.findViewById(R.id.group_name)).setText(friend.name);
        ((TextView) convertView.findViewById(R.id.group_name)).setGravity(Gravity.CENTER_VERTICAL);
        ((TextView) convertView.findViewById(R.id.group_first_task)).setVisibility(View.GONE);
        ((Button)convertView.findViewById(R.id.btn_deny)).setVisibility(View.INVISIBLE);
        ((Button)convertView.findViewById(R.id.btn_accept)).setVisibility(View.INVISIBLE);
        convertView.findViewById(R.id.group_name).setAlpha(1);
        convertView.findViewById(R.id.group_color).setAlpha(1);
        convertView.setClickable(true);

        // Delete
        convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((friends) mContext).OnDeleteClicked(view, position);
            }
        });

        if(friend.invitation)
           markInvitation(convertView,position);
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

    private void markInvitation(View convertView, final int position){
        convertView.findViewById(R.id.group_name).setAlpha((float)0.5);
        convertView.findViewById(R.id.group_color).setAlpha((float)0.5);

        ((TextView)convertView.findViewById(R.id.group_name)).setWidth(450);
        convertView.setClickable(false);

        Button btn_deny = (Button)convertView.findViewById(R.id.btn_deny);
        Button btn_accept = (Button)convertView.findViewById(R.id.btn_accept);
        btn_deny.setVisibility(View.VISIBLE);
        btn_accept.setVisibility(View.VISIBLE);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = {"friend_username"};
                String[] values = {myFriends.get(position).username};
                try{
                    String response = Requester.post(mContext.getResources().getString(R.string.server_uri)+"deny_friendReqs",keys,values);
                    JSONObject responseObj = new JSONObject(response);
                    boolean valid = responseObj.getBoolean("valid");
                    if(valid){
                        Toast.makeText(mContext, "已狠心拒绝邀请", Toast.LENGTH_SHORT).show();
                        ((friends)mContext).restart();
                    }
                    else{
                        String error_info = responseObj.getString("error_info");
                        Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "怎么可以拒绝别人呢？", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = {"friend_username"};
                String[] values = {myFriends.get(position).username};
                try{
                    String response = Requester.post(mContext.getResources().getString(R.string.server_uri)+"agree_friendReqs",keys,values);
                    JSONObject responseObj = new JSONObject(response);
                    boolean valid = responseObj.getBoolean("valid");
                    if(valid){
                        Toast.makeText(mContext, "成功建起友谊的桥梁", Toast.LENGTH_SHORT).show();
                        ((friends)mContext).restart();
                    }
                    else{
                        String error_info = responseObj.getString("error_info");
                        Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "不好意思，后会有期", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
