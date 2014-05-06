package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.BareListView;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 *
 *
 */
public class AddTaskDetailFragment extends Fragment
		implements 	CalendarDatePickerDialog.OnDateSetListener,
		View.OnClickListener, ReminderListAdapter.OnItemRemoveClickListener,
		FragmentCommunicator
{

	public static final int MAX_REMINDER_COUNT = 5;

	private BareListView remindersListView;

	private ReminderListAdapter remindersAdapter;

	private ArrayList<Reminder> reminders;

	private ViewGroup remindersAddButton;

	private EditText taskNameView;
	private EditText taskDescriptionView;

	private Button fromDateButton;
	private Button fromTimeButton;
	private Button toDateButton;
	private Button toTimeButton;

	private Date fromDate;
	private Date toDate;
	private Date lastDate;
	// this is the time difference between from and to fields, that should be preserved
	private long timeDifference;

	private ActivityCommunicator activityCommunicator;

	public static final int MESSAGE_REQUEST_TASK = -99;
	public static final int MESSAGE_RESPONSE_TASK = -98;

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

		taskNameView = (EditText) rootView.findViewById(R.id.add_task_name_input);
		taskDescriptionView = (EditText) rootView.findViewById(R.id.add_task_description_input);

		Reminder reminder = new Reminder();
		reminder.setGap(5);
		reminder.setVibrate(true);
		reminder.setSound(true);

		reminders.add(reminder);

		remindersAdapter.notifyDataSetChanged();

		return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
		activityCommunicator = (ActivityCommunicator) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
		activityCommunicator = null;
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
			toCalendar.setTime(toDate);

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
			fromCalendar.setTime(fromDate);
			fromCalendar.set(year, monthOfYear, dayOfMonth);
			fromDate = fromCalendar.getTime();

			// keep the interval between from and to
			toDate = new Date(fromDate.getTime() + timeDifference);
		}
		else if(tag.equals(getString(R.string.DATE_PICKER_TO)))
		{
			// literally change the time
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(toDate);
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

	@Override
	public void onMessage(int msgType, Object obj)
	{
		switch(msgType)
		{
			case MESSAGE_REQUEST_TASK:
				// Just build the task object and send it to the Activity to use it
				activityCommunicator.onMessage(MESSAGE_RESPONSE_TASK, buildTask());
			break;
		}
	}


	public class FromTimeListener implements RadialTimePickerDialog.OnTimeSetListener
	{

		@Override
		public void onTimeSet(RadialPickerLayout radialPickerLayout,  int hourOfDay, int minute)
		{
			// literally change the time
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(fromDate);
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
			toCalendar.setTime(toDate);
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

	public AddTaskActivity getParent()
	{
		return (AddTaskActivity) getActivity();
	}

	public Task buildTask()
	{
		Task task = new Task();

		task.setTaskName(taskNameView.getText().toString());
		task.setTaskDesc(taskDescriptionView.getText().toString());

		SimpleDateFormat fo = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
		Utils.log.d(fo.format(fromDate));
		Utils.log.d(fo.format(toDate));

		task.setBeginDate((int)(fromDate.getTime()/1000));
		task.setEndDate((int) (toDate.getTime()/1000));

		if(task.getDateCreated() <= 0)
		{
			task.setDateCreated(Utils.getUnixTimestamp());
		}
		task.setLastModified(Utils.getUnixTimestamp());

		try
		{
			task.setStatus(Task.STATE_ACTIVE);

			UltimateApplication app = (UltimateApplication) getActivity().getApplication();

			for (int i = 0; i < reminders.size(); i++) {
				reminders.get(i).setTitle(task.getTaskName());
				reminders.get(i).setTargetDate((task.getBeginDate()/1000) - reminders.get(i).getGap());

				if (app.user != null) {
					reminders.get(i).setOwnerId(app.user.getId());
				}
			}
		}
		catch(Exception e)
		{
			Utils.log.w(e.toString());
			e.printStackTrace();
		}

		return task;
	}

}
