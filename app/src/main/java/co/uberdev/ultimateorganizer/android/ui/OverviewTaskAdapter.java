package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.CloneHistoryItem;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.BareListView;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by dunkuCoder on 02/05/2014
 * OverviewTaskAdapter, to display the created tasks on Overview activity.
 */

public class OverviewTaskAdapter extends ArrayAdapter<Task> implements View.OnClickListener
{
    private ArrayList<Task> overviewTaskList;
    private LayoutInflater inflater;
    private Context context;
	private LocalStorage localStorage;

	private OnTaskRemovedListener onTaskRemovedListener;

    public OverviewTaskAdapter(Context context, ArrayList<Task> overviewTaskList)
    {
        super(context, R.layout.item_task_active, overviewTaskList);

        this.context = context;
        this.overviewTaskList = overviewTaskList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	private class ViewHolder
    {
		// the outer layout that listens clicks
		public RelativeLayout taskItemLayout;
        // task's title such as "Third Homework"
		public TextView taskTitle;
        // more info about the task such as "Letter to His Papa"
		public TextView taskDescription;
        // when the task is due to, such as "Tue, 15 May\n 10.30 - 12.30"
		public TextView taskDate;
        // checkbox of the task
		public CheckBox checkbox;

        public ImageView alertIcon;

		public ImageButton menuButton;

		public BareListView tagsList;

		public TextView courseLabel;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = null;

        int layoutId;

        if(getItemViewType(position) == 1)
        {
            layoutId = R.layout.item_task_active;
        }
        else if(getItemViewType(position) == 2)
        {
            layoutId = R.layout.item_task_overdue;
        }
        else if( getItemViewType(position) == 3)
        {
            layoutId = R.layout.item_task_complete;
        }
        else
        {
            layoutId = R.layout.item_task_complete;
        }

        if( convertView == null)
        {
            view = inflater.inflate( layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();

            // getItemViewType method is used to determine which type of task the current task is
            // Task's property (int) status is used to determine it, 3 different processes are
            // carried out according to their statuses
            if(getItemViewType(position) == 1)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox);
            }
            // This one is an overdue task so it has an extra property added to the viewholder, the alert icon...
            else if(getItemViewType(position) == 2)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox);
                viewHolder.alertIcon = (ImageView) view.findViewById(R.id.task_item_alert_icon);
            }
            else if(getItemViewType(position) == 3)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox);
            }

			viewHolder.taskItemLayout = (RelativeLayout) view.findViewById(R.id.task_item_layout);
			viewHolder.menuButton = (ImageButton) view.findViewById(R.id.task_item_expand_icon);

            // Each item is tagged
			view.setTag(R.id.overview_task_item_object,viewHolder);

            // OnClickListener set for checkboxes
			if(viewHolder.checkbox != null)
            {
                viewHolder.checkbox.setOnClickListener(this);
            }

			viewHolder.taskItemLayout.setOnClickListener(this);
			viewHolder.menuButton.setOnClickListener(this);

            // Tag bar below the task item is represented as an instance of BareListView
			viewHolder.tagsList = (BareListView) view.findViewById(R.id.task_item_tag_bar);
			viewHolder.tagsList.setHeaderVisible(false);
			viewHolder.tagsList.setFooterVisible(false);
			viewHolder.tagsList.setOrientation(LinearLayout.HORIZONTAL);

			viewHolder.courseLabel = (TextView) view.findViewById(R.id.task_item_course);
        }
        else
        {
            view = convertView;
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.overview_task_item_object);

		viewHolder.menuButton.setTag(R.id.task_item_position, position);
		viewHolder.taskItemLayout.setTag(R.id.task_item_position, position);
		viewHolder.checkbox.setTag(R.id.task_item_position, position);

		// item is set to the given index of the CoursesItem arraylist
        Task item = overviewTaskList.get(position);

		// set tags for the outer layout. we won't be using it at the moment though
        view.setTag(R.id.overview_task_item_id, item.getId());
        view.setTag(R.id.overview_task_item_index, position);

        viewHolder.taskTitle.setText( item.getTaskName());
        viewHolder.taskDescription.setText( item.getTaskDesc());
		if(Utils.isDateToday(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			viewHolder.taskDate.setText(
					context.getString(R.string.today_capital) + "\n" +
					dateFormat.format(new Date((long)item.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)item.getEndDate()*1000))
			);
		}
		else if(Utils.isDateYesterday(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			viewHolder.taskDate.setText(
					context.getString(R.string.yesterday_capital) + "\n" +
					dateFormat.format(new Date((long)item.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)item.getEndDate()*1000))
			);
		}
		else if(Utils.isDateTomorrow(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			viewHolder.taskDate.setText(
					context.getString(R.string.tomorrow_capital) + "\n" +
					dateFormat.format(new Date((long)item.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)item.getEndDate()*1000))
			);
		}
		else
		{
			Date beginDate = new Date((long)item.getBeginDate()*1000);
			Date endDate = new Date((long)item.getEndDate()*1000);
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.setTime(beginDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);

			if(beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				viewHolder.taskDate.setText(
						dateFormat.format(beginDate) + "\n" +
							timeFormat.format(beginDate) + " - " + timeFormat.format(endDate)
				);
			}
			else
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
				viewHolder.taskDate.setText(
						dateFormat.format(new Date((long) item.getBeginDate() * 1000)) + "-\n" +
								dateFormat.format(new Date((long) item.getEndDate() * 1000))
				);
			}
		}

		TaskItemTagsListAdapter tagsListAdapter = TaskItemTagsListAdapter.fromCoreTags(
				getContext(), R.layout.item_task_tag, overviewTaskList.get(position).getTags()
		);

		viewHolder.tagsList.setAdapter(tagsListAdapter);

		if(item.getCourseCodeCombined().isEmpty())
		{
			viewHolder.courseLabel.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.courseLabel.setVisibility(View.VISIBLE);
			viewHolder.courseLabel.setText(item.getCourse().getDepartmentCode()+" "+item.getCourse().getCourseCode());
			if(item.getCourse().getCourseColor() != 0)
			{
				viewHolder.courseLabel.setTextColor(item.getCourse().getCourseColor());
			}
			else
			{
				viewHolder.courseLabel.setTextColor(getContext().getResources().getColor(R.color.task_item_course_default));
			}
		}

        return view;
    }

    @Override
    public void onClick(final View view)
    {
        // If the task item is clicked upon the details for that task are shown in a new activity
		if(view.getId() == R.id.task_item_layout)
		{
			try
			{
				Intent openTaskDetailsIntent = new Intent(context, TaskDetailActivity.class);
				openTaskDetailsIntent.putExtra(
						context.getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID),
						overviewTaskList.get(getPositionByTaskItem(view)).getLocalId());
				context.startActivity(openTaskDetailsIntent);
			}
			catch(Exception e)
			{
				Utils.log.w("could not get position from view");
				e.printStackTrace();
			}
		}
        // Expand icon is clicked, user will see some options about the task
		else if(view.getId() == R.id.task_item_expand_icon)
		{
			PopupMenu menu = new PopupMenu(getContext(), view);
			menu.getMenuInflater().inflate(R.menu.task_item_overflow_menu, menu.getMenu());

			menu.show();

			// ugly inline click listener. no other choice
			menu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					switch(item.getItemId())
					{
                        // The task will be edited
						case R.id.menu_task_edit:
							// user pressed edit menu item
							openTaskForEdit(getPositionByTaskItem(view));
							return true;
                        // The task will be deleted
						case R.id.menu_task_delete:
							// user pressed delete menu item
							removeTask(getPositionByTaskItem(view));
							return true;
					}
					return false;
				}
			});
		}
		else if(view.getId() == R.id.task_item_checkbox)
		{
			try
			{
				CheckBox checkBox = (CheckBox) view;
				int position = (Integer) view.getTag(R.id.task_item_position);

				Task task = overviewTaskList.get(position);
				int status = task.getStatus();

				// do it in a simple way.
				if (status == Task.STATE_COMPLETED)
				{
					task.setStatus(Task.STATE_ACTIVE);
					checkBox.setChecked(false);
				}
				else
				{
					task.setStatus(Task.STATE_COMPLETED);
					checkBox.setChecked(true);
				}

				// update the task in the database!
				if(localStorage != null) {
					task.setDb(localStorage.getDb());
					task.update();
				}
				else
				{
					Utils.log.w("cannot update Task because LocalStorage is null");
				}

				// TODO: sync

				notifyDataSetChanged();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
    }

    @Override
    public int getItemViewType(int position)
    {
        // status deleted
        if(getItem(position).getStatus() == Task.STATE_DELETED)
        {
            return 0;
        }
		// status overdue
		else if(getItem(position).getStatus() == Task.STATE_ACTIVE &&
				Utils.getUnixTimestamp() > getItem(position).getEndDate())
		{
			return 2;
		}
        // status active
        else if(getItem(position).getStatus() == Task.STATE_ACTIVE)
        {
            return 1;
        }
        // status archived
        else if(getItem(position).getStatus() == Task.STATE_ARCHIVED ||
				getItem(position).getStatus() == Task.STATE_COMPLETED)
        {
            return 3;
        }
        else
        {
            return 4;
        }
    }

    @Override
    public int getViewTypeCount()
    {
        return 5;
    }

	public int getPositionByTaskItem(View view)
	{
		return (Integer) view.getTag(R.id.task_item_position);
	}

	public void openTaskForEdit(int position)
	{
		long localId = overviewTaskList.get(position).getLocalId();
		Intent editIntent = new Intent(context, EditTaskActivity.class);
		editIntent.putExtra(context.getString(R.string.INTENT_DETAILS_TASK_LOCAL_ID), localId);

		context.startActivity(editIntent);
	}

	public void removeTask(int position)
	{
		try
		{
			Task toBeRemoved = overviewTaskList.get(position);
			if (toBeRemoved != null && toBeRemoved.getLocalId() != 0 && localStorage != null) {
				toBeRemoved.setDb(localStorage.getDb());
				toBeRemoved.remove();
				toBeRemoved.cancelReminders(context);

				overviewTaskList.remove(position);
				notifyDataSetChanged();

				// delete potential history item
				CloneHistoryItem historyItem = new CloneHistoryItem(localStorage.getDb());
				historyItem.setCloneLocalId(toBeRemoved.getLocalId());
				historyItem.remove();

				// display an informative toast
				Toast.makeText(context, context.getString(R.string.task_removed), Toast.LENGTH_SHORT).show();

				if (onTaskRemovedListener != null) {
					onTaskRemovedListener.onTaskRemoved(position, toBeRemoved);
				} else
					Utils.log.d("ontaskremovedlistener null");
			} else {
				Utils.log.w("oops");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setLocalStorage(LocalStorage storage)
	{
		localStorage = storage;
	}

	public interface OnTaskRemovedListener
	{
		public void onTaskRemoved(int position, Task task);
	}

	public void setOnTaskRemovedListener(OnTaskRemovedListener listener)
	{
		onTaskRemovedListener = listener;
	}

}
