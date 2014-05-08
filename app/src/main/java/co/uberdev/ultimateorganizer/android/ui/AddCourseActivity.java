package co.uberdev.ultimateorganizer.android.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 *  created by dunkuCoder 07/07/2014
 */

public class AddCourseActivity extends FragmentActivity implements ActivityCommunicator
{
    private LocalStorage localStorage;
    private FragmentCommunicator fragmentCommunicator;

    public static final int MESSAGE_SWITCH_TO_DETAILS = -98;
    public static final int MESSAGE_COURSE_RETRIEVE = -99;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.add_course_academic_year_color)));

        localStorage = new LocalStorage(this);

        AddCourseFragment fragment = AddCourseFragment.newInstance();
        fragmentCommunicator = fragment;

        setContentView(R.layout.activity_add_course);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.add_course_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onDestroy()
    {
        try
        {
            localStorage.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(localStorage != null)
        {
            localStorage.reopen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Bundle extras = getIntent().getExtras();
        if(id == android.R.id.home && extras != null && extras.getString(getString(R.string.INTENT_CALLER_ACTIVITY)) != null) {
            finish();
            return true;
        }

        if (id == R.id.action_add_course) {
            // literally add the course, show a toast and return back to HomeActivity
            fragmentCommunicator.onMessage(AddCourseFragment.MESSAGE_REQUEST_COURSE, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public FragmentManager getSupportFragmentManager()
    {
        return super.getSupportFragmentManager();
    }

    public android.app.FragmentManager getFragmentManager()
    {
        return super.getFragmentManager();
    }

    @Override
    public void onMessage(int msgType, Object obj)
    {
        switch(msgType)
        {
            // fragment
            case AddCourseFragment.MESSAGE_RESPONSE_COURSE:
                try
                {
                    Course enteredCourse = (Course) obj;
                    if(!enteredCourse.isEmpty())
                    {
                        enteredCourse.setDb(localStorage.getDb());

                        // do all the neccesary insertions.

                        // insert the main course
                        if (enteredCourse.insert())
                        {
                            // TODO: sync!

                            // show a little success
                            Toast.makeText(this, getString(R.string.msg_success_add_course), Toast.LENGTH_SHORT).show();
                            Utils.log.d("inserted. now finish");

                            // Add new course activity can just go back, but this might be different for edit course activity.
                            finish();
                        }
                        else
                        {
                            // db error! do not let the user go
                            Toast.makeText(this, getString(R.string.msg_cannot_add_course), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        finish();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(this, getString(R.string.msg_cannot_add_course), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

    public LocalStorage getLocalStorage()
    {
        return localStorage;
    }
}
