package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;


public class CoursesListFragment extends Fragment implements FragmentCommunicator
{
	public static final int MESSAGE_REFRESH_COURSES = -96;

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

        coursesListAdapter = new CoursesListAdapter(getActivity(), coursesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_courses_list, container, false);

		UltimateApplication app = (UltimateApplication) getActivity().getApplication();
		((TextView)rootView.findViewById(R.id.schedule_user_name)).setText(app.getUser().getFirstName()+" "+app.getUser().getLastName());
		((TextView)rootView.findViewById(R.id.schedule_user_info)).setText(app.getUser().getSchoolName()+", "+app.getUser().getDepartmentName());

        coursesListView = (ListView) rootView.findViewById(R.id.schedule_courses_listview);

        coursesListView.setAdapter(coursesListAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

	@Override
	public void onMessage(int msgType, Object obj)
	{
		if(msgType == MESSAGE_REFRESH_COURSES)
		{
			loadCourses();
		}
	}

    @Override
    public void onResume()
    {
        super.onResume();

        loadCourses();
    }

	public void loadCourses()
	{
		// TODO: make this method an async task loader
		Courses allCourses = new Courses(getParent().getLocalStorage().getDb());
		allCourses.loadAllCourses();

		coursesList.clear();
		coursesList.addAll(allCourses.toCourseArrayList());
		coursesListAdapter.notifyDataSetChanged();
	}

    public ScheduleActivity getParent()
    {
        return (ScheduleActivity) getActivity();
    }

}
