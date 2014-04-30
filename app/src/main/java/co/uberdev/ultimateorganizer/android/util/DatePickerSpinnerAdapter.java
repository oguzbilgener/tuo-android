package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by begum on 08/04/14.
 */

public class DatePickerSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

	private Calendar calendar;
	private Format dateFormat;
	private LayoutInflater inflater;
	public static final int resourceId = R.layout.date_picker_display;


	public static DatePickerSpinnerAdapter newInstance(Context context)
	{
		ArrayList<String> items = new ArrayList<String>();
		items.add("test");
		return new DatePickerSpinnerAdapter(context, items);
	}

	private DatePickerSpinnerAdapter(Context context, ArrayList<String> items)
	{
		super(context, resourceId, R.id.overview_spinner_item_text, items);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dateFormat = android.text.format.DateFormat.getDateFormat(context);
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

		// Use Android's localized date format
		SimpleDateFormat sdf = new SimpleDateFormat(((SimpleDateFormat) dateFormat).toLocalizedPattern());
		titleText.setText(sdf.format(calendar.getTime()));


		return view;
	}
}
