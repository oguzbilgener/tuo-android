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
import co.uberdev.ultimateorganizer.core.CoreCourse;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoursesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoursesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CoursesListFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private ListView coursesListView;
    private ArrayList<Course> coursesList;
    private CoursesListAdapter coursesListAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CoursesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesListFragment newInstance()
    {
        CoursesListFragment fragment = new CoursesListFragment();
        return fragment;
    }
    public CoursesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        coursesList = new ArrayList<Course>();
        Course course = new Course();
        course.setCourseTitle("Algorithms and Programming II");
        course.setInstructorName("David Davenport");
        course.setDepartmentCode("CS");
        course.setCourseCode("102");
        course.setSectionCode(1);
        course.setCourseSemester(getString(R.string.PREF_SEMESTER_CODE));
        course.setCourseColor(Color.parseColor("#ff0000"));

        coursesList.add(course);

        coursesListAdapter = new CoursesListAdapter(getActivity(), coursesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_courses_list, container, false);

        coursesListView = (ListView) rootView.findViewById(R.id.schedule_courses_listview);

        coursesListView.setAdapter(coursesListAdapter);

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
