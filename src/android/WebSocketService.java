package com.amankumar.cordova.BackgroundServiceAndroid;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.logging.HttpLoggingInterceptor;

import io.ionic.starter.MainActivity;

/**
 * Created by chetanya on 13/9/17.
 */

public class WebSocketService extends Service {

    private String TAG = "WebSocketService";
    private OkHttpClient okHttpClient;

    private static WebSocket webSocket;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    // private Bus mBus = new Bus();


    @Override
    public void onCreate() {
        super.onCreate();
        //mBus.register(this);
        Log.d(TAG, "onCreate: " + "Executed ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: " + "Executed");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("FamilyCare Service")
                    .setSmallIcon(0)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
            //do heavy work on a background thread
            //stopSelf();
        }
        startSocket();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: " + "Executed");
        return null;
    }

    private void startSocket() {

        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // The singleton HTTP client.
        okHttpClient = new OkHttpClient.Builder()
                .pingInterval(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .build();
        Log.d(TAG, "start: called");
        Request request = new Request.Builder().url(Urls.URL_WEBSOCKET_INITIATE).build();
        EchoWebSocketListener listener = new EchoWebSocketListener(this);
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }


    @Subscribe
    public void BusCallBack(EventModels eventModels) {
        if (eventModels.getEventtype().equals(Urls.EVENT_SOCKET_FAILURE)) {
            webSocket = null;
            okHttpClient = null;
            startSocket();
        } else if (eventModels.getEventtype().equals(Urls.EVENT_SEND_MESSAGE)) {
            if (webSocket != null) {
                webSocket.send(eventModels.getMessage());
            }
        }
    }

    public static void sendMessageIntoSocket(String msg) {
        if (msg == null) {
            return;
        } else {
            if (webSocket != null)
                webSocket.send(msg);
        }
    }


    /*@Override
    public void onMessage(String message) {

        Log.d(TAG, "onMessage: called from socket listener");


    }

    @Override
    public void onConnect() {

        Log.d(TAG, "onConnect: called from socket listener");

    }

    @Override
    public void onDisconnect(int code, String reason) {

        Log.d(TAG, "onDisconnect: called from socket listener");

    }

    @Override
    public void onError(String cause) {

        Log.d(TAG, "onError: called from socket listener");

    }*/
}
