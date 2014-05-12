package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import co.uberdev.ultimateorganizer.core.Core;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by oguzbilgener on 12/05/14.
 */
public class CloneHistoryItem extends Core implements CoreStorable
{
	private transient SQLiteDatabase db;

	private long itemId;
	private long originalId;
	private long cloneId;
	private long cloneLocalId;

	public CloneHistoryItem()
	{
		this(null);
	}

	public CloneHistoryItem(SQLiteDatabase db)
	{
		this.db = db;
		itemId = 0;
		cloneId = 0;
		cloneLocalId = 0;
		originalId = 0;
	}

	public long getItemId()
	{
		return itemId;
	}

	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}

	public long getOriginalId()
	{
		return originalId;
	}

	public void setOriginalId(long originalId)
	{
		this.originalId = originalId;
	}

	public long getCloneId()
	{
		return cloneId;
	}

	public void setCloneId(long cloneId)
	{
		this.cloneId = cloneId;
	}

	public long getCloneLocalId()
	{
		return cloneLocalId;
	}

	public void setCloneLocalId(long cloneLocalId)
	{
		this.cloneLocalId = cloneLocalId;
	}

	public SQLiteDatabase getDb()
	{
		return db;
	}

	public void setDb(SQLiteDatabase db)
	{
		this.db = db;
	}

	@Override
	public String getTableName()
	{
		return CoreDataRules.tables.cloned_tasks;
	}

	@Override
	public boolean insert()
	{
		if(db != null)
		{
			try {
				String insertSql = "INSERT INTO " + getTableName() + " (" +
						CoreDataRules.columns.cloned_tasks.originalId + ", " +
						CoreDataRules.columns.cloned_tasks.cloneId + ", " +
						CoreDataRules.columns.cloned_tasks.cloneLocalId +" "+
						") VALUES (?,?,?)";

				int n = 1;
				SQLiteStatement ss = db.compileStatement(insertSql);
				ss.bindLong(n++, getOriginalId());
				ss.bindLong(n++, getCloneId());
				ss.bindLong(n++, getCloneLocalId());

				long itemId = ss.executeInsert();
				ss.close();

				setItemId(itemId);


				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean update()
	{
		return false;
	}

	/**
	 * Warning: deletes by clone local id
	 * @return
	 */
	@Override
	public boolean remove()
	{
		if(db != null)
		{
			String removeSql = "DELETE FROM "+getTableName()+" WHERE "+
					CoreDataRules.columns.cloned_tasks.cloneLocalId+" = ?";
			SQLiteStatement ss = db.compileStatement(removeSql);
			ss.bindLong(1, getCloneLocalId());

			ss.execute();
			ss.close();



			return true;
		}
		return false;
	}
}
