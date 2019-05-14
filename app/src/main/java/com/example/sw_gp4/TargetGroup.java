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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sw_gp4.MainActivity.currUserName;

public class TargetGroup extends AppCompatActivity
{
    public static int maxId;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext=this;
    private ImageButton addButton;
    private FloatingActionButton addButton2;
    private ArrayList<String> userNames;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(mContext,GroupList.class);
                    startActivity(intent1);
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
        //String[] keys = {"username"};
        //String[] values = {"user1"}; //
        //String response = PostRequester.request("full_url", keys, values);
        //String response = "{\"groups\":[\n" +
        //        "                 {\"id\":\"1\", \"name\":\"Group Name 1\"},\n" +
        //        "                 {\"id\":\"2\", \"name\":\"Group Name 2\"},\n" +
        //        "                 {\"id\":\"3\", \"name\":\"Group Name 3\"},\n" +
        //        "                 {\"id\":\"4\", \"name\":\"Group Name 44\"}]\n" +
        //        "            }";
        //try {
        //    JSONObject responseObj = new JSONObject(response);
        //    JSONArray groups = (JSONArray) responseObj.getJSONArray("groups");
            userNames = new ArrayList<String>();
            userNames.add(currUserName);
        //    }
        //} catch (JSONException e) {
        //    e.printStackTrace();
        //}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_group);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Group List
        updateData();
        mListView = (ListView) findViewById(R.id.user_list);
        mAdapter = new ListViewAdapter(this, userNames);
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
        mListView = (ListView) findViewById(R.id.user_list);
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
        boolean valid = true;
        // Toast the returns; update if successful
        if(valid){
            userNames.remove(position);
            mAdapter.resetData(userNames);
        }
        return valid;
    }

    public void OnAddClicked(View view)
    {
        // Todo: 设计一个带输入的弹出框

    }

}
