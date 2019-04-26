package com.example.sw_gp4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupList extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private void addGroupView(String color, String name, String preview){ // Position, size...?
        /*
        Swipe-to-delete view: https://demonuts.com/android-listview-swipe-delete/
         */

        LinearLayout myLayout = findViewById(R.id.group_list);

        Button myButton = new Button(this);
        myButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        myLayout.addView(myButton);
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
        String response = "{\"num\":\"3\",\n\"list\":{\n\"1\":{{\"name\":\"Group Name 1\",\"preview\":\"Upcoming DDL preview 1\"},\n\"2\":{\"name\":\"Group Name 2\",\"preview\":\"Upcoming DDL preview 2\"},\n\"3\":{\"name\":\"Group Name 3\",\"preview\":\"Upcoming DDL preview 3\"}}}";
        try {
            JSONObject responseObj = new JSONObject(response);
            int num_groups = responseObj.getInt("num groups");
            for(int i=1; i<=num_groups; ++i){
                JSONObject list = (JSONObject) responseObj.get("list");
                JSONObject item_i = (JSONObject) list.get(Integer.toString(i));
                String name = (String) item_i.get("name");
                String preview = (String) item_i.get("preview");
                addGroupView("", name, preview); // TODO Color format?
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create group_list
        //String[] keys = {"key1"};
        //String[] values = {"value1"};
        //String response = PostRequester.request("full_url", keys, values); // TODO API?
        renderGroupListView();
    }

}
