package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreNotes;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Notes extends CoreNotes implements CoreSelectable
{
	@Override
	public String getTableName() {
		return CoreDataRules.tables.notes;
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
