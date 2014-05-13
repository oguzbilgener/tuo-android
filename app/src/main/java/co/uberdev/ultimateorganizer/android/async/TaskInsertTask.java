package co.uberdev.ultimateorganizer.android.async;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class TaskInsertTask extends AsyncTask<Void, Integer, Integer>
{
	public static final int ERROR_NETWORK = 13;
	public static final int ERROR_UNKNOWN = 9;
	public static final int ERROR_UNAUTHORIZED = 10;
	public static final int SUCCESS = 0;

	private Activity activity;
	private CoreUser authorizedUser;
	private Task taskToInsert;


	public TaskInsertTask(Activity activity, CoreUser user, Task task)
	{
		this.activity = activity;
		this.authorizedUser = user;
		this.taskToInsert = task;
	}

	@Override
	protected void onPreExecute()
	{
		Utils.log.w("sending task to server: "+taskToInsert);
	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		try
		{
			Utils.log.d(authorizedUser.getPublicKey()+" \n "+authorizedUser.getSecretToken());
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.insertTask(taskToInsert);

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("insert task HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			CoreTask insertedTask = result.getAsTask();
			if(insertedTask != null)
			{
				boolean dbOpened = false;
				// the database in the reference might not be open
				if(!taskToInsert.getDb().isOpen())
				{
					taskToInsert.setDb(new LocalStorage(activity).getDb());
					activity = null;
					dbOpened = true;
				}

				// update local task with distant task's id
				taskToInsert.setId(insertedTask.getId());
				// make a database query to set the id
				taskToInsert.update();

				if(dbOpened)
				{
					taskToInsert.getDb().close();
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
			Utils.log.w("Could not insert task because there is no internet connection");
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Utils.log.w("Invalid login.");
		}
		else if(result == SUCCESS)
		{
			Utils.log.d("inserted task into server "+taskToInsert.asJsonString());
		}
		else
		{
			Utils.log.w("Unknown error - "+result);
		}
	}
}