package com.amankumar.cordova.BackgroundServiceAndroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.content.pm.PackageManager;

import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by innotical on 26/8/17.
 */

public class EchoWebSocketListener extends WebSocketListener {

    private String TAG = "WebSocket";
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private String textMsg;
    private Handler mHandler;
    private Context mContext;

    public EchoWebSocketListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "onOpen: " + response.toString());
    }

    @Override
    public void onMessage(WebSocket webSocket, final String text) {

        Log.d(TAG, "onMessage: " + text);

        mHandler = new Handler(Looper.getMainLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject obj = new JSONObject(text);
                    Log.d(TAG, obj.toString());

                    String value = obj.optString("id");
                    Log.d(TAG, value);

                    if (value.equals("incomingCall")) {
                        forceMainActivityReload();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BackgroundServiceAndroid.callJS(text);
                            }
                        }, 2000);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "onMessage: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Log.e(TAG, "onClosing: " + reason + "  code" + code);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAG, "onFailure: " + t.getMessage());
    }

    private void forceMainActivityReload() {
        PackageManager pm = mContext.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(mContext.getApplicationContext().getPackageName());
        mContext.startActivity(launchIntent);
    }
}
