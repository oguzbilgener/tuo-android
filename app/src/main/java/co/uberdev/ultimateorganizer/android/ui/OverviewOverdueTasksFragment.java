package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Tasks;

/**
 * Created by mozart on 02/05/14.
 */
public class OverviewOverdueTasksFragment extends OverviewCommonFragment
{

	public static OverviewOverdueTasksFragment newInstance()
	{
		OverviewOverdueTasksFragment fragment = new OverviewOverdueTasksFragment();
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		Tasks overdueTasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
		overdueTasks.loadOverdueTasks();

		overviewTaskList = overdueTasks.toTaskArrayList();
        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
    }
}
