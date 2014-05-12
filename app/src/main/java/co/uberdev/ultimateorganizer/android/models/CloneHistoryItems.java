package co.uberdev.ultimateorganizer.android.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by oguzbilgener on 12/05/14.
 */
public class CloneHistoryItems extends ArrayList<CloneHistoryItem> implements CoreSelectable
{
	private transient SQLiteDatabase db;

	public CloneHistoryItems()
	{
		this(null);
	}

	public CloneHistoryItems(SQLiteDatabase db)
	{
		this.db = db;
	}

	@Override
	public String getTableName()
	{
		return CoreDataRules.tables.cloned_tasks;
	}

	@Override
	public boolean loadFromDb(String sqlCriteria, String[] params, int limit)
	{
		try
		{
			if(db != null)
			{
				String sqlString = "SELECT * FROM "+getTableName();
				if(params.length > 0)
					sqlString +=  " WHERE "+sqlCriteria;

				Cursor loader = db.rawQuery(sqlString,	params);

				if(!loader.moveToFirst())
					return true; // no rows in the db, still valid

				while(!loader.isAfterLast())
				{
					CloneHistoryItem item = new CloneHistoryItem();
					item.setItemId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.cloned_tasks.itemId)));
					item.setOriginalId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.cloned_tasks.originalId)));
					item.setCloneId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.cloned_tasks.cloneId)));
					item.setCloneLocalId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.cloned_tasks.cloneLocalId)));

					Utils.log.w(item.asJsonString());
					this.add(item);

					loader.moveToNext();
				}

				loader.close();
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

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

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}
}
