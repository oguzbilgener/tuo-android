package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class GetTasksTask extends AsyncTask<Void, Integer, Integer>
{
	public static final int ERROR_NETWORK = 13;
	public static final int ERROR_UNKNOWN = 9;
	public static final int ERROR_UNAUTHORIZED = 10;
	public static final int SUCCESS = 0;
	public static final int CANCELLED = -1;

	private LocalStorage localStorage;
	private CoreUser authorizedUser;
	private TaskListener taskListener;
	private Task[] tasks;

	public GetTasksTask(LocalStorage storage, CoreUser user, TaskListener listener)
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

			APIResult result = client.getTasks(authorizedUser);

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("Get Tasks HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			if(isCancelled())
			{
				return CANCELLED;
			}

			tasks = result.getAsClientTaskArray();

			Tasks oldTasks = new Tasks(localStorage.getDb());
			oldTasks.loadAllAliveTasks();

			ArrayList<Task> toInsert = new ArrayList<Task>();
			ArrayList<Task> toUpdate = new ArrayList<Task>();

			if(tasks != null)
			{
				for(int i=0; i<tasks.length; i++)
				{
					boolean exists = false;
					for(int j=0;j<oldTasks.size();j++)
					{
						if(tasks[i].getId() == oldTasks.get(j).getId())
						{
							exists = true;
							if (tasks[i].getLastModified() > oldTasks.get(j).getLastModified()) {
								tasks[i].setLocalId(oldTasks.get(j).getLocalId());
								toUpdate.add(tasks[j]);
							}
							break;
						}
					}
					if(!exists)
					{
						toInsert.add(tasks[i]);
					}
				}

				for(int i=0; i<toInsert.size(); i++)
				{
					toInsert.get(i).setDb(localStorage.getDb());
					toInsert.get(i).insert();
				}

				for(int i=0; i<toUpdate.size(); i++)
				{
					toUpdate.get(i).setDb(localStorage.getDb());
					toUpdate.get(i).update();
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
				taskListener.onPostExecute(result, tasks);
			}
		}
		else
		{
			Utils.log.w("unknown error");
		}
	}
}