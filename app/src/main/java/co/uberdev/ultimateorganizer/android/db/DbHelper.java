package co.uberdev.ultimateorganizer.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.uberdev.ultimateorganizer.core.CoreDataRules;

/**
 * Created by oguzbilgener on 30/04/14.
 */
public class DbHelper extends SQLiteOpenHelper
{
	public static final String DB_NAME = "tuo_db";
	public static final int DB_VERSION = 1;


	public DbHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// create tasks table
		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.tasks+" (" +
				CoreDataRules.columns.tasks.localId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				CoreDataRules.columns.tasks.id+" INTEGER, "+
				CoreDataRules.columns.tasks.ownerId+" INTEGER, " +
				CoreDataRules.columns.tasks.taskName+" TEXT, " +
				CoreDataRules.columns.tasks.taskDesc+" TEXT, " +
				CoreDataRules.columns.tasks.status+" INTEGER, " +
				CoreDataRules.columns.tasks.tags+" TEXT, " +
				CoreDataRules.columns.tasks.reminders+" TEXT, "+
				CoreDataRules.columns.tasks.relatedTasks+" TEXT, " +
				CoreDataRules.columns.tasks.relatedTasksLocal+" TEXT, " +
				CoreDataRules.columns.tasks.relatedNotes+" TEXT, " +
				CoreDataRules.columns.tasks.personal+" INTEGER, " +
				CoreDataRules.columns.tasks.dateCreated+" INTEGER, " +
				CoreDataRules.columns.tasks.lastModified+" INTEGER, " +
				CoreDataRules.columns.tasks.beginDate+" INTEGER, " +
				CoreDataRules.columns.tasks.endDate+" INTEGER, " +
				CoreDataRules.columns.tasks.course+" TEXT, " +
				CoreDataRules.columns.tasks.courseId+" INTEGER, " +
				CoreDataRules.columns.tasks.courseCodeCombined+" TEXT, " +
				CoreDataRules.columns.tasks.taskOwnerNameCombined+" TEXT, " +
				CoreDataRules.columns.tasks.alarmIndex+" INTEGER "+
				")");

		// create tags table
		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.tags+" (" +
				CoreDataRules.columns.tags.localId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				CoreDataRules.columns.tags.id+" INTEGER, "+
				CoreDataRules.columns.tags.ownerId+" INTEGER, " +
				CoreDataRules.columns.tags.tagTitle+" TEXT, " +
				CoreDataRules.columns.tags.color+" INTEGER " +
				")");

		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.courses+" (" +
				CoreDataRules.columns.courses.localId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				CoreDataRules.columns.courses.id+" INTEGER, " +
				CoreDataRules.columns.courses.ownerId+" INTEGER, " +
				CoreDataRules.columns.courses.title+" TEXT, " +
				CoreDataRules.columns.courses.semester+" TEXT, " +
				CoreDataRules.columns.courses.departmentCode+" TEXT, " +
				CoreDataRules.columns.courses.courseCode+" TEXT, " +
				CoreDataRules.columns.courses.sectionCode+" INTEGER, " +
				CoreDataRules.columns.courses.instructor_name+" TEXT, " +
				CoreDataRules.columns.courses.color+" INTEGER " +
				")");

		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.notes+" ( " +
				CoreDataRules.columns.notes.localId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				CoreDataRules.columns.notes.id+" INTEGER, " +
				CoreDataRules.columns.notes.ownerId+" INTEGER, " +
				CoreDataRules.columns.notes.content+" TEXT, " +
				CoreDataRules.columns.notes.dateCreated+" INTEGER, " +
				CoreDataRules.columns.notes.lastModified+" INTEGER, " +
				CoreDataRules.columns.notes.attachment+" TEXT, " +
				CoreDataRules.columns.notes.relatedTaskID+" INTEGER " +
				")");

		db.execSQL("CREATE TABLE "+CoreDataRules.tables.cloned_tasks+" (" +
				CoreDataRules.columns.cloned_tasks.itemId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				CoreDataRules.columns.cloned_tasks.originalId+" INTEGER, " +
				CoreDataRules.columns.cloned_tasks.cloneId+" INTEGER, " +
				CoreDataRules.columns.cloned_tasks.cloneLocalId+" INTEGER " +
				")");

// no need to create a new table for reminder!!
//		// create reminders table
//		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.reminders +" (" +
//				CoreDataRules.columns.reminders.localId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//				CoreDataRules.columns.reminders.id+" INTEGER, "+
//				CoreDataRules.columns.reminders.targetDate+" INTEGER, "+
//				CoreDataRules.columns.reminders.ownerId+" INTEGER, "+
//				CoreDataRules.columns.reminders.localTaskId+" INTEGER, "+
//				CoreDataRules.columns.reminders.taskId+" INTEGER, "+
//				CoreDataRules.columns.reminders.title+" TEXT, "+
//				CoreDataRules.columns.reminders.details+" TEXT, " +
//				CoreDataRules.columns.reminders.gap+" INTEGER, " +
//				CoreDataRules.columns.reminders.light+" INTEGER, " +
//				CoreDataRules.columns.reminders.vibrate+" INTEGER," +
//				CoreDataRules.columns.reminders.sound+" INTEGER " +
//				")");

		// create notes table
		// TODO
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}
