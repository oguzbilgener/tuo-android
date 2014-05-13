package co.uberdev.ultimateorganizer.android.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.async.RegisterTask;
import co.uberdev.ultimateorganizer.android.ui.HomeActivity;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 *  created by dunkuCoder 09/05/2014
 */

public class RegisterActivity extends Activity implements ActivityCommunicator
{
    public static final int MESSAGE_REGISTER_CLICK = -99;
    private boolean registering;
	private RegisterTask registerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_TITLE);

        registering = false;


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new RegisterFragment())
                    .commit();
        }
    }

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(registerTask != null)
		{
			registerTask.cancel(true);
		}
	}

	@Override
	public void onConfigurationChanged (Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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
        if(msgType == MESSAGE_REGISTER_CLICK)
        {
            if(!registering && obj != null)
            {
                Bundle credentials = (Bundle) obj;
                String email = credentials.getString(getString(R.string.REGISTER_EMAIL));
                String password = credentials.getString(getString(R.string.REGISTER_PASSWORD));
                String firstName = credentials.getString(getString(R.string.REGISTER_FIRST_NAME));
                String lastName = credentials.getString(getString(R.string.REGISTER_LAST_NAME));
                String schoolName = credentials.getString(getString(R.string.REGISTER_SCHOOL_NAME));
                String departmentName = credentials.getString(getString(R.string.REGISTER_DEPARTMENT_NAME));
                registerTask = new RegisterTask(this);
				registerTask.execute(email, password, firstName, lastName, schoolName, departmentName);
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
