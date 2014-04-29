package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreCourses;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Courses extends CoreCourses implements CoreSelectable
{

	@Override
	public String getTableName() {
		return CoreDataRules.tables.courses;
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
