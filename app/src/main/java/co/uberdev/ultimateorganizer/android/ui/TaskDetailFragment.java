package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.BareListView;

/**
 * Created by ozgur on 06.05.2014.
 */
public class TaskDetailFragment extends Fragment
{
    private TextView taskTitle;
    private TextView taskDescription;
    private TextView taskFromDate;
    private TextView taskToDate;
    private BareListView taskRelatedCourses;

    public TaskDetailFragment()
    {

    }

    public static Fragment newInstance()
    {
        return new TaskDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_task_detail, container, false);

        taskTitle = (TextView) rootView.findViewById(R.id.task_title);
        taskDescription = (TextView) rootView.findViewById(R.id.task_description);
        taskFromDate = (TextView) rootView.findViewById(R.id.task_from_date);
        taskToDate = (TextView) rootView.findViewById(R.id.task_to_date);
        taskRelatedCourses = (BareListView) rootView.findViewById(R.id.task_detail_tags_list);

        return rootView;
    }
}
