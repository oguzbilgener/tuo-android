package co.uberdev.ultimateorganizer.android.util;

import android.app.Application;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by oguzbilgener on 16/03/14.
 */
public class UltimateApplication extends Application {
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
}
