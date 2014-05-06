package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;

import java.util.Calendar;

/**
 * Created by oguzbilgener on 15/03/14.
 */
public class Utils
{
	public static final class log
	{
		public static final String TAG = "ultimateapp";

		public static void d(String msg)
		{
			Log.d(TAG, msg);
		}

		public static void e(String msg)
		{
			Log.e(TAG, msg);
		}

		public static void i(String msg)
		{
			Log.i(TAG, msg);
		}

		public static void w(String msg)
		{
			Log.w(TAG, msg);
		}
	}

	public static float getPixelsByDp(Context context,float dp)
	{
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static int getUnixTimestamp()
	{
		return (int)(System.currentTimeMillis()/1000L);
	}

	public static int mixTwoColors( int color1, int color2, float amount )
	{
		final byte ALPHA_CHANNEL = 24;
		final byte RED_CHANNEL   = 16;
		final byte GREEN_CHANNEL =  8;
		final byte BLUE_CHANNEL  =  0;

		final float inverseAmount = 1.0f - amount;

		int a = ((int)(((float)(color1 >> ALPHA_CHANNEL & 0xff )*amount) +
				((float)(color2 >> ALPHA_CHANNEL & 0xff )*inverseAmount))) & 0xff;
		int r = ((int)(((float)(color1 >> RED_CHANNEL & 0xff )*amount) +
				((float)(color2 >> RED_CHANNEL & 0xff )*inverseAmount))) & 0xff;
		int g = ((int)(((float)(color1 >> GREEN_CHANNEL & 0xff )*amount) +
				((float)(color2 >> GREEN_CHANNEL & 0xff )*inverseAmount))) & 0xff;
		int b = ((int)(((float)(color1 & 0xff )*amount) +
				((float)(color2 & 0xff )*inverseAmount))) & 0xff;

		return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
	}

	public static Fragment findFragmentByPosition(int position, FragmentManager fragmentManager,
			FragmentPagerAdapter fragmentPagerAdapter, ViewPager viewPager) {
		return fragmentManager.findFragmentByTag(
				"android:switcher:" + viewPager.getId() + ":"
						+ fragmentPagerAdapter.getItemId(position));
	}

	public static String toMonthString(int i) {
		String[] months = {"January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December"};

		return months[i];
	}

	public static boolean isDateToday(int timestamp)
	{
		Calendar todayBeginCalendar = Calendar.getInstance();
		todayBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayBeginCalendar.set(Calendar.MINUTE, 0);
		todayBeginCalendar.set(Calendar.SECOND, 0);

		Calendar tomorrowBeginCalendar = (Calendar) todayBeginCalendar.clone();
		tomorrowBeginCalendar.add(Calendar.DATE, 1);

		int todayBeginTimestamp = (int)(todayBeginCalendar.getTimeInMillis()/1000);
		int tomorrowBeginTimestamp = (int)(tomorrowBeginCalendar.getTimeInMillis()/1000);

		if(timestamp >= todayBeginTimestamp && timestamp < tomorrowBeginTimestamp)
		{
			return true;
		}
		return false;
	}

	public static boolean isDateYesterday(int timestamp)
	{
		Calendar todayBeginCalendar = Calendar.getInstance();
		todayBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayBeginCalendar.set(Calendar.MINUTE, 0);
		todayBeginCalendar.set(Calendar.SECOND, 0);

		Calendar yesterdayBeginCalendar = (Calendar) todayBeginCalendar.clone();
		yesterdayBeginCalendar. add(Calendar.DATE, -1);

		int todayBeginTimestamp = (int)(todayBeginCalendar.getTimeInMillis()/1000);
		int yesterdayBeginTimestamp = (int)(yesterdayBeginCalendar.getTimeInMillis()/1000);

		if(timestamp >= yesterdayBeginTimestamp && timestamp < todayBeginTimestamp)
		{
			return true;
		}
		return false;
	}

	public static boolean isDateTomorrow(int timestamp)
	{
		Calendar tomorrowBeginCalendar = Calendar.getInstance();
		tomorrowBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tomorrowBeginCalendar.set(Calendar.MINUTE, 0);
		tomorrowBeginCalendar.set(Calendar.SECOND, 0);
		tomorrowBeginCalendar.add(Calendar.DATE, 1);

		Calendar otherdayBeginCalendar = (Calendar) tomorrowBeginCalendar.clone();
		otherdayBeginCalendar.add(Calendar.DATE, 1);

		int tomorrowBeginTimestamp = (int)(tomorrowBeginCalendar.getTimeInMillis()/1000);
		int otherdayBeginTimestamp = (int)(otherdayBeginCalendar.getTimeInMillis()/1000);

		if(timestamp >= tomorrowBeginTimestamp && timestamp < otherdayBeginTimestamp)
		{
			return true;
		}
		return false;
	}

}
