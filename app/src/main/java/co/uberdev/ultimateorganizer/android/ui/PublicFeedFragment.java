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
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PublicFeedFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private ListView publicFeedListView;
    private ArrayList<Task> publicFeedTasksList;
    private PublicFeedAdapter publicFeedAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PublicFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicFeedFragment newInstance()
    {
        PublicFeedFragment fragment = new PublicFeedFragment();
        return fragment;
    }
    public PublicFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        publicFeedTasksList = new ArrayList<Task>();
        Task task = new Task();
        task.setCourseCodeCombined( "TURK102-13");
        task.setTaskName("Third Homework");
        task.setTaskDesc("Letter to Papa");
        task.setTaskOwnerNameCombined("Ani Kristo");
        task.setEndDate( 1234567);
        publicFeedTasksList.add(task);

        Task task2 = new Task();
        task2.setCourseCodeCombined( "CS102-13");
        task2.setTaskName("Project");
        task2.setTaskDesc("Finish It");
        task2.setTaskOwnerNameCombined("Begum Ozcan");
        task2.setEndDate( 12345367);
        publicFeedTasksList.add(task2);

        publicFeedAdapter = new PublicFeedAdapter(getActivity(), publicFeedTasksList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_academic_network_public_feed, container, false);

        publicFeedListView = (ListView) rootView.findViewById(R.id.academic_network_public_feed_listview);

        publicFeedListView.setAdapter(publicFeedAdapter);

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
