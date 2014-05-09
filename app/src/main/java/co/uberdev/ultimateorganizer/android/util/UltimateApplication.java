package co.uberdev.ultimateorganizer.android.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 16/03/14.
 */
public class UltimateApplication extends Application
{
	public CoreUser user;

	@Override
	public void onCreate() {
		// A little hack to force to show overflow menu
		// on devices with physical menu button
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			Utils.log.w("cannot force overflow menu");
		}
	}

	public void loginUser(CoreUser authorized)
	{
		this.user = authorized;

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(getString(R.string.PREFS_USER_OBJ), user.asJsonString());
		editor.commit();
	}

	public void logoutUser()
	{
		this.user = null;
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(getString(R.string.PREFS_USER_OBJ), "");
		editor.commit();
	}

	public boolean retrieveUser()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String userStr = prefs.getString(getString(R.string.PREFS_USER_OBJ), "");
		if(!userStr.isEmpty())
		{
			CoreUser user = CoreUser.fromJson(userStr, CoreUser.class);
			if(user != null)
			{
				this.user = user;
				return true;
			}
			return false;
		}
		return false;
	}

	public CoreUser getUser()
	{
		return user;
	}
}
