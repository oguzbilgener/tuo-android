package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by dunkuCodder 02/05/2014
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OverviewTaskFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private ListView overviewTaskListview;
    private ArrayList<Task> overviewTaskList;
    private OverviewTaskAdapter overviewTaskAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverviewTaskFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static OverviewTaskFragment newInstance()
    {
        OverviewTaskFragment fragment = new OverviewTaskFragment();
        return fragment;
    }
    public OverviewTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overviewTaskList = new ArrayList<Task>();
        Task task = new Task();
        task.setTaskName("Uberbasgan!!!");
        task.setTaskDesc("lol");
        task.setEndDate( 1234567);
        overviewTaskList.add(task);

        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tasks_home, container, false);

        overviewTaskListview = (ListView) rootView.findViewById(R.id.overview_tasks_listview);

        overviewTaskListview.setAdapter(overviewTaskAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
