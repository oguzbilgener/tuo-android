package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;

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
		Utils.log.i("Notes Fragment onResume");

//		EventBus.getDefault().register(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Utils.log.i("Notes Fragment onPause");

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
		Utils.log.i("NotesFragment onAttach");
	}

	@Override
	public void onDetach()
	{
		Utils.log.i("NotesFragment onDetach");
		super.onDetach();
	}
}
