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


    // from https://github.com/daimajia/AndroidSwipeLayout
    private void addSwipeLayout(){
        SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.swipe_layout);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.buttom_wrapper));
    }

    private void renderGroupListView(){ // String response
        /* The return is in json.
        Suppose it's
            {"num groups":"n",
             "list":{
                 "1":{"name":"Group Name 1","preview":"Upcoming DDL preview 1"},
                 "2":{"name":"Group Name 2","preview":"Upcoming DDL preview 2"},
                 ...
                 "n":{"name":"Group Name 3","preview":"Upcoming DDL preview 3"}}}
        sorted by their upcoming DDLs. (Please refer to the UI design slides.)
        * */
        int colors[] = {R.color.gp_1, R.color.gp_2, R.color.gp_3, R.color.gp_4, R.color.gp_5};
        String response = "{\"num\":\"3\",\n\"list\":{\n\"1\":{{\"name\":\"Group Name 1\",\"preview\":\"Upcoming DDL preview 1\"},\n\"2\":{\"name\":\"Group Name 2\",\"preview\":\"Upcoming DDL preview 2\"},\n\"3\":{\"name\":\"Group Name 3\",\"preview\":\"Upcoming DDL preview 3\"}}}";
        try {
            JSONObject responseObj = new JSONObject(response);
            int num_groups = responseObj.getInt("num groups");
            for(int i=1; i<=num_groups; ++i){
                JSONObject list = (JSONObject) responseObj.get("list");
                JSONObject item_i = (JSONObject) list.get(Integer.toString(i));
                String name = (String) item_i.get("name");
                int color = colors[i%5];
                //String preview = (String) item_i.get("preview");
                // TODO: feed these into an adapter (name & color. preview later)
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


        //setContentView(R.layout.activity_group_list);
        mListView = (ListView) findViewById(R.id.group_list);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        // What is "mode" for???
        //mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListView", "onItemSelected:" + position);
                // TODO: handle this
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Log.e("ListView", "onNothingSelected:");
            }
        });



        // Create group_list
        //String[] keys = {"key1"};
        //String[] values = {"value1"};
        //String response = PostRequester.request("full_url", keys, values); // TODO API?

        //renderGroupListView();
    }

}
