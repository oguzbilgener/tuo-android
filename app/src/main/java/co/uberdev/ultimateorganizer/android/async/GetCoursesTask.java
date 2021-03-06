package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class GetCoursesTask extends AsyncTask<Void, Integer, Integer>
{
	public static final int ERROR_NETWORK = 13;
	public static final int ERROR_UNKNOWN = 9;
	public static final int ERROR_UNAUTHORIZED = 10;
	public static final int SUCCESS = 0;

	private LocalStorage localStorage;
	private CoreUser authorizedUser;
	private TaskListener taskListener;
	private Course[] courses;

	public GetCoursesTask(LocalStorage storage, CoreUser user, TaskListener listener)
	{
		this.localStorage = storage;
		this.authorizedUser = user;
		this.taskListener = listener;
	}

	@Override
	protected void onPreExecute()
	{

	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		try
		{
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.getCourses(authorizedUser);
			Utils.log.d("courses response body: \n"+result.getResponseBody());

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("Get Courses HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			courses = result.getAsClientCourseArray();

			Courses oldCourses = new Courses(localStorage.getDb());
			oldCourses.loadAllCourses();

			if(courses != null)
			{
				oldCourses.removeAll();

				for(int i=0; i<courses.length; i++)
				{
					Course course = courses[i];
					course.setDb(localStorage.getDb());
					course.insert();
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
			Utils.log.w("no network connection");
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Utils.log.w("unauthorized");
		}
		else if(result == SUCCESS)
		{
			if(taskListener != null)
			{
				taskListener.onPostExecute(result, courses);
			}
		}
		else
		{
			Utils.log.w("unknown error");
		}
	}
}