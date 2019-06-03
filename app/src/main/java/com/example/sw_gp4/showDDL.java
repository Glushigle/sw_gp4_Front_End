package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ddl);
        overridePendingTransition(0,0);//去掉滑入动画

        // Navigation bar
        // Every activity with a navigation bar should add these lines
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_showDDL);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置顶部标题栏
        Calendar cal = Calendar.getInstance();
        final int initYear = cal.get(Calendar.YEAR);
        final int initMonth = cal.get(Calendar.MONTH);
        String initDate = String.format("%d年%d月",initYear,initMonth+1);
        final TextView tv_date = findViewById(R.id.showddlTvDate);
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
                        show_ddl_view(year,month+1);
                    }
                },initYear,initMonth).show();
            }
        });
        Toolbar toolbar = findViewById(R.id.showddlTbar);
        setSupportActionBar(toolbar);

        //显示DDL内容
        show_ddl_view(initYear,initMonth+1);
    }
    private void show_ddl_view(int year, int month) {
        LinearLayout li_parent = findViewById(R.id.li_ddl_disp);//父布局
        if (li_parent.getChildCount() != 0) {
            li_parent.removeAllViews();
            ScrollView scrollView = findViewById(R.id.showddlScroll);
            scrollView.smoothScrollTo(0,0);
        }
        List<DDLOfDay> ddlofmonth = getDataOfDate(year,month);
        if (ddlofmonth == null || ddlofmonth.isEmpty()) {
            Log.d("show_ddl_view","none");
            if (ddlofmonth == null)
                Log.d("show_ddl_view","null");
            LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            TextView tv_none = new TextView(this);
            tv_none.setLayoutParams(para);
            tv_none.setGravity(Gravity.CENTER);
            tv_none.setBackgroundColor(Color.rgb(255,255,255));
            tv_none.setTextColor(Color.rgb(132,133,135));
            tv_none.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            //tv_none.setTypeface(Typeface.DEFAULT_BOLD);
            tv_none.setText("暂无数据\n点击上方加号新建");//设置显示的文本
            li_parent.addView(tv_none);
        }
        else {
            int sz = ddlofmonth.size();
            DDLOfDay ddlofday;
            for (int i = 0; i < sz; ++i) {
                ddlofday = ddlofmonth.get(i);
                add_ddl_view(li_parent,ddlofday.day,ddlofday.week,ddlofday.data);
            }
        }
    }
    private List<DDLOfDay> getDataOfDate(int year, int month) {
        Log.d("getdataofdate",String.valueOf(year)+String.valueOf(month));
        String full_url = this.getString(R.string.server_uri)+"get_tasklist";
        String[] keys = {};
        String[] values = {};
        String response = Requester.get(full_url,keys,values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if (valid){
                JSONArray allData = responseObj.getJSONArray("task list");//所有数据
                int size = allData.length();//数据长度
                Log.d("data size = ",String.valueOf(size));
                List<DDLOfDay> ddlofmonth = new ArrayList<>();//指定月份的所有ddl
                int currentDay = 0;
                String ym = String.format("%d-%02d",year,month);
                DDLOfDay ddlofday = new DDLOfDay("00","None");//某一天的所有ddl
                JSONObject tmpObj;
                for (int i = 0; i < size; ++i) {//提取需要的数据
                    tmpObj = allData.getJSONObject(i);
                    Log.d("date = ",tmpObj.getString("finish_time"));
                    String deadline = tmpObj.getString("finish_time");
                    if (deadline.startsWith(ym)) {//指定月份的ddl
                        String dy = deadline.substring(8,10);//该月的第几天
                        int d = Integer.parseInt(dy);
                        if (d != currentDay) {
                            if (currentDay != 0) {
                                ddlofmonth.add(ddlofday);//存储之前某一天的数据
                                ddlofday.data.clear();
                            }
                            currentDay = d;
                            ddlofday.day = dy;
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date temp = format.parse(deadline);
                                format = new SimpleDateFormat("E");
                                ddlofday.week = format.format(temp);//星期几
                            } catch(ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        String id = tmpObj.getString("task_id");//ddl信息
                        String time = deadline.substring(11,16);
                        String title = tmpObj.getString("title");
                        String description = tmpObj.getString("info");
                        String status = tmpObj.getString("status");
                        ddlofday.data.add(new DDLText(id,time,status,title,description));
                    }
                }
                if (currentDay != 0)//最后出现的一天的ddl
                    ddlofmonth.add(ddlofday);
                return ddlofmonth;
            }
            else{
                Toast.makeText(this, "获取DDL失败", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch(JSONException e) {
            Toast.makeText(this, "目前没有DDL～", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
    private void add_ddl_view(LinearLayout li_parent, String date, String week, List<DDLText> data) {
        //屏幕宽度
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int screenWidth = dm.widthPixels;
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
        li_date.setBackgroundColor(Color.rgb(255,255,255));
        LinearLayout.LayoutParams para2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_date = new TextView(this);
        tv_date.setLayoutParams(para2);
        tv_date.setGravity(Gravity.CENTER);//文本居中
        tv_date.setBackgroundColor(Color.rgb(255,255,255));//背景颜色
        tv_date.setTextColor(Color.rgb(0,0,0));//文本颜色
        tv_date.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv_date.setTypeface(Typeface.DEFAULT_BOLD);
        tv_date.setText(date);//设置显示的文本
        li_date.addView(tv_date);//加入布局li_date
        TextView tv_week = new TextView(this);
        tv_week.setLayoutParams(para2);
        tv_week.setGravity(Gravity.CENTER);//文本居中
        tv_week.setBackgroundColor(Color.rgb(255,255,255));//背景颜色
        tv_week.setTextColor(Color.rgb(0,0,0));//文本颜色
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
        leftSlideAdapter adapter = new leftSlideAdapter(this,data,screenWidth-ftmp );
        re_ddl.setAdapter(adapter);
        re_ddl.setItemAnimator(new DefaultItemAnimator());
        li_date_ddl.addView(re_ddl);
        li_parent.addView(li_date_ddl);//加入布局li_parent
    }
    public void onClickAddBtn(View view) {//切换到添加页
        startActivity(new Intent(mContext, WriteDDL.class));
        finish();
    }
    public void onClickExitBtn(View view) {
        String full_url = this.getString(R.string.server_uri)+"logout";
        String[] keys = {};
        String[] values = {};
        String response = Requester.get(full_url, keys, values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean success = responseObj.getBoolean("valid");
            if (success){
                Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "退出失败", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(this, "bug", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//登出
    }
    public void onItemClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onItemClick");
        DDLText ddl = adapter.getData(position);
        String full_url = this.getString(R.string.server_uri)+"update_task";
        String[] keys = {"task_id", "status"};
        String[] values = {ddl.ddl_id, "1"};
        String response = Requester.post(full_url,keys,values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");
            if (valid){
                Toast.makeText(this, "已标记为完成", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "标记失败", Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e) {
            Toast.makeText(this, "bug in onItemClick()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void onDeleteClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onDeleteClick");
        DDLText ddl = adapter.getData(position);
        String full_url = this.getString(R.string.server_uri)+"delete_task";
        String[] keys = {"task_id"};
        String[] values = {ddl.ddl_id};Log.d("taskid",values[0]);
        String response = Requester.post(full_url,keys,values);Log.d("debug","0");
        try {Log.d("debug","1");
            JSONObject responseObj = new JSONObject(response);
            boolean valid = responseObj.getBoolean("valid");Log.d("debug","2");
            if (valid){
                Toast.makeText(this, "删除DDL成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "删除DDL失败", Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e) {
            Toast.makeText(this, "bug in onDeleteClick()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        adapter.removeData(position);
    }
    public void onEditClick(View v, int position, leftSlideAdapter adapter) {
        Log.d("disp","onEditClick");
        DDLText ddl = adapter.getData(position);
        Intent intent = new Intent(this, WriteDDL.class);
        intent.putExtra("task_id", ddl.ddl_id);
        //startActivityForResult(intent,0);
        startActivity(intent);
        finish();
    }
}