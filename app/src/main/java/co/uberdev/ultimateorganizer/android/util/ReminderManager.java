package co.uberdev.ultimateorganizer.android.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.models.Task;

/**
 * Created by oguzbilgener on 07/05/14.
 */
public class ReminderManager
{
	public static void remind(Context context, Task task, Reminder reminder)
	{
		long reminderId = task.getNextAlarmId();
		reminder.setAlarmId(reminderId);

		Intent remindIntent = new Intent(context, AlarmReceiver.class);
		remindIntent.putExtra(context.getString(R.string.ALARM_DATA_REMINDER), reminder.asJsonString());

		PendingIntent sender = PendingIntent.getBroadcast(context,
				(int) reminderId, remindIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTargetDate()*1000, sender);
		Utils.log.d("remind "+reminder+" "+new SimpleDateFormat("HH:mm:ss").format(new Date((long)reminder.getTargetDate()*1000)));
	}

	public static void cancel(Context context, Reminder reminder)
	{
		if(reminder.getAlarmId() == 0)
		{
			return;
		}

		Intent cancelIntent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context,
				(int) reminder.getAlarmId(), cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		Utils.log.d("cancel "+reminder+" "+new SimpleDateFormat("HH:mm:ss").format(new Date((long)reminder.getTargetDate()*1000)));
	}

}
