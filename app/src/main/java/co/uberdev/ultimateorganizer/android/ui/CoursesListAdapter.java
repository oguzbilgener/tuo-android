package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreCourse;

/**
 * Created by mozart on 23/04/14.
 */
public class CoursesListAdapter extends ArrayAdapter<CoreCourse>
{
    ArrayList<CoreCourse> coursesList;
    LayoutInflater inflater;

    public CoursesListAdapter(Context context, ArrayList<CoreCourse> coursesList)
    {
        super(context, R.layout.item_courses_list, coursesList);

        this.coursesList = coursesList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {


    }
}
