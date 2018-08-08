package com.amankumar.cordova.BackgroundServiceAndroid;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaWebView;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */

public class BackgroundServiceAndroid extends CordovaPlugin {

    private static final String TAG = "BackgroundService";
    public static CordovaWebView gWebView;
    public static String JS_callBack = "BackgroundServiceAndroid.callbackResult";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "in initialize");
        // your init code here
        gWebView = webView;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("sendDataToWebSocket")) {
            String data = args.getString(0);
            this.sendDataToWebSocket(data, callbackContext);
            return true;
        }
        return false;
    }

    private void sendDataToWebSocket(String data, CallbackContext callbackContext) {
        Log.d(TAG, "in sendDataToWebSocket method" + data);

        if (data != null) {
            //
            try {
                WebSocketService.sendMessageIntoSocket(data);
                callbackContext.success("Data sent successfully Successfully!!!");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                callbackContext.error("Some error in sending data to websocket");
            }

        } else {
            callbackContext.error("Null data provided in sendDataToWebSocket's method");
        }
    }

    public static void callJS(String data) {
        String callBack = "javascript:" + JS_callBack + "(" + data + ")";
        try {
            Log.d(TAG, "sending data to JS" + data);
            gWebView.sendJavascript(callBack);
        } catch (Exception e) {
            Log.d(TAG, "some error in sending data to JS, error => ");
            e.printStackTrace();
        }
    }
}
