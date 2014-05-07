package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Reminder;
import co.uberdev.ultimateorganizer.android.util.BareListDataDelegate;
import co.uberdev.ultimateorganizer.android.util.BareListDataListener;

/**
 * Created by oguzbilgener on 02/05/14.
 */
public class ReminderListAdapter extends ArrayAdapter<Reminder>
		implements BareListDataDelegate, View.OnClickListener, AdapterView.OnItemSelectedListener
{
	private Context context;
	private List<Reminder> items;
	private LayoutInflater inflater;
	private int resourceId;

	private String[] dateKeys;
	private int[] dateValues;
	private String[] typeKeys;
	private int[] typeValues;

	private BareListDataListener dataListener;

	private OnItemRemoveClickListener itemRemoveClickListener;

	public ReminderListAdapter(Context context, int resource, List<Reminder> objects)
	{
		super(context, resource, objects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceId = resource;
		items = objects;

		dateKeys = context.getResources().getStringArray(R.array.reminder_before_event_keys);
		dateValues = context.getResources().getIntArray(R.array.reminder_before_event_values);
		typeKeys = context.getResources().getStringArray(R.array.reminder_types_keys);
		typeValues = context.getResources().getIntArray(R.array.reminder_types_values);

		setNotifyOnChange(true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);

		Spinner reminderDateSpinner = (Spinner) itemView.findViewById(R.id.spinner_reminder_date);
		Spinner reminderTypeSpinner = (Spinner) itemView.findViewById(R.id.spinner_reminder_type);

		ImageButton removeButton =  (ImageButton) itemView.findViewById(R.id.button_remove_reminder);
		// save position info in button to distinguish remove button clicks
		removeButton.setTag(R.id.add_task_reminder_remove_index, position);
		// listen for remove button clicks first, distinguish them and pass them to the listener
		removeButton.setOnClickListener(this);

		ReminderDateSpinnerAdapter dateAdapter = new ReminderDateSpinnerAdapter(context, android.R.layout.simple_list_item_1, position);
		ReminderTypeSpinnerAdapter typeAdapter = new ReminderTypeSpinnerAdapter(context, android.R.layout.simple_list_item_1, position);

		reminderDateSpinner.setAdapter(dateAdapter);
		reminderTypeSpinner.setAdapter(typeAdapter);

		reminderDateSpinner.setOnItemSelectedListener(this);
		reminderTypeSpinner.setOnItemSelectedListener(this);

		reminderDateSpinner.setTag(R.id.add_task_reminder_spinner_index, position);
		reminderTypeSpinner.setTag(R.id.add_task_reminder_spinner_index, position);

		reminderDateSpinner.setTag(R.id.add_task_reminder_spinner_type,
				context.getResources().getInteger(R.integer.reminder_type_date));
		reminderTypeSpinner.setTag(R.id.add_task_reminder_spinner_type,
				context.getResources().getInteger(R.integer.reminder_type_type));

		reminderTypeSpinner.setSelection(typeAdapter.getPositionByReminderIndex(position));
		reminderDateSpinner.setSelection(dateAdapter.getPositionByReminderIndex(position));

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
		if(v.getId() == R.id.button_remove_reminder)
		{
			try
			{
				int position = (Integer)v.getTag(R.id.add_task_reminder_remove_index);

				// pass this event to the ItemRemoveClickListener, if exists.
				if(itemRemoveClickListener != null)
				{
					itemRemoveClickListener.onReminderRemoveClick(v, position);
				}
			}
			catch(Exception e)
			{
				// button has no tag. do nothing.
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		try
		{
			int reminderIndex = (Integer) parent.getTag(R.id.add_task_reminder_spinner_index);
			int spinnerType = (Integer) parent.getTag(R.id.add_task_reminder_spinner_type);

			Reminder reminder = items.get(reminderIndex);

			if(spinnerType == context.getResources().getInteger(R.integer.reminder_type_date))
			{
				// nothing selected for date / gap spinner
				reminder.setGap(dateValues[position]);

				// Workaround for Android's calling this method late
				((TextView) view).setText(dateKeys[position]);
			}
			else
			{
				// nothing selected for type spinner
				if(position == context.getResources().getInteger(R.integer.reminder_type_silent))
				{
					reminder.setSound(false);
					reminder.setVibrate(false);
				}
				else if(position == context.getResources().getInteger(R.integer.reminder_type_vibration))
				{
					reminder.setSound(false);
					reminder.setVibrate(true);
				}
				else if(position == context.getResources().getInteger(R.integer.reminder_type_ring))
				{
					reminder.setSound(true);
					reminder.setVibrate(true);
				}

				// Workaround for Android's calling this method late
				((TextView) view).setText(typeKeys[position]);
			}
		}
		catch(Exception e)
		{
			// do nothing
			e.printStackTrace();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		try
		{
			int reminderIndex = (Integer) parent.getTag(R.id.add_task_reminder_spinner_index);
			int spinnerType = (Integer) parent.getTag(R.id.add_task_reminder_spinner_type);

			Reminder reminder = items.get(reminderIndex);

			if(spinnerType == R.integer.reminder_type_date)
			{
				// nothing selected for date / gap spinner
				reminder.setGap(0);
			}
			else
			{
				// nothing selected for type spinner
				reminder.setSound(false);
				reminder.setVibrate(false);
			}
		}
		catch(Exception e)
		{
			// do nothing
			e.printStackTrace();
		}
	}


	public class ReminderDateSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter
	{
		private int reminderIndex;

		public ReminderDateSpinnerAdapter(Context context, int resource, int index)
		{
			super(context, resource, dateKeys);
			reminderIndex = index;
		}

		// The view to be displayed when the dropdown is closed
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return getItemView(position, convertView, parent, false);
		}

		// The view to be displayed when the dropdown is open
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			return getItemView(position, convertView, parent, true);
		}

		public View getItemView(int position, View convertView, ViewGroup parent, boolean isOpen)
		{
			// just inflate a simple list item
			View view;
			TextView label;
			if(convertView == null)
			{
				view = (View) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
				label = (TextView) view.findViewById(android.R.id.text1);

				view.setTag(R.id.reminder_date_spinner_holder, label);
			}
			else
			{
				view = convertView;
				label = (TextView) convertView.getTag(R.id.reminder_date_spinner_holder);
			}

			label.setTag(R.id.reminder_label_tag, "lol");

			if(!isOpen)
			{
				label.setText(dateKeys[getPositionByReminderIndex(reminderIndex)]);
			}
			else
			{
				label.setText(dateKeys[position]);
			}

			return view;
		}

		public int getPositionByReminderIndex(int index)
		{
			for (int i = 0; i < dateValues.length; i++)
			{
				if (items.get(index).getGap() == dateValues[i])
				{
					return i;
				}
			}
			return 0;
		}

	}

	public class ReminderTypeSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter
	{
		private int reminderIndex;

		public ReminderTypeSpinnerAdapter(Context context, int resource, int index)
		{
			super(context, resource, typeKeys);
			reminderIndex = index;
		}

		// The view to be displayed when the dropdown is closed
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return getItemView(position, convertView, parent, false);
		}

		// The view to be displayed when the dropdown is open
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			return getItemView(position, convertView, parent, true);
		}

		public View getItemView(int position, View convertView, ViewGroup parent, boolean isOpen)
		{
			// just inflate a simple list item
			View view;
			TextView label;
			if(convertView == null)
			{
				view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
				label = (TextView) view.findViewById(android.R.id.text1);
				view.setTag(R.id.reminder_type_spinner_holder, label);
			}
			else
			{
				view = convertView;
				label = (TextView) convertView.getTag(R.id.reminder_type_spinner_holder);
			}

			label.setTag(R.id.reminder_label_tag, "lol");

			if(!isOpen)
			{
				Reminder item = items.get(reminderIndex);
				int x = getPositionByReminderIndex(reminderIndex);
				label.setText(typeKeys[getPositionByReminderIndex(reminderIndex)]);
			}
			else
			{
				label.setText(typeKeys[position]);
			}

			return view;
		}

		public int getPositionByReminderIndex(int index)
		{
			Reminder item = items.get(index);
			if (!item.isVibrate() && !item.isSound())
			{
				return context.getResources().getInteger(R.integer.reminder_type_silent);
			}
			else if (item.isVibrate() && item.isSound())
			{
				return context.getResources().getInteger(R.integer.reminder_type_ring);
			}
			else
			{
				return context.getResources().getInteger(R.integer.reminder_type_vibration);
			}
		}
	}

	public interface OnItemRemoveClickListener
	{
		public void onReminderRemoveClick(View view, int position);
	}
}