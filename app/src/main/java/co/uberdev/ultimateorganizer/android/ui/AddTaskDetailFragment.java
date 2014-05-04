package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.util.BareListView;

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
		View.OnClickListener, ReminderListAdapter.OnItemRemoveClickListener
{

	public static final int MAX_REMINDER_COUNT = 5;

    private OnFragmentInteractionListener mListener;

	private BareListView remindersListView;

	private ReminderListAdapter remindersAdapter;

	private ArrayList<Reminder> reminders;

	private ViewGroup remindersAddButton;

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

		reminders = new ArrayList<Reminder>();

		remindersAdapter = new ReminderListAdapter(getActivity(), R.layout.item_add_task_reminder, reminders);
		remindersAdapter.setItemRemoveClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_task_detailed, container, false);

		remindersListView = (BareListView) rootView.findViewById(R.id.reminders_list_view);

		remindersListView.setAdapter(remindersAdapter);

		remindersAddButton = (ViewGroup) inflater.inflate(R.layout.button_add_reminder, container, false);
		remindersListView.setFooter(remindersAddButton);
		remindersAddButton.setOnClickListener(this);

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

		Reminder reminder = new Reminder();
		reminder.setGap(5);
		reminder.setVibrate(true);
		reminder.setVibrate(true);

		reminders.add(reminder);

		remindersAdapter.notifyDataSetChanged();

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
					.newInstance(new FromTimeListener(), fromCalendar.get(Calendar.HOUR_OF_DAY), fromCalendar.get(Calendar.MINUTE),
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
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(fromDate);

			RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
					.newInstance(new ToTimeListener(), toCalendar.get(Calendar.HOUR_OF_DAY), toCalendar.get(Calendar.MINUTE),
							DateFormat.is24HourFormat(getActivity()));

			timePickerDialog.show(getParent().getSupportFragmentManager(), getString(R.string.TIME_PICKER_FROM));
		}
		else if(id == R.id.button_add_reminder)
		{
			reminders.add(new Reminder());
			remindersAdapter.notifyDataSetChanged();
			if(reminders.size() >= MAX_REMINDER_COUNT)
			{
				remindersAddButton.setVisibility(View.GONE);
			}
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

			if(fromDate.getTime() >= toDate.getTime())
			{
				fromDate = new Date(toDate.getTime() - timeDifference);
			}
		}

		fromDateButton.setText(getDateString(fromDate));
		fromTimeButton.setText(getTimeString(fromDate));

		toDateButton.setText(getDateString(toDate));
		toTimeButton.setText(getTimeString(toDate));

		// update time difference
		updateTimeDifference();
	}

	@Override
	public void onItemRemoveClick(View view, int position)
	{
		// user removed a reminder.
		reminders.remove(position);
		remindersAdapter.notifyDataSetChanged();
		// make the add button visible again
		if(reminders.size() < MAX_REMINDER_COUNT)
		{
			remindersAddButton.setVisibility(View.VISIBLE);
		}
	}


	public class FromTimeListener implements RadialTimePickerDialog.OnTimeSetListener
	{

		@Override
		public void onTimeSet(RadialPickerLayout radialPickerLayout,  int hourOfDay, int minute)
		{
			// literally change the time
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			fromCalendar.set(Calendar.MINUTE, minute);
			fromDate = fromCalendar.getTime();

			// keep the interval between from and to
			if(fromDate.getTime() >= toDate.getTime())
			{
				toDate = new Date(fromDate.getTime() + timeDifference);
			}

			fromDateButton.setText(getDateString(fromDate));
			fromTimeButton.setText(getTimeString(fromDate));

			toDateButton.setText(getDateString(toDate));
			toTimeButton.setText(getTimeString(toDate));

			updateTimeDifference();
		}
	}

	public class ToTimeListener implements RadialTimePickerDialog.OnTimeSetListener
	{
		@Override
		public void onTimeSet(RadialPickerLayout radialPickerLayout,  int hourOfDay, int minute)
		{
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			toCalendar.set(Calendar.MINUTE, minute);
			toDate = toCalendar.getTime();

			// keep the interval between from and to
			if(fromDate.getTime() >= toDate.getTime())
			{
				fromDate = new Date(toDate.getTime() - timeDifference);
			}

			fromDateButton.setText(getDateString(fromDate));
			fromTimeButton.setText(getTimeString(fromDate));

			toDateButton.setText(getDateString(toDate));
			toTimeButton.setText(getTimeString(toDate));

			updateTimeDifference();
		}
	}

	private void updateTimeDifference()
	{
		timeDifference = toDate.getTime() - fromDate.getTime();
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
