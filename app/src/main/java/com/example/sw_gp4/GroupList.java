package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;

public class GroupList extends AppCompatActivity {
    public static String currUserName;
    public static ArrayList<Group> group_;
    private ListView mListView;
    private GroupListAdapter mAdapter;
    private Context mContext=this;
    private ImageButton addButton;

    private int colors[] = { // TODO 色卡
            R.color.gp_1,
            R.color.gp_2,
            R.color.gp_3,
            R.color.gp_4,
            R.color.gp_5};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_GroupList:
                    //进到小组页
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    Intent intent2 = new Intent(mContext,showDDL.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    Intent intent3 = new Intent(mContext,Ranking.class);
                    startActivity(intent3);
                    finish();
                    return true;
            }
            return false;
        }
    };

    private void updateData(){
        String[] keys = {"username"};
        String[] values = {currUserName};
        String response = Requester.get("https://222.29.159.164:10016/get_grouplist", keys, values);


        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("group list");
            group_ = new ArrayList<Group>();
            for(int i=0; i<groups.length(); ++i)
            {  String[] keys2 = {"group_id"};
                String[] values2 = {(String) groups.getJSONObject(i).getString("group_id")};//group是刚找到的
                String response2 = Requester.get("https://222.29.159.164:10016/get_group_task", keys2, values2);
                JSONObject responseObj2 = new JSONObject(response2);
                JSONArray taskList = (JSONArray) responseObj.getJSONArray("task list");
                //接下来提取第一个信息扔进去
                String title = taskList.getJSONObject(0).getString("title");
                String time = taskList.getJSONObject(0).getString("finish_time");
                group_.add
                (new Group // Group(String group_id, String group_name, String owner_id, String info, int color_id)
                        (
                                (String) groups.getJSONObject(i).getString("group_id"),
                                (String) groups.getJSONObject(i).getString("name"),
                                (String) groups.getJSONObject(i).getString("owner_id"),
                                (String) groups.getJSONObject(i).getString("info"),
                                colors[(groups.getJSONObject(i).getInt("group_id")-1)%colors.length],
                                new DDLText(time,title)
                        )

                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_GroupList);

        // Group List
        updateData();
        mListView = (ListView) findViewById(R.id.group_list);
        mAdapter = new GroupListAdapter(this,group_);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
                TargetGroup.currGroup = group_.get(position);
                //System.out.println("POSITION="+position);
                TargetGroup.userNames = new ArrayList<String>();//TODO:应从服务器获取数据
                //System.out.println("CURRUSERNAME="+currUserName);
                TargetGroup.userNames.add(currUserName);
                TargetGroup.currPosition = position;
                //System.out.println("CURRGROUP="+TargetGroup.currGroup.id + " " + TargetGroup.currGroup.group_name);
                TargetGroup.isAdding = false;
                Intent intent = new Intent(mContext,TargetGroup.class);
                startActivity(intent);
                finish();
            }
        });
        addButton = (ImageButton) findViewById(R.id.imageButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
                OnAddClicked(v);
            }
        });

    }

    public void OnDeleteClicked(View view, int position){
        // TODO: confirmation
        String[] keys = {"group_id"};
        String[] values = {group_.get(position).group_id};
        String response = Requester.post("https://222.29.159.164:10016/delete_group", keys, values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if(valid){
                Toast.makeText(mContext, "Group deleted", Toast.LENGTH_SHORT).show();
                updateData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OnAddClicked(View view){
        TargetGroup.isAdding = true;//代表是添加
        startActivity(new Intent(mContext, TargetGroup.class));
        //finish();
    }
}
