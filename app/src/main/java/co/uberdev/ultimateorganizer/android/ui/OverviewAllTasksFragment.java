package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.models.Task;

/**
 * Created by oguzbilgener on 24/04/14.
 */
public class OverviewAllTasksFragment extends OverviewCommonFragment
{

    protected static int[] filter = new int[]{Task.STATE_ACTIVE, Task.STATE_ARCHIVED, Task.STATE_COMPLETED, Task.STATE_DELETED};


	public static OverviewAllTasksFragment newInstance()
	{
		OverviewAllTasksFragment fragment = new OverviewAllTasksFragment();
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		overviewTaskList = new ArrayList<Task>();
		Task task = new Task();
		task.setTaskName("alll");
		task.setTaskDesc("lol");
		task.setEndDate(1234567);
		try{
			task.setStatus( 1);
		}
		catch( Exception e){
			e.printStackTrace();
		}

		overviewTaskList.add(task);
		Task task2 = new Task();
		task2.setTaskName( "hmm");
		task2.setTaskDesc( "asd");
		task2.setEndDate( 333);
		try{
			task2.setStatus(1);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		overviewTaskList.add(task2);



		overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
	}

}
