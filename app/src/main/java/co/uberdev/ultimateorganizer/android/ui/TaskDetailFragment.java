package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by ozgur on 06.05.2014.
 */
public class TaskDetailFragment extends Fragment
{
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


        return rootView;
    }
}
