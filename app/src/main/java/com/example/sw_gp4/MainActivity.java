package com.example.sw_gp4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        //Toast.makeText(this, "登录失败233", Toast.LENGTH_SHORT).show();//print helloworld
        Intent intent = new Intent(this,showDDL.class);
        startActivity(intent);
    }
}