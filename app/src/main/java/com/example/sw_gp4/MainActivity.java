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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public String encode(String str){
        int tmp;
        char ch[] = new char[100];
        ch = str.toCharArray();
        for(int i = 0; i< str.length();i++){
            tmp = (int)ch[i];
            tmp+=4;
            ch[i]=(char)tmp;
        }
        String code = new String(ch);
        //Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
        return code;
    }

    public void login(View v) {
        EditText username = (EditText) findViewById(R.id.et_user_name) ;
        EditText password = (EditText) findViewById(R.id.et_psw) ;

        String encoded_password = encode(password.getText().toString());
        String full_url = this.getString(R.string.server_uri)+"login";
        String[] keys = {"username","password"};
        String[] values = {username.getText().toString(), encoded_password};
        String response = Requester.post(full_url, keys, values);
        try {
            JSONObject responseObj = new JSONObject(response);
            boolean success = responseObj.getBoolean("valid");
            if (success){
                GroupList.currUserName = username.getText().toString();//用currUserName记录当前用户名
                User.username = username.getText().toString(); // 大家一起share username嘛^^ 每個activity都要用欸
                Intent intent = new Intent(this,showDDL.class);
                startActivity(intent);
                finish();
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
    }

}