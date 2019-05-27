package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class friendTask extends AppCompatActivity {

    private Context mContext=this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_GroupList:
                    //进到小组页
                    startActivity(new Intent(mContext,GroupList.class));
                    finish();
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    startActivity(new Intent(mContext,showDDL.class));
                    finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    startActivity(new Intent(mContext,Ranking.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_task);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_Ranking);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}
