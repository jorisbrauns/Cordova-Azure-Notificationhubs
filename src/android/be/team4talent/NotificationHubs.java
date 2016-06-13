package be.team4talent.notificationhubs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.iid.InstanceID;
import android.widget.Toast;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class NotificationHubs extends CordovaPlugin implements PluginConstants {

    public static NotificationHubs notificationHubs;
    public static final String LOG_TAG = "NotificationHubs";
    private String senderId;
    private String hubname;
    private String endpoint;
    private static CallbackContext callback = null;
    private static boolean gForeground = false;
    private static CordovaWebView gWebView;

    /**
     * Constructor.
     */
    public NotificationHubs() {
    }

	@Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        notificationHubs = this;
        gForeground = true;
    }

	@Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        gForeground = false;
    }

	@Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        gForeground = true;
    }
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        gForeground = false;
        gWebView = null;
    }

	/**
     * Executes the request.
     *
     * @param action        	The action to execute.
     * @param args          	JSONArry of arguments for the plugin.
     * @param callbackContext 	The callback context used when calling back into JavaScript.
     * @return              	True if the action was valid, false if not.
     */
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	
        Log.d(LOG_TAG, "Action: " + action);
		Log.d(LOG_TAG, "Data: " + args.toString());

		gWebView = this.webView;
		callback = callbackContext;
		
		try {
			if (action.equals(REGISTER)) {
			
				senderId = args.optJSONObject(0).optString(SENDERID, null);

				if (senderId == null) {
					callbackContext.error("You need to provide a Sender ID, please check: https://developers.google.com/cloud-messaging/android/client?configured=true for more information.");
					return false;
				}
			
				hubname = args.optJSONObject(0).optString(HUBNAME, null);
				if (hubname == null) {
					callbackContext.error("You need to provide a hubname from Azure.");
					return false;
				}
			
				endpoint = args.optJSONObject(0).optString(ENDPOINT, null);
				if (endpoint == null) {
					callbackContext.error("You need to provide a endpoint from Azure.");
					return false;
				}

                final SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(APP_DOMAIN, Context.MODE_PRIVATE).edit();
                editor.putString(SENDERID, senderId)
					  .putString(HUBNAME, hubname)
					  .putString(ENDPOINT, endpoint);
				editor.commit();

				RegisterWithNotificationHubs(callbackContext);
				
				return true;
			} 

			callbackContext.error("Action not Recognized.");
       
			return false;
			
		} catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
            return false;
		} 
    }

	public void RegisterWithNotificationHubs( CallbackContext callbackContext )
    {
        Log.d(LOG_TAG, "Registering with Notification Hubs");
		cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
              	if (CheckPlayServices()) {
					Log.i(LOG_TAG, "CheckPlayServices passed!");
					Intent intent = new Intent(cordova.getActivity(), RegistrationIntentService.class);
					intent.putExtra(SENDERID, senderId);
					intent.putExtra(HUBNAME, hubname);
					intent.putExtra(ENDPOINT, endpoint);
					cordova.getActivity().startService(intent);
				} 
            }
		});
    }
	
   /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
    */
	private boolean CheckPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(cordova.getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GooglePlayServicesUtil.getErrorDialog(resultCode, cordova.getActivity(), 9000).show();
                    }
                });
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                cordova.getActivity().finish();
            }
            return false;
        }
        return true;
    }

	public void ToastNotify(final String notificationMessage)
    {
		cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cordova.getActivity().getApplicationContext(), notificationMessage, android.widget.Toast.LENGTH_LONG ).show();
			}
		});
    }

	public static boolean isVisible() {
      return gForeground;
    }
	
    public static boolean isActive() {
        return gWebView != null;
    }

	public static void sendEvent(String message) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
        pluginResult.setKeepCallback(true);
        if (callback != null) {
            callback.sendPluginResult(pluginResult);
        }
    }

	/**
	* Gets the application context from cordova's main activity.
	* @return the application context
	*/
    private Context getApplicationContext() {
        return this.cordova.getActivity().getApplicationContext();
    }
}
