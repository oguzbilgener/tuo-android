package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;

/**
 * Created by dunkuCodder 02/05/2014
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewCommonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewCommonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OverviewCommonFragment extends BaseFragment
{
    private OnFragmentInteractionListener mListener;

    private ListView overviewTaskListview;
    private ArrayList<Task> overviewTaskList;
    private OverviewTaskAdapter overviewTaskAdapter;

    protected static int[] filter = new int[]{Task.STATE_ACTIVE, Task.STATE_ARCHIVED, Task.STATE_COMPLETED, Task.STATE_DELETED};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverviewTaskFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static OverviewCommonFragment newInstance()
    {
        OverviewCommonFragment fragment = new OverviewCommonFragment();
        return fragment;
    }
    public OverviewCommonFragment() {
        // Required empty public constructor
    }

    public static int getViewId() {
        return R.id.base_fragment_frame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overviewTaskList = new ArrayList<Task>();
        Task task = new Task();
        task.setTaskName("Uberbasgan!!!");
        task.setTaskDesc("lol");
        task.setEndDate(1234567);
        try{
            task.setStatus( 2);
        }
        catch( Exception e){
            e.printStackTrace();
        }

        overviewTaskList.add(task);
        Task task2 = new Task();
        task2.setTaskName( "sex");
        task2.setTaskDesc( "amsikgotmeme");
        task2.setEndDate( 333);
        try{
            task2.setStatus(1);
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }



        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tasks_home, container, false);

        overviewTaskListview = (ListView) rootView.findViewById(R.id.overview_tasks_list_view);

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
