// Todo: add try...catch
package com.example.sw_gp4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TargetGroup extends AppCompatActivity
{
    public static ArrayList<String> userNames;
    public static Group currGroup;
    public static int currPosition;
    public static boolean isAdding;
    private ListView mListView;
    private ListViewAdapterForTargetGroup mAdapter;
    private Context mContext = this;
    private Button addButton;
    private Button saveButton;
    //private EditText targetGroupName;
    private Button changeButton;
    private EditText userAwaiting;
    private ImageButton closeButton;
    private int colors[] = {
            R.color.gp_1,
            R.color.gp_2,
            R.color.gp_3,
            R.color.gp_4,
            R.color.gp_5};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_GroupList:
                    //进到小组页
                    Intent intent1 = new Intent(mContext, GroupList.class);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    Intent intent2 = new Intent(mContext, showDDL.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    Intent intent3 = new Intent(mContext, Ranking.class);
                    startActivity(intent3);
                    finish();
                    return true;
            }
            return false;
        }
    };
    private JSONObject responseObj;

    private void updateData()
    {
        if(isAdding)
        {
            /*
            String[] keys = {"name"};
            String[] values = {"New Group"};
            String response = Requester.post("https://222.29.159.164:10016/create_group", keys, values);
            try
            {
                //JSONObject responseObj = new JSONObject(response);
                JSONObject responseObj = new JSONObject(response);
                boolean valid = responseObj.getBoolean("valid");
                if(valid){
                    currGroup = (new Group
                    (
                            (String) responseObj.getString("group_id"),
                            (String) responseObj.getString("name"),
                            (String) responseObj.getString("owner_id"),
                            (String) responseObj.getString("info"),
                            colors[(responseObj.getInt("group_id")-1)%colors.length]
                        )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            //Todo:临时改成前端显示，记得改回来
            userNames = new ArrayList<>();
            GroupList.maxId++;
            currGroup = new Group(String.valueOf(GroupList.maxId),"New Group",GroupList.currUserName,"",(GroupList.maxId-1)%colors.length);
        }
        else
        {
            currGroup = GroupList.group_.get(currPosition);
        }
        //获取组员列表
        /*
        System.out.println("currGroup = "+currGroup);
        String[] keys = {"group_id"};
        String[] values = {currGroup.group_id};
        String response = Requester.post("https://222.29.159.164:10016/get_group_member", keys, values);
        try
        {
            JSONArray memberList = new JSONArray(response);
            boolean valid = responseObj.getBoolean("valid");
            if(valid){
                for(int i = 0;i < memberList.length();++i)
                {
                    userNames.add((String)memberList.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //TOdo:同理
        userNames.add(GroupList.currUserName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_group);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Group List
        updateData();
        mListView = (ListView) findViewById(R.id.user_list);
        mAdapter = new ListViewAdapterForTargetGroup(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
            }
        });
        mListView = (ListView) findViewById(R.id.user_list);
        addButton = (Button) findViewById(R.id.btn_add_member);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OnAddClicked(v);
            }
        });
        /*changeButton = (Button) findViewById(R.id.btn_add_member);
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currGroup.group_name = targetGroupName.getText().toString();
                GroupList.group_.get(currPosition).group_name = currGroup.group_name;
                //TODO: 虽然改了group_，但是group_name改不了，显示不会变
            }
        });*/
        closeButton = (ImageButton) findViewById(R.id.btn_cross);
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mContext, GroupList.class));
                finish();
            }
        });
        saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO:~
                startActivity(new Intent(mContext, GroupList.class));
                finish();
            }
        });

        userAwaiting = (EditText) findViewById(R.id.text_member_name);


    }

    public boolean OnDeleteClicked(View view, int position)
    {
        boolean valid = true;
        // Toast the returns; update if successful
        if (valid)
        {
            userNames.remove(position);
            mAdapter.resetData(userNames);
        }
        return valid;
    }

    public void OnAddClicked(View view)
    {
        /*
        String[] keys = {"group_id", "user_id"};
        String[] values = {
                currGroup.group_id,
                userAwaiting.getText().toString()
        };
        String response = Requester.post("https://222.29.159.164:10016/add_member", keys, values);

        try
        {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if (valid)
            {
                Toast.makeText(mContext, "Member added", Toast.LENGTH_SHORT).show();
                mAdapter.resetData(userNames);

            }
            else
            {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }*/
userNames.add(userAwaiting.getText().toString());
mAdapter.resetData(userNames);
    }
}