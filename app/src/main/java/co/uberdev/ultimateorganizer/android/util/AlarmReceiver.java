package co.uberdev.ultimateorganizer.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.ui.HomeActivity;

/**
 * Created by oguzbilgener on 07/05/14.
 */
public class AlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Utils.log.d("alarm received ");
		NotificationManager notificationManager =
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		String text = "?";
		Bundle extras = intent.getExtras();
		if(extras != null && extras.containsKey(context.getString(R.string.ALARM_DATA_REMINDER)))
		{
			text = extras.getString(context.getString(R.string.ALARM_DATA_REMINDER));
		}
		// stupid notification for testing purposes
		Notification notification = new Notification(R.drawable.ic_action_accept,
				text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, HomeActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, "~", text,
				contentIntent);

		// Send the notification.
		notificationManager.notify("~", 0, notification);
	}
}
