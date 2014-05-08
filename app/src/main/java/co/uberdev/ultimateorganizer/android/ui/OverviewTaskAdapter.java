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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.BareListView;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreTask;

/**
 * Created by dunkuCoder on 02/05/2014
 * OverviewTaskAdapter, to display the created tasks on Overview activity.
 */
public class OverviewTaskAdapter extends ArrayAdapter<Task> implements View.OnClickListener
{
    ArrayList<Task> overviewTaskList;
    LayoutInflater inflater;
    Context context;
    View.OnClickListener checkboxListener;

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

            if(getItemViewType(position) == 1)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox);
            }
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

			view.setTag(R.id.overview_task_item_object,viewHolder);
			viewHolder.menuButton.setTag(R.id.task_item_position, position);

			if(viewHolder.checkbox != null)
            	viewHolder.checkbox.setOnClickListener( checkboxListener);

			viewHolder.taskItemLayout.setOnClickListener(this);
			viewHolder.menuButton.setOnClickListener(this);

			viewHolder.tagsList = (BareListView) view.findViewById(R.id.task_item_tag_bar);
			viewHolder.tagsList.setHeaderVisible(false);
			viewHolder.tagsList.setFooterVisible(false);
			viewHolder.tagsList.setOrientation(LinearLayout.HORIZONTAL);
        }
        else
        {
            view = convertView;
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.overview_task_item_object);

		viewHolder.taskItemLayout.setTag(R.id.task_item_position, position);

		// item is set to the given index of the CoursesItem arraylist
        Task item = overviewTaskList.get(position);

		// set tags for the outer layout. we won't be using it at the moment though
        view.setTag(R.id.overview_task_item_id, item.getId());
        view.setTag(R.id.overview_task_item_index, position);

		// TODO: move this click listener outside.
        checkboxListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getItemViewType(position) == 1)
                {
                    try {
                        getItem(position).setStatus(3);
                    }
                    catch(CoreTask.BadStateException e)
                    {
                        e.printStackTrace();
                    }
                    viewHolder.checkbox.setChecked(true);
                }
                else if(getItemViewType(position) == 2)
                {
                    try{
                        getItem(position).setStatus(3);
                    }
                    catch(CoreTask.BadStateException e)
                    {
                        e.printStackTrace();
                    }
                    viewHolder.checkbox.setChecked(true);
                }
                else if( getItemViewType(position) == 3)
                {
                    try{
                        getItem(position).setStatus(1);
                    }
                    catch(CoreTask.BadStateException e)
                    {
                        e.printStackTrace();
                    }
                    viewHolder.checkbox.setChecked(false);
                }
            }
        };

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
			Date endDate = new Date((long)item.getBeginDate()*1000);
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

        return view;
    }

    @Override
    public void onClick(final View view)
    {
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
						case R.id.menu_task_edit:
							// user pressed edit menu item
							openTaskForEdit(getPositionByTaskItem(view));
							return true;

						case R.id.menu_task_delete:
							// user pressed delete menu item
                            // TODO add the method to delete the item
							return false;
					}
					return false;
				}
			});
		}
    }

    @Override
    public int getItemViewType(int position)
    {
        // status deleted
        if(getItem(position).getStatus() == 0)
        {
            return 0;
        }
        // status active
        else if(getItem(position).getStatus() == 1)
        {
            return 1;
        }
        // status overdue
        else if(getItem(position).getStatus() == 2)
        {
            return 2;
        }
        // status archived
        else if(getItem(position).getStatus() == 3)
        {
            return 3;
        }
        else
        {
            Utils.log.d( "hmmmmmm 4 ?!");
            return 4;
        }
    }

    @Override
    public int getViewTypeCount()
    {
        return 4;
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

}
