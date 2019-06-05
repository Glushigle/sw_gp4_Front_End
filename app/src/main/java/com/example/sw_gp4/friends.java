package com.example.sw_gp4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class friends extends AppCompatActivity {
    public static ArrayList<MyFriend> myfriends;
    private ListView mListView;
    private friendsAdapter mAdapter;
    public class MyFriend {
        public String name;
        public String username;
        public boolean invitation;
        public int color;
        public MyFriend(String name, String username, boolean invitation){
            this.name = name;
            this.username = username;
            this.color = ColorConverter.fromName(username);
            this.invitation = invitation;
        }
    }

    private Context mContext=this;
    String username;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_GroupList:
                    //进到小组页
                    Intent intent1 = new Intent(mContext,GroupList.class);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    Intent intent2 = new Intent(mContext,showDDL.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    Intent intent3 = new Intent(mContext,friends.class);
                    startActivity(intent3);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_Ranking);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = (ListView) findViewById(R.id.friend_list);
        mAdapter = new friendsAdapter(this, myfriends);
        mListView.setAdapter(mAdapter);
        updateData();

        ((GradientDrawable) findViewById(R.id.my_color).getBackground()).setColor(ColorConverter.fromName(User.username));
        ((TextView)findViewById(R.id.my_name)).setText(User.username+" 的好友");


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(myfriends.get(position).invitation) return;

                Toast.makeText(mContext, "点选好友"+myfriends.get(position).username, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, FriendTask.class);
                intent.putExtra("color", myfriends.get(position).color);
                intent.putExtra("friend_username", myfriends.get(position).username);
                startActivity(intent);
                finish();
            }
        });
    }

    public void add_friend(View v){
        EditText search_friends = ((EditText)findViewById(R.id.search_friends));
        String friend_username = search_friends.getText().toString();
        String[] keys = {"friend_username"};
        String[] values = {friend_username};
        try{
            String response = Requester.post(this.getString(R.string.server_uri)+"add_friend", keys, values);
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if(valid){
                Toast.makeText(this, "好友请求已发送", Toast.LENGTH_SHORT).show();
                search_friends.setText("");
            }
            else{
                String error_info = responseObj.getString("error_info");
                Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "请输入有效好友名", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(){

        String[] keys = {};
        String[] values = {};
        myfriends = new ArrayList<MyFriend>();
        try {
            String response = Requester.get(getResources().getString(R.string.server_uri)+"get_friendlist", keys, values);
            JSONObject responseObj = new JSONObject(response);
            JSONArray friendlist = (JSONArray) responseObj.getJSONArray("friend list");
            for(int i=0; i<friendlist.length(); ++i){
                JSONObject friend = friendlist.getJSONObject(i);
                if(!friend.getString("username").equals(User.username))
                    myfriends.add(new MyFriend(
                            friend.getString("name"),
                            friend.getString("username"),
                            false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            String response = Requester.get(getResources().getString(R.string.server_uri)+"get_friendreq", keys, values);
            JSONObject responseObj = new JSONObject(response);
            JSONArray friendlist = (JSONArray) responseObj.getJSONArray("friend requests");
            for(int i=0; i<friendlist.length(); ++i){
                JSONObject friend = friendlist.getJSONObject(i);
                if(!friend.getString("username").equals(User.username))
                    myfriends.add(new MyFriend(
                            friend.getString("name"),
                            friend.getString("username"),
                            true));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.resetData(myfriends);
        mAdapter.notifyDataSetChanged();

    }

    public void OnDeleteClicked(View view, final int position){

        new AlertDialog.Builder(this)
                .setMessage("确定要删除好友吗？")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] keys = {"friend_username"};
                        String[] values = {myfriends.get(position).username};
                        String response = Requester.post(getResources().getString(R.string.server_uri)+"delete_friend", keys, values);
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean valid = responseObj.getBoolean("valid");
                            if(valid){
                                Toast.makeText(mContext, "已删除好友", Toast.LENGTH_SHORT).show();
                                updateData();
                            }
                            else{
                                String error_info = responseObj.getString("error_info");
                                Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "无效操作。请珍惜友谊", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
        mAdapter.closeAllItems();
    }

    public void restart(){
        Intent intent = new Intent(mContext,friends.class);
        startActivity(intent);
        finish();
    }
}
