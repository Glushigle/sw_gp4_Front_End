// Todo: add try...catch
package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class TargetGroupDDL extends AppCompatActivity
{
    public static ArrayList<DDLForGroup> ddls;
    public static Group currGroup;
    public static int currPosition;
    public static boolean isAdding;
    private ListView mListView;
    private ListViewAdapterForTargetGroupDDL mAdapter;
    private Context mContext = this;
    private TextView nameText;
    private ImageButton addButton;
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

    private void updateData()
    {
        if(isAdding)
        {
            String[] keys = {"name"};
            String[] values = {"New Group"};
            String response = Requester.post(this.getString(R.string.server_uri)+"create_group", keys, values);
            try
            {
                JSONObject responseObj = new JSONObject(response);
                boolean valid = responseObj.getBoolean("valid");
                if(valid){
                    currGroup = (new Group
                    (
                            (String) responseObj.getString("group_id"),
                            (String) responseObj.getString("name"),
                            (String) responseObj.getString("info"),
                            ColorConverter.fromName((String) responseObj.getString("name")),
                            new DDLForGroup("13:00","Test 3"),
                            false
                        )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //String[] keys2 = {"group_id","title","deadline"};
            //String[] values2 = {currGroup.group_id,"Test 2","2018-09-09 12:00"};
            //String response2 = Requester.post(this.getString(R.string.server_uri)+"create_group_task", keys2, values2);
        }
        else
        {
            currGroup = GroupList.group_.get(currPosition);
        }
        //获取DDL列表
        String[] keys = {"group_id"};
        String[] values = {currGroup.group_id};
        String response = Requester.post(this.getString(R.string.server_uri)+"get_group_task", keys, values);
        try
        {
            JSONObject responseObj = new JSONObject(response);
            JSONArray tasks = (JSONArray) responseObj.getJSONArray("task list");
            ddls = new ArrayList<DDLForGroup>();
            if(!tasks.isNull(0))
            {
                currGroup.firstTask = new DDLForGroup
                        (
                                tasks.getJSONObject(0).getString("finish_time"),
                                tasks.getJSONObject(0).getString("title")
                        );
                for(int i = 0; i < tasks.length();++i)
                {
                    ddls.add(new DDLForGroup
                    (
                            tasks.getJSONObject(i).getString("finish_time"),
                            tasks.getJSONObject(0).getString("title"))
                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_group_ddl);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_GroupList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Group List
        updateData();
        mListView = (ListView) findViewById(R.id.ddl_list);
        mAdapter = new ListViewAdapterForTargetGroupDDL(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
            }
        });
        mListView = (ListView) findViewById(R.id.ddl_list);
        addButton = (ImageButton) findViewById(R.id.imageButton2);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OnAddClicked(v);
            }
        });

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

        nameText = (TextView) findViewById(R.id.group_name);
        nameText.setText(currGroup.group_name);
    }

    public boolean OnDeleteClicked(View view, int position)
    {
        boolean valid = true;
        // Toast the returns; update if successful
        if (valid)
        {
            ddls.remove(position);
            mAdapter.resetData(ddls);
        }
        return valid;
    }
    public void OnAddClicked(View view)
    {

    }
}