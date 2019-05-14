package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class showDDL extends AppCompatActivity {
    private Context mContext=this;
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // TODO: 写navigation bar.
                case R.id.navigation_home:
                    //进到小组页
                    Intent intent = new Intent(mContext,GroupList.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:
                    //进到好友页
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_show_ddl);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTitle("show_ddl_view");
        show_ddl_view();
    }

    private void show_ddl_view() {
        LinearLayout li_parent = findViewById(R.id.li_ddl_disp);//父布局
        //get_ddl_info();
        String date = "2019-05-01";
        String[] texts = new String[10];
        for (int i = 0; i < 10; ++i) {
            texts[i] = "hello world "+String.valueOf(i);
        }
        for (int i = 0; i < 5; ++i) {
            add_ddl_view(li_parent,date,texts);
        }
    }
    private void add_ddl_view(LinearLayout li_parent, String date, String[] texts) {
        //布局参数
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        para.setMargins(0,0,0,5);
        //新建一个布局来显示日期和ddl
        LinearLayout li_date = new LinearLayout(this);
        li_date.setOrientation(LinearLayout.HORIZONTAL);//横向
        li_date.setLayoutParams(para);
        //显示日期
        LinearLayout.LayoutParams para_date = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);//参数
        TextView tv_date = new TextView(this);
        tv_date.setLayoutParams(para_date);
        tv_date.setGravity(Gravity.CENTER);//文本居中
        tv_date.setBackgroundColor(Color.rgb(125,125,125));//背景颜色
        tv_date.setTextColor(Color.rgb(255,255,255));//文本颜色
        tv_date.setText(date);//设置显示的文本
        li_date.addView(tv_date);//加入布局li_date
        //新建一个布局来显示ddl
        LinearLayout li_text = new LinearLayout(this);
        li_text.setOrientation(LinearLayout.VERTICAL);//纵向
        LinearLayout.LayoutParams para1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        para1.setMargins(0,0,0,0);
        li_text.setLayoutParams(para1);
        //显示ddl文本
        int amt = texts.length;//数量
        for (int i = 0; i < amt; ++i) {
            TextView tv_text = new TextView(this);
            tv_text.setLayoutParams(para1);
            tv_text.setGravity(Gravity.CENTER);//文本居中
            if (i % 2 == 0)//背景颜色
                tv_text.setBackgroundColor(Color.rgb(22,133,169));
            else
                tv_text.setBackgroundColor(Color.rgb(219,90,107));
            tv_text.setTextColor(Color.rgb(255,255,255));//文本颜色
            tv_text.setText(texts[i]);//设置显示的文本
            li_text.addView(tv_text);//加入布局li_text
        }
        li_date.addView(li_text);//加入布局li_date
        li_parent.addView(li_date);//加入布局li_parent
    }
    public void click_addBtn(View view) {
        //
    }
}