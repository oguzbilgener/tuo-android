package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

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
}
