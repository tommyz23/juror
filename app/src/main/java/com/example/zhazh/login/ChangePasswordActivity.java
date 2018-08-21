package com.example.zhazh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nju.software.data.Holiday;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText presentPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    private String presentPassword_s;
    private String newPassword_s;
    private String confirmPassword_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        presentPassword = (EditText) findViewById(R.id.present_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

        Button button = (Button) findViewById(R.id.bt_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean suitPassword = checkPassword();
                if(suitPassword)
                    change(newPassword_s);
            }
        });
    }
     public void change(String newPassword){
         SharedPreferences read = getSharedPreferences("user", MODE_PRIVATE);
         String id = read.getString("id","");
         String param = "RequestType=Pswd&id=" + id + "newPassword=" + newPassword;
         Log.d("param", param);
         HttpUtil.sendRequest(param, new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 runOnUiThread(
                         new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(ChangePasswordActivity.this, "更改失败，请稍后再试", Toast.LENGTH_LONG).show();
                             }
                         }
                 );
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 String s = response.body().string();
                 final String result = JSONUtil.parseBoolJSON(s);
                 runOnUiThread(
                         new Runnable() {
                             @Override
                             public void run() {
                                 if(result.equals("true"))
                                     Toast.makeText(ChangePasswordActivity.this, "更改成功", Toast.LENGTH_LONG).show();
                                 else Toast.makeText(ChangePasswordActivity.this, "更改失败，请稍后重试", Toast.LENGTH_LONG).show();
                             }
                         }
                 );
             }
         });
     }

     public boolean checkPassword(){
         presentPassword_s = presentPassword.getText().toString().trim();
         newPassword_s = newPassword.getText().toString().trim();
         confirmPassword_s = confirmPassword.getText().toString().trim();
         SharedPreferences read = getSharedPreferences("user", MODE_PRIVATE);
         String password = read.getString("password", "");
         Log.e("1", password);
         Log.e("1", presentPassword_s);
         if (TextUtils.equals(presentPassword_s, newPassword_s)) {
             Toast.makeText(ChangePasswordActivity.this, "新旧密码不能相同", Toast.LENGTH_LONG).show();
         } else if (TextUtils.equals(newPassword_s, confirmPassword_s) == false) {
             Toast.makeText(ChangePasswordActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
         } else if (!TextUtils.equals(presentPassword_s, password)){
             Toast.makeText(ChangePasswordActivity.this, "旧密码错误，请重新输入", Toast.LENGTH_LONG).show();
         } else {
             return true;
         }
         return false;
     }
}
