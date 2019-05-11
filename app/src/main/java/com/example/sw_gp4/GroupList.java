// Todo: add try...catch
package com.example.sw_gp4;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity {

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext=this;

    private ArrayList<String> group_ids;
    private ArrayList<String> group_names;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // TODO: 写navigation bar
                case R.id.navigation_home:
                    //进到小组页
                    return true;
                case R.id.navigation_dashboard:
                    //进到个人页
                    return true;
                case R.id.navigation_notifications:
                    //进到好友页
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
                "                 {\"id\":\"1\", \"name\":\"Group Name 1\"},\n" +
                "                 {\"id\":\"2\", \"name\":\"Group Name 2\"},\n" +
                "                 {\"id\":\"3\", \"name\":\"Group Name 3\"}]\n" +
                "            }";
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("groups");
            group_ids = new ArrayList<String>();
            group_names = new ArrayList<String>();
            for(int i=0; i<groups.length(); ++i){
                group_ids.add((String) groups.getJSONObject(i).getString("id"));
                group_names.add((String) groups.getJSONObject(i).getString("name"));
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

        // What is "mode" for???
        //mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
                // open SwipeLayout第?個小孩
                //((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });

    }

    protected boolean OnDeleteGroupClicked(View view){
        // Todo: delete/leave group request
        // Confirmation
        // POST to database
        boolean valid = true;
        // Toast the returns; update if successful
        if(valid){
            // TODO
            //group_names.remove(view.getContext().);
            //notifyDataSetChanged();
        }
        return valid;
    }

}
