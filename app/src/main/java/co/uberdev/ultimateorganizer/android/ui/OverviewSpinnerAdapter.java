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
import co.uberdev.ultimateorganizer.android.util.OverviewNavigationItem;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by begum on 08/04/14.
 */

public class OverviewSpinnerAdapter extends ArrayAdapter implements SpinnerAdapter {

    private ArrayList<OverviewNavigationItem> list = new ArrayList<OverviewNavigationItem>();
    private LayoutInflater inflater;
    public static final int resourceId = R.layout.overview_spinner_item;

    public OverviewSpinnerAdapter(Context context, ArrayList<OverviewNavigationItem> list)
	{
        super(context, resourceId, list);

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
        if(position >= list.size())
            return null;

        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.overview_spinner_item, parent, false);

            holder = new ViewHolder();
			try {
				holder.text = (TextView) convertView.findViewById(resourceId);
			}catch(Exception e) {
				Utils.log.d("hoooo"); e.printStackTrace();}
            convertView.setTag(R.id.overview_spinner_holder, holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag(R.id.overview_spinner_holder);
        }
		if(holder.text != null)
        	holder.text.setText(list.get(position).title);

        return convertView;
    }
}
