package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class Ranking extends AppCompatActivity {

    private Context mContext=this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // TODO: 写navigation bar
                case R.id.navigation_home:
                    //进到小组页
                    Intent intent1 = new Intent(mContext,GroupList.class);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    //进到个人页
                    Intent intent2 = new Intent(mContext,showDDL.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //进到好友页
                    return true;
            }
            return false;
        }
    };

    public void seeFriends(View view) {
        // TODO does not work :(
        Intent intent = new Intent(mContext,friends.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
