package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;
import co.uberdev.ultimateorganizer.core.CoreTask;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Task extends CoreTask implements CoreStorable
{
	private SQLiteDatabase db;
	public Task(SQLiteDatabase db)
	{
		super();
		this.db = db;
	}

    public Task()
    {
        this( null);
    }


	/**
	 * loads the
	 */
	public void loadRemindersFromDb()
	{
		Reminders reminders = new Reminders();
		// TODO: add criteria
		reminders.loadFromDb();
		// pass the reference
		this.reminders = reminders;
	}

	@Override
	public String getTableName() {
		return CoreDataRules.tables.tasks;
	}

	@Override
	public boolean insert()
	{
		if(db != null)
		{
			String insertSql = "INSERT INTO "+getTableName()+" (" +
					CoreDataRules.columns.tasks.ownerId+", " +
					CoreDataRules.columns.tasks.taskName+", " +
					CoreDataRules.columns.tasks.taskDesc+", " +
					CoreDataRules.columns.tasks.status+", " +
					CoreDataRules.columns.tasks.tags+", " +
					CoreDataRules.columns.tasks.dateCreated+", " +
					CoreDataRules.columns.tasks.lastModified+", " +
					CoreDataRules.columns.tasks.beginDate+", " +
					CoreDataRules.columns.tasks.endDate+" " +
					") VALUES (?,?,?,?,?,?,?,?,?)";

			int n = 1;
			SQLiteStatement ss = db.compileStatement(insertSql);
			ss.bindLong(n++, getOwnerId());
			ss.bindString(n++, getTaskName());
			ss.bindString(n++, getTaskDesc());
			ss.bindLong(n++, getStatus());
			ss.bindString(n++, getTags().asJsonString()); // as json
			ss.bindLong(n++, getDateCreated());
			ss.bindLong(n++, getLastModified());
			ss.bindLong(n++, getBeginDate());
			ss.bindLong(n++, getEndDate());

			ss.execute();
			ss.close();

			// now insert should be complete
			// time to insert reminders one by one
			for(int i=0; i<reminders.size(); i++)
			{
				((Reminder)reminders.get(i)).insert();
			}

			return true;
		}
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
