package be.team4talent.notificationhubs;

import android.content.Context;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.microsoft.windowsazure.messaging.NotificationHub;

public class RegistrationIntentService extends IntentService implements PluginConstants {

    private static final String LOG_TAG = "NotificationHubs_RegistrationIntentService";

    public RegistrationIntentService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(APP_DOMAIN, Context.MODE_PRIVATE);

        try {
              synchronized (LOG_TAG) {
               
                // Initially this call goes out to the network to retrieve the token, subsequent calls are local.
                final String senderId = intent.getExtras().getString(SENDERID);
				final String hubName = intent.getExtras().getString(HUBNAME);
				final String endPoint = intent.getExtras().getString(ENDPOINT);
				String registrationId = sharedPreferences.getString(REGISTRATIONID, null);

                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
         
                Log.d(LOG_TAG, "SENDERID: " + senderId);
                Log.d(LOG_TAG, "GCM Registration Token: " + token);
                Log.d(LOG_TAG, "ENDPOINT: " + endPoint);
                Log.d(LOG_TAG, "HUBNAME: " + hubName);
				Log.d(LOG_TAG, "Azure registration id: " + registrationId);

				//azure registration if id 
				if(registrationId == null){
					NotificationHub hub = new NotificationHub(hubName, endPoint, getApplicationContext());
					registrationId = hub.register(token).getRegistrationId();
				} 

				NotificationHubs.notificationHubs.sendEvent(registrationId);
				
                final SharedPreferences.Editor edit = sharedPreferences.edit();
				edit.putString(REGISTRATIONID, registrationId)
					.putString(TOKEN, token);
				edit.commit();
            }

        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to complete token refresh", e);
        }

    }

}