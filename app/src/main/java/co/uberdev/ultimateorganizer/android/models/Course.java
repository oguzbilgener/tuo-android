package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Course extends CoreCourse implements CoreStorable
{
	@Override
	public String getTableName() {
		return CoreDataRules.tables.courses;
	}

	@Override
	public boolean insert() {
		return false;
	}

	@Override
	public boolean update() {
		return false;
	}

	@Override
	public boolean remove() {
		return false;
	}

}
