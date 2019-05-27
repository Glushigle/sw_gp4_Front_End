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

    public class Friend {
        public int rank;
        public int color;
        public String username;
        public String percentage;
        public boolean invitation;
        public Friend(int rank, int color, String username, String percentage){
            this.rank = rank;
            this.color = color;
            this.username = username;
            this.percentage = percentage;
        }
    }

    private void updateData(){
        /* Suppose the return format is
            {"task_list":[
                 {"task_id":"1", "title":"Title 1", "finish_time":"2019-05-20 14:00:00", "status":"1", "info":"Info 1"},
                 {"task_id":"2", "title":"Title 2", "finish_time":"2019-05-21 14:00:00", "status":"0", "info":"Info 2"},
                 ...
                 {"task_id":"3", "title":"Title 3", "finish_time":"2019-05-22 14:00:00", "status":"1", "info":"Info 3"}]
            }
        */
        //String[] keys = {"friend_username"};
        //String[] values = {this.friend.username};
        //String response = PostRequester.request("full_url", keys, values);
        String response = "{\"task_list\":[\n" +
                "                 {\"task_id\":\"1\", \"title\":\"Title 1\", \"finish_time\":\"2019-05-20 14:00:00\", \"status\":\"1\", \"info\":\"Info 1\"},\n" +
                "                 {\"task_id\":\"2\", \"title\":\"Title 2\", \"finish_time\":\"2019-05-21 14:00:00\", \"status\":\"0\", \"info\":\"Info 2\"},\n" +
                "                 {\"task_id\":\"3\", \"title\":\"Title 3\", \"finish_time\":\"2019-05-22 14:00:00\", \"status\":\"1\", \"info\":\"Info 3\"}]\n" +
                "            }";
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray task_list = (JSONArray) responseObj.getJSONArray("task_list");
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
                    startActivity(new Intent(mContext,GroupList.class));
                    finish();
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    startActivity(new Intent(mContext,showDDL.class));
                    finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    startActivity(new Intent(mContext,Ranking.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setProgress(ProgressBar bar, String rawPercentage){
        float percent = (float) (Float.parseFloat(rawPercentage.replaceAll("[^\\d]", ""))/100.0);
        float degree = -90+360*percent;
        RotateDrawable rd = ((RotateDrawable)getDrawable(R.drawable.progressbar_custom));
        rd.setFromDegrees(degree);
        rd.setToDegrees(degree);
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
                        extras.getInt("rank"),
                        extras.getInt("color"),
                        extras.getString("friend_username"),
                        extras.getString("percentage"));
            }
        } else {
            try {
                this.friend = new Friend(
                        Integer.parseInt((String) savedInstanceState.getSerializable("rank")),
                        Integer.parseInt((String) savedInstanceState.getSerializable("color")),
                        (String) savedInstanceState.getSerializable("friend_username"),
                        (String) savedInstanceState.getSerializable("percentage"));
            } catch (Exception e){
                e.printStackTrace();
                this.friend = null;
            }
        }

        //this.friend = new Friend(1,0,"Kate","75%");
        // Update this friends' panel
        ((ImageView)findViewById(R.id.friend_color)).setBackgroundColor(this.friend.color);
        ((TextView)findViewById(R.id.rank)).setText("第"+String.valueOf(this.friend.rank)+"名");
        ((TextView)findViewById(R.id.name)).setText(this.friend.username);
        ((TextView)findViewById(R.id.percent)).setText(this.friend.percentage+"%");
        setProgress((ProgressBar)findViewById(R.id.progress), this.friend.percentage);



        // Get the friend's tasks
        updateData();

        recyclerView = (RecyclerView) findViewById(R.id.friend_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FriendTaskAdapter(mContext,tasks);
        recyclerView.setAdapter(mAdapter);
    }


}
