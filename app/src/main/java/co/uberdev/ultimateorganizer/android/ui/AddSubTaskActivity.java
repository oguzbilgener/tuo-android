package co.uberdev.ultimateorganizer.android.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

public class AddSubTaskActivity extends FragmentActivity implements ActivityCommunicator
{
	private LocalStorage localStorage;
	private FragmentCommunicator fragmentCommunicator;

	public static final int MESSAGE_SWITCH_TO_DETAILS = -98;
	public static final int MESSAGE_TASK_RETRIEVE = -99;

	public static final int RESULT_CODE_NEW_SUB_TASK = 1338;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_add_sub_task)));

		localStorage = new LocalStorage(this);

		AddTaskDetailFragment fragment;
		Task parentTask = null;

		// check for intent extras
		try
		{
			Bundle extras = getIntent().getExtras();
			if(extras != null)
			{
				String keyForTaskByLocalId = getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID);
				String keyForTaskByJsonObject = getString(R.string.INTENT_DETAILS_TASK_JSON_OBJECT);

				if(extras.containsKey(keyForTaskByLocalId))
				{
					Long localId = extras.getLong(keyForTaskByLocalId);

					// fetch task details from database
					Tasks matchingTasks = new Tasks(localStorage.getDb());
					matchingTasks.loadFromDb(CoreDataRules.columns.tasks.localId+" = ?", new String[]{String.valueOf(localId)}, 0);
					if(matchingTasks.size() > 0)
					{
						parentTask = (Task) matchingTasks.get(0);
					}
					else
					{
						throw new Exception("no task found with local id " + localId);
					}
				}
				else if(extras.containsKey(keyForTaskByJsonObject))
				{
					String taskJson = extras.getString(keyForTaskByJsonObject);
					parentTask = Task.fromJson(taskJson, Task.class);
				}
				else
				{
					throw new Exception("no id or task object found in intent extras");
				}

				if(parentTask == null)
				{
					throw new Exception("parent task is still null");
				}
			}
			else
			{
				throw new Exception("no intent extras");
			}

		}
		catch(Exception e)
		{
			if(e.getMessage() != null)
				Utils.log.w(e.getMessage());
			else
				Utils.log.w(e.getMessage());

			Toast.makeText(this, getString(R.string.add_subtask_no_parent_task), Toast.LENGTH_SHORT).show();
		}

		if(parentTask != null)
		{
			String activityTitle = !parentTask.getTaskName().isEmpty() ? parentTask.getTaskName() : getString(R.string.task);

			getActionBar().setTitle(String.format(getString(R.string.title_activity_add_sub_task),activityTitle));

			fragment = AddTaskDetailFragment.newInstanceWithParentTask(this, parentTask);
			fragmentCommunicator = fragment;

			setContentView(R.layout.activity_add_task);
			if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
						.add(R.id.add_task_container, fragment)
						.commit();
			}
		}
		else
		{
			finish();
		}

		// add_subtask_unknown_no_parent_task
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
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
		Bundle extras = getIntent().getExtras();
		if(id == android.R.id.home)
		{
			finish();
			return true;
		}

        if (id == R.id.action_add_task) {
			// literally add the task, show a toast and return back to HomeActivity
			fragmentCommunicator.onMessage(AddTaskDetailFragment.MESSAGE_REQUEST_TASK, null);
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
			case AddTaskDetailFragment.MESSAGE_RESPONSE_TASK:
				try
				{
					Task subTask = (Task) obj;
					subTask.setDb(localStorage.getDb());

					// do all the neccesary insertions.

					// TODO: insert related tasks, tags etc

					// insert the main task
					if(subTask.insert())
					{
						// TODO: sync!

						// show a little success
						Toast.makeText(this, getString(R.string.msg_success_add_task), Toast.LENGTH_SHORT).show();

						// set a result to pass the task object via intent and go back to main AddTaskActivity
						Intent resultIntent = new Intent();
						resultIntent.putExtra(getString(R.string.INTENT_RESULT_SUB_TASK_OBJECT), subTask.asJsonString());
						setResult(RESULT_CODE_NEW_SUB_TASK, resultIntent);
						finish();
					}
					else
					{
						// db error! do not let the user go
						Toast.makeText(this, getString(R.string.msg_cannot_add_task), Toast.LENGTH_SHORT).show();
					}
				}
				catch(Exception e)
				{
					Toast.makeText(this, getString(R.string.msg_cannot_add_task), Toast.LENGTH_SHORT).show();
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
