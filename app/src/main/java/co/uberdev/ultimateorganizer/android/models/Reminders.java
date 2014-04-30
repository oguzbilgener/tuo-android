package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreReminders;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by oguzbilgener on 30/04/14.
 */
public class Reminders extends CoreReminders implements CoreSelectable
{
	@Override
	public String getTableName() {
		return CoreDataRules.tables.reminders;
	}

	@Override
	public boolean loadFromDb(String sqlCriteria, String[] params, int limit) {
		return false;
	}

	@Override
	public boolean loadFromDb(int limit) {
		return false;
	}

	@Override
	public boolean loadFromDb() {
		return false;
	}
}
