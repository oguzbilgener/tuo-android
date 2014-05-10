package co.uberdev.ultimateorganizer.android.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.network.LoginTask;
import co.uberdev.ultimateorganizer.android.ui.HomeActivity;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.core.CoreUser;

public class LoginActivity extends Activity implements ActivityCommunicator
{
	public static final int MESSAGE_LOGIN_CLICK = -99;
	private boolean loggingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.RGBA_8888);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE);

		loggingIn = false;


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
		else if(id == android.R.id.home) {
			Intent homeIntent = new Intent(this, HomeActivity.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(homeIntent);
		}
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onMessage(int msgType, Object obj)
	{
		if(msgType == MESSAGE_LOGIN_CLICK)
		{
			if(!loggingIn && obj != null)
			{
				Bundle credentials = (Bundle) obj;
				String email = credentials.getString(getString(R.string.LOGIN_EMAIL));
				String password = credentials.getString(getString(R.string.LOGIN_PASSWORD));
				new LoginTask(this).execute(email, password);
			}
		}
	}



	public void finishLogin(CoreUser user)
	{
		UltimateApplication app = (UltimateApplication) getApplication();
		app.loginUser(user);

		Intent homeIntent = new Intent(this, HomeActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(homeIntent);
	}

}
