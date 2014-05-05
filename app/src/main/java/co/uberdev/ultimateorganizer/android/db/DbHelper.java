package co.uberdev.ultimateorganizer.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.uberdev.ultimateorganizer.android.util.Utils;
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
		Utils.log.e("CREATE DB");
		// create tasks table
		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.tasks+" (" +
				CoreDataRules.columns.tasks.id+" INTEGER PRIMARY KEY, " +
				CoreDataRules.columns.tasks.ownerId+" INTEGER, " +
				CoreDataRules.columns.tasks.taskName+" TEXT, " +
				CoreDataRules.columns.tasks.taskDesc+" TEXT, " +
				CoreDataRules.columns.tasks.status+" INTEGER, " +
				CoreDataRules.columns.tasks.tags+" TEXT, " +
				CoreDataRules.columns.tasks.reminders+" TEXT, "+
				CoreDataRules.columns.tasks.relatedTasks+" TEXT, " +
				CoreDataRules.columns.tasks.relatedNotes+" TEXT, " +
				CoreDataRules.columns.tasks.personal+" INTEGER, " +
				CoreDataRules.columns.tasks.dateCreated+" INTEGER, " +
				CoreDataRules.columns.tasks.lastModified+" INTEGER, " +
				CoreDataRules.columns.tasks.beginDate+" INTEGER, " +
				CoreDataRules.columns.tasks.endDate+" INTEGER, " +
				CoreDataRules.columns.tasks.course+" TEXT, " +
				CoreDataRules.columns.tasks.courseId+" INTEGER, " +
				CoreDataRules.columns.tasks.courseCodeCombined+" TEXT, " +
				CoreDataRules.columns.tasks.taskOwnerNameCombined+" TEXT " +
				")");

		// create tags table
		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.tags+" (" +
				CoreDataRules.columns.tags.id+" INTEGER PRIMARY KEY, " +
				CoreDataRules.columns.tags.ownerId+" INTEGER, " +
				CoreDataRules.columns.tags.tagTitle+" TEXT, " +
				CoreDataRules.columns.tags.color+" INTEGER " +
				")");

		// create reminders table
		db.execSQL("CREATE TABLE "+ CoreDataRules.tables.reminders +" (" +
				CoreDataRules.columns.reminders.id+" INTEGER PRIMARY KEY, " +
				CoreDataRules.columns.reminders.targetDate+" INTEGER, "+
				CoreDataRules.columns.reminders.ownerId+" INTEGER, "+
				CoreDataRules.columns.reminders.localTaskId+" INTEGER, "+
				CoreDataRules.columns.reminders.taskId+" INTEGER, "+
				CoreDataRules.columns.reminders.title+" TEXT, "+
				CoreDataRules.columns.reminders.details+" TEXT, " +
				CoreDataRules.columns.reminders.gap+" INTEGER, " +
				CoreDataRules.columns.reminders.light+" INTEGER, " +
				CoreDataRules.columns.reminders.vibrate+" INTEGER," +
				CoreDataRules.columns.reminders.sound+" INTEGER " +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}
