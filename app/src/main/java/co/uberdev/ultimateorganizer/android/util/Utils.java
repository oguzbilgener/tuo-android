package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.graphics.Color;
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

	// yep, fake
	public static int colorForTagStr(String tag)
	{
		if(tag.equals("tag"))
		{
			return Color.parseColor("#000000");
		}
		else if(tag.equals("lol"))
		{
			return Color.parseColor("#b2b2b2");
		}
		else if(tag.equals("CS 102"))
		{
			return Color.parseColor("#008CBA");
		}
		else if(tag.equals("ENG 102"))
		{
			return Color.parseColor("#BA7300");
		}
		else if(tag.equals("MATH 102"))
		{
			return Color.parseColor("#0019BA");
		}
		else if(tag.equals("MATH 132"))
		{
			return Color.parseColor("#7000BA");
		}
		else if(tag.equals("PHYS 102"))
		{
			return Color.parseColor("#308F00");
		}
		else if(tag.equals("TURK 102"))
		{
			return Color.parseColor("#8F001A");
		}
		else if(tag.equals("Lab"))
		{
			return Color.parseColor("#777777");
		}
		else if(tag.equals("presentation"))
		{
			return Color.parseColor("#008F7C");
		}
		else if(tag.equals("date"))
		{
			return Color.parseColor("#8F005D");
		}
		else if(tag.equals("programming"))
		{
			return Color.parseColor("#8F8500");
		}

		return 255;
	}

}
