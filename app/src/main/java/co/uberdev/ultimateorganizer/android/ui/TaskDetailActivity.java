package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

public class TaskDetailActivity extends FragmentActivity
{
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_task_details)));

        localStorage = new LocalStorage(this);

        Task taskToShow = null;

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(getResources().getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID)))
        {


            long localId = (Long) getIntent().getExtras().getLong(getResources().getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID));
            Utils.log.d("LocalId: "+localId);
            Tasks tasksResult = new Tasks(localStorage.getDb());
            tasksResult.loadFromDb(CoreDataRules.columns.tasks.localId + " = ? ", new String[] { String.valueOf(localId) }, 0);

            if (tasksResult.size() > 0)
            {
                taskToShow = (Task) tasksResult.get(0);
            }
            else
            {
                Toast.makeText(this, getString(R.string.task_not_found_error_message), Toast.LENGTH_SHORT).show();
                // finish the activity
                finish();
            }
        }
        if(taskToShow == null)
            taskToShow = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TaskDetailFragment.newInstance(this, taskToShow))
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_detail, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public void onResume()
    {
        super.onResume();

        if (localStorage != null)
            localStorage.reopen();
    }

    public void onDestroy()
    {
        localStorage.close();
        super.onDestroy();
    }
}
