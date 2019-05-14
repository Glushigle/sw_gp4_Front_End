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

    private int my_rank;
    private ArrayList<Integer> friend_colors;
    private ArrayList<String> friend_ids;
    private ArrayList<String> friend_names;
    private ArrayList<String> friend_percents;

    private void updateData(){
        /* Suppose the return format is
            {"my rank": "2",
             "ranking":[
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
        String response = "{\"my rank\":\"2\"," +
                "           \"ranking\":[\n" +
                "                 {\"id\":\"1\", \"name\":\"Bob\",   \"percent\":\"100\"},\n" +
                "                 {\"id\":\"2\", \"name\":\"Alice\", \"percent\": \"97\"},\n" +
                "                 {\"id\":\"3\", \"name\":\"Kate\",  \"percent\": \"95\"},\n" +
                "                 {\"id\":\"4\", \"name\":\"Carol\", \"percent\": \"92\"},\n" +
                "                 {\"id\":\"5\", \"name\":\"Dave\",  \"percent\": \"90\"}]\n" +
                "            }";
        try {
            JSONObject responseObj = new JSONObject(response);
            my_rank = (int) responseObj.getInt("my rank");
            JSONArray groups = (JSONArray) responseObj.getJSONArray("ranking");
            friend_ids = new ArrayList<String>();
            friend_names = new ArrayList<String>();
            friend_percents = new ArrayList<String>();
            friend_colors = new ArrayList<Integer>();
            for(int i=0; i<groups.length(); ++i){
                friend_ids.add((String) groups.getJSONObject(i).getString("id"));
                friend_names.add((String) groups.getJSONObject(i).getString("name"));
                friend_percents.add((String) groups.getJSONObject(i).getString("percent"));
                friend_colors.add(colors[i%colors.length]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        ((GradientDrawable) findViewById(R.id.my_color).getBackground()).setColor(
                ContextCompat.getColor(mContext, friend_colors.get(my_rank-1)));
        ((TextView)findViewById(R.id.my_name)).setText(friend_names.get(my_rank-1));
        ((TextView)findViewById(R.id.my_rank)).setText("Rank "+Integer.toString(my_rank));
        ((TextView)findViewById(R.id.my_percent)).setText(friend_percents.get(my_rank-1)+"%");
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_Ranking);

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
