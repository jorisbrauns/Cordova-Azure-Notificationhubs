package be.team4talent.notificationhubs;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import org.json.JSONException;
import java.io.IOException;

public class TokenRefreshListenerService extends InstanceIDListenerService implements PluginConstants {
    public static final String LOG_TAG = "NotificationHubs_InstanceIDListenerService";

    @Override
    public void onTokenRefresh() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(APP_DOMAIN, Context.MODE_PRIVATE);
        String senderID = sharedPref.getString(SENDERID, "");
        if (!"".equals(senderID)) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
        }
    }
}
