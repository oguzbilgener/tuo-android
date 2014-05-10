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
import co.uberdev.ultimateorganizer.android.models.Course;

public class CourseSelectSpinnerAdapter extends ArrayAdapter<Course> implements SpinnerAdapter {

    private ArrayList<Course> courses;
    private LayoutInflater inflater;

    public CourseSelectSpinnerAdapter(Context context, ArrayList<Course> list)
	{
        super(context, android.R.layout.simple_list_item_2, list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.courses = list;
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
		View view;
		view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		TextView titleText = (TextView) view.findViewById(android.R.id.text1);
		TextView subText = (TextView) view.findViewById(android.R.id.text2);
        if(position == 0)
		{
			titleText.setText(getContext().getString(R.string.course_spinner_default));
			subText.setText("");
		}
		else
		{
			titleText.setText(courses.get(position).getCourseCodeCombined());
			subText.setText(courses.get(position).getCourseTitle());
			if(courses.get(position).getCourseColor() != 0)
			{
				titleText.setTextColor(courses.get(position).getCourseColor());
			}
		}

       	return view;
    }
}
