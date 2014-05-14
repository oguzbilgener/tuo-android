package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.util.BareListDataDelegate;
import co.uberdev.ultimateorganizer.android.util.BareListDataListener;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by dunkuCoder on 14/05/14.
 * RelatedTasksListAdapter is used to display related tasks of a task when the details for that task is displayed.
 */

public class RelatedTasksListAdapter extends ArrayAdapter<Task>
{
    private Context context;
    private List<Task> items;
    private LayoutInflater inflater;
    private int resourceId;


    public RelatedTasksListAdapter(Context context, int resource, List<Task> objects)
    {
        super(context, resource, objects);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceId = resource;
        items = objects;

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);


        TextView relatedTaskTitleView = (TextView) itemView.findViewById(R.id.related_task_title);
        relatedTaskTitleView.setText(items.get(position).getTaskName());

        TextView relatedTaskDateView = (TextView) itemView.findViewById(R.id.related_task_date);

        // copy-paste code :(
        if(Utils.isDateToday(items.get(position).getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            relatedTaskDateView.setText(
                    context.getString(R.string.today_capital) + "\n" +
                            dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
            );
        }
        else if(Utils.isDateYesterday(items.get(position).getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            relatedTaskDateView.setText(
                    context.getString(R.string.yesterday_capital) + " " +
                            dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
            );
        }
        else if(Utils.isDateTomorrow(items.get(position).getBeginDate()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            relatedTaskDateView.setText(
                    context.getString(R.string.tomorrow_capital) + " " +
                            dateFormat.format(new Date((long)items.get(position).getBeginDate()*1000)) + " - " + dateFormat.format(new Date((long)items.get(position).getEndDate()*1000))
            );
        }
        else
        {
            Date beginDate = new Date((long)items.get(position).getBeginDate()*1000);
            Date endDate = new Date((long)items.get(position).getBeginDate()*1000);
            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(beginDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);

            if(beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                relatedTaskDateView.setText(
                        dateFormat.format(beginDate) + " " +
                                timeFormat.format(beginDate) + " - " + timeFormat.format(endDate)
                );
            }
            else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
                relatedTaskDateView.setText(
                        dateFormat.format(new Date((long) items.get(position).getBeginDate() * 1000)) + " - " +
                                dateFormat.format(new Date((long) items.get(position).getEndDate() * 1000))
                );
            }
        }


        return itemView;
    }
}