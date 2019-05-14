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

    private ArrayList<Integer> friend_colors;
    private ArrayList<String> friend_ids;
    private ArrayList<String> friend_names;
    private ArrayList<String> friend_percents;

    private boolean updateData(){
        /* Suppose the return format is
            {"groups":[
                 {"id":"1", "name":"Name 1", "percent":"100"},
                 {"id":"2", "name":"Name 2", "percent":"97"},
                 ...
                 {"id":"n", "name":"Name n", "percent":"0"}]
            }
        */
        int colors[] = {R.color.gp_1,
                R.color.gp_2,
                R.color.gp_3,
                R.color.gp_4,
                R.color.gp_5};
        // TODO: programmatically get response
        //String[] keys = {"username"};
        //String[] values = {"user1"};
        //String response = PostRequester.request("full_url", keys, values);
        String response = "{\"ranking\":[\n" +
                "                 {\"id\":\"1\", \"name\":\"Bob\",   \"percent\":\"100\"},\n" +
                "                 {\"id\":\"2\", \"name\":\"Alice\", \"percent\": \"97\"},\n" +
                "                 {\"id\":\"3\", \"name\":\"Kate\",  \"percent\": \"95\"},\n" +
                "                 {\"id\":\"4\", \"name\":\"Carol\", \"percent\": \"92\"},\n" +
                "                 {\"id\":\"5\", \"name\":\"Dave\",  \"percent\": \"90\"}]\n" +
                "            }";
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray groups = (JSONArray) responseObj.getJSONArray("ranking");
            friend_ids = new ArrayList<String>();
            friend_names = new ArrayList<String>();
            friend_percents = new ArrayList<String>();
            friend_colors = new ArrayList<Integer>();
            for(int i=0; i<groups.length(); ++i){
                friend_ids.add((String) groups.getJSONObject(i).getString("id"));
                friend_names.add((String) groups.getJSONObject(i).getString("name"));
                friend_percents.add((String) groups.getJSONObject(i).getString("percent"));
                friend_colors.add(colors[i%colors.length]); // TODO 色卡
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

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
                    return true;
            }
            return false;
        }
    };

    private void setMyRank(){
        int myRank = 2; // TODO: get id from cookie; match rank by myId
        ((GradientDrawable) findViewById(R.id.my_color).getBackground()).setColor(
                ContextCompat.getColor(mContext, friend_colors.get(myRank-1)));
        ((TextView)findViewById(R.id.my_name)).setText(friend_names.get(myRank-1));
        ((TextView)findViewById(R.id.my_rank)).setText("Rank "+Integer.toString(myRank));
        ((TextView)findViewById(R.id.my_percent)).setText(friend_percents.get(myRank-1)+"%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_Ranking);

        findViewById(R.id.add_friend).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, friends.class));
                finish();
            }
        });

        updateData();
        setMyRank();

        recyclerView = (RecyclerView) findViewById(R.id.friends_rank);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RankingAdapter(mContext,friend_colors,friend_names,friend_percents);
        recyclerView.setAdapter(mAdapter);

    }
}
