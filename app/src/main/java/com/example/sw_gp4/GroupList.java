// Todo: add try...catch
package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity
{
    public ArrayList<Group> group_;
    public static int maxId;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext=this;
    private ImageButton addButton;
    private FloatingActionButton addButton2;
    private ArrayList<String> group_ids;
    private ArrayList<String> group_names;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // TODO: 写navigation bar
                case R.id.navigation_home:
                    //进到小组页
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(mContext,showDDL.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    //进到好友页
                    return true;
            }
            return false;
        }
    };
    private JSONObject responseObj;

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
        // TODO: 由于数据是前台给出的，所以每一次切换Activiti都会刷新数据，待更改
        //String[] keys = {"username"};
        //String[] values = {"user1"}; // TODO: username cookie
        //String response = PostRequester.request("full_url", keys, values);
        String response = "{\"groups\":[\n" +
                "                 {\"id\":\"1\", \"name\":\"Group Name 1\"},\n" +
                "                 {\"id\":\"2\", \"name\":\"Group Name 2\"},\n" +
                "                 {\"id\":\"3\", \"name\":\"Group Name 3\"},\n" +
                "                 {\"id\":\"4\", \"name\":\"Group Name 44\"}]\n" +
                "            }";
        try {
            maxId = 4;
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("groups");
            group_ids = new ArrayList<String>();
            group_names = new ArrayList<String>();
            group_ = new ArrayList<Group>();
            for(int i=0; i<groups.length(); ++i){
                //System.out.println((String) groups.getJSONObject(i).getString("id"));
                group_ids.add((String) groups.getJSONObject(i).getString("id"));
                group_names.add((String) groups.getJSONObject(i).getString("name"));
                group_.add(new Group((String) groups.getJSONObject(i).getString("id"),(String) groups.getJSONObject(i).getString("name")));
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

        // Group List
        updateData();
        mListView = (ListView) findViewById(R.id.group_list);
        mAdapter = new ListViewAdapter(this, group_names);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
                // open SwipeLayout第?個小孩
                //((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);

            }
        });
        mListView = (ListView) findViewById(R.id.group_list);
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
        addButton2 = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addButton2.setOnClickListener(new View.OnClickListener()
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
            group_names.remove(position);
            group_.remove(position);
            mAdapter.resetData(group_names);
        }
        return valid;
    }

    public void OnAddClicked(View view)
    {
        // Todo: open add activity
        Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
        //try
        //{
            String response = "{\"newGroups\":[\n" +
                    "                 {\"id\":\"5\", \"name\":\"New Group\"},\n" +
                    "            }";
            //responseObj = new JSONObject(response);
            //JSONArray groups = (JSONArray) responseObj.getJSONArray("newGroups");
            int tempId = getId();
            group_ids.add(Integer.toString(tempId));
            //group_ids.add((String) groups.getJSONObject(0).getString("id"));
            group_names.add("New Group "+tempId);
            group_.add(new Group(tempId,"New Group "+tempId));
            //group_names.add((String) groups.getJSONObject(0).getString("name"));
            mAdapter.resetData(group_names);
        //}
        //catch (JSONException e)
        //{
        //    e.printStackTrace();
        //}
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
