package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreNote;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Note extends CoreNote implements CoreStorable
{

	@Override
	public String getTableName() {
		return CoreDataRules.tables.notes;
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
