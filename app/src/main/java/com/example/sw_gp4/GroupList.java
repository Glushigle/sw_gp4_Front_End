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
        //服务器坏掉时的测试用code
        /*String response = "{\"group list\":\n" +
                "    [{\"group_id\": 1, \"info\": \"\", \"name\": \"2019软件工程第四组\", \"owner_id\": 3},\n" +
                "    {\"group_id\": 2, \"info\": \"\", \"name\": \"2019软件工程\", \"owner_id\": 3},\n" +
                "    {\"group_id\": 3, \"info\": \"\", \"name\": \"Multi-Document Processing小组\", \"owner_id\": 3},\n" +
                "    {\"group_id\": 4, \"info\": \"\", \"name\": \"北京大学珍珠奶茶研究社\", \"owner_id\": 3}],\n" +
                "    \"valid\": true}";*/
        try {
            String response = Requester.get("https://222.29.159.164:10016/get_grouplist", keys, values);
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("group list");
            group_ = new ArrayList<Group>();
            for(int i=0; i<groups.length(); ++i){
                String[] keys2 = {"group_id"};
                String[] values2 = {(String)groups.getJSONObject(i).getString("group_id")};
                String response2 = Requester.post("https://222.29.159.164:10016/get_group_task", keys2, values2);
                    JSONObject responseObj2 = new JSONObject(response2);
                    JSONArray tasks = (JSONArray) responseObj2.getJSONArray("task list");
                    if(!tasks.isNull(0)) //todo:允许无任务
                    {
                        group_.add
                                (new Group // Group(String group_id, String group_name, String owner_id, String info, int color_id)
                                        (
                                                (String) groups.getJSONObject(i).getString("group_id"),
                                                (String) groups.getJSONObject(i).getString("name"),
                                          
                                                //todo: change owner_id to owner_username (ask for api from backend)
                                                (String) groups.getJSONObject(i).getString("owner_id"),
                                                
                                                //todo: get "whether it is a request"
                                          
                                                (String) groups.getJSONObject(i).getString("info"),
                                                colors[(groups.getJSONObject(i).getInt("group_id")-1)%colors.length],
                                                new DDLForGroup
                                                        (
                                                                tasks.getJSONObject(0).getString("finish_time"),
                                                                tasks.getJSONObject(0).getString("title")
                                                        )
                                        )

                                );
                    }
                    else
                    {
                        group_.add
                                (new Group // Group(String group_id, String group_name, String owner_id, String info, int color_id)
                                        (
                                                (String) groups.getJSONObject(i).getString("group_id"),
                                                (String) groups.getJSONObject(i).getString("name"),
                                                (String) groups.getJSONObject(i).getString("owner_id"),
                                                (String) groups.getJSONObject(i).getString("info"),
                                                colors[(groups.getJSONObject(i).getInt("group_id")-1)%colors.length],
                                                null
                                        )

                                );
                    }
            }
            mAdapter.resetData(group_);
            mAdapter.notifyDataSetChanged();
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

                // Todo: check invitation: if it is, don't do anything

                Toast.makeText(mContext, "点选小组", Toast.LENGTH_SHORT).show();
                TargetGroup.currGroup = group_.get(position);
                TargetGroup.userNames = new ArrayList<String>();
                TargetGroup.userNames.add(currUserName);
                TargetGroup.currPosition = position;
                Intent intent = new Intent(mContext,TargetGroup.class);
                startActivity(intent);
                finish();
                //todo：以下注释内容将取代上面的内容，只需要补充group id和group name
                /*Intent intent = new Intent(mContext,showGroupDDL.class);
                intent.putExtra("group_id",);
                intent.putExtra("group_name",);
                startActivity(intent);
                finish();*/
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
                        String response = Requester.post("https://222.29.159.164:10016/delete_group", keys, values);
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean valid = responseObj.getBoolean("valid");
                            if(valid){
                                Toast.makeText(mContext, "已删除小组", Toast.LENGTH_SHORT).show();
                                updateData();
                            }
                            else{
                                // todo: delete group error: check api
                                //String error_info = responseObj.getString("error_info");
                                //Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
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
        TargetGroupDDL.isAdding = true;//代表是添加
        startActivity(new Intent(mContext, TargetGroupDDL.class));
        finish();
    }
}
