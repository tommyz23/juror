package nju.software.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.zhazh.login.HomePageActivity;
import com.example.zhazh.login.R;

import java.io.IOException;
import java.util.List;

import nju.software.data.Message;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DocService extends Service {
    private Context context = this;
    private Handler delay = new Handler();
    private int period = 10000;
    private String id;
    private String name;
    private int notify_id = 2;
    private Runnable getNewDoc = new Runnable() {
        @Override
        public void run() {
            getNotify();
            delay.postDelayed(getNewDoc, period);
        }
    };
    public DocService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        delay.postDelayed(getNewDoc, period);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getNotify(){
        String param = "RequestType=GetNewDoc&id=" + id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    Log.e("error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                List<String> list = JSONUtil.parseNotifyDocJSON(r);
                for(String s: list){
                    Log.d("doc", s);
                    sendNotify(s);
                }
            }
        });
    }

    private void sendNotify(String index){
        String show = name + "陪审员，" + index + "一案审理结束，需要你在裁判文书上签字，请签字";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, HomePageActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            String channelName = "nju_software_juror_channel";//渠道名字
            String channelId = "nju_software_juror_1"; // 渠道ID
            String description = "ChannelForJuror"; // 渠道解释说明
            mChannel = new NotificationChannel(channelId, channelName, importance);
            //mChannel.setDescription(description);
            //mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle("您有新的文书需要签字，请及时查看")
                    .setContentText(show)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("您有新的文书需要签字，请及时查看")
                    .setContentText(show)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            notificationManager.notify(notify_id, notification);
            notify_id += 2;
        }
        Message message = new Message();
        message.setIndex(index);
        message.setMessage(show);
        message.save();
    }
}
