package co.uberdev.ultimateorganizer.android.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class CourseInsertTask extends AsyncTask<Void, Integer, Integer>
{
	public static int ERROR_NETWORK = 13;
	public static int ERROR_UNKNOWN = 9;
	public static int ERROR_UNAUTHORIZED = 10;
	public static int SUCCESS = 0;

	private Activity activity;
	private CoreUser authorizedUser;
	private Course courseToInsert;

	public CourseInsertTask(Activity activity, CoreUser user, Course course)
	{
		this.activity = activity;
		this.authorizedUser = user;
		this.courseToInsert = course;
	}

	@Override
	protected void onPreExecute()
	{
		Utils.log.w("sending course to server: "+ courseToInsert);
	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		try
		{
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.insertCourse(courseToInsert);

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("insert course HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			CoreCourse insertedCourse = result.getAsCourse();
			if(insertedCourse != null)
			{
				boolean dbOpened = false;
				// the database in the reference might not be open
				if(!courseToInsert.getDb().isOpen())
				{
					courseToInsert.setDb(new LocalStorage(activity).getDb());
					dbOpened = true;
				}

				// update local course with distant task's id
				courseToInsert.setId(insertedCourse.getId());
				// make a database query to set the id
				courseToInsert.update();

				if(dbOpened)
				{
					courseToInsert.getDb().close();
				}

				return SUCCESS;
			}
			return ERROR_UNKNOWN;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return ERROR_NETWORK;
		}
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		if(result == ERROR_NETWORK)
		{
			// TODO: try inserting later when the device is online
			Utils.log.w("Could not insert course because there is no internet connection");
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Toast.makeText(activity, activity.getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
		}
		else if(result == SUCCESS)
		{
			Utils.log.d("inserted course into server " + courseToInsert.asJsonString());
		}
		else
		{
			Utils.log.w("Unknown error - "+result);
		}
	}
}