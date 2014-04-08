package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.database.DataSetObserver;
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

    private ArrayList<String> list = new ArrayList<String>();
    private LayoutInflater inflater;
    int resource;

    public OverviewSpinnerAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.list = list;
    }

    private class ViewHolder
    {
        TextView text;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(position >= list.size())
            return null;

        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.overview_spinner_item, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.overview_spinner_item_text);
            convertView.setTag(R.integer.overview_spinner_holder, holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag(R.integer.overview_spinner_holder);
        }

        holder.text.setText(list.get(position));

        return convertView;
    }
}
