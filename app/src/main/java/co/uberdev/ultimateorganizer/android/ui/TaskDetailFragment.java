package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.BareListView;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by ozgur on 06.05.2014.
 */

public class TaskDetailFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private TextView taskTitle;
    private TextView taskDescription;
    private TextView taskDate;
    private ListView taskRelatedTasks;
    private BareListView taskRelatedTags;

    public Task shownTask;
    private RelativeLayout taskNameLayout;

    public TaskItemTagsListAdapter tagsListAdapter;
    public RelatedTasksListAdapter relatedTasksListAdapter;

    public TaskDetailFragment()
    {

    }

    public static TaskDetailFragment newInstance(Context context, Task shownTask)
    {
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.shownTask = shownTask;
        // Store task json string in arguments just in case a fragment is resurrected from background
        String taskJsonStr = shownTask.asJsonString();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.ARGS_TASK_JSON_OBJECT), taskJsonStr);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance()
    {
        return new TaskDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_task_detail, container, false);


        taskNameLayout = (RelativeLayout) rootView.findViewById(R.id.task_name_layout);
        taskTitle = (TextView) rootView.findViewById(R.id.task_title);
        taskDescription = (TextView) rootView.findViewById(R.id.task_description);
        taskDate = (TextView) rootView.findViewById(R.id.task_date);
        taskRelatedTasks = (ListView) rootView.findViewById(R.id.task_detail_related_tasks_listview);
        taskRelatedTags = (BareListView) rootView.findViewById(R.id.task_detail_tags_list);

        taskTitle.setText(shownTask.getTaskName());
        taskDescription.setText(shownTask.getTaskDesc());

        if ( shownTask.getCourse() != null && shownTask.getCourse().getCourseColor() != 0)
        {
            taskNameLayout.setBackgroundColor(shownTask.getCourse().getCourseColor());
        }

        if(Utils.isDateToday(shownTask.getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            taskDate.setText(
                    getString(R.string.today_capital) + "\n" +
                            dateFormat.format(new Date((long)shownTask.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)shownTask.getEndDate()*1000))
            );
        }
        else if(Utils.isDateYesterday(shownTask.getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            taskDate.setText(
                    getString(R.string.yesterday_capital) + "\n" +
                            dateFormat.format(new Date((long)shownTask.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)shownTask.getEndDate()*1000))
            );
        }
        else if(Utils.isDateTomorrow(shownTask.getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            taskDate.setText(
                    getString(R.string.tomorrow_capital) + "\n" +
                            dateFormat.format(new Date((long)shownTask.getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)shownTask.getEndDate()*1000))
            );
        }
        else
        {
            Date beginDate = new Date((long)shownTask.getBeginDate()*1000);
            Date endDate = new Date((long)shownTask.getEndDate()*1000);
            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(beginDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);

            if(beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                taskDate.setText(
                        dateFormat.format(beginDate) + "\n" +
                                timeFormat.format(beginDate) + " - " + timeFormat.format(endDate)
                );
            }
            else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
                taskDate.setText(
                        dateFormat.format(new Date((long) shownTask.getBeginDate() * 1000)) + "-\n" +
                                dateFormat.format(new Date((long) shownTask.getEndDate() * 1000))
                );
            }
        }

        return rootView;
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{

	}
}
