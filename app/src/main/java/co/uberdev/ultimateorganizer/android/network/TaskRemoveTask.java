package co.uberdev.ultimateorganizer.android.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class TaskRemoveTask extends AsyncTask<Void, Integer, Integer>
{
	public static int ERROR_NETWORK = 13;
	public static int ERROR_UNKNOWN = 9;
	public static int ERROR_UNAUTHORIZED = 10;
	public static int SUCCESS = 0;

	private Activity activity;
	private CoreUser authorizedUser;
	private Task taskToRemove;

	public TaskRemoveTask(Activity activity, CoreUser user, Task task)
	{
		this.activity = activity;
		this.authorizedUser = user;
		this.taskToRemove = task;
	}

	@Override
	protected void onPreExecute()
	{
		Utils.log.w("sending task to server: "+ taskToRemove);
	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		try
		{
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.removeTask(taskToRemove);

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("update task HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			return SUCCESS;
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
			Utils.log.w("Could not remove task because there is no internet connection");
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Toast.makeText(activity, activity.getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
		}
		else if(result == SUCCESS)
		{
			Utils.log.d("removed task from the server " + taskToRemove.asJsonString());
		}
		else
		{
			Utils.log.w("Unknown error - "+result);
		}
	}
}