package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
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
    private ArrayList<String> group_ids;
    private ArrayList<String> group_names;
    private ArrayList<Integer> group_colors;

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
        /* Suppose the return format is
            {"groups":[
                 {"id":"1", "name":"Group Name 1"},
                 {"id":"2", "name":"Group Name 2"},
                 ...
                 {"id":"n", "name":"Group Name n"}]
            }
        */

        // TODO: programmatically get response (後端API實現了嗎？)
        //String[] keys = {"username"};
        //String[] values = {"user1"}; // TODO: username cookie
        //String response = PostRequester.request("full_url", keys, values);
        String response = "{\"groups\":[\n" +
                "                 {\"id\":\"1\", \"name\":\"2019软件工程第四组\"},\n" +
                "                 {\"id\":\"2\", \"name\":\"2019软件工程\"},\n" +
                "                 {\"id\":\"3\", \"name\":\"Multi-Document Processing小组\"},\n" +
                "                 {\"id\":\"4\", \"name\":\"北京大学山鹰社\"},\n" +
                "                 {\"id\":\"5\", \"name\":\"北京大学珍珠奶茶研究社\"}]\n" +
                "            }";
        try {
            maxId = 5;

            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("groups");
            group_ids = new ArrayList<String>();
            group_names = new ArrayList<String>();
            group_colors = new ArrayList<Integer>();
            group_ = new ArrayList<Group>();
            for(int i=0; i<groups.length(); ++i){
                group_ids.add((String) groups.getJSONObject(i).getString("id"));
                group_names.add((String) groups.getJSONObject(i).getString("name"));
                group_colors.add(colors[(Integer.parseInt(group_ids.get(i))-1)%colors.length]);
                group_.add
                (new Group
                        (
                                (String) groups.getJSONObject(i).getString("id"),
                                (String) groups.getJSONObject(i).getString("name")

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
        mAdapter = new GroupListAdapter(this,group_colors, group_names);
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

    public boolean OnDeleteClicked(View view, int position){
        // Todo: delete/leave group request
        // Confirmation
        // POST to database
        boolean valid = true;
        // Toast the returns; update if successful
        if(valid){
            group_ids.remove(position);
            group_colors.remove(position);
            group_names.remove(position);
            group_.remove(position);
            mAdapter.resetData(group_colors, group_names);
        }
        return valid;
    }

    public void OnAddClicked(View view){
        int tempId = getId();
        group_ids.add(Integer.toString(tempId));
        //group_ids.add((String) groups.getJSONObject(0).getString("id"));
        group_names.add("New Group "+tempId);
        group_colors.add(colors[(tempId-1)%colors.length]);
        group_.add(new Group(tempId,"New Group "+tempId));
        //group_.get(1).member.add(currUserName);//第一个成员是自己
        //group_names.add((String) groups.getJSONObject(0).getString("name"));
        mAdapter.resetData(group_colors,group_names);

    }
    public int getId()
    {
        for(int i = 1; i <= maxId;++i)
        {
            if(!group_ids.contains(Integer.toString(i)))
            {
                return i;
            }
        }
        maxId++;
        return maxId;
    }
}
