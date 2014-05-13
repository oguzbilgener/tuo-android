package co.uberdev.ultimateorganizer.android.async;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.auth.LoginActivity;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by oguzbilgener on 10/05/14.
 */
public class LoginTask extends AsyncTask<String, Integer, Integer>
{
	public static final int ERROR_NETWORK = 13;
	public static final int ERROR_UNKNOWN = 9;
	public static final int ERROR_UNAUTHORIZED = 10;
	public static final int SUCCESS = 0;

	private LoginActivity activity;
	private CoreUser authorizedUser;

	public LoginTask(LoginActivity activity)
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

		TuoClient client = new TuoClient(null, null);

		try
		{
			APIResult result = client.logIn(emailAddress, password);

			if(result.getResponseCode() != APIResult.RESPONSE_SUCCESS)
			{
				Utils.log.w("login HTTP "+result.getResponseCode());
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
	public void onProgressUpdate(Integer... values) //publishProgress (Progress... values)
	{
		int percent = values[0];

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
			activity.finishLogin(authorizedUser);
		}
		else
		{
			Toast.makeText(activity, activity.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
		}
	}
}