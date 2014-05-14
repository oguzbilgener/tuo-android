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

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by begum on 08/04/14.
 */

public class OverviewSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    // The list of the items that will be displayed on the spinner
    private ArrayList<String> list;
    private LayoutInflater inflater;
    public static final int resourceId = R.layout.overview_spinner_item;
	// TODO: create a different layout for this:
	public static final int resourceIdAlt = R.layout.overview_spinner_item;

    public OverviewSpinnerAdapter(Context context, ArrayList<String> list)
	{
        super(context, resourceId, R.id.overview_spinner_item_text, list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    private class ViewHolder
    {
        TextView text;
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
        if(position >= list.size())
            return null;

		View view;
		if(getItemViewType(position) == 0)
		{
			// According to the position, sets the text on the spinner to the corresponding element in the String array
            view = inflater.inflate(resourceId, parent, false);
			TextView titleText = (TextView) view.findViewById(R.id.overview_spinner_item_text);
			titleText.setText(list.get(position));

			// Parses the colors according to spinner's open/closed state
            if(isOpen)
			{
				titleText.setTextColor(Color.parseColor("#000000"));
			}
			else
			{
				titleText.setTextColor(Color.parseColor("#ffffff"));
			}
		}
		else
		{
			// the last item
			view = inflater.inflate(resourceIdAlt, parent, false);
			TextView titleText = (TextView) view.findViewById(R.id.overview_spinner_item_text);
			titleText.setText("t");
		}

       	return view;
    }

	/**
	 * The last item is a complex view
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position)
	{
		return position < list.size()-1 ? 0 : 1;
	}

	/**
	 * We have two different item types for this Spinner. One is a simple text item,
	 * the other one is a rather complex view
	 * @return
	 */
	@Override
	public int getViewTypeCount()
	{
		return 2;
	}
}
