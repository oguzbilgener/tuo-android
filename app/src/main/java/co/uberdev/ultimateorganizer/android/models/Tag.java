package co.uberdev.ultimateorganizer.android.models;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;
import co.uberdev.ultimateorganizer.core.CoreTag;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Tag extends CoreTag implements CoreStorable
{
	public Tag()
	{
		name = "";
		color = 0;
	}

	public Tag(CoreTag coreTag)
	{
		setName(coreTag.getName());
		setColor(coreTag.getColor());
		setLocalId(coreTag.getLocalId());
		setOwnerId(coreTag.getOwnerId());
		setId(coreTag.getLocalId());
	}



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
