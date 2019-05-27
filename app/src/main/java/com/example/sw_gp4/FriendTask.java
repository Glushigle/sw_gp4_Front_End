package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
        public Friend(int rank, int color, String username, String percentage){
            this.rank = rank;
            this.color = color;
            this.username = username;
            this.percentage = percentage;
        }
    }
    public class Task {
        public final String id;
        public final String title;
        public final int[] finish_time; //{2019,5,20,13,0,0}
        public final boolean status;
        public final String info;
        public Task(String id, String  title, String finish_time, boolean status, String info){
            this.id = id;
            this.title = title;
            this.finish_time = timeParser(finish_time);
            this.status = status;
            this.info = info;
        }
        private int[] timeParser(String finish_time){
            String [] dt = finish_time.split(" ");  //"2019-05-20 13:00:00"
            String[] d = dt[0].split("-");          //"2019-05-20"
            String[] t = dt[1].split(":");          //"13:00:00"
            int[] rtn = {Integer.valueOf(d[0]),Integer.valueOf(d[1]),Integer.valueOf(d[2]),
                         Integer.valueOf(t[0]),Integer.valueOf(t[1]),Integer.valueOf(t[2])};
            return rtn;
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
        int colors[] = {R.color.gp_1,
                R.color.gp_2,
                R.color.gp_3,
                R.color.gp_4,
                R.color.gp_5};
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
                        task.getBoolean("status"),
                        task.getString("info")));
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
    private int convertProgress(String rawPercentage){
        int percent = Integer.parseInt(rawPercentage.replaceAll("[^\\d]", ""));
        ProgressBar bar = (ProgressBar)findViewById(R.id.progress);
        return bar.getMin() + (bar.getMax() - bar.getMin()) * (percent / 100);
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
        /*
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
        */
        this.friend = new Friend(1,R.color.gp_4,"Kate","75%");
        // Update this friends' panel
        ((TextView)findViewById(R.id.rank)).setText(String.valueOf(this.friend.rank));
        ((TextView)findViewById(R.id.name)).setText(this.friend.username);
        ((ImageView)findViewById(R.id.friend_color)).setBackgroundColor(getResources().getColor(this.friend.color));
        ((TextView)findViewById(R.id.percent)).setText(this.friend.percentage);
        ((ProgressBar)findViewById(R.id.progress)).setProgress(convertProgress(this.friend.percentage), false);



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
