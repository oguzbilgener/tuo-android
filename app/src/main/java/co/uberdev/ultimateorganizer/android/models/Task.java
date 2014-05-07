package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;
import co.uberdev.ultimateorganizer.core.CoreTags;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreUtils;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Task extends CoreTask implements CoreStorable
{
	private transient SQLiteDatabase db;

	public Task(SQLiteDatabase db)
	{
		super();
		this.db = db;
		this.course = new CoreCourse();
		this.tags = new CoreTags();
		this.relatedNotes = new ArrayList<Long>();
		this.relatedTasks = new ArrayList<Long>();
		this.courseCodeCombined = this.course.getCourseCodeCombined();
		this.courseId = this.course.getId();
		this.taskOwnerNameCombined = "";
	}

    public Task()
    {
        this(null);
    }

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * loads the asd
	 */
	public void loadRemindersFromDb()
	{
		Reminders reminders = new Reminders();
		// TODO: test this
		reminders.loadFromDb(
				CoreDataRules.columns.tasks.ownerId+" = ?",
				new String[]{Long.toString(this.ownerId)},
				0);
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
			try {
				String insertSql = "INSERT INTO " + getTableName() + " (" +
						CoreDataRules.columns.tasks.id + ", " +
						CoreDataRules.columns.tasks.ownerId + ", " +
						CoreDataRules.columns.tasks.taskName + ", " +
						CoreDataRules.columns.tasks.taskDesc + ", " +
						CoreDataRules.columns.tasks.status + ", " +
						CoreDataRules.columns.tasks.tags + ", " +
						CoreDataRules.columns.tasks.dateCreated + ", " +
						CoreDataRules.columns.tasks.lastModified + ", " +
						CoreDataRules.columns.tasks.beginDate + ", " +
						CoreDataRules.columns.tasks.endDate + ", " +
						CoreDataRules.columns.tasks.course + ", " +
						CoreDataRules.columns.tasks.courseCodeCombined + ", " +
						CoreDataRules.columns.tasks.courseId + ", " +
						CoreDataRules.columns.tasks.personal + ", " +
						CoreDataRules.columns.tasks.relatedNotes + ", " +
						CoreDataRules.columns.tasks.relatedTasks + ", " +
						CoreDataRules.columns.tasks.taskOwnerNameCombined + " " +
						") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				int n = 1;
				SQLiteStatement ss = db.compileStatement(insertSql);
				ss.bindLong(n++, getId());
				ss.bindLong(n++, getOwnerId());
				ss.bindString(n++, getTaskName());
				ss.bindString(n++, getTaskDesc());
				ss.bindLong(n++, getStatus());
				ss.bindString(n++, getTags().asJsonString()); // as json
				ss.bindLong(n++, getDateCreated());
				ss.bindLong(n++, getLastModified());
				ss.bindLong(n++, getBeginDate());
				ss.bindLong(n++, getEndDate());
				ss.bindString(n++, getCourse().asJsonString());
				ss.bindString(n++, getCourseCodeCombined());
				ss.bindLong(n++, getCourseId());
				ss.bindLong(n++, personal ? 1 : 0);
				ss.bindString(n++, CoreUtils.longArrayListToJson(getRelatedNotes()));
				ss.bindString(n++, CoreUtils.longArrayListToJson(getRelatedTasks()));
				ss.bindString(n++, getTaskOwnerNameCombined());

				ss.execute();
				ss.close();

				// now insert should be complete
				// time to insert reminders one by one
				if(this.reminders != null)
				{
					for (int i = 0; i < reminders.size(); i++)
					{
						boolean res = ((Reminder) reminders.get(i)).insert();
						Utils.log.d(res+" "+i);
					}
				}

				// insert tags one by one
				if(this.tags != null)
				{
					for (int i = 0; i < tags.size(); i++)
					{
						boolean res = ((Tag) tags.get(i)).insert();
						Utils.log.d(res+" "+i);
					}
				}

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
		if(db != null)
		{
			try
			{
				int n = 1;
				String updateSql = "UPDATE " + CoreDataRules.tables.tasks + " SET " +
						CoreDataRules.columns.tasks.id + " = ?, " +
						CoreDataRules.columns.tasks.ownerId + " = ? , " +
						CoreDataRules.columns.tasks.beginDate + " = ? , " +
						CoreDataRules.columns.tasks.courseId + " = ? , " +
						CoreDataRules.columns.tasks.course + " = ? , " +
						CoreDataRules.columns.tasks.courseCodeCombined + " = ? , " +
						CoreDataRules.columns.tasks.dateCreated + " = ? , " +
						CoreDataRules.columns.tasks.endDate + " = ? , " +
						CoreDataRules.columns.tasks.personal + " = ? , " +
						CoreDataRules.columns.tasks.relatedNotes + " = ? , " +
						CoreDataRules.columns.tasks.relatedTasks + " = ? , " +
						CoreDataRules.columns.tasks.status + " = ? , " +
						CoreDataRules.columns.tasks.tags + " = ? , " +
						CoreDataRules.columns.tasks.taskDesc + " = ? , " +
						CoreDataRules.columns.tasks.taskName + " = ? , " +
						CoreDataRules.columns.tasks.taskOwnerNameCombined + " = ? " +
						" WHERE " + CoreDataRules.columns.tasks.localId + " = " + getId();

				SQLiteStatement ss = db.compileStatement(updateSql);
				ss.bindLong(n++, getId());
				ss.bindLong(n++, getOwnerId());
				ss.bindString(n++, getTaskName());
				ss.bindString(n++, getTaskDesc());
				ss.bindLong(n++, getStatus());
				ss.bindString(n++, getTags().asJsonString()); // as json
				ss.bindLong(n++, getDateCreated());
				ss.bindLong(n++, getLastModified());
				ss.bindLong(n++, getBeginDate());
				ss.bindLong(n++, getEndDate());
				ss.bindString(n++, getCourse().asJsonString());
				ss.bindString(n++, getCourseCodeCombined());
				ss.bindLong(n++, getCourseId());
				ss.bindLong(n++, personal ? 1 : 0);
				ss.bindString(n++, CoreUtils.longArrayListToJson(getRelatedNotes()));
				ss.bindString(n++, CoreUtils.longArrayListToJson(getRelatedTasks()));
				ss.bindString(n++, getTaskOwnerNameCombined());

				ss.execute();
				ss.close();

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
	public boolean remove()
	{
		if(db != null && localId > 0)
		{
			String removeSql = "DELETE FROM "+CoreDataRules.tables.tasks+" WHERE id = "+localId;
			// TODO: complete this.
		}
		return false;
	}
}
