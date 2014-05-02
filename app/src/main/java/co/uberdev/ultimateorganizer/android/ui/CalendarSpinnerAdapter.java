package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by begum on 28/04/14.
 */
public class CalendarSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter
{
    private ArrayList<String> list;
    private LayoutInflater inflater;
    private Date date;
    private Calendar calendar;
    public static final int resourceId = R.layout.calendar_spinner_item;
    public static final int resourceIdSimple = R.layout.calendar_spinner_item_simple;

    public CalendarSpinnerAdapter(Context context, ArrayList<String> list)
    {
        super(context, resourceId, list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.list = list;

        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    // The view to be displayed when the dropdown is closed
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getItemView(position,  parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getItemView(position, parent, true);
    }

    public View getItemView(int position,  ViewGroup parent, boolean isOpen)
    {
        if(position >= list.size())
            return null;

        View view;

        int rid = resourceId;
        if(!isOpen)
            rid = resourceIdSimple;

        view = inflater.inflate(rid, parent, false);
        TextView text = (TextView) view.findViewById(R.id.calendar_spinner_item_text);
        TextView dateText = (TextView) view.findViewById(R.id.calendar_spinner_item_date);

        text.setText(list.get(position));

        switch (position) {
            case 0:
                dateText.setText(Utils.toMonthString(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.DAY_OF_MONTH));
                break;
            case 1:
                // :))))9)))
                Calendar weekStart = Calendar.getInstance();
                Calendar weekEnd = Calendar.getInstance();

                weekStart.setFirstDayOfWeek(Calendar.MONDAY);
                weekStart.set(Calendar.HOUR_OF_DAY, 0);
                weekStart.clear(Calendar.MILLISECOND);
                weekStart.clear(Calendar.MINUTE);
                weekStart.clear(Calendar.SECOND);
                weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());

                weekEnd.setFirstDayOfWeek(Calendar.MONDAY);
                weekEnd.set(Calendar.HOUR_OF_DAY, 0);
                weekEnd.clear(Calendar.MILLISECOND);
                weekEnd.clear(Calendar.MINUTE);
                weekEnd.clear(Calendar.SECOND);
                weekEnd.set(Calendar.DAY_OF_WEEK, weekEnd.getFirstDayOfWeek());
                weekEnd.add(Calendar.WEEK_OF_YEAR, 1);
                weekEnd.add(Calendar.DAY_OF_WEEK, -1);


                dateText.setText(Utils.toMonthString(weekStart.get(Calendar.MONTH)) + " "+ weekStart.get(Calendar.DAY_OF_MONTH) +
                    " - " + Utils.toMonthString(weekEnd.get(Calendar.MONTH)) + " " + weekEnd.get(Calendar.DAY_OF_MONTH));
                break;
            case 2:
                dateText.setText(Utils.toMonthString(calendar.get(Calendar.MONTH)));
                break;
            case 3:
                try {
                    dateText.setText(calendar.get(Calendar.YEAR));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }


        if(isOpen)
        {
            text.setTextColor(Color.parseColor("#000000"));
            dateText.setTextColor(Color.parseColor("#000000"));
        }
        else
        {
            text.setTextColor(Color.parseColor("#ffffff"));
            dateText.setTextColor(Color.parseColor("#ffffff"));
        }

        return view;
    }

	@Override
    public int getItemViewType(int position)
    {
        return 0;
    }


    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

}
