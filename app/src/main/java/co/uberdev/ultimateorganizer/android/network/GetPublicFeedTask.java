package co.uberdev.ultimateorganizer.android.network;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;

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

	private Activity activity;
	private CoreUser authorizedUser;
	private TaskListener taskListener;
	private CoreTask[] publicTasks;

	public GetPublicFeedTask(Activity activity, CoreUser user, TaskListener listener)
	{
		this.activity = activity;
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

			publicTasks = result.getAsTaskArray();

			if(publicTasks != null)
			{
				for(CoreTask t : publicTasks)
				{
					Utils.log.i(t.asJsonString());
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
		if(taskListener != null)
		{
			taskListener.onPostExecute(result, publicTasks);
		}
	}
}