package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context mContext=this;
    private ImageButton addButton;

    private String currUserName;
    private int my_rank;
    private ArrayList<Integer> friend_colors;
    private ArrayList<String> friend_usernames;
    //private ArrayList<String> friend_names;

    private void updateData(){
        try {
            String[] keys = {};
            String[] values = {};
            String response = Requester.get(
                    getResources().getString(R.string.server_uri)+"get_friendlist",
                    keys, values);
            JSONObject responseObj = new JSONObject(response);//TODO 問後端我ranking多少
            JSONArray friends = (JSONArray) responseObj.getJSONArray("friend list");
            friend_usernames = new ArrayList<String>();
            //friend_names = new ArrayList<String>();
            friend_colors = new ArrayList<Integer>();
            for(int i=0; i<friends.length(); ++i){
                friend_usernames.add((String) friends.getJSONObject(i).getString("username"));
                //friend_names.add((String) groups.getJSONObject(i).getString("name"));
                friend_colors.add(ColorConverter.fromName(friend_usernames.get(i)));
            }

            //TODO set my rank
            my_rank = friend_usernames.size();
            friend_usernames.add(currUserName);
            friend_colors.add(ColorConverter.fromName((currUserName)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =null;
            /*new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                    return true;
            }
            return false;
        }
    };*/

    private void setMyRank(){
        ((GradientDrawable) findViewById(R.id.my_color).getBackground()).setColor(friend_colors.get(my_rank-1));
        ((TextView)findViewById(R.id.my_name)).setText(friend_usernames.get(my_rank-1));
        ((TextView)findViewById(R.id.my_rank)).setText("Rank "+Integer.toString(my_rank));
    }

    public void OnAddClicked(View view){
        startActivity(new Intent(mContext, friends.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_Ranking);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        addButton = (ImageButton) findViewById(R.id.imageButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OnAddClicked(v);
            }
        });

        updateData();
        setMyRank();

        recyclerView = (RecyclerView) findViewById(R.id.friends_rank);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RankingAdapter(mContext,friend_colors,friend_usernames);
        recyclerView.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                this.currUserName = null;
            } else {
                this.currUserName = extras.getString("username");
            }
        } else {
            this.currUserName  = (String) savedInstanceState.getSerializable("username");
        }

    }
}
