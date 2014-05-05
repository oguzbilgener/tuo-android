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
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;

public class AddTaskActivity extends FragmentActivity implements ActivityCommunicator
{
	private FragmentCommunicator fragmentCommunicator;

	public static final int MESSAGE_SWITCH_TO_DETAILS = -98;
	public static final int MESSAGE_TASK_RETRIEVE = -99;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_overview)));

		AddTaskDetailFragment fragment = AddTaskDetailFragment.newInstance();
		fragmentCommunicator = fragment;

        setContentView(R.layout.activity_add_task);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.add_task_container, fragment)
                    .commit();
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
		if(id == android.R.id.home && extras != null && extras.getString(getString(R.string.INTENT_CALLER_ACTIVITY)) != null) {
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
					Task enteredTask = (Task) obj;
					enteredTask.setDb(new LocalStorage(this).getDb());

					// do all the neccesary insertions.

					// TODO: insert related tasks, tags etc

					// insert the main task
					if(enteredTask.insert())
					{
						// TODO: sync!

						// show a little success
						Toast.makeText(this, getString(R.string.msg_success_add_task), Toast.LENGTH_SHORT).show();

						// Add new task activity can just go back, but this might be different for edit task activity.
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
}
