package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.models.Tag;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.BareListView;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.ReminderManager;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreReminders;
import co.uberdev.ultimateorganizer.core.CoreTags;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 *
 *
 */
public class AddTaskDetailFragment extends Fragment
		implements 	CalendarDatePickerDialog.OnDateSetListener,
		View.OnClickListener, ReminderListAdapter.OnItemRemoveClickListener,
		AddedTagsListAdapter.OnItemRemoveClickListener,
		SubTasksListAdapter.OnItemRemoveClickListener,
		FragmentCommunicator, TextView.OnEditorActionListener,
		AdapterView.OnItemSelectedListener
{

	public static final int MAX_REMINDER_COUNT = 5;
	public static final int MAX_RELATED_TASK_COUNT = 5;

	public static final int REQUEST_CODE_NEW_SUB_TASK = 1337;

	public LocalStorage localStorage;

	private CoreUser user;

	private BareListView remindersListView;
	private BareListView tagsListView;
	private BareListView subTasksListView;
	private Spinner coursesSpinner;
	private Switch privacySwitch;

	private ReminderListAdapter remindersAdapter;
	private AddedTagsListAdapter tagsAdapter;
	private SubTasksListAdapter subTasksAdapter;
	private CourseSelectSpinnerAdapter courseAdapter;

	private ArrayList<Reminder> reminders;
	private ArrayList<Tag> tags;
	private ArrayList<Task> subTasks;
	private ArrayList<Course> courses;

	public Task editableTask;
	public Task parentTask;
	private CoreCourse relatedCourse;

	private ViewGroup remindersAddButton;
	private ViewGroup subTaskAddButton;
	private EditText tagInput;

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
	public static final int MESSAGE_RESULT_SUB_TASK = -91;

    public static AddTaskDetailFragment newInstance(LocalStorage storage)
	{
        AddTaskDetailFragment fragment = new AddTaskDetailFragment();
		fragment.localStorage = storage;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

	public static AddTaskDetailFragment newInstance(Context context, LocalStorage storage, Task editableTask)
	{
		if(context == null || editableTask == null)
			return newInstance(storage);

		AddTaskDetailFragment fragment = new AddTaskDetailFragment();
		fragment.localStorage = storage;
		fragment.editableTask = editableTask;
		// Store task json string in arguments just in case a fragment is resurrected from background
		String taskJsonStr = editableTask.asJsonString();
		Bundle args = new Bundle();
		args.putString(context.getString(R.string.ARGS_TASK_JSON_OBJECT), taskJsonStr);
		fragment.setArguments(args);
		return fragment;
	}

	public static AddTaskDetailFragment newInstanceWithParentTask(Context context, LocalStorage storage, Task parentTask)
	{
		AddTaskDetailFragment fragment = new AddTaskDetailFragment();
		fragment.localStorage = storage;
		fragment.parentTask = parentTask;
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
		tags = new ArrayList<Tag>();
		subTasks = new ArrayList<Task>();
		courses = new ArrayList<Course>();

		relatedCourse = new CoreCourse();

		remindersAdapter = new ReminderListAdapter(getActivity(), R.layout.item_add_task_reminder, reminders);
		remindersAdapter.setItemRemoveClickListener(this);

		tagsAdapter = new AddedTagsListAdapter(getActivity(), R.layout.item_add_task_tag, tags);
		tagsAdapter.setItemRemoveClickListener(this);

		subTasksAdapter = new SubTasksListAdapter(getActivity(), R.layout.item_add_task_sub_task, subTasks);
		subTasksAdapter.setItemRemoveClickListener(this);

		courseAdapter = new CourseSelectSpinnerAdapter(getActivity(), courses);

		UltimateApplication app = (UltimateApplication) getActivity().getApplication();
		user = app.getUser();

		// load courses of user if user is logged in
		if(user != null && localStorage != null)
		{
			Courses coursesOfUser = new Courses(localStorage.getDb());
			coursesOfUser.loadAllCourses();

			courses.add(new Course());
			courses.addAll(coursesOfUser.toCourseArrayList());
			courseAdapter.notifyDataSetChanged();
		}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_task_detailed, container, false);

		remindersListView = (BareListView) rootView.findViewById(R.id.reminders_list_view);
		tagsListView = (BareListView) rootView.findViewById(R.id.tags_list_view);
		subTasksListView = (BareListView) rootView.findViewById(R.id.sub_tasks_list_view);
		coursesSpinner = (Spinner) rootView.findViewById(R.id.add_task_course_spinner);

		coursesSpinner.setOnItemSelectedListener(this);

		remindersListView.setAdapter(remindersAdapter);
		tagsListView.setAdapter(tagsAdapter);
		subTasksListView.setAdapter(subTasksAdapter);
		coursesSpinner.setAdapter(courseAdapter);

		// Create tag input
		tagInput = new EditText(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				(int) Utils.getPixelsByDp(getActivity(),
				getResources().getDimension(R.dimen.add_task_tags_input_height))
		);
		int tagInputMarginHorizontal = (int) Utils.getPixelsByDp(getActivity(),getResources().getDimension(R.dimen.add_task_tags_input_margin_horizontal) );
		int tagInputMarginVertical = (int) Utils.getPixelsByDp(getActivity(),getResources().getDimension(R.dimen.add_task_tags_input_margin_vertical) );
		params.setMargins(tagInputMarginHorizontal, tagInputMarginVertical, tagInputMarginHorizontal, tagInputMarginVertical);

		tagInput.setLayoutParams(params);
		tagInput.setId(R.id.add_task_tag_input);
		tagInput.setHint(getString(R.string.tag_input_hint));
		tagInput.setSingleLine(true);
		tagInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		tagInput.setOnEditorActionListener(this);

		tagsListView.setFooter(tagInput);

		// Create reminders add button
		remindersAddButton = (ViewGroup) inflater.inflate(R.layout.button_add_reminder, container, false);
		remindersListView.setFooter(remindersAddButton);
		remindersAddButton.setOnClickListener(this);

		// Create sub tasks add button
		subTaskAddButton = (ViewGroup) inflater.inflate(R.layout.button_add_sub_task, container, false);
		subTasksListView.setFooter(subTaskAddButton);
		subTaskAddButton.setOnClickListener(this);

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

		privacySwitch = (Switch) rootView.findViewById(R.id.add_task_privacy_switch);

		if(editableTask != null)
		{
			try {
				fillFromTask(editableTask);
			} catch (Exception e) {
				e.printStackTrace();
				// show an error toast
				Toast.makeText(getActivity(), getString(R.string.edit_task_unknown_error), Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Reminder reminder = new Reminder();
			reminder.setGap(5);
			reminder.setVibrate(true);
			reminder.setSound(true);

			reminders.add(reminder);

			remindersAdapter.notifyDataSetChanged();
		}

		if(parentTask != null)
		{
			subTasksListView.setVisibility(View.GONE);
			tagsListView.setVisibility(View.GONE);
			rootView.findViewById(R.id.add_task_course_view).setVisibility(View.GONE);

			rootView.findViewById(R.id.title_course_list).setVisibility(View.GONE);
			rootView.findViewById(R.id.title_tags_list).setVisibility(View.GONE);
			rootView.findViewById(R.id.title_sub_tasks_list).setVisibility(View.GONE);
		}
		else if(editableTask != null)
		{
			subTasksListView.setVisibility(View.GONE);
			rootView.findViewById(R.id.title_sub_tasks_list).setVisibility(View.GONE);
		}

		if(user == null || localStorage == null)
		{
			coursesSpinner.setVisibility(View.GONE);
			rootView.findViewById(R.id.title_course_list).setVisibility(View.GONE);
		}

		//
		else if(relatedCourse != null && courses !=null)
		{
			for(int i=0;i<courses.size();i++)
			{
				if(relatedCourse.getCourseCodeCombined().equals(courses.get(i).getCourseCodeCombined()))
				{
					coursesSpinner.setSelection(i);
					break;
				}
			}
		}
		else
		{
			coursesSpinner.setSelection(0);
		}

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
		else if(id == R.id.add_task_button_to_date)
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
		else if(id == R.id.button_add_sub_task)
		{
			Intent addSubTaskIntent = new Intent(getActivity(), AddSubTaskActivity.class);

			Task temporaryParentTask = new Task();
			temporaryParentTask.setTaskName(taskNameView.getText().toString());
			temporaryParentTask.setTaskDesc(taskNameView.getText().toString());
			addSubTaskIntent.putExtra(getString(R.string.INTENT_DETAILS_TASK_JSON_OBJECT), temporaryParentTask.asJsonString());

			getActivity().startActivityForResult(addSubTaskIntent, REQUEST_CODE_NEW_SUB_TASK);
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
			fromCalendar.set(Calendar.SECOND, 0);
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
			toCalendar.set(Calendar.SECOND, 0);
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
	public void onReminderRemoveClick(View view, int position)
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
	public void onTagRemoveClick(View view, int position)
	{
		tags.remove(position);
		tagsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSubTaskRemoveClick(View view, int position)
	{
		subTasks.remove(position);
		subTasksAdapter.notifyDataSetChanged();

		if(subTasks.size() < MAX_RELATED_TASK_COUNT)
		{
			subTaskAddButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onMessage(int msgType, Object obj)
	{
		switch(msgType)
		{
			case MESSAGE_REQUEST_TASK:
				// do no do anything if task name is empty
				if(!taskNameView.getText().toString().isEmpty())
				{
					// Just build the task object and send it to the Activity to use it
					activityCommunicator.onMessage(MESSAGE_RESPONSE_TASK, buildTask());
				}
			break;

			case MESSAGE_RESULT_SUB_TASK:
				useSubTaskObject(obj);
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int keyCode, KeyEvent event)
	{
		if(v.getId() == R.id.add_task_tag_input)
		{
			if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ENDCALL || keyCode == 0)
			{
				String tagName = ((EditText) v).getText().toString().trim();
				// Do not let user to enter the same tag
				if(tagName.isEmpty() || tagsListContainsTagName(tagName))
				{
					return false;
				}
				Tag tag = new Tag();
				tag.setName(tagName);
				tag.setColor(Utils.colorForTagStr(tag.getName()));
				v.setText("");
				tags.add(tag);
				tagsAdapter.notifyDataSetChanged();
				return true;
			}
		}
		return false;
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
			fromCalendar.set(Calendar.SECOND, 0);
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
			toCalendar.set(Calendar.SECOND, 0);
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

	public FragmentActivity getParent()
	{
		return (FragmentActivity) getActivity();
	}

	public Task buildTask()
	{
		// Try to retrieve the task object form editableTask.
		// If not, try arguments
		// If there is still no task, we cannot do anything but creating a new task object.
		// Then it will not be an edit action of course.
		Bundle args = getArguments();
		Task task;
		if(editableTask == null && (args == null || !args.containsKey(getString(R.string.ARGS_TASK_JSON_OBJECT))))
		{
			task = new Task();
		}
		else if(editableTask != null)
		{
			task = editableTask;
		}
		else
		{
			String taskStr = args.getString(getString(R.string.ARGS_TASK_JSON_OBJECT));
			task = Task.fromJson(taskStr, Task.class);
			if(task == null)
			{
				Utils.log.w("editable task string could not be parsed");
				task = new Task();
			}
		}

		task.setTaskName(taskNameView.getText().toString());
		task.setTaskDesc(taskDescriptionView.getText().toString());

		task.setBeginDate((int)(fromDate.getTime()/1000));
		task.setEndDate((int) (toDate.getTime()/1000));

		if(task.getDateCreated() <= 0)
		{
			task.setDateCreated(Utils.getUnixTimestamp());
		}
		task.setLastModified(Utils.getUnixTimestamp());

		// remove old reminders from AlarmManager
		for (int i=0; i<reminders.size(); i++)
		{
			ReminderManager.cancel(getActivity(), reminders.get(i));
		}

		try
		{
			task.setStatus(Task.STATE_ACTIVE);

			UltimateApplication app = (UltimateApplication) getActivity().getApplication();

			// flush the old reminders
			task.setReminders(new CoreReminders());

			// add new reminders one by one to task object
			for (int i=0; i<reminders.size(); i++)
			{
				reminders.get(i).setTitle(task.getTaskName());
				reminders.get(i).setTargetDate(task.getBeginDate() - reminders.get(i).getGap() * 60);

				if (app.user != null)
				{
					reminders.get(i).setOwnerId(app.user.getId());
				}

				task.addReminder(reminders.get(i));
			}

			// set a related course
			task.setCourse(relatedCourse);
			task.setCourseCodeCombined(relatedCourse.getCourseCodeCombined());

			// flush the old tags
			task.setTags(new CoreTags());

			// add the tags
			for(int i=0; i<tags.size(); i++)
			{
				task.addTag(tags.get(i));
			}


			if(subTasks != null && subTasks.size() > 0)
			{
				// flush the old sub tasks
				task.setRelatedTasksLocal(new ArrayList<Long>());

				for(int i=0; i<subTasks.size(); i++)
				{
					// set the same related course for the sub task
					subTasks.get(i).setCourse(relatedCourse);
					// set the same tags for the sub task
					subTasks.get(i).setTags(task.getTags());
					// update on the database to remember the relation
					subTasks.get(i).setDb(localStorage.getDb());
					subTasks.get(i).update();

					// form relation!
					task.getRelatedTasksLocal().add(subTasks.get(i).getLocalId());
				}
			}

			if(privacySwitch.isChecked())
			{
				task.setPersonal(false);
			}
			else
			{
				task.setPersonal(true);
			}
		}
		catch(Exception e)
		{
			Utils.log.w(e.toString());
			e.printStackTrace();
		}


		Utils.log.d("built task: \n"+task);


		return task;
	}

	public void fillFromTask(Task task)
	{
		// fill name and description
		taskNameView.setText(task.getTaskName());
		taskDescriptionView.setText(task.getTaskDesc());

		// fill dates
		fromDate = new Date((long)task.getBeginDate()*1000);
		toDate = new Date((long)task.getEndDate()*1000);

		fromDateButton.setText(getDateString(fromDate));
		fromTimeButton.setText(getTimeString(fromDate));

		// they do not seem to be inserted into db
		if(task.getReminders() != null)
		{
			// fill reminders
			for (int i = 0; i < task.getReminders().size(); i++) {
				this.reminders.add((Reminder) task.getReminders().get(i));
			}
			this.remindersAdapter.notifyDataSetChanged();
		}


		if(task.getTags() != null)
		{
			for( int i = 0; i < task.getTags().size(); i++)
			{
				this.tags.add((Tag) task.getTags().get(i));
			}
			this.tagsAdapter.notifyDataSetChanged();
		}

		if(task.isPersonal())
		{
			privacySwitch.setChecked(false);
		}
		else
		{
			privacySwitch.setChecked(true);
		}

		this.relatedCourse =  task.getCourse();
	}

	private void useSubTaskObject(Object subTaskObj)
	{
		try
		{
			Task subTask = (Task) subTaskObj;

			subTasks.add(subTask);
			subTasksAdapter.notifyDataSetChanged();

			if(subTasks.size() == MAX_RELATED_TASK_COUNT)
			{
				subTaskAddButton.setVisibility(View.GONE);
			}
		}
		catch(Exception e)
		{
			if(e.getMessage() != null)
				Utils.log.w(e.getMessage());
			else
				Utils.log.w(e.toString());

			Toast.makeText(getActivity(), getString(R.string.add_subtask_no_relation_error), Toast.LENGTH_SHORT).show();
		}
	}

	public boolean tagsListContainsTagName(String name)
	{
		for(int i=0;i<tags.size();i++)
		{
			if(tags.get(i).getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent.getId() == R.id.add_task_course_spinner)
		{
			if(position == 0)
			{

				// Workaround for Android's calling this method late
				((TextView) view.findViewById(android.R.id.text1)).setText(getString(R.string.course_spinner_default));
				((TextView) view.findViewById(android.R.id.text2)).setText("");
			}
			else
			{

				// Workaround for Android's calling this method late
				((TextView)  view.findViewById(android.R.id.text1)).setText(courses.get(position).getCourseCodeCombined());
				((TextView)  view.findViewById(android.R.id.text2)).setText(courses.get(position).getCourseTitle());
			}
			relatedCourse = courses.get(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{

	}

}
