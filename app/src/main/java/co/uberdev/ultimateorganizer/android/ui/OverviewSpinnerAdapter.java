package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
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

    private ArrayList<String> list;
    private LayoutInflater inflater;
    public static final int resourceId = R.layout.overview_spinner_item;
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

	@Override
	 public View getView(int position, View convertView, ViewGroup parent)
	{
		return getItemView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return getItemView(position, convertView, parent);
	}

    public View getItemView(int position, View convertView, ViewGroup parent)
    {
        if(position >= list.size())
            return null;

		int resId;

		if(getItemViewType(position) == 0)
		{
			resId = resourceId;
		}
		else
		{
			resId = resourceIdAlt;
		}

        ViewHolder holder;

//        if(convertView == null)
//        {
            convertView = inflater.inflate(resId, parent, false);

            holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(resourceId);
//            convertView.setTag(R.id.overview_spinner_holder, holder);
//        }
//        else
//        {
//            holder = (ViewHolder) convertView.getTag(R.id.overview_spinner_holder);
//        }
		if(holder.text != null)
		{
			if(getItemViewType(position) == 0)
			{
				holder.text.setText(list.get(position));
			}
			else
			{
				holder.text.setText("last");
			}
		}

        return convertView;
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
