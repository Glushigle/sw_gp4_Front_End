package com.example.sw_gp4;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://blog.csdn.net/withiter/article/details/19908679 TODO 改成好方法
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void login(View v) {
        EditText username = (EditText) findViewById(R.id.et_user_name) ;
        EditText password = (EditText) findViewById(R.id.et_psw) ;

        String full_url = getResources().getString(R.string.server_uri)+"login";
        String[] keys = {"username","password"};
        String[] values = {username.getText().toString(), password.getText().toString()};
        String response = Requester.post(full_url, keys, values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean success = responseObj.getBoolean("valid");
            if (success){
                GroupList.currUserName = username.getText().toString();//用currUserName记录当前用户名
                User.username = username.getText().toString(); // 大家一起share username嘛^^ 每個activity都要用欸
                Intent intent = new Intent(this,showDDL.class);
                startActivity(intent);
            }else{
                String info = responseObj.getString("error_info");
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(this, "bug", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
  
    public void register(View v) {
        Intent reg = new Intent(this,registation.class);
        startActivity(reg);
//        EditText username = (EditText) findViewById(R.id.et_user_name) ;
//        EditText password = (EditText) findViewById(R.id.et_psw) ;
//
//        String full_url = getResources().getString(R.string.server_uri)+"register";
//        String[] keys = {"username","password"};
//        String[] values = {username.getText().toString(), password.getText().toString()};
//        String response = Requester.post(full_url, keys, values);
//        try {
//            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
//            JSONObject responseObj = new JSONObject(response);
//            boolean success = responseObj.getBoolean("valid");
//            if (success){
//                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//            }else{
//                String info = responseObj.getString("error_info");
//                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (JSONException e) {
//            Toast.makeText(this, "bug", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }

}