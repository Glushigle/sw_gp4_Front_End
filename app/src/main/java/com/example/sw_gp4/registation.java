package com.example.sw_gp4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class registation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
    }
    public String encode(String str){
        int tmp, length = str.length();
        char ch[] = new char[100];
        ch = str.toCharArray();
        for(int i = 0; i < length; i++){
            tmp = (int)ch[i];
            tmp += 4;
            ch[i] = (char)tmp;
        }
        String code = new String(ch);
        return code;
    }
    public void reg(View v){
        EditText email = (EditText) findViewById(R.id.reg_email) ;
        EditText username = (EditText) findViewById(R.id.reg_username) ;
        EditText password = (EditText) findViewById(R.id.reg_psw) ;
        EditText password2 = (EditText) findViewById(R.id.reg_psw_2) ;
        boolean hasDigit = false;
        boolean hasLetter = false;
        for(int i=0 ; i<password.getText().toString().length() ; i++){
            if(Character.isDigit(password.getText().toString().charAt(i))){
                hasDigit = true;
            }
            if(Character.isLetter(password.getText().toString().charAt(i))){
                hasLetter = true;
            }
        }

        if(email.getText().toString().isEmpty() || username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
            Toast.makeText(this, "注册信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.getText().toString().contains("@")){
            Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if(username.getText().toString().length() < 4 || username.getText().toString().length() > 10){
            Toast.makeText(this, "用户名长度不符合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().length() < 4 || username.getText().toString().length() > 10){
            Toast.makeText(this, "密码长度不符合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(hasDigit && hasLetter)){
            Toast.makeText(this, "密码需要包含字母和数字", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        String full_url = this.getString(R.string.server_uri)+"register";
        String[] keys = {"username","password"};
        String[] values = {username.getText().toString(), encode(password.getText().toString())};
        String response = Requester.post(full_url, keys, values);
        try {
            //Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            JSONObject responseObj = new JSONObject(response);
            boolean success = responseObj.getBoolean("valid");
            if (success){
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
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
    public void back(View v){
        finish();
    }
}
