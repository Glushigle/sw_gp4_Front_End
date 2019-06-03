package com.example.sw_gp4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Collections;

public class GroupList extends AppCompatActivity {
    public static String currUserName;
    public static ArrayList<Group> group_;
    public static int maxId;
    private ListView mListView;
    private GroupListAdapter mAdapter;
    private Context mContext=this;
    private ImageButton addButton;

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

    private void updateData(){

        group_ = new ArrayList<Group>();

        // Invitation=Request
        try {
            String[] keys = {};
            String[] values = {};
            String response = Requester.get(getResources().getString(R.string.server_uri)+"get_groupreq", keys, values);
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("group invitations");
            for(int i=0; i<groups.length(); ++i){
                group_.add(new Group(
                        (String) groups.getJSONObject(i).getString("group_id"),
                        (String) groups.getJSONObject(i).getString("name"),
                        (String) groups.getJSONObject(i).getString("info"),
                        ColorConverter.fromId(groups.getJSONObject(i).getInt("group_id")),
                        null,
                        true
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Groups I'm already in
        try {
            String[] keys = {};
            String[] values = {};
            String response = Requester.get(getResources().getString(R.string.server_uri)+"get_grouplist", keys, values);
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("group list");
            for(int i=0; i<groups.length(); ++i){
                String[] keys2 = {"group_id"};
                String[] values2 = {(String)groups.getJSONObject(i).getString("group_id")};
                String response2 = Requester.post(getResources().getString(R.string.server_uri)+"get_group_task", keys2, values2);
                JSONObject responseObj2 = new JSONObject(response2);
                JSONArray tasks = (JSONArray) responseObj2.getJSONArray("task list");
                Group group = null;
                if(!tasks.isNull(0)) //todo:允许无任务
                {
                    group = new Group
                            (
                                    (String) groups.getJSONObject(i).getString("group_id"),
                                    (String) groups.getJSONObject(i).getString("name"),
                                    (String) groups.getJSONObject(i).getString("info"),
                                    ColorConverter.fromId(groups.getJSONObject(i).getInt("group_id")),
                                    new DDLForGroup
                                            (
                                                    tasks.getJSONObject(0).getString("finish_time"),
                                                    tasks.getJSONObject(0).getString("title")
                                            ),
                                    false
                            );
                }
                else
                {
                    group = new Group
                            (
                                    (String) groups.getJSONObject(i).getString("group_id"),
                                    (String) groups.getJSONObject(i).getString("name"),
                                    (String) groups.getJSONObject(i).getString("info"),
                                    ColorConverter.fromId(groups.getJSONObject(i).getInt("group_id")),
                                    null,
                                    false
                            );
                }

                String responseOwner = Requester.get(getResources().getString(R.string.server_uri)+"check_ownership", keys2, values2);
                group.im_leader = (new JSONObject(response2)).getBoolean("valid");

                group_.add(group);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.resetData(group_);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_GroupList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // Group List
        mListView = (ListView) findViewById(R.id.group_list);
        mAdapter = new GroupListAdapter(this,group_, mListView);
        mListView.setAdapter(mAdapter);
        updateData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(group_.get(position).invitation) return;

                Toast.makeText(mContext, "点选小组", Toast.LENGTH_SHORT).show();
                /*TargetGroup.currGroup = group_.get(position);
                TargetGroup.userNames = new ArrayList<String>();
                TargetGroup.userNames.add(currUserName);
                TargetGroup.currPosition = position;
                Intent intent = new Intent(mContext,TargetGroup.class);
                startActivity(intent);
                finish();*/
                //todo：以下内容将取代上面的注释内容
                Group gp = group_.get(position);
                Intent intent = new Intent(mContext,showGroupDDL.class);
                intent.putExtra("group_id",gp.group_id);
                intent.putExtra("group_name",gp.group_name);
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
                Toast.makeText(mContext, "添加小组", Toast.LENGTH_SHORT).show();
                OnAddClicked(v);
            }
        });

    }

    public void OnDeleteClicked(View view, final int position){

        new AlertDialog.Builder(this)
                .setMessage("确定要删除小组吗？")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] keys = {"group_id"};
                        String[] values = {group_.get(position).group_id};
                        String response = Requester.post(getResources().getString(R.string.server_uri)+"delete_group", keys, values);
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean valid = responseObj.getBoolean("valid");
                            if(valid){
                                Toast.makeText(mContext, "已删除小组", Toast.LENGTH_SHORT).show();
                                updateData();
                            }
                            else{
                                String error_info = responseObj.getString("error_info");
                                Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
        mAdapter.closeAllItems();
    }

    public void OnAddClicked(View view){
        //TargetGroupDDL.isAdding = true;//代表是添加
        //startActivity(new Intent(mContext, TargetGroupDDL.class));
        startActivity(new Intent(mContext,showGroupDDL.class));
        finish();
    }
}
