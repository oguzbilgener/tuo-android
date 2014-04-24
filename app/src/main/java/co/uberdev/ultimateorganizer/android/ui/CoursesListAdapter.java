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
public class CoursesListAdapter extends ArrayAdapter<CoreCourse> implements View.OnClickListener
{
    ArrayList<CoreCourse> coursesList;
    LayoutInflater inflater;
    Context context;

    public CoursesListAdapter(Context context, ArrayList<CoreCourse> coursesList)
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

        }
        else
        {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag(R.id.i);

        view.setTag(R.id.newsitem_id, item.getItemId());
        view.setTag(R.id.newsitem_index, position);

        viewHolder.titleText.setText(item.getTitle());
        viewHolder.username.setText(item.getUsername());
        viewHolder.domainText.setText(item.getDomain());
        viewHolder.pointsText.setText(item.getPoints());
        holder.commentsText.setText(item.getComments());

    }

    @Override
    public void onClick(View view) {

    }
}
