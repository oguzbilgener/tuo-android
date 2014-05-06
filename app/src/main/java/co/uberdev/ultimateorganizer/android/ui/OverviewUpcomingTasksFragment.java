package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Tasks;

/**
 * Created by mozart on 02/05/14.
 */
public class OverviewUpcomingTasksFragment extends OverviewCommonFragment
{

	public static OverviewUpcomingTasksFragment newInstance()
	{
		OverviewUpcomingTasksFragment fragment = new OverviewUpcomingTasksFragment();
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		Tasks upcomingTasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
		upcomingTasks.loadUpcomingTasks();

		overviewTaskList = upcomingTasks.toTaskArrayList();
        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
    }
}
