package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.BareListDataDelegate;
import co.uberdev.ultimateorganizer.android.util.BareListDataListener;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by oguzbilgener on 02/05/14.
 */
public class SubTasksListAdapter extends ArrayAdapter<Task>
		implements BareListDataDelegate, View.OnClickListener
{
	private Context context;
	private List<Task> items;
	private LayoutInflater inflater;
	private int resourceId;

	private BareListDataListener dataListener;

	private OnItemRemoveClickListener itemRemoveClickListener;

	public SubTasksListAdapter(Context context, int resource, List<Task> objects)
	{
		super(context, resource, objects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceId = resource;
		items = objects;

		setNotifyOnChange(true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);

		ImageButton removeButton =  (ImageButton) itemView.findViewById(R.id.button_remove_sub_task);
		// save position info in button to distinguish remove button clicks
		removeButton.setTag(R.id.add_task_sub_task_remove_index, position);
		// listen for remove button clicks first, distinguish them and pass them to the listener
		removeButton.setOnClickListener(this);

		TextView subTaskTitleView = (TextView) itemView.findViewById(R.id.sub_task_title);
		subTaskTitleView.setText(items.get(position).getTaskName());

		TextView subTaskDateView = (TextView) itemView.findViewById(R.id.sub_task_date);

		// copy-paste code :(
		if(Utils.isDateToday(items.get(position).getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			subTaskDateView.setText(
					context.getString(R.string.today_capital) + " " +
							dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
			);
		}
		else if(Utils.isDateYesterday(items.get(position).getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			subTaskDateView.setText(
					context.getString(R.string.yesterday_capital) + " " +
							dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
			);
		}
		else if(Utils.isDateTomorrow(items.get(position).getBeginDate()))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			subTaskDateView.setText(
					context.getString(R.string.tomorrow_capital) + " " +
							dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
			);
		}
		else
		{
			Date beginDate = new Date((long)items.get(position).getBeginDate()*1000);
			Date endDate = new Date((long)items.get(position).getBeginDate()*1000);
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.setTime(beginDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);

			if(beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				subTaskDateView.setText(
						dateFormat.format(beginDate) + " " +
								timeFormat.format(beginDate) + " - " + timeFormat.format(endDate)
				);
			}
			else
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
				subTaskDateView.setText(
						dateFormat.format(new Date((long) items.get(position).getBeginDate() * 1000)) + " - " +
								dateFormat.format(new Date((long) items.get(position).getEndDate() * 1000))
				);
			}
		}


		return itemView;
	}

	@Override
	public void setDataListener(BareListDataListener dataListener)
	{
		this.dataListener = dataListener;
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
		if(dataListener != null)
		{
			dataListener.onDataSetChanged();
		}
	}

	public void setItemRemoveClickListener(OnItemRemoveClickListener itemRemoveClickListener)
	{
		this.itemRemoveClickListener = itemRemoveClickListener;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.button_remove_sub_task)
		{
			try
			{
				int position = (Integer)v.getTag(R.id.add_task_sub_task_remove_index);

				// pass this event to the ItemRemoveClickListener, if exists.
				if(itemRemoveClickListener != null)
				{
					itemRemoveClickListener.onSubTaskRemoveClick(v, position);
				}
			}
			catch(Exception e)
			{
				// button has no tag. do nothing.
			}
		}
	}


	public interface OnItemRemoveClickListener
	{
		public void onSubTaskRemoveClick(View view, int position);
	}
}