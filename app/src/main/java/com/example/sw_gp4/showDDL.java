package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class showDDL extends AppCompatActivity implements leftSlideAdapter.slideViewClickListener {

    private Context mContext=this;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ddl);

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_showDDL);

        //设置顶部标题栏
        Calendar cal = Calendar.getInstance();
        final int initYear = cal.get(Calendar.YEAR);
        final int initMonth = cal.get(Calendar.MONTH);
        String initDate = String.format("%d年%d月",initYear,initMonth+1);
        final TextView tv_date = findViewById(R.id.showddlTvDate);
        //tv_date.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_date.setText(initDate);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new datePickerDialog(showDDL.this, 0, new datePickerDialog.dateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year, int month) {
                        String date = String.format("%d年%d月",year,month+1);
                        tv_date.setText(date);
                        //更新ddl
                    }
                },initYear,initMonth).show();
            }
        });
        Toolbar toolbar = findViewById(R.id.showddlTbar);
        //toolbar.setTitle("我的Deadline");
        setSupportActionBar(toolbar);

        //显示DDL内容
        show_ddl_view(initYear,initMonth);
    }
    private void show_ddl_view(int year, int month) {
        LinearLayout li_parent = findViewById(R.id.li_ddl_disp);//父布局
        //get_ddl_info();
        String[] date = {"01","02","03"};
        String[] week = {"Mon","Tue","Wed"};
        DDLText ddl = new DDLText("12:00","My DDL","hello world");
        List<DDLText> data = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            data.add(ddl);
        }
        data.add(new DDLText("11:00","My DDL"));
        data.add(new DDLText("13:00","My DDL","my code works but why"));
        for (int i = 0; i < 3; ++i) {
            add_ddl_view(li_parent,date[i],week[i],data);
        }
    }
    private List<DDLOfDay> getDataOfDate(int year, int month) {
        return null;
    }
    private void add_ddl_view(LinearLayout li_parent, String date, String week, List<DDLText> data) {
        //布局参数
        LinearLayout.LayoutParams para0 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        para0.setMargins(0,0,0,5);
        //新建一个布局来显示日期和ddl
        LinearLayout li_date_ddl = new LinearLayout(this);
        li_date_ddl.setOrientation(LinearLayout.HORIZONTAL);//横向
        li_date_ddl.setLayoutParams(para0);
        //显示日期
        int ftmp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,45,getResources().getDisplayMetrics());
        LinearLayout.LayoutParams para1 = new LinearLayout.LayoutParams(
                ftmp,ViewGroup.LayoutParams.MATCH_PARENT);//参数
        LinearLayout li_date = new LinearLayout(this);
        li_date.setOrientation(LinearLayout.VERTICAL);
        li_date.setLayoutParams(para1);
        //li_date.setGravity(Gravity.CENTER_HORIZONTAL);
        li_date.setBackgroundColor(Color.rgb(125,125,125));
        LinearLayout.LayoutParams para2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_date = new TextView(this);
        tv_date.setLayoutParams(para2);
        tv_date.setGravity(Gravity.CENTER);//文本居中
        tv_date.setBackgroundColor(Color.rgb(125,125,125));//背景颜色
        tv_date.setTextColor(Color.rgb(255,255,255));//文本颜色
        tv_date.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv_date.setTypeface(Typeface.DEFAULT_BOLD);
        tv_date.setText(date);//设置显示的文本
        li_date.addView(tv_date);//加入布局li_date
        TextView tv_week = new TextView(this);
        tv_week.setLayoutParams(para2);
        tv_week.setGravity(Gravity.CENTER);//文本居中
        tv_week.setBackgroundColor(Color.rgb(125,125,125));//背景颜色
        tv_week.setTextColor(Color.rgb(255,255,255));//文本颜色
        tv_week.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv_week.setTypeface(Typeface.DEFAULT_BOLD);
        tv_week.setText(week);//设置显示的文本
        li_date.addView(tv_week);//加入布局li_date
        li_date_ddl.addView(li_date);
        //显示ddl
        LinearLayout.LayoutParams para3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView re_ddl = new RecyclerView(this);
        re_ddl.setLayoutParams(para3);
        re_ddl.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        leftSlideAdapter adapter = new leftSlideAdapter(this,data);
        re_ddl.setAdapter(adapter);
        re_ddl.setItemAnimator(new DefaultItemAnimator());
        li_date_ddl.addView(re_ddl);
        li_parent.addView(li_date_ddl);//加入布局li_parent
    }
    public void onClickAddBtn(View view) {
        //切换到添加页
        //Intent intent = new Intent(this,WriteDDL.class);
        //startActivity(intent);
        startActivity(new Intent(mContext, WriteDDL.class));
        finish();
    }
    public void onClickExitBtn(View view) {
        //登出
    }
    public void onItemClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onItemClick");
        //return;
    }
    public void onDeleteClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onDeleteClick");
        adapter.removeData(position);
    }
    public void onEditClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onEditClick");
        /*DDLText ddl = adapter.getData(position);
        Intent intent = new Intent(this,editDDL.class);
        intent.putExtra("title",ddl.title);
        intent.putExtra("description",ddl.description);
        startActivityForResult(intent,0);*/
    }/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
        }
    }*/
}