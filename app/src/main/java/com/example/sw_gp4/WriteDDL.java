package com.example.sw_gp4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sw_gp4.FriendTask.Task;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteDDL extends AppCompatActivity {

    private boolean existing;   // cf created
    private boolean personal;   // cf group
    //private String task_id;
    private String group_id;
    private String group_name;
    private Task task;

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
                    // TODO: group/personal的 create_ddl的navigation的selected不一样
                    //进到个人页
                    //startActivity(new Intent(mContext,showDDL.class));
                    //finish();
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
    private ImageButton.OnClickListener mOnCrossClickedListener
            = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            startActivity(new Intent(mContext, showDDL.class));
            finish();
        }
    };
    private String fetchInput(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
    private String formattedDDL(){
        return String.valueOf(timeSet[0])+"-"+String.valueOf(timeSet[1])+"-"+String.valueOf(timeSet[2])+
                " "+String.valueOf(timeSet[3])+":"+String.valueOf(timeSet[4])+":00";
    }
    private  Button.OnClickListener mOnSaveClickedListener
            = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {

            String response = null;

            // Save New Personal DDL
            if(((WriteDDL)mContext).personal){
                String[] keys = {"title","deadline","info","publicity"};
                String[] values = {
                        fetchInput(R.id.ddl_title),
                        formattedDDL(),
                        fetchInput(R.id.ddl_info),
                        ((Switch)findViewById(R.id.ddl_reminder_switch)).isChecked()? "1":"0"
                };
                response = Requester.post(getResources().getString(R.string.server_uri)+"create_task",keys,values);
            }

            // Save New Group DDL
            else{
                String[] keys = {"title","deadline","info","publicity", "group_id"};
                String[] values = {
                        fetchInput(R.id.ddl_title),
                        formattedDDL(),
                        fetchInput(R.id.ddl_info),
                        ((Switch)findViewById(R.id.ddl_reminder_switch)).isChecked()? "1":"0",
                        ((WriteDDL)mContext).group_id
                };
                response = Requester.post(getResources().getString(R.string.server_uri)+"create_task",keys,values);
            }

            try{
                JSONObject responseObj = new JSONObject(response);
                boolean valid = responseObj.getBoolean("valid");
                if(valid){
                    Toast.makeText(mContext, "DDL saved", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(mContext, showDDL.class));
                    finish();
                }
                else{
                    Toast.makeText(mContext, "Invalid DDL!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private int[] timeSet = {0,0,0,0,0};
    private int[] cal2int(Calendar cal){
        int[] dt = { cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),
                cal.get(Calendar.HOUR_OF_DAY),0};
        return dt;
    }
    private int[] next(String gap){
        Calendar cal = Calendar.getInstance();
        switch (gap){
            case "day":
                cal.add(Calendar.DATE, 1);
                cal.set(Calendar.HOUR_OF_DAY, 9);
                break;
            case "hour":
                cal.add(Calendar.HOUR_OF_DAY, 1);
                break;
        }
        return cal2int(cal);
    }
    private void updateTime(int[] dt){
        this.timeSet = dt;
    }
    private String formatDate(int y, int M, int d){
        this.timeSet[0] = y;
        this.timeSet[1] = M;
        this.timeSet[2] = d;
        return String.format(Locale.CHINESE,"%d 年 %d 月 %d 日",y,M,d);
    }
    private  String formatTime(int h, int m){
        this.timeSet[3] = h;
        this.timeSet[4] = m;
        /*String apm = "上午";
        if(h>=12) apm = "下午";
        h = (h+11)%12+1;*/
        return String.format(Locale.CHINESE," %02d : %02d",h,m);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_ddl);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_showDDL);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ((ImageButton)findViewById(R.id.btn_cross)).setOnClickListener(mOnCrossClickedListener);
        ((Button)findViewById(R.id.btn_save)).setOnClickListener(mOnSaveClickedListener);

        // Get 1) it's create/edit  2) whether it's a group ddl
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                this.task.id = null;
                this.group_id = null;
                this.group_name = null;
            } else {
                this.task.id = extras.getString("task_id");
                this.group_id = extras.getString("group_id");
                this.group_name = extras.getString("group_name");
            }
        } else {
            this.task.id = (String) savedInstanceState.getSerializable("task_id");
            this.group_id = (String) savedInstanceState.getSerializable("group_id");
            this.group_name = (String) savedInstanceState.getSerializable("group_name");
        }
        this.existing = (this.task.id != null);
        this.personal = (this.group_id == null);

        // Date/time picker initial value
        final TextView ddl_date = findViewById(R.id.ddl_date);
        final TextView ddl_time = findViewById(R.id.ddl_time);
        if(this.existing){
            //get timeSet from backend. Same for personal/group-wise
            String[] keys = {"task_id"};
            String[] values = {task.id};
            String response = Requester.post(getResources().getString(R.string.server_uri)+"get_task",keys,values);
            try{
                JSONObject responseObj = new JSONObject(response);
                boolean valid = responseObj.getBoolean("valid");
                if(valid){
                    Toast.makeText(mContext, "DDL received", Toast.LENGTH_SHORT).show();
                    JSONObject taskObj = responseObj.getJSONObject("task");
                    // todo: check api with backend
                    this.task = new Task(
                            taskObj.getString("task_id"),
                            taskObj.getString("title"),
                            taskObj.getString("finish_time"),
                            Boolean.valueOf(taskObj.getString("status")),
                            taskObj.getString("info"));
                }
                else{
                    Toast.makeText(mContext, "get_task: Invalid DDL!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            final int[] init_dt = next("hour");
            this.timeSet = init_dt;
            ddl_date.setText(formatDate(init_dt[0],init_dt[1],init_dt[2]));
            ddl_time.setText(formatTime(init_dt[3],init_dt[4]));
        }
        // Date/time picker listener
        ddl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(mContext, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        ddl_date.setText(formatDate(i,i1,i2));
                    }
                },timeSet[0],timeSet[1]-1,timeSet[2]).show();
            }
        });
        ddl_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ddl_time.setText(formatTime(i,i1));
                    }
                },timeSet[3],timeSet[4],false).show();
            }
        });

        // Group task: tune layout
        if(!this.personal){
            TextView groupname_view = findViewById(R.id.group_name);
            groupname_view.setText(this.group_name);
            groupname_view.setVisibility(View.VISIBLE);
        }

    }
}
