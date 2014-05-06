package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
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

    public static class ViewHolder
    {
        // task's title such as "Third Homework"
        protected TextView taskTitle;
        // more info about the task such as "Letter to His Papa"
        protected TextView taskDescription;
        // when the task is due to, such as "Tue, 15 May\n 10.30 - 12.30"
        protected TextView taskDate;
        // checkbox of the task
        protected CheckBox checkbox;
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
            Utils.log.d( "hmm else");
            layoutId = R.layout.item_task_complete;
        }

        if( convertView == null)
        {
            view = inflater.inflate( layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();

            if(getItemViewType(position) == 1)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title_active);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description_active);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox_active);
            }
            else if(getItemViewType(position) == 2)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title_overdue);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description_overdue);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox_overdue);
            }
            else if(getItemViewType(position) == 3)
            {
                viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title_complete);
                viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description_complete);
                viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.task_item_checkbox_complete);
            }
			view.setTag(R.id.overview_task_item_object,viewHolder);

			if(viewHolder.checkbox != null)
            	viewHolder.checkbox.setOnClickListener( checkboxListener);
        }
        else
        {
            view = convertView;
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.overview_task_item_object);

        // item is set to the given index of the CoursesItem arraylist
        Task item = overviewTaskList.get(position);

        view.setTag(R.id.overview_task_item_id, item.getId());
        view.setTag(R.id.overview_task_item_index, position);

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
		Utils.log.d("&&&&& "+(item.getEndDate()-item.getBeginDate()));
		if(Utils.isDateToday(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Utils.log.d(dateFormat.format(new Date(item.getBeginDate())));
			viewHolder.taskDate.setText(
					context.getString(R.string.today_capital) + "\n" +
					dateFormat.format(new Date(item.getBeginDate())) + "-" + dateFormat.format(new Date(item.getEndDate()))
			);
		}
		else if(Utils.isDateYesterday(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			viewHolder.taskDate.setText(
					context.getString(R.string.yesterday_capital) + "\n" +
					dateFormat.format(new Date(item.getBeginDate())) + "-" + dateFormat.format(new Date(item.getEndDate()))
			);
		}
		else if(Utils.isDateTomorrow(item.getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			viewHolder.taskDate.setText(
					context.getString(R.string.tomorrow_capital) + "\n" +
					dateFormat.format(new Date(item.getBeginDate())) + "-" + dateFormat.format(new Date(item.getEndDate()))
			);
		}
		else
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
			viewHolder.taskDate.setText(
					dateFormat.format(new Date(item.getBeginDate())) + "-\n" +
					dateFormat.format(new Date(item.getEndDate()))
			);
		}

        return view;
    }

    @Override
    public void onClick(View view)
    {

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
        return 5;
    }
}
