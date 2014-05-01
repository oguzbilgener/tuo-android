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
 * Created by mozart on 23/04/14.
 */
public class PublicFeedAdapter extends ArrayAdapter<Task> implements View.OnClickListener
{
    ArrayList<Task> publicFeedTasksList;
    LayoutInflater inflater;
    Context context;

    public PublicFeedAdapter(Context context, ArrayList<Task> publicFeedTasksList)
    {
        super(context, R.layout.item_academic_network_public_feed, publicFeedTasksList);

        this.context = context;
        this.publicFeedTasksList = publicFeedTasksList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder
    {
        // courseName includes department code, course code, section code such as TURK102-13
        protected TextView courseName;
        // task's title such as "Third Homework"
        protected TextView taskTitle;
        // more info about the task such as "Letter to His Papa"
        protected TextView taskDescription;
        // who submitted the task such as "Ani Kristo"
        protected TextView taskOwner;
        // when the task is due to, such as "Tue, 15 May\n 10.30 - 12.30"
        protected TextView taskDate;

        // such as the user is verified or has a low rating
        protected ImageView taskOwnerStatusIcon;
        // simple tick icon that will add the task into  user's own tasks
        protected ImageView taskAcceptIcon;
        // so that the task will not be seen in the public feed list
        protected ImageView taskRejectIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;

        int layoutId = R.layout.item_academic_network_public_feed;

        if( convertView == null)
        {
            view = inflater.inflate( layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.courseName = (TextView) view.findViewById(R.id.item_public_feed_course_code);
            viewHolder.taskTitle = (TextView) view.findViewById(R.id.item_public_feed_task_name);
            viewHolder.taskDescription = (TextView) view.findViewById(R.id.item_public_feed_task_description);
            viewHolder.taskOwner = (TextView) view.findViewById(R.id.item_public_feed_owner);
            viewHolder.taskDate = (TextView) view.findViewById(R.id.item_public_feed_due_date);
            viewHolder.taskOwnerStatusIcon = (ImageView) view.findViewById(R.id.item_public_feed_low_rating_icon);
            viewHolder.taskAcceptIcon = (ImageView) view.findViewById(R.id.item_public_feed_accept_icon);
            viewHolder.taskRejectIcon = (ImageView) view.findViewById(R.id.item_public_feed_reject_icon);

            view.setTag(R.id.taskitem_object,viewHolder);

            Utils.log.d( "hmm null");
        }
        else
        {
            view = convertView;

            Utils.log.d( "hmm not null");
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.taskitem_object);

        // item is set to the given index of the CoursesItem arraylist
        Task item = publicFeedTasksList.get(position);

        view.setTag(R.id.taskitem_id, item.getId());
        view.setTag(R.id.taskitem_index, position);

        viewHolder.courseName.setText( item.getCourseCodeCombined());
        viewHolder.taskTitle.setText( item.getTaskName());
        viewHolder.taskDescription.setText( item.getTaskDesc());
        viewHolder.taskOwner.setText( "by " + item.getTaskOwnerNameCombined());
        viewHolder.taskDate.setText(item.getEndDate() + "");
        // DO NOT FORGET TO ADD STATUS ICON FOR THE USER
//        viewHolder.taskOwnerStatusIcon
        viewHolder.taskAcceptIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_accept_dark));
        viewHolder.taskRejectIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_remove));

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
