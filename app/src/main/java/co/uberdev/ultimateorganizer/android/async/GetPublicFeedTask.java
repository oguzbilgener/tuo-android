package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.models.CloneHistoryItems;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class GetPublicFeedTask extends AsyncTask<Void, Integer, Integer>
{
	public static int ERROR_NETWORK = 13;
	public static int ERROR_UNKNOWN = 9;
	public static int ERROR_UNAUTHORIZED = 10;
	public static int SUCCESS = 0;

	private CoreUser authorizedUser;
	private TaskListener taskListener;
	private CoreTask[] publicTasks;
	private CloneHistoryItems historyItems;

	public GetPublicFeedTask(CoreUser user, CloneHistoryItems items, TaskListener listener)
	{
		this.authorizedUser = user;
		this.historyItems = items;
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
			Utils.log.d(authorizedUser.getPublicKey()+" \n "+authorizedUser.getSecretToken());
			TuoClient client = new TuoClient(authorizedUser.getPublicKey(), authorizedUser.getSecretToken());

			APIResult result = client.getFeed(authorizedUser);
			Utils.log.d("public feed response body: \n"+result.getResponseBody());

			if(result.getResponseCode() == APIResult.RESPONSE_UNAUTHORIZED)
			{
				return ERROR_UNAUTHORIZED;
			}
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("Get Public Feed HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}

			publicTasks = result.getAsClientTaskArray();

			if(publicTasks != null)
			{
				for(CoreTask t : publicTasks)
				{
					Utils.log.i(t.asJsonString());
				}

				// load all the history items in the background! not the best solution though.
				historyItems.loadFromDb("", new String[]{}, 0);

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
		if(taskListener != null)
		{
			taskListener.onPostExecute(result, publicTasks);
		}
	}
}