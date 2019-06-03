package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

public class Navigation {
    private Context mContext;
    private String username;
    public Navigation(Context mContext){
        // TODO change all
        this.mContext = mContext;
    }
    final public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_GroupList:
                    //进到小组页
                    Intent intent1 = new Intent(mContext,GroupList.class);
                    mContext.startActivity(intent1);
                    //mContext.finish();
                    return true;
                case R.id.navigation_showDDL:
                    //进到个人页
                    Intent intent2 = new Intent(mContext,showDDL.class);
                    mContext.startActivity(intent2);
                    //mContext.finish();
                    return true;
                case R.id.navigation_Ranking:
                    //进到好友页
                    Intent intent3 = new Intent(mContext,friends.class);
                    mContext.startActivity(intent3);
                    return true;
            }
            return false;
        }
    };
}
