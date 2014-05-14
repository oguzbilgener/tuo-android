package co.uberdev.ultimateorganizer.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.core.CoreReminder;

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
			Utils.log.d(text);
			CoreReminder reminder = CoreReminder.fromJson(text, CoreReminder.class);

			Notification notification = new Notification.Builder(context)
					.setContentTitle(reminder.getTitle())
					.setContentText(reminder.getDetails())
					.setVibrate(new long[] {400, 400})
					.setSmallIcon(R.drawable.ic_action_accept).build();
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.ledARGB = 0xff00ff00;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;
			if(reminder.isVibrate())
				notification.flags |= Notification.DEFAULT_VIBRATE;
			if(reminder.isSound())
				notification.flags |= Notification.DEFAULT_SOUND;

			// Send the notification.
			notificationManager.notify(0, notification);
		}

		// stupid notification for testing purposes
		/*Notification notification = new Notification(R.drawable.ic_action_accept,
				text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, HomeActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, "~", text,
				contentIntent);*/


	}
}
