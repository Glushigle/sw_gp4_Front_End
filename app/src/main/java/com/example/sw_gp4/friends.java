package com.example.sw_gp4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class friends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }
    public void add_friends(View v){
        Toast.makeText(this, "好友请求已发送", Toast.LENGTH_SHORT).show();
    }
}
