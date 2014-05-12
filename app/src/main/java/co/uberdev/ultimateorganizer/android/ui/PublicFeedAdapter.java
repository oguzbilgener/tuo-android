package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.CloneHistoryItem;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.Utils;

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
        protected ImageButton taskAcceptIcon;
        // so that the task will not be seen in the public feed list
        protected ImageButton taskRejectIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
		Utils.log.d("getView "+position);
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
            viewHolder.taskAcceptIcon = (ImageButton) view.findViewById(R.id.item_public_feed_accept_icon);
            viewHolder.taskRejectIcon = (ImageButton) view.findViewById(R.id.item_public_feed_reject_icon);

            view.setTag(R.id.taskitem_object,viewHolder);
        }
        else
        {
            view = convertView;
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
        // DO NOT FORGET TO ADD STATUS ICON FOR THE USER
//        viewHolder.taskOwnerStatusIcon
        viewHolder.taskAcceptIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_accept_dark));
        viewHolder.taskRejectIcon.setImageDrawable( getContext().getResources().getDrawable( R.drawable.ic_action_remove));

		viewHolder.taskAcceptIcon.setTag(R.id.task_item_position, position);
		viewHolder.taskAcceptIcon.setOnClickListener(this);

		viewHolder.taskRejectIcon.setTag(R.id.task_item_position, position);
		viewHolder.taskRejectIcon.setOnClickListener(this);

		if(item.getCourse().getCourseColor() != 0)
		{
			Utils.log.w(item.getCourse().getCourseColor()+"");
			viewHolder.courseName.setTextColor(item.getCourse().getCourseColor());
		}

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

        return view;
    }

    @Override
    public void onClick(View view)
	{
		if(view.getId() == R.id.item_public_feed_accept_icon)
		{
			try
			{
				int position = (Integer) view.getTag(R.id.task_item_position);
				Task task = publicFeedTasksList.get(position);

				Intent cloneIntent = new Intent(getContext(), EditTaskActivity.class);
				cloneIntent.putExtra(getContext().getString(R.string.INTENT_DETAILS_TASK_JSON_OBJECT), task.asJsonString());
				cloneIntent.putExtra(getContext().getString(R.string.INTENT_FROM_PUBLIC_FEED), true);
				getContext().startActivity(cloneIntent);
			}
			catch(Exception e)
			{
				Toast.makeText(getContext(), getContext().getString(R.string.oops), Toast.LENGTH_SHORT).show();;
				e.printStackTrace();
			}
		}
		else if(view.getId() == R.id.item_public_feed_reject_icon)
		{
			try
			{
				int position = (Integer) view.getTag(R.id.task_item_position);
				Task taskToReject = publicFeedTasksList.get(position);
				// get a sqlite object in the easiest way
				SQLiteDatabase db = taskToReject.getDb();
				CloneHistoryItem rejectedItem = new CloneHistoryItem(db);
				rejectedItem.setOriginalId(taskToReject.getId());
				rejectedItem.insert();

				// simply remove here
				publicFeedTasksList.remove(position);
				notifyDataSetChanged();
			}
			catch(Exception e)
			{
				Toast.makeText(getContext(), getContext().getString(R.string.oops), Toast.LENGTH_SHORT).show();;
				e.printStackTrace();
			}
	}
    }
}
