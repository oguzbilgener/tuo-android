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
import java.util.List;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by begum on 28/04/14.
 */
public class CalendarSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private Date date;
    private Calendar calendar;
    public static final int resourceId = R.layout.calendar_spinner_item;

    public CalendarSpinnerAdapter(Context context, ArrayList<String> list)
    {
        super(context, resourceId, R.id.calendar_spinner_item_text, list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;

        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    private class ViewHolder
    {
        TextView text;
        TextView date;
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
        if(getItemViewType(position) == 0)
        {
            view = inflater.inflate(resourceId, parent, false);
            TextView text = (TextView) view.findViewById(R.id.calendar_spinner_item_text);
            TextView dateText = (TextView) view.findViewById(R.id.calendar_spinner_item_date);
            text.setText(list.get(position));

            // Might be wrong to set the date here. Couldn't think od another way, though.
            // These are int values right now. Need to work on them.
            switch (position) {
                case 0:
                    dateText.setText(calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 1:
                    dateText.setText(calendar.get(Calendar.WEEK_OF_MONTH));
                    break;
                case 2:
                    dateText.setText(calendar.get(Calendar.MONTH));
                    break;
                case 3:
                    dateText.setText(calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
                    break;
            }


            if(isOpen)
            {
                text.setTextColor(Color.parseColor("#000000"));
            }
            else
            {
                text.setTextColor(Color.parseColor("#ffffff"));
            }
        }
        else
        {
            view = inflater.inflate(resourceId, parent, false);
            TextView text = (TextView) view.findViewById(R.id.calendar_spinner_item_text);
            TextView date = (TextView) view.findViewById(R.id.calendar_spinner_item_date);
            text.setText("t");
            date.setText("d");
        }

        return view;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position < list.size()-1 ? 0 : 1;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }


}
