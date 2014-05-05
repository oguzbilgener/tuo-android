package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.models.Tasks;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class OverviewAllTasksFragment extends OverviewCommonFragment
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

		Tasks allTasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
		allTasks.loadAllTasks();

		overviewTaskList = allTasks.toTaskArrayList();
		for(Task t : overviewTaskList)
		{
			Utils.log.d(t.asJsonString());
		}

		overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
	}

}
