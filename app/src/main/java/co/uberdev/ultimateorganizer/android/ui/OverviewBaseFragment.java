package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.os.Bundle;

import co.uberdev.ultimateorganizer.android.async.GetTasksTask;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by oguzbilgener on 15/03/14.
 */
public class OverviewBaseFragment extends BaseFragment
{

    // Required empty constructor
    public OverviewBaseFragment()
    {

    }

    public static OverviewBaseFragment newInstance() {
        OverviewBaseFragment fragment = new OverviewBaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

		Utils.log.d("START TASKS SYNC");
        if(((UltimateApplication)getActivity().getApplication()).getUser() != null)
		    new GetTasksTask(getHomeActivity().getLocalStorage(), ((UltimateApplication)getActivity().getApplication()).getUser(), getHomeActivity()).execute();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

	public HomeActivity getHomeActivity()
	{
		return (HomeActivity) getActivity();
	}
}