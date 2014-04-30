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
				CoreDataRules.columns.tasks.id+" INTEGER PRIMARY KEY, " +
				CoreDataRules.columns.tasks.ownerId+" INTEGER, " +
				CoreDataRules.columns.tasks.taskName+" TEXT, " +
				CoreDataRules.columns.tasks.taskDesc+" TEXT, " +
				CoreDataRules.columns.tasks.status+" INT, " +
				CoreDataRules.columns.tasks.tags+" TEXT, " +
				CoreDataRules.columns.tasks.dateCreated+" INTEGER, " +
				CoreDataRules.columns.tasks.lastModified+" INTEGER, " +
				CoreDataRules.columns.tasks.beginDate+" INTEGER, " +
				CoreDataRules.columns.tasks.endDate+" INTEGER " +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}
