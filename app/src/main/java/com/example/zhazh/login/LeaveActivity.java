package com.example.zhazh.login;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import nju.software.data.Holiday;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LeaveActivity extends Activity {
    int mYear, mMonth, mDay, nYear, nMonth, nDay;
    Date startTime, endTime, intervalStart, intervalEnd;
    Button button1;
    TextView textView1;
    Button button2;
    TextView textView2;
    int dateDialog;
    Button submitLeave;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        button1 = (Button) findViewById(R.id.bt_begin_date);
        textView1 = (TextView) findViewById(R.id.tv_begin_date);
        button2 = (Button) findViewById(R.id.bt_end_date);
        textView2 = (TextView) findViewById(R.id.tv_end_date);
        submitLeave = (Button) findViewById(R.id.bt_leave);
        editText = (EditText) findViewById(R.id.et_reason);

        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dateDialog = 1;
                showDialog(dateDialog);
            }
        });

        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dateDialog = 2;
                showDialog(dateDialog);
            }
        });

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        nYear = ca.get(Calendar.YEAR);
        nMonth = ca.get(Calendar.MONTH);
        nDay = ca.get(Calendar.DAY_OF_MONTH);
        submitLeave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mMonth = mMonth + 1;
                nMonth = nMonth + 1;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String s1 = mYear + "-" + mMonth + "-" + mDay;
                String s2 = nYear + "-" + nMonth + "-" + nDay;
                String reason = editText.getText().toString().trim();
                Calendar firstDayOfNextWeek = getNextWeekFirstDay();
                Calendar lastDayOfNextWeek = getNextWeekLastDay();
                String first = new SimpleDateFormat("yyyy-MM-dd").format(firstDayOfNextWeek.getTime());
                String last = new SimpleDateFormat("yyyy-MM-dd").format(lastDayOfNextWeek.getTime());
                try {
                    startTime = sdf.parse(s1);
                    endTime = sdf.parse(s2);
                    intervalStart = sdf.parse(first);
                    intervalEnd = sdf.parse(last);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (suitTime()) {
                    Holiday holiday = new Holiday(s1, s2, reason);
                    leave(holiday);
                } else {
                    Toast.makeText(LeaveActivity.this, "请假区间不符合规定，请重新选择", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
            case 2:
                return new DatePickerDialog(this, ndateListener, nYear, nMonth, nDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */

    public void display1() {
        textView1.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display1();
        }
    };

    public void display2() {
        textView2.setText(new StringBuffer().append(nYear).append("-").append(nMonth + 1).append("-").append(nDay));
    }

    private DatePickerDialog.OnDateSetListener ndateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            nYear = year;
            nMonth = monthOfYear;
            nDay = dayOfMonth;
            display2();
        }
    };

    public static Calendar getNextWeekFirstDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println(cal.getTime());
        return cal;
    }

    public static Calendar getNextWeekLastDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 2);
        else
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        System.out.println(cal.getTime());
        return cal;
    }

    public void leave(Holiday holiday) {
        SharedPreferences read = getSharedPreferences("user", MODE_PRIVATE);
        String id = read.getString("id", "");
        String param = "RequestType=Off&id=" + id +
                "&start=" + holiday.getStartTime() + "&end=" + holiday.getEndTime() + "&reason=" + holiday.getReason();
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LeaveActivity.this, "请假失败", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = JSONUtil.parseBoolJSON(response.body().string());
                if (result.equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LeaveActivity.this, LeaveRecordActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (result.equals("exist")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LeaveActivity.this, "请假区间与已有区间重合，请重新选择请假时间", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LeaveActivity.this, "连接失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public boolean suitTime(){
        boolean startBeforeIEnd = startTime.before(intervalEnd);
        boolean endAfterIStart = endTime.after(intervalStart);
        boolean startBeforeEnd = startTime.before(endTime)||startTime.equals(endTime);
        return startBeforeEnd && startBeforeIEnd && endAfterIStart;
    }
}