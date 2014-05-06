package co.uberdev.ultimateorganizer.android.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreReminders;
import co.uberdev.ultimateorganizer.core.CoreSelectable;
import co.uberdev.ultimateorganizer.core.CoreTags;
import co.uberdev.ultimateorganizer.core.CoreTasks;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class Tasks extends CoreTasks implements CoreSelectable
{
	private transient SQLiteDatabase db;

	public Tasks(SQLiteDatabase db)
	{
		this.db = db;
	}

	public Tasks()
	{
		this(null);
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
	public String getTableName() {
		return CoreDataRules.tables.tasks;
	}

	@Override
	public boolean loadFromDb(String sqlCriteria, String[] params, int limit)
	{
		try
		{
			if(db != null)
			{
				Utils.log.e(sqlCriteria);
				String sqlString = "SELECT * FROM "+CoreDataRules.tables.tasks;
				if(params.length > 0)
					sqlString +=  " WHERE "+sqlCriteria;

				Cursor loader = db.rawQuery(sqlString,	params);

				if(!loader.moveToFirst())
					return true; // no rows in the db, still valid

				while(!loader.isAfterLast())
				{
					Task task = new Task();
					task.setLocalId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.tasks.localId))); // local id = sqlite id
					task.setId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.tasks.id))); // task id = server id
					task.setTaskName(loader.getString(loader.getColumnIndex(CoreDataRules.columns.tasks.taskName)));
					task.setTaskDesc(loader.getString(loader.getColumnIndex(CoreDataRules.columns.tasks.taskDesc)));
					task.setOwnerId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.tasks.ownerId)));
					task.setTaskOwnerNameCombined(loader.getString(loader.getColumnIndex(CoreDataRules.columns.tasks.taskOwnerNameCombined)));
					task.setStatus((int) loader.getLong(loader.getColumnIndex(CoreDataRules.columns.tasks.status)));

					// parse tags and set
					CoreTags tags = CoreTags.fromJson(loader.getString(
							loader.getColumnIndex(CoreDataRules.columns.tasks.tags)
						), CoreTags.class);
					task.setTags(tags);

					// parse related task ids
					ArrayList relatedTaskIds = new Gson().fromJson(loader.getString(
							loader.getColumnIndex(CoreDataRules.columns.tasks.relatedTasks)
					),  new TypeToken<ArrayList<Long>>(){}.getType());
					task.setRelatedTasks(relatedTaskIds);

					// parse related notes ids
					ArrayList relatedNoteIds = new Gson().fromJson(loader.getString(
							loader.getColumnIndex(CoreDataRules.columns.tasks.relatedNotes)
					),  new TypeToken<ArrayList<Long>>(){}.getType());
					task.setRelatedTasks(relatedNoteIds);

					int personal = loader.getInt(loader.getColumnIndex(CoreDataRules.columns.tasks.personal));
					task.setPersonal(personal == 1);

					// parse reminders and set
					task.setReminders(CoreReminders.fromJson(loader.getString(
							loader.getColumnIndex(CoreDataRules.columns.tasks.reminders)
					), CoreReminders.class));

					// parse course and set
					task.setCourse(CoreCourse.fromJson(loader.getString(
							loader.getColumnIndex(CoreDataRules.columns.tasks.course)
					), CoreCourse.class));

					task.setCourseId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.tasks.courseId)));
					task.setCourseCodeCombined(loader.getString(loader.getColumnIndex(CoreDataRules.columns.tasks.courseCodeCombined)));

					task.setDateCreated(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.tasks.dateCreated)));
					task.setLastModified(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.tasks.lastModified)));
					task.setBeginDate(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.tasks.beginDate)));
					task.setEndDate(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.tasks.endDate)));

					this.add(task);

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
		// TODO: parse tags as Tag objects

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

	/**
	 * Loads all the alive (not deleted/archived) tasks to be shown in Overview / All Tasks
	 * @return
	 */
	public boolean loadAllAliveTasks()
	{
		String sqlCriteria = " "+CoreDataRules.columns.tasks.status+" = ? OR "+
				CoreDataRules.columns.tasks.status+" = ? ORDER BY "+CoreDataRules.columns.tasks.beginDate;
		String[] params = new String[] { Integer.toString(Task.STATE_ACTIVE), Integer.toString(Task.STATE_COMPLETED) };

		return loadFromDb(sqlCriteria, params, 0);
	}

	/**
	 * Loads all the overdue tasks to be shown in Overview / Overdue
	 * @return
	 */
	public boolean loadOverdueTasks()
	{
		String sqlCriteria = " "+CoreDataRules.columns.tasks.status+" = ? AND "+
				CoreDataRules.columns.tasks.endDate+" < ? ORDER BY "+CoreDataRules.columns.tasks.beginDate;
		String[] params = new String[] {Integer.toString(Task.STATE_ACTIVE), Integer.toString(Utils.getUnixTimestamp())};
		return loadFromDb(sqlCriteria, params, 0);
	}

	/**
	 * Loads all the completed tasks to be shown in Overview / Completed
	 * @return
	 */
	public boolean loadCompletedTasks()
	{
		String sqlCriteria = " "+CoreDataRules.columns.tasks.status+" = ? "+
				" ORDER BY "+CoreDataRules.columns.tasks.beginDate;
		String[] params = new String[] {Integer.toString(Task.STATE_COMPLETED)};
		return loadFromDb(sqlCriteria, params, 0);
	}

	public boolean loadUpcomingTasks()
	{
		String sqlCriteria = " "+CoreDataRules.columns.tasks.status+" = ? AND "+
				CoreDataRules.columns.tasks.beginDate+" > ? "+
				" ORDER BY "+CoreDataRules.columns.tasks.beginDate;
		String[] params = new String[] {Integer.toString(Task.STATE_ACTIVE), Integer.toString(Utils.getUnixTimestamp())};
		return loadFromDb(sqlCriteria, params, 0);
	}

	public ArrayList<Task> toTaskArrayList()
	{
		ArrayList<Task> list = new ArrayList<Task>();
		for(int i=0; i<size(); i++)
		{
			list.add((Task) get(i));
		}
		return list;
	}
}
