package cordova.plugins.BackgroundServiceAndroid;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Service;
import android.app.PendingIntent;
import android.app.Notification;

import android.widget.Toast;

import android.content.Intent;
import android.util.Log;

import android.os.IBinder;
import android.os.Build;

import android.support.v4.app.NotificationCompat;

import com.familycare.MainActivity;

import java.lang.Exception;
/**
 * This class keeps app running in the background.
 */
 
public class MyService extends Service {

    private static final String TAG = "BackgroundService-MyService";
    private static final String CHANNEL_ID = "7";
	public static String JS_callBack = "BackgroundServiceAndroid.callbackResult";
    
	public static CordovaWebView gWebView;


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Toast.makeText(this, "App got killed", Toast.LENGTH_SHORT).show();
        callJS("LOGOUT");
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    // Implement your logic here.
        // super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        Toast.makeText(this, "App got killed from startcommand", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("App Update")
                    .setContentText("This is a foreground service notification")
                    .setSmallIcon(0)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

            return START_NOT_STICKY;
        } else {
            return START_STICKY;
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }


    public void callJS(String data){
        String callBack = "javascript:" + JS_callBack + "(" + data + ")";
        try {
            Log.d(TAG, "sending data to JS" + data);
            gWebView.sendJavascript(callBack);
        } catch(Exception e) {
            Log.d(TAG, "some error in sending data to JS, error => ");
            e.printStackTrace();
        }
    }
}