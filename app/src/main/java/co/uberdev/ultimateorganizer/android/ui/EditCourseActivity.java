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
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

/**
*  created by dunkuCoder 07/07/2014
*/

public class EditCourseActivity extends FragmentActivity implements ActivityCommunicator
{
    private FragmentCommunicator fragmentCommunicator;
    private LocalStorage localStorage;
    private Course editedCourse;

    // TODO:
    // Use this class in a different way so that it recieves an existing Course and updates it.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.add_course_academic_year_color)));

        localStorage = new LocalStorage(this);

        AddCourseFragment fragment;
        // check for intent extras
        try
        {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                String keyForCourseByLocalId = getString(R.string.INTENT_DETAILS_COURSE_LOCAL_ID);
                String keyForCourseByJsonObject = getString(R.string.INTENT_DETAILS_COURSE_JSON_OBJECT);

                if(extras.containsKey(keyForCourseByLocalId))
                {
                    Long localId = extras.getLong(keyForCourseByLocalId);

                    // fetch course details from database
                    Courses matchingCourses = new Courses(localStorage.getDb());
                    matchingCourses.loadFromDb(CoreDataRules.columns.courses.localId+" = ?", new String[]{String.valueOf(localId)}, 0);
                    if(matchingCourses.size() > 0)
                    {
                        editedCourse = (Course) matchingCourses.get(0);
                    }
                    else
                    {
                        throw new Exception("no course found with local id " + localId);
                    }
                }
                else if(extras.containsKey(keyForCourseByJsonObject))
                {
                    String courseJson = extras.getString(keyForCourseByJsonObject);
                    editedCourse = Course.fromJson(courseJson, Course.class);
                }
                else
                {
                    throw new Exception("no id or course object found in intent extras");
                }

                if(editedCourse == null)
                {
                    throw new Exception("edited course is still null");
                }
            }
        }
        catch(Exception e)
        {
            if(e.getMessage() != null)
                Utils.log.w(e.getMessage());
            else
                Utils.log.w(e.getMessage());

            Toast.makeText(this, getString(R.string.edit_course_no_course), Toast.LENGTH_SHORT).show();
        }
        fragment = AddCourseFragment.newInstance(this, editedCourse);
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
            // Ask the fragment to pass the course object
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
            case AddCourseFragment.MESSAGE_RESPONSE_COURSE:

                Course editableCourse = (Course) obj;

                Utils.log.d(editableCourse.toString());

                editableCourse.setDb(localStorage.getDb());

                if(editableCourse.getLocalId() != 0)
                {
                    if(editableCourse.insert())
                    {
                        // TODO: sync!
                        if(editableCourse.getId() != 0)
                        {
                            // only sync if we have a server id
                        }

                        Toast.makeText(this, getString(R.string.edit_course_success), Toast.LENGTH_SHORT).show();
                        // let the user go
                        finish();
                    }
                    else
                    {
                        // display error
                        Toast.makeText(this, getString(R.string.edit_course_unknown_error), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // no id? then we have to create a new course in order not to lose data
                    // insert the main course
                    if (editableCourse.insert())
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

                break;
        }
    }
}
