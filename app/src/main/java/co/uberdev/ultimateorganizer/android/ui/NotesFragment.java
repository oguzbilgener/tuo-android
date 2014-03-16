package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by oguzbilgener on 15/03/14.
 */
public class NotesFragment extends Fragment
{

	// Required empty constructor
	public NotesFragment()
	{

	}

	public static NotesFragment newInstance() {
		NotesFragment fragment = new NotesFragment();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("Notes");
		return rootView;
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
