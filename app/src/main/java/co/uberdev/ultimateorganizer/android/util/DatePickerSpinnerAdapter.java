package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Calendar;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by begum on 08/04/14.
 */

public class DatePickerSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

	private Calendar calendar;
	private LayoutInflater inflater;
	public static final int resourceId = R.layout.date_picker_display;

	public DatePickerSpinnerAdapter(Context context)
	{
		super(context, resourceId, R.id.overview_spinner_item_text);

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
		TextView titleText = (TextView) view.findViewById(R.id.date_picker_display_text);
		// TODO: consider locales to print date
		titleText.setText(calendar.toString());


		return view;
	}
}
