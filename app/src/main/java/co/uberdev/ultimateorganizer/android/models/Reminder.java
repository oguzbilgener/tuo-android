package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreReminder;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by oguzbilgener on 30/04/14.
 */
public class Reminder extends CoreReminder implements CoreStorable
{
	private transient SQLiteDatabase db;

	public Reminder(SQLiteDatabase db)
	{
		this.db = db;
	}

	public Reminder()
	{
		this(null);
	}

	@Override
	public String getTableName() {
		return CoreDataRules.tables.reminders;
	}

	@Override
	public boolean insert()
	{
		if(db != null)
		{
			String insertSql = "INSERT INTO "+getTableName()+" (" +
					CoreDataRules.columns.reminders.targetDate+", "+
					CoreDataRules.columns.reminders.taskId+", "+
					CoreDataRules.columns.reminders.ownerId+", "+
					CoreDataRules.columns.reminders.localTaskId+", "+
					CoreDataRules.columns.reminders.gap+", "+
					CoreDataRules.columns.reminders.title+", "+
					CoreDataRules.columns.reminders.details+", "+
					CoreDataRules.columns.reminders.vibrate+", "+
					CoreDataRules.columns.reminders.sound+", "+
					CoreDataRules.columns.reminders.light+" "+
					") VALUES (?,?,?,?,?,?,?,?)";

			int n = 1;
			SQLiteStatement ss = db.compileStatement(insertSql);
			ss.bindLong(n++, getTargetDate());
			ss.bindLong(n++, getOwnerId());
			ss.bindLong(n++, getLocalTaskId());
			ss.bindLong(n++, getTaskId());
			ss.bindLong(n++, getGap());
			ss.bindString(n++, getTitle());
			ss.bindString(n++, getDetails());
			ss.bindLong(n++, isVibrate() ? 1 : 0);
			ss.bindLong(n++, isSound() ? 1 : 0);
			ss.bindLong(n++, isLight() ? 1 : 0);

			ss.execute();
			ss.close();

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
