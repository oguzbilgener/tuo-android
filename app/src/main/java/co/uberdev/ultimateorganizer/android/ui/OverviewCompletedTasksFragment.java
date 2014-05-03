package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.models.Task;
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
		Utils.log.d("AAA");
        overviewTaskList = new ArrayList<Task>();
        Task task = new Task();
        task.setTaskName("completedtask");
        task.setTaskDesc("lol");
        task.setEndDate(1234567);
        try{
            task.setStatus( 2);
        }
        catch( Exception e){
            e.printStackTrace();
        }

        overviewTaskList.add(task);
        Task task2 = new Task();
        task2.setTaskName( "sex");
        task2.setTaskDesc( "amsikgotmeme");
        task2.setEndDate( 333);
        try{
            task2.setStatus(1);
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }



        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
    }
}
