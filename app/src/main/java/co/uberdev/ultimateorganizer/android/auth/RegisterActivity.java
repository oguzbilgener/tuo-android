package co.uberdev.ultimateorganizer.android.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.ui.HomeActivity;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.client.APIResult;
import co.uberdev.ultimateorganizer.client.TuoClient;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 *  created by dunkuCoder 09/05/2014
 */

public class RegisterActivity extends Activity implements ActivityCommunicator
{
    public static final int MESSAGE_REGISTER_CLICK = -99;
    private boolean registering;

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
                new RegisterTask(this).execute(email, password, firstName, lastName, schoolName, departmentName);
            }
        }
    }

    private static class RegisterTask extends AsyncTask<String, Integer, Integer>
    {
        public static int ERROR_NETWORK = 13;
        public static int ERROR_UNAUTHORIZED = 10;
        public static int SUCCESS = 0;

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
                activity.finishLogin(authorizedUser);
            }
            else
            {
                Toast.makeText(activity, activity.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
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
