package co.uberdev.ultimateorganizer.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by oguzbilgener on 16/03/14.
 */
public class CalendarMonthlyFragment extends Fragment {

	public static CalendarMonthlyFragment newInstance() {
		CalendarMonthlyFragment fragment = new CalendarMonthlyFragment();
		Bundle args = new Bundle();
		args.putString("123","hoooooo");
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
		rootView.setBackgroundColor(Color.CYAN);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("monthly");
		return rootView;
	}
}
