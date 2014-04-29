package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;

/**
 * Created by mozart on 23/04/14.
 */
public class CoursesListAdapter extends ArrayAdapter<CourseItem> implements View.OnClickListener
{
    ArrayList<CourseItem> coursesList;
    LayoutInflater inflater;
    Context context;

    public CoursesListAdapter(Context context, ArrayList<CourseItem> coursesList)
    {
        super(context, R.layout.item_courses_list, coursesList);

        this.context = context;
        this.coursesList = coursesList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public static class ViewHolder
    {
        protected TextView courseName;
        protected TextView courseDescription;
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
            viewHolder.courseDescription = (TextView) view.findViewById(R.id.schedule_course_description);
            viewHolder.courseInstructor = (TextView) view.findViewById(R.id.schedule_course_instructor);

            view.setTag(R.id.courseitem_object,viewHolder);

            Utils.log.d( "hmm null");
         }
        else
        {
            view = convertView;

            Utils.log.d( "hmm not null");
        }

        // ViewHolder receives tags for the next items
        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.courseitem_object);

        // item is set to the given index of the CoursesItem arraylist
        CourseItem item = coursesList.get(position);

        view.setTag(R.id.courseitem_id, item.getItemId());
        view.setTag(R.id.courseitem_index, position);

        viewHolder.courseName.setText(item.getCourseName());
        viewHolder.courseDescription.setText(item.getCourseDescription());
        viewHolder.courseInstructor.setText(item.getCourseInstructor());

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
