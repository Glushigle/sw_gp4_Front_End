package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SweepGradient;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendTask extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context mContext=this;

    private Friend friend;
    private ArrayList<Task> tasks;

    private class Friend {
        public int color;
        public String username;
        public Friend(String username, int color){
            this.color = color;
            this.username = username;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_task);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_Ranking);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // Get this friend's info
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                this.friend = null;
            } else {
                this.friend = new Friend(
                        extras.getString("friend_username"),
                        extras.getInt("color"));
            }
        } else {
            try {
                this.friend = new Friend(
                        (String) savedInstanceState.getSerializable("friend_username"),
                        Integer.parseInt((String) savedInstanceState.getSerializable("color")));
            } catch (Exception e){
                e.printStackTrace();
                this.friend = null;
            }
        }

        //this.friend = new Friend(1,0,"Kate","75%");
        // Update this friends' panel
        ((ImageView)findViewById(R.id.friend_color)).setBackgroundColor(this.friend.color);
        ((TextView)findViewById(R.id.name)).setText(this.friend.username+" 的DDL");


        // Get the friend's tasks
        updateData();

        recyclerView = (RecyclerView) findViewById(R.id.friend_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FriendTaskAdapter(mContext,tasks);
        recyclerView.setAdapter(mAdapter);
    }

    private void updateData(){
        String[] keys = {"friend_username"};
        String[] values = {this.friend.username};
        String response = Requester.post(getResources().getString(R.string.server_uri)+"get_friend_tasklist", keys, values);
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray task_list = (JSONArray) responseObj.getJSONArray("friend task list");
            tasks = new ArrayList<Task>();
            for(int i=0; i<task_list.length(); ++i){
                JSONObject task = task_list.getJSONObject(i);
                tasks.add(new Task(
                        task.getString("task_id"),
                        task.getString("title"),
                        task.getString("finish_time"),
                        task.getInt("status"),
                        task.getString("info"),
                        1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

}
