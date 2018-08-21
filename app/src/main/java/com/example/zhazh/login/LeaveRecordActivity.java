package com.example.zhazh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nju.software.data.Holiday;
import nju.software.data.LeaveRecordAdapter;
import nju.software.data.RemoveHoliday;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LeaveRecordActivity extends AppCompatActivity implements RemoveHoliday{
    TextView textViewBack;
    static String id;
    List<Holiday> list;
    LeaveRecordAdapter adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init(){
        setContentView(R.layout.activity_leave_record);
        textViewBack = (TextView)findViewById(R.id.tv_back);
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaveRecordActivity.this, HomePageActivity.class);
                intent.putExtra("id",R.id.navigation_message);
                startActivity(intent);
                //LeaveRecordActivity.this.finish();
            }
        });
        SharedPreferences read = getSharedPreferences("user", MODE_PRIVATE);
        id = read.getString("id", "");
        getRecord();
    }
    public void getRecord(){
        String param = "RequestType=Holiday&id=" + id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("holiday_error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                list = JSONUtil.parseHolidayJSON(s);
                showRecord();
            }
        });
    }

    public void showRecord(){
        //final List<Holiday> list = list1;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView tableListView1 = findViewById(R.id.list);
                LeaveRecordAdapter adapter1 = new LeaveRecordAdapter(LeaveRecordActivity.this,list);
                tableListView1.setAdapter(adapter1);
            }
        });
    }

    @Override
    public void dataChanged() {
        Log.d("remove", "dataChanged: ");
        init();
    }
}
