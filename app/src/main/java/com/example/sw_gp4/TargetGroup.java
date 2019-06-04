package com.example.sw_gp4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    public static String groupId = null;
    public static String groupName = null;
    public static boolean isAdding;
    private ListView mListView;
    private ListViewAdapterForTargetGroup mAdapter;
    private Context mContext = this;
    private Button addButton;
    //private EditText targetGroupName;
    private  EditText GN;
    private EditText userAwaiting;
    private ImageButton closeButton;
    private int colors[] = {
            R.color.gp_1,
            R.color.gp_2,
            R.color.gp_3,
            R.color.gp_4,
            R.color.gp_5};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                    Intent intent3 = new Intent(mContext,friends.class);
                    startActivity(intent3);
                    finish();
                    return true;
            }
            return false;
        }
    };

    private void updateData()
    {
        GN.setText(groupName);
        String[] keys = {"group_id"};
        String[] values = {groupId};
        String response = Requester.post("https://222.29.159.164:10014/"+"get_group_member", keys, values);
        try
        {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if(valid){
                JSONArray memberList = responseObj.getJSONArray("member list");
                userNames = new ArrayList<String>();
                for(int i = 0;i < memberList.length();++i)
                {
                    userNames.add(memberList.getJSONObject(i).getString("username"));
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
        setContentView(R.layout.activity_target_group);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_GroupList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        groupName = intent.getStringExtra("group_name");
        groupId = intent.getStringExtra("group_id");

        // Group List

        GN = findViewById(R.id.group_name);
        updateData();
        mListView = (ListView) findViewById(R.id.ddl_list);
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
        mListView = (ListView) findViewById(R.id.ddl_list);
        addButton = (Button) findViewById(R.id.button);
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

        GN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText editText = new EditText(TargetGroup.this);
                new AlertDialog.Builder(TargetGroup.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("请输入小组名").setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                String[] keys = {"name","group_id"};
                                String[] values = {editText.getText().toString().trim(),groupId};
                                String response = Requester.post(
                                        //R.string.server_uri
                                        "https://222.29.159.164:10014/update_group"
                                        //  + "create_group"
                                        , keys, values);
                                try
                                {
                                    JSONObject responseObj = new JSONObject(response);
                                    boolean valid = responseObj.getBoolean("valid");
                                    if (valid)
                                    {
                                        Toast.makeText(mContext, "已添加小组", Toast.LENGTH_SHORT).show();
                                        groupName = editText.getText().toString().trim();
                                        updateData();
                                    }
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                mAdapter.closeAllItems();
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
            String[] keys = {"user_username","group_id"};
            String[] values = {userNames.get(position),groupId};
            String response = Requester.post(
                    //R.string.server_uri
                    "https://222.29.159.164:10014/delete_member"
                    //  + "create_group"
                    , keys, values);
            try
            {
                JSONObject responseObj = new JSONObject(response);
                boolean valid2 = responseObj.getBoolean("valid");
                if (valid2)
                {
                    userNames.remove(position);
                    mAdapter.resetData(userNames);

                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    updateData();
                }
                else
                {
                    String error_info = responseObj.getString("error_info");
                    Toast.makeText(mContext, "删除失败："+error_info, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return valid;
    }

    public void OnAddClicked(View view)
    {
        String[] keys = {"group_id", "user_username"};
        String[] values = {
                groupId,
                userAwaiting.getText().toString()
        };
        String response = Requester.post("https://222.29.159.164:10014/"+"add_member", keys, values);

        try
        {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if (valid)
            {
                Toast.makeText(mContext, "小组邀请已送出", Toast.LENGTH_SHORT).show();
                mAdapter.resetData(userNames);

            }
            else
            {
                String error_info = responseObj.getString("error_info");
                Toast.makeText(mContext, error_info, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}