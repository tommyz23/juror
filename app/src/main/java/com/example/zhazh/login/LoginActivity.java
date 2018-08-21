package com.example.zhazh.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import nju.software.data.User;
import nju.software.service.CaseMessage;
import nju.software.service.DocService;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText code;
    private CheckBox remenber;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText)findViewById(R.id.user_name);
        code = (EditText)findViewById(R.id.code);
        remenber = (CheckBox)findViewById(R.id.remenber);
        Button button = (Button)findViewById(R.id.log_in);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString().trim();
                String password = code.getText().toString().trim();
                String param = "RequestType=Login&id=" + name + "&password=" + password;
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"手机号或密码不能为空",Toast.LENGTH_LONG).show();
                }
                else{
                    HttpUtil.sendRequest(param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("error",e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseString = response.body().string();
                            Log.d("response", responseString);
                            user = JSONUtil.parseUserJSON(responseString);
                            String result = JSONUtil.parseBoolJSON(responseString);
                            if(result.equals("true")){
                                saveUser();
                                startServices();
                                startActivity();
                            }else {
                                Toast.makeText(LoginActivity.this,"手机号或密码错误，请重新登录",Toast.LENGTH_LONG).show();
                                userName.setText("");
                                code.setText("");
                            }
                        }
                    });
                }
            }
        });
    }

    public void saveUser(){
        SharedPreferences.Editor editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        editor.putString("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("phoneNumber", user.getPhoneNumber());
        editor.putString("password", user.getPassword());
        editor.apply();
    }

    public void startServices(){
        Intent serviceIntent1 = new Intent(LoginActivity.this, CaseMessage.class);
        serviceIntent1.putExtra("id", user.getId());
        serviceIntent1.putExtra("name", user.getName());
        startService(serviceIntent1);
        Intent serviceIntent = new Intent(LoginActivity.this, DocService.class);
        serviceIntent.putExtra("id", user.getId());
        serviceIntent.putExtra("name", user.getName());
        startService(serviceIntent);
    }

    public void startActivity(){
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
