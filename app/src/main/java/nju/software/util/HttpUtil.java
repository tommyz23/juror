package nju.software.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 连接服务器
 * Created by Administrator on 2018/7/30.
 */

public class HttpUtil {
    public static void sendRequest(String param, okhttp3.Callback callback){
        String url = "http://192.168.43.184:8080/juror/DispatcherServlet?";
        String urlWithParam = url + param;
        Log.d("url",urlWithParam);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(80, TimeUnit.SECONDS)
                .readTimeout(80,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(urlWithParam).build();
        client.newCall(request).enqueue(callback);
    }
}
