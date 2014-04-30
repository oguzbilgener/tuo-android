package co.uberdev.ultimateorganizer.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by oguzbilgener on 30/04/14.
 */
public class LocalStorage
{
	private SQLiteDatabase db;
	private DbHelper helper;

	public LocalStorage(Context context) throws SQLException
	{
		helper = new DbHelper(context);
		db = helper.getWritableDatabase();
	}

	public void close()
	{
		try
		{
			db.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public SQLiteDatabase getDb()
	{
		return db;
	}
}
