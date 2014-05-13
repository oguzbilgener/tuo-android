package co.uberdev.ultimateorganizer.android.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreCourses;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class GetCoursesTask extends AsyncTask<Void, Integer, Integer>
{
	public static int ERROR_NETWORK = 13;
	public static int ERROR_UNKNOWN = 9;
	public static int ERROR_UNAUTHORIZED = 10;
	public static int SUCCESS = 0;

	private LocalStorage localStorage;
	private Context context;
	private CoreUser authorizedUser;
	private TaskListener taskListener;
	private CoreCourses courses;

	public GetCoursesTask(LocalStorage storage, Context context, CoreUser user, TaskListener listener)
	{
		this.localStorage = storage;
		this.context = context;
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

			APIResult result = client.getTasks();
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

			courses = result.getAsCourses();

			Courses oldCourses = new Courses(localStorage.getDb());
			oldCourses.loadAllCourses();

			for(int i=0;i<courses.size();i++)
			{
				for(int j=0;j<oldCourses.size();j++)
				{
					if(courses.get(i).getCourseCodeCombined().equals(
							oldCourses.get(i).getCourseCodeCombined()))
					{
//						courses
//						oldCourses.get(i).setId();
						break;
					}
				}
			}


			if(courses != null)
			{
				for(CoreCourse c : courses)
				{
					Utils.log.i(c.asJsonString());
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
			Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Toast.makeText(context, context.getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(context, context.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
		}
	}
}