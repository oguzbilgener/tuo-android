package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;
import co.uberdev.ultimateorganizer.core.CoreTag;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Tag extends CoreTag implements CoreStorable
{
	@Override
	public String getTableName() {
		return CoreDataRules.tables.tags;
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
