package co.uberdev.ultimateorganizer.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class OverviewAllTasksFragment extends OverviewCommonFragment implements FragmentCommunicator
{
	public static OverviewAllTasksFragment newInstance()
	{
		OverviewAllTasksFragment fragment = new OverviewAllTasksFragment();
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		new RetrieveFromDatabase().execute();
	}

	@Override
	public void onMessage(int msgType, Object obj)
	{
		if(msgType == HomeActivity.MESSAGE_TASKS_SYNCED)
		{
			new RetrieveFromDatabase().execute();
		}
	}

	private class RetrieveFromDatabase extends AsyncTask<Void, Integer, Integer>
	{
		public static final int ERROR_UNKNOWN = 9;
		public static final int SUCCESS = 0;

		private Tasks tasks;

		@Override
		protected void onPreExecute()
		{
			if(activityCommunicator != null)
			{
				activityCommunicator.onMessage(HomeActivity.MESSAGE_LOADING_STARTED, null);
			}
			Utils.log.d("rfd started");
		}

		@Override
		protected Integer doInBackground(Void... params)
		{
            if(getHomeActivity() == null || getHomeActivity().getLocalStorage() == null)
                return ERROR_UNKNOWN;
			tasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
			tasks.loadAllAliveTasks();

			overviewTaskList.clear();
			overviewTaskList.addAll(tasks.toTaskArrayList());
			return SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			if(activityCommunicator != null)
			{
				activityCommunicator.onMessage(HomeActivity.MESSAGE_LOADING_FINISHED, null);
			}

			if(result == SUCCESS)
			{
				overviewTaskAdapter.notifyDataSetChanged();
			}
			else
			{
				Utils.log.w("unknown error");
			}
		}
	}

}
