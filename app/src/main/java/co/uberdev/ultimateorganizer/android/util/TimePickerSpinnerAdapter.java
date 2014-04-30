package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by begum on 08/04/14.
 */

public class TimePickerSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

	private Calendar calendar;
	private LayoutInflater inflater;
	public static final int resourceId = R.layout.time_picker_display;


	public static TimePickerSpinnerAdapter newInstance(Context context)
	{
		ArrayList<String> items = new ArrayList<String>();
		items.add("test");
		return new TimePickerSpinnerAdapter(context, items);
	}

	private TimePickerSpinnerAdapter(Context context, ArrayList<String> items)
	{
		super(context, resourceId, R.id.overview_spinner_item_text, items);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calendar = Calendar.getInstance();
	}

	public void updateCalendar(Calendar cal)
	{
		calendar = cal;
	}


	// The view to be displayed when the dropdown is closed
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getItemView(position,  parent, false);
	}

	// The view to be displayed when the dropdown is open
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return getItemView(position, parent, true);
	}

	public View getItemView(int position,  ViewGroup parent, boolean isOpen)
	{
		ViewGroup view;

		view = (ViewGroup) inflater.inflate(resourceId, parent, false);
		TextView titleText = (TextView) view.findViewById(R.id.time_picker_display_text);

		titleText.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));


		return view;
	}
}
