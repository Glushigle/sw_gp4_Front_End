package com.example.sw_gp4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {

        //Toast.makeText(this, "登录失败233", Toast.LENGTH_SHORT).show();

        EditText username = (EditText) findViewById(R.id.et_user_name) ;
        EditText password = (EditText) findViewById(R.id.et_psw) ;


        if (username.getText().toString().equals("username") &&
                password.getText().toString().equals("password")){
            Intent intent = new Intent(this,showDDL.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, username.getText().toString()
//                    + password.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}