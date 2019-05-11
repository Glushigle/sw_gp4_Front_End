// Todo: add try...catch
package com.example.sw_gp4;

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

import org.json.JSONException;
import org.json.JSONObject;

public class GroupList extends AppCompatActivity {

    private ListView mListView;
    private ListViewAdapter mAdapter;

    private int num_groups;
    private String[] group_names;

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
        /* The return is in json.
        Suppose it's
            {"num groups":"n",
             "list":{
                 "1":"Group Name 1",
                 "2":"Group Name 2",
                 ...
                 "n":"Group Name 3"}}
        sorted by their upcoming DDLs. (Please refer to the UI design slides.)
        * */

        // Create group_list
        //String[] keys = {"key1"};
        //String[] values = {"value1"};
        //String response = PostRequester.request("full_url", keys, values); // TODO: API
        // TODO: programmatically get response
        String response = "{\"num groups\":\"3\",\n\"list\":{\n\"1\":\"Group Name 1\",\"2\":\"Group Name 2\",\"3\":\"Group Name 3\"}}";
        try {
            JSONObject responseObj = new JSONObject(response);
            num_groups = responseObj.getInt("num groups");
            group_names = new String[num_groups];
            JSONObject list = (JSONObject) responseObj.get("list");
            for(int i=1; i<=num_groups; ++i){
                group_names[i-1] = (String) list.get(Integer.toString(i));
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
        mAdapter = new ListViewAdapter(this, num_groups, group_names);
        mListView.setAdapter(mAdapter);

        // What is "mode" for???
        //mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });




    }

}
