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
import co.uberdev.ultimateorganizer.android.models.CloneHistoryItem;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.async.TaskInsertTask;
import co.uberdev.ultimateorganizer.android.async.TaskUpdateTask;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.ReminderManager;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

public class EditTaskActivity extends FragmentActivity implements ActivityCommunicator
{
	private FragmentCommunicator fragmentCommunicator;
	private LocalStorage localStorage;
	private Task editedTask;

	// properties used when cloning tasks
	private boolean cloneTask = false;
	private long originalTaskId = 0;

	// TODO:
	// Use this class in a different way so that it recieves an existing Task and updates it.
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_edit_task)));

		localStorage = new LocalStorage(this);

		AddTaskDetailFragment fragment;
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
						editedTask = (Task) matchingTasks.get(0);
					}
					else
					{
						throw new Exception("no task found with local id " + localId);
					}
				}
				else if(extras.containsKey(keyForTaskByJsonObject))
				{
					String taskJson = extras.getString(keyForTaskByJsonObject);
					editedTask = Task.fromJson(taskJson, Task.class);
				}
				else
				{
					throw new Exception("no id or task object found in intent extras");
				}

				if(editedTask == null)
				{
					throw new Exception("edited task is still null");
				}
			}
		}
		catch(Exception e)
		{
			if(e.getMessage() != null)
				Utils.log.w(e.getMessage());
			else
				Utils.log.w(e.getMessage());

				Toast.makeText(this, getString(R.string.edit_task_no_task), Toast.LENGTH_SHORT).show();
		}
		// store the id of the edited task anyways, even if this is not a cloning action
		originalTaskId = editedTask.getId();
		fragment = AddTaskDetailFragment.newInstance(this, localStorage, editedTask);
		fragmentCommunicator = fragment;

		if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(getString(R.string.INTENT_FROM_PUBLIC_FEED)))
		{
			fragment.cloneTask = true;
			cloneTask = true;
		}

        setContentView(R.layout.activity_add_task);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.add_task_container, fragment)
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
			// Ask the fragment to pass the task object
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
			case AddTaskDetailFragment.MESSAGE_RESPONSE_TASK:

				Task editableTask = (Task) obj;

				Utils.log.d(editableTask.toString());

				editableTask.setDb(localStorage.getDb());

				if(editableTask.getLocalId() != 0)
				{
					// add new reminders to alarm manager
					for(int i=0; i<editableTask.getReminders().size(); i++)
					{
						ReminderManager.remind(this, editableTask, (Reminder) editableTask.getReminders().get(i));
					}

					if(editableTask.update())
					{
						Utils.log.d("updated task");
						// only sync if we have a server id
						if(editableTask.getId() != 0)
						{
							new TaskUpdateTask(((UltimateApplication) getApplication()).getUser(), editableTask).execute();
						}

						Toast.makeText(this, getString(R.string.edit_task_success), Toast.LENGTH_SHORT).show();
						// let the user go
						finish();
					}
					else
					{
						// display error
						Toast.makeText(this, getString(R.string.edit_task_unknown_error), Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					// no id? then we have to create a new task in order not to lose data
					// insert the main task
					if (editableTask.insert())
					{
						Utils.log.d("inserted task ["+cloneTask+"]");

						// show a little success
						Toast.makeText(this, getString(R.string.msg_success_add_task), Toast.LENGTH_SHORT).show();
						Utils.log.d("inserted. now finish");

						// Send this new task to the server
						new TaskInsertTask(this, ((UltimateApplication) getApplication()).getUser(), editableTask).execute();

						// Add new task activity can just go back, but this might be different for edit task activity.
						if(!cloneTask)
						{
							finish();
						}
						else
						{
							// Store cloning history
							CloneHistoryItem historyItem = new CloneHistoryItem(localStorage.getDb());
							historyItem.setOriginalId(originalTaskId);
							historyItem.setCloneLocalId(editableTask.getLocalId());
							historyItem.insert();

							Utils.log.d("history item: "+historyItem.asJsonString());

							// We have just cloned a task.
							// return straight to home!
							Intent homeIntent = new Intent(this, HomeActivity.class);
							homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(homeIntent);
						}
					}
					else
					{
						// db error! do not let the user go
						Toast.makeText(this, getString(R.string.msg_cannot_add_task), Toast.LENGTH_SHORT).show();
					}
				}

				break;
		}
	}
}
