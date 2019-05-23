package com.example.sw_gp4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

public class WriteDDL extends AppCompatActivity {
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
    private  Button.OnClickListener mOnSaveClickedListener
            = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {

            // Todo: check with api and send "make public" as well
            String[] keys = {"title","deadline","info"};
            String[] values = {
                    fetchInput(R.id.ddl_title),
                    fetchInput(R.id.ddl_date) + " " + fetchInput(R.id.ddl_time),
                    fetchInput(R.id.ddl_info)
            };
            String response = Requester.post("https://222.29.159.164:10016/create_task",keys,values);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_ddl);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_showDDL);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ((ImageButton)findViewById(R.id.btn_cross)).setOnClickListener(mOnCrossClickedListener);
        ((Button)findViewById(R.id.btn_save)).setOnClickListener(mOnSaveClickedListener);
    }
}
