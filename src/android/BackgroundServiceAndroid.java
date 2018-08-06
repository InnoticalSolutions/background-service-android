package cordova.plugins.BackgroundServiceAndroid;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaWebView;
import cordova.plugins.BackgroundServiceAndroid.MyService;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */

public class BackgroundServiceAndroid extends CordovaPlugin {

    private static final String TAG = "BackgroundService";
	public static CordovaWebView gWebView;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "in initialize");
        // your init code here
		gWebView = webView;
        MyService.gWebView = webView;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        Log.d(TAG, "in echo method");

        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
