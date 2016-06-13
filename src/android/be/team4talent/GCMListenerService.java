package be.team4talent.notificationhubs;

import android.util.Log;
import android.annotation.SuppressLint;
import com.google.android.gms.gcm.GcmListenerService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.content.res.Resources;
import java.util.Random;


@SuppressLint("NewApi")
public class GCMListenerService extends GcmListenerService implements PluginConstants {

	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private static final String LOG_TAG = "NotificationHubs_GCMIntentService";

    @Override
    public void onMessageReceived(String from, Bundle extras) {
        Log.d(LOG_TAG, "onMessage - from: " + from);

		createNotification(getApplicationContext(), extras);

        String nhMessage = extras.getString("message");
        if (NotificationHubs.notificationHubs.isVisible()) {
            NotificationHubs.notificationHubs.ToastNotify(nhMessage);
        }

    }

	private void createNotification(Context context, Bundle extras) {
		String appName = getAppName(this);
		String packageName = context.getPackageName();
		Resources resources = context.getResources();
		int notId = parseInt(NOT_ID, extras);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(extras.getString(TITLE))
						.setStyle(new NotificationCompat.BigTextStyle().bigText(extras.getString(MESSAGE)))
                        .setSound(defaultSoundUri)
						.setSmallIcon(context.getApplicationInfo().icon)
                        .setContentText(extras.getString(MESSAGE));

		Intent notificationIntent = new Intent(context, NotificationActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationIntent.putExtra(BUNDLE, extras);
		notificationIntent.putExtra(NOT_ID, notId);

		int requestCode = new Random().nextInt();
		PendingIntent contentIntent = PendingIntent.getActivity(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	public static String getAppName(Context context) {
        CharSequence appName =  context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
        return (String)appName;
    }

	private int parseInt(String value, Bundle extras) {
		int retval = 0;

		try {
			retval = Integer.parseInt(extras.getString(value));
		}
		catch(NumberFormatException e) {
			Log.e(LOG_TAG, "Number format exception - Error parsing " + value + ": " + e.getMessage());
		}
		catch(Exception e) {
			Log.e(LOG_TAG, "Number format exception - Error parsing " + value + ": " + e.getMessage());
		}

		return retval;
	}
}
