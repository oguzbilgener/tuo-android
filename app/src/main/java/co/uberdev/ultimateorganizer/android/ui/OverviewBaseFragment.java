package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.os.Bundle;

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

//		EventBus.getDefault().register(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();

//		EventBus.getDefault().unregister(this);
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
}
