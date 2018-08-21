package nju.software.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.zhazh.login.FragmentHome1;
import com.example.zhazh.login.HomePageActivity;
import com.example.zhazh.login.LoginActivity;
import com.example.zhazh.login.R;

import nju.software.data.Case;
import nju.software.data.Message;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CaseMessage extends Service {

    private Context context = this;
    private Handler delay = new Handler();
    private int period = 10000;
    private String id;
    private String name;
    int notify_id = 1;
    private Runnable getNewCase = new Runnable() {
        @Override
        public void run() {
            String url = "RequestType=NewCase&id=" + id;
            HttpUtil.sendRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error", "error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    List<Case> caseList = JSONUtil.parseNotifyJSON(s), soonCaseList = new ArrayList<>();
                    int i = 0;

                    for (Case cases : caseList) {
                        if (cases.getIndex().equals("over")) {
                            i = caseList.indexOf(cases);
                            if (i != caseList.size())
                                soonCaseList = caseList.subList(i + 1, caseList.size());
                            break;
                        } else {
                            sendNewNotify(name, cases);
                        }
                    }
                    for (Case cases : soonCaseList) {
                        sendSoonNotify(name, cases);
                    }
                }
            });
            //onDestroy();
            delay.postDelayed(getNewCase, period);
        }
    };

    public CaseMessage() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        delay.postDelayed(getNewCase, period);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendNewNotify(String name, Case cases) {
        String show = name + "陪审员，你被选取为" + cases.getIndex() + "案件的陪审员，" +
                "请你于" + cases.getTime() + "到" + cases.getAddress() + "，谢谢。";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, HomePageActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = null;
            String channelName = "nju_software_juror_channel";//渠道名字
            String channelId = "nju_software_juror_1"; // 渠道ID
            String description = "ChannelForJuror"; // 渠道解释说明
            mChannel = new NotificationChannel(channelId, channelName, importance);
            //mChannel.setDescription(description);
            //mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle("您有新的开庭信息，请及时查看")
                    .setContentText(show)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("您有新的开庭信息，请及时查看")
                    .setContentText(show)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        }
        Message message = new Message();
        message.setIndex(cases.getIndex());
        message.setMessage(show);
        message.save();
    }

    private void sendSoonNotify(String name, Case cases) {
        String show = name + "陪审员，" + cases.getIndex() + "案件即将开庭" +
                "请你于" + cases.getTime() + "到" + cases.getAddress() + "，谢谢。";
        Intent intent = new Intent(context, HomePageActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = null;
            String channelName = "nju_software_juror_channel";//渠道名字
            String channelId = "nju_software_juror_1"; // 渠道ID
            String description = "ChannelForJuror"; // 渠道解释说明
            mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            //mChannel.setDescription(description);
            //mChannel.enableLights(true); //是否在桌面icon右上角展示小红点

            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle("您有案件即将开庭，请及时查看")
                    .setContentText(show)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("您有案件即将开庭，请及时查看")
                    .setContentText(show)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        }
        Message message = new Message();
        message.setIndex(cases.getIndex());
        message.setMessage(show);
        message.save();
    }
}
