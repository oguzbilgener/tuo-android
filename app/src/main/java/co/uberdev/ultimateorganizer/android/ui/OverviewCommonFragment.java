package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.async.TaskRemoveTask;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreUser;

public class OverviewCommonFragment extends Fragment implements OverviewTaskAdapter.OnTaskRemovedListener,
		FragmentCommunicator
{
    protected ActivityCommunicator activityCommunicator;

    protected ListView overviewTaskListview;
    protected ArrayList<Task> overviewTaskList;
    protected OverviewTaskAdapter overviewTaskAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverviewTaskFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static OverviewCommonFragment newInstance()
    {
        OverviewCommonFragment fragment = new OverviewCommonFragment();
        return fragment;
    }
    public OverviewCommonFragment() {
        // Required empty public constructor
    }

    public static int getViewId() {
        return R.id.base_fragment_frame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overviewTaskList = new ArrayList<Task>();

        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
		overviewTaskAdapter.setLocalStorage(getHomeActivity().getLocalStorage());

		overviewTaskAdapter.setOnTaskRemovedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tasks_home, container, false);

        overviewTaskListview = (ListView) rootView.findViewById(R.id.overview_tasks_list_view);

        overviewTaskListview.setAdapter(overviewTaskAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
		activityCommunicator = (ActivityCommunicator) activity;

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        activityCommunicator = null;
    }

	@Override
	public void onTaskRemoved(int position, Task task)
	{
		Utils.log.d("onTaskRemoved");
		// remove task from the server as well, if the user is signed in
		CoreUser user = ((UltimateApplication) getActivity().getApplication()).getUser();
		if(user != null)
		{
			new TaskRemoveTask(user, task).execute();
		}
	}

	public HomeActivity getHomeActivity()
	{
		return (HomeActivity) getActivity();
	}

	@Override
	public void onMessage(int msgType, Object obj)
	{
	}
}
