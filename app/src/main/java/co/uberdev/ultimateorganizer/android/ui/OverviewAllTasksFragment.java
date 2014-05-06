package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

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


		overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		Utils.log.d("overview all tasks fragment resume");

		Tasks allTasks = new Tasks(getHomeActivity().getLocalStorage().getDb());
		allTasks.loadAllAliveTasks();

		overviewTaskList.clear();
		overviewTaskList.addAll(allTasks.toTaskArrayList());
		allTasks.toTaskArrayList();
	}

}
