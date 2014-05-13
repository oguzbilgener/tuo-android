package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.auth.RegisterActivity;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class RegisterTask extends AsyncTask<String, Integer, Integer>
{
	public static int ERROR_NETWORK = 13;
	public static int ERROR_UNKNOWN = 9;
	public static int ERROR_UNAUTHORIZED = 10;
	public static int SUCCESS = 0;

	/**
	 * Warning: keeping strong references to an Activity might cause memory leaks.
	 * This is why this task should be cancelled when the activity is destroyed
	 */
	private RegisterActivity activity;
	private CoreUser authorizedUser;

	public RegisterTask(RegisterActivity activity)
	{
		this.activity = activity;
	}

	@Override
	protected void onPreExecute()
	{
		activity.setProgressBarIndeterminateVisibility(true);
	}

	@Override
	protected Integer doInBackground(String... params)
	{
		String emailAddress = params[0];
		String password = params[1];
		String firstName = params[2];
		String lastName = params[3];
		String schoolName = params[4];
		String departmentName = params[5];

		TuoClient client = new TuoClient(null, null);

		try
		{
			// TODO birthday ought not to be 0!
			APIResult result = client.register(emailAddress, password, firstName, lastName, schoolName, departmentName, 0);
			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("register HTTP "+result.getResponseCode());
				return ERROR_UNKNOWN;
			}
			authorizedUser = result.getAsUser();
			if(authorizedUser != null)
			{
				return SUCCESS;
			}
			else
			{
				return ERROR_UNAUTHORIZED;
			}
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
		activity.setProgressBarIndeterminateVisibility(false);
		if(result == ERROR_NETWORK)
		{
			Toast.makeText(activity, activity.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}
		else if(result == ERROR_UNAUTHORIZED)
		{
			Toast.makeText(activity, activity.getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
		}
		else if(result == SUCCESS)
		{
			Toast.makeText(activity, activity.getString(R.string.register_success), Toast.LENGTH_SHORT).show();
			activity.finishLogin(authorizedUser);
		}
		else
		{
			Toast.makeText(activity, activity.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
		}
	}
}