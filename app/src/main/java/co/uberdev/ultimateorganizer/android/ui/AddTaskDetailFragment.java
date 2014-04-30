package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTaskDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddTaskDetailFragment extends Fragment
		implements 	CalendarDatePickerDialog.OnDateSetListener,
		RadialTimePickerDialog.OnTimeSetListener,
		View.OnClickListener
{

    private OnFragmentInteractionListener mListener;

	private Button fromDateButton;
	private Button fromTimeButton;
	private Button toDateButton;
	private Button toTimeButton;

	private Date fromDate;
	private Date toDate;
	private Date lastDate;
	// this is the time difference between from and to fields, that should be preserved
	private long timeDifference;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTaskDetailFragment newInstance()
	{
        AddTaskDetailFragment fragment = new AddTaskDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public AddTaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

		timeDifference = 3600 * 1000; // An hour
		fromDate = new Date();
		toDate = new Date(fromDate.getTime() + timeDifference);

		lastDate = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_task_detailed, container, false);

		fromDateButton = (Button) rootView.findViewById(R.id.add_task_button_from_date);
		fromTimeButton = (Button) rootView.findViewById(R.id.add_task_button_from_time);

		toDateButton = (Button) rootView.findViewById(R.id.add_task_button_to_date);
		toTimeButton = (Button) rootView.findViewById(R.id.add_task_button_to_time);

		fromDateButton.setText(getDateString(fromDate));
		fromTimeButton.setText(getTimeString(fromDate));

		toDateButton.setText(getDateString(toDate));
		toTimeButton.setText(getTimeString(toDate));

		fromDateButton.setOnClickListener(this);
		fromTimeButton.setOnClickListener(this);
		toDateButton.setOnClickListener(this);
		toTimeButton.setOnClickListener(this);

		return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	@Override
	public void onResume()
	{
		super.onResume();
		// Retrieve pickers if possible

		// from date picker
		CalendarDatePickerDialog fromCalendarDatePickerDialog = (CalendarDatePickerDialog) getParent().getSupportFragmentManager()
				.findFragmentByTag(getString(R.string.DATE_PICKER_FROM));
		if (fromCalendarDatePickerDialog != null) {
			fromCalendarDatePickerDialog.setOnDateSetListener(this);
		}

		// from time picker


		// to date picker
		CalendarDatePickerDialog toCalendarDatePickerDialog = (CalendarDatePickerDialog) getParent().getSupportFragmentManager()
				.findFragmentByTag(getString(R.string.DATE_PICKER_TO));
		if (toCalendarDatePickerDialog != null) {
			toCalendarDatePickerDialog.setOnDateSetListener(this);
		}

		// to time picker
	}

	@Override
	public void onClick(View view)
	{
		int id = view.getId();

		if(id == R.id.add_task_button_from_date)
		{
			// show date picker for from

			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(fromDate);

			FragmentManager fm = getParent().getSupportFragmentManager();
			CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
					.newInstance(this, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
							fromCalendar.get(Calendar.DAY_OF_MONTH));
			calendarDatePickerDialog.show(getParent().getSupportFragmentManager(), getString(R.string.DATE_PICKER_FROM));

		}
		else if(id == R.id.add_task_button_from_time)
		{
			// show time picker for from
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(fromDate);

			RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
					.newInstance(this, fromCalendar.get(Calendar.HOUR_OF_DAY), fromCalendar.get(Calendar.MINUTE),
							DateFormat.is24HourFormat(getActivity()));
			timePickerDialog.show(getParent().getSupportFragmentManager(), getString(R.string.TIME_PICKER_FROM));
		}
		if(id == R.id.add_task_button_to_date)
		{
			// show date picker for to
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(toDate);

			CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
					.newInstance(this, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
							toCalendar.get(Calendar.DAY_OF_MONTH));
			calendarDatePickerDialog.show(getParent().getSupportFragmentManager(), getString(R.string.DATE_PICKER_TO));

		}
		else if(id == R.id.add_task_button_to_time)
		{
			// show time picker for to
			TimePickerBuilder tpb = new TimePickerBuilder()
					.setFragmentManager(getParent().getSupportFragmentManager())
					.setStyleResId(R.style.BetterPickersDialogFragment)
					.setReference(R.string.TIME_PICKER_TO);
			tpb.show();
		}

	}

	public String getDateString(Date date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
		return dateFormat.format(date);
	}

	public String getTimeString(Date date)
	{
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		return timeFormat.format(date);
	}

	@Override
	public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int monthOfYear, int dayOfMonth)
	{
		String tag = calendarDatePickerDialog.getTag();

		if(tag.equals(getString(R.string.DATE_PICKER_FROM)))
		{
			// literally change the time
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.set(year, monthOfYear, dayOfMonth);
			fromDate = fromCalendar.getTime();

			// keep the interval between from and to
			toDate = new Date(fromDate.getTime() + timeDifference);
		}
		else if(tag.equals(getString(R.string.DATE_PICKER_TO)))
		{
			// literally change the time
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.set(year, monthOfYear, dayOfMonth);
			toDate = toCalendar.getTime();
		}

		fromDateButton.setText(getDateString(fromDate));
		fromTimeButton.setText(getTimeString(fromDate));

		toDateButton.setText(getDateString(toDate));
		toTimeButton.setText(getTimeString(toDate));
	}

	@Override
	public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);

		lastDate = calendar.getTime();
		String tag = "";

//		if(tag.equals(getString(R.string.TIME_PICKER_FROM)))
//		{
//			// literally change the time
//			Calendar fromCalendar = Calendar.getInstance();
//			fromCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//			fromCalendar.set(Calendar.MINUTE, minute);
//			fromDate = fromCalendar.getTime();
//
//			// keep the interval between from and to
//			toDate = new Date(fromDate.getTime() + timeDifference);
//		}
//		else if(tag.equals(getString(R.string.TIME_PICKER_TO)))
//		{
//			// literally change the time
//			Calendar toCalendar = Calendar.getInstance();
//			toCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//			toCalendar.set(Calendar.MINUTE, minute);
//			toDate = toCalendar.getTime();
//		}
//
//		fromDateButton.setText(getDateString(fromDate));
//		fromTimeButton.setText(getTimeString(fromDate));
//
//		toDateButton.setText(getDateString(toDate));
//		toTimeButton.setText(getTimeString(toDate));
	}

	/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public static interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

	public AddTaskActivity getParent()
	{
		return (AddTaskActivity) getActivity();
	}

}
