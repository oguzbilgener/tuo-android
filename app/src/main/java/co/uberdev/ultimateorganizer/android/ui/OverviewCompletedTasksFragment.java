package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Tasks;

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
		Tasks completedTasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
		completedTasks.loadCompletedTasks();

		overviewTaskList.addAll(completedTasks.toTaskArrayList());
		overviewTaskAdapter.notifyDataSetChanged();
	}
}
