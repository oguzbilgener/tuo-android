package co.uberdev.ultimateorganizer.android.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.async.TaskRemoveTask;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.CloneHistoryItem;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreUser;

public class TaskDetailActivity extends FragmentActivity
{
    private LocalStorage localStorage;
	private Task taskToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_task_details)));

        localStorage = new LocalStorage(this);

        taskToShow = null;

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
		if (id == R.id.button_task_detail_edit_task) {
			long localId = taskToShow.getLocalId();
			Intent editIntent = new Intent(this, EditTaskActivity.class);
			editIntent.putExtra(getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID), localId);

			startActivity(editIntent);
			return true;
		}

		if(id == R.id.button_task_detail_delete_task ) {

			if (taskToShow != null && taskToShow.getLocalId() != 0 && localStorage != null) {
				taskToShow.setDb(localStorage.getDb());
				taskToShow.remove();
				taskToShow.cancelReminders(this);

				// delete potential history item
				CloneHistoryItem historyItem = new CloneHistoryItem(localStorage.getDb());
				historyItem.setCloneLocalId(taskToShow.getLocalId());
				historyItem.remove();

				// display an informative toast
				Toast.makeText(this, getString(R.string.task_removed), Toast.LENGTH_SHORT).show();


				CoreUser user = ((UltimateApplication) getApplication()).getUser();
				if (user != null) {
					new TaskRemoveTask(user, taskToShow).execute();
				}
				finish();
			}
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
