package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class TaskUpdateTask extends AsyncTask<Void, Integer, Integer>
{
	public static final int ERROR_NETWORK = 13;
	public static final int ERROR_UNKNOWN = 9;
	public static final int ERROR_UNAUTHORIZED = 10;
	public static final int SUCCESS = 0;

	private CoreUser authorizedUser;
	private Task taskToUpdate;

	public TaskUpdateTask(CoreUser user, Task task)
	{
		this.authorizedUser = user;
		this.taskToUpdate = task;
	}

	@Override
	protected void onPreExecute()
	{
		Utils.log.w("sending task to server: "+ taskToUpdate);
	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		try
		{
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.updateTask(taskToUpdate);

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
			// TODO: try updating later when the device is online -- or Sync Adapter?
			Utils.log.w("Could not update task because there is no internet connection");
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Utils.log.w("Invalid login");
		}
		else if(result == SUCCESS)
		{
			Utils.log.d("updated task at server " + taskToUpdate.asJsonString());
		}
		else
		{
			Utils.log.w("Unknown error - "+result);
		}
	}
}