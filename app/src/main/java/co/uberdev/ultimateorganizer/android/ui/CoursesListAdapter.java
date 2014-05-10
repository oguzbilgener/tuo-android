package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by mozart on 23/04/14.
 */
public class CoursesListAdapter extends ArrayAdapter<Course> implements View.OnClickListener
{
    ArrayList<Course> coursesList;
    LayoutInflater inflater;
    Context context;

    public CoursesListAdapter(Context context, ArrayList<Course> coursesList)
    {
        super(context, R.layout.item_courses_list, coursesList);

        this.context = context;
        this.coursesList = coursesList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public static class ViewHolder
    {
        protected TextView courseName;
        protected TextView courseCode;
        protected TextView courseInstructor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;

        int layoutId = R.layout.item_courses_list;

        if( convertView == null)
        {
            view = inflater.inflate( layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.courseName = (TextView) view.findViewById(R.id.schedule_course_name);
            viewHolder.courseCode = (TextView) view.findViewById(R.id.schedule_course_code);
            viewHolder.courseInstructor = (TextView) view.findViewById(R.id.schedule_course_instructor);

            view.setTag(R.id.courseitem_object,viewHolder);
         }
        else
        {
            view = convertView;

        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.courseitem_object);

        // item is set to the given index of the CoursesItem arraylist
        Course item = coursesList.get(position);

        view.setTag(R.id.courseitem_id, item.getId());
        view.setTag(R.id.courseitem_index, position);

        viewHolder.courseCode.setText(item.getCourseCodeCombined());
        viewHolder.courseName.setText(item.getCourseTitle());
		Utils.log.d("color"+item.getCourseColor());
		if(item.getCourseColor() != 0)
		{
			viewHolder.courseName.setTextColor(item.getCourseColor());
			viewHolder.courseCode.setTextColor(item.getCourseColor());
		}

        viewHolder.courseInstructor.setText(item.getInstructorName());

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
