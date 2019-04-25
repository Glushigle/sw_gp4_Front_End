package com.example.sw_gp4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

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

    private void generateGroupView(String name, String text){ // Position, size...?

    }

    private void responseToGroupListView(String response){
        /* The return is in json.
        Suppose it's
            {"1": {"Group Name 1":"Upcoming DDL text"},
             "2": {"Group Name 2":"Upcoming DDL text"},
             ...,
             "n": {"Group Name n":"Upcoming DDL text"}}
        sorted by their next DDLs. (Plz refer to the UI design slides.)
        * */
        // String to json
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create group_list
        PostRequester requester = new PostRequester();
        String[] keys = {"key1"};
        String[] values = {"value1"};
        String response = requester.request("full_url", keys, values); // TODO API?
    }

}
