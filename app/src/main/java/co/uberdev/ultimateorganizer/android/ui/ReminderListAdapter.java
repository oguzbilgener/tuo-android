package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by oguzbilgener on 02/05/14.
 */
public class ReminderListAdapter extends ArrayAdapter
{
	private Context context;
	private List<?> items;
	private LayoutInflater inflater;
	private int resourceId;
	public ReminderListAdapter(Context context, int resource, List<?> objects)
	{
		super(context, resource, objects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceId = resource;
		items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);

		Spinner reminderDateSpinner = (Spinner) itemView.findViewById(R.id.spinner_reminder_date);
		Spinner reminderTypeSpinner = (Spinner) itemView.findViewById(R.id.spinner_reminder_type);

		reminderDateSpinner.setAdapter(new ReminderDateSpinnerAdapter(context, android.R.layout.simple_list_item_1, new ArrayList<String>()));
		reminderTypeSpinner.setAdapter(new ReminderTypeSpinnerAdapter(context, android.R.layout.simple_list_item_1, new ArrayList<String>()));

		return itemView;
	}

	public class ReminderDateSpinnerAdapter extends ArrayAdapter<String>
	{

		public ReminderDateSpinnerAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
		}
	}

	public class ReminderTypeSpinnerAdapter extends ArrayAdapter<String>
	{

		public ReminderTypeSpinnerAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
		}
	}
}
