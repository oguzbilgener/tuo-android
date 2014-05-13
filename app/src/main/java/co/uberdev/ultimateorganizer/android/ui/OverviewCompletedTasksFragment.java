package co.uberdev.ultimateorganizer.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by mozart on 02/05/14.
 */
public class OverviewCompletedTasksFragment extends OverviewCommonFragment
{


	public static OverviewCompletedTasksFragment newInstance()
	{
		OverviewCompletedTasksFragment fragment = new OverviewCompletedTasksFragment();
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
			tasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
			tasks.loadCompletedTasks();

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
