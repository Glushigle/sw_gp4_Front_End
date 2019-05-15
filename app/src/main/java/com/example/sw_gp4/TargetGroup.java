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

import org.json.JSONObject;

import java.util.ArrayList;

public class TargetGroup extends AppCompatActivity
{
    public static ArrayList<String> userNames;
    public static Group currGroup;
    public static int currPosition;
    private ListView mListView;
    private ListViewAdapterForTargetGroup mAdapter;
    private Context mContext=this;
    private ImageButton addButton;
    private EditText targetGroupName;
    private Button changeButton;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_GroupList:
                    //进到小组页
                    Intent intent1 = new Intent(mContext,GroupList.class);
                    startActivity(intent1);
                    finish();
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
        //    userNames = new ArrayList<String>();
        //    userNames.add(currUserName);

            currGroup.group_id = GroupList.group_.get(currPosition).group_id;
            currGroup.group_name = GroupList.group_.get(currPosition).group_name;
            currGroup.member = GroupList.group_.get(currPosition).member;
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
        addButton = (ImageButton) findViewById(R.id.imageButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext, "button1 click", Toast.LENGTH_SHORT).show();
                OnAddClicked(v);
            }
        });
        changeButton = (Button)findViewById(R.id.btn_add_member);
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show();
                //EditText username = (EditText) findViewById(R.id.target_group_name) ;
                currGroup.group_name = targetGroupName.getText().toString();
                GroupList.group_.get(currPosition).group_name = currGroup.group_name;
                //TODO: 虽然改了group_，但是group_name改不了，显示不会变
            }
        });

        targetGroupName = (EditText) findViewById(R.id.text_member_name);
        targetGroupName.setText(currGroup.group_name);



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
        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(this).setTitle("请输入组员名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //按下确定键后的事件 Todo:给后端发送一个只有用户名的请求，返回该用户名是否存在
                        Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                        boolean success = true;
                        if(success)
                        {
                            userNames.add(et.getText().toString());
                            currGroup.member.add(et.getText().toString());
                            mAdapter.resetData(userNames);

                        }
                    }
                }).setNegativeButton("取消",null).show();

    }

}
