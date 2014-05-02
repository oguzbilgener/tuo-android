package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;

/**
 * Created by dunkuCoder on 02/05/2014
 * OverviewTaskAdapter, to display the created tasks on Overview activity.
 */
public class OverviewTaskAdapter extends ArrayAdapter<Task> implements View.OnClickListener
{
    ArrayList<Task> overviewTaskList;
    LayoutInflater inflater;
    Context context;

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;

        int layoutId = R.layout.item_task_active;

        if( convertView == null)
        {
            view = inflater.inflate( layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.taskTitle = (TextView) view.findViewById(R.id.task_item_task_title_active);
            viewHolder.taskDescription = (TextView) view.findViewById(R.id.task_item_description_active);
            viewHolder.taskDate = (TextView) view.findViewById(R.id.task_item_date);
            view.setTag(R.id.task_item_active_object,viewHolder);

            Utils.log.d( "hmm null");
        }
        else
        {
            view = convertView;

            Utils.log.d( "hmm not null");
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.task_item_active_object);

        // item is set to the given index of the CoursesItem arraylist
        Task item = overviewTaskList.get(position);

        view.setTag(R.id.task_item_active_id, item.getId());
        view.setTag(R.id.task_item_active_index, position);

        viewHolder.taskTitle.setText( item.getTaskName());
        viewHolder.taskDescription.setText( item.getTaskDesc());
        viewHolder.taskDate.setText(item.getEndDate() + "");
        // DO NOT FORGET TO ADD STATUS ICON FOR THE USER
//        viewHolder.taskOwnerStatusIcon
//        viewHolder.taskAcceptIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_accept_dark));
//        viewHolder.taskRejectIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_remove));

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
