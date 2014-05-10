package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 *  created by dunkuCoder 07/07/2014
 */

public class AddCourseFragment extends Fragment implements FragmentCommunicator
{
    public Course editableCourse;

    private EditText departmentCode;
    private EditText courseNumber;
    private EditText sectionNumber;
    private EditText courseName;
    private EditText instructorName;

    private ActivityCommunicator activityCommunicator;

    public static final int MESSAGE_REQUEST_COURSE = -99;
    public static final int MESSAGE_RESPONSE_COURSE = -98;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCourseFragment newInstance()
    {
        AddCourseFragment fragment = new AddCourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddCourseFragment newInstance(Context context, Course editableCourse)
    {
        if(context == null || editableCourse == null)
            return newInstance();

        AddCourseFragment fragment = new AddCourseFragment();
        fragment.editableCourse = editableCourse;
        // Store task json string in arguments just in case a fragment is resurrected from background
        String courseJsonStr = editableCourse.asJsonString();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.ARGS_COURSE_JSON_OBJECT), courseJsonStr);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_course, container, false);

        departmentCode = (EditText) rootView.findViewById(R.id.add_course_department_name_edittext);
        courseNumber = (EditText) rootView.findViewById(R.id.add_course_course_no_edittext);
        sectionNumber = (EditText) rootView.findViewById(R.id.add_course_section_no_edittext);
        courseName = (EditText) rootView.findViewById(R.id.add_course_course_name_edittext);
        instructorName = (EditText) rootView.findViewById(R.id.add_course_instructor_name_edittext);

        if(editableCourse != null)
        {
            try {
                fillFromCourse(editableCourse);
            } catch (Exception e) {
                e.printStackTrace();
                // show an error toast
                Toast.makeText(getActivity(), getString(R.string.edit_course_unknown_error), Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCommunicator = null;
    }

    public FragmentActivity getParent()
    {
        return (FragmentActivity) getActivity();
    }

    public Course buildCourse()
    {
        // Try to retrieve the course object form editableCourse.
        // If not, try arguments
        // If there is still no course, we cannot do anything but creating a new course object.
        // Then it will not be an edit action of course.
        Bundle args = getArguments();
        Course course;
        if(editableCourse == null && !args.containsKey(getString(R.string.ARGS_COURSE_JSON_OBJECT)))
        {
            course = new Course();
        }
        else if(editableCourse != null)
        {
            course = editableCourse;
        }
        else
        {
            String courseStr = args.getString(getString(R.string.ARGS_COURSE_JSON_OBJECT));
            course = Course.fromJson(courseStr, Course.class);
            if(course == null)
            {
                Utils.log.w("editable task string could not be parsed");
                course = new Course();
            }
        }

        course.setDepartmentCode(departmentCode.getText().toString());
        course.setCourseCode(courseNumber.getText().toString());

        course.setCourseSemester(getString(R.string.PREF_SEMESTER_CODE));
        String sectionNumberStr = sectionNumber.getText().toString();
        course.setSectionCode(Integer.parseInt(sectionNumberStr != null && !sectionNumberStr.isEmpty() ? sectionNumberStr : "0"));

        course.setCourseTitle(courseName.getText().toString());
        course.setInstructorName(instructorName.getText().toString());

		course.setCourseColor(Utils.colorForTagStr( course.getDepartmentCode()+" "+course.getCourseCode() ));

        return course;
    }

    public void fillFromCourse(Course course)
    {
        // fill name and description
        departmentCode.setText(course.getDepartmentCode());
        courseNumber.setText(course.getCourseCode());
        sectionNumber.setText(course.getSectionCode());
        courseName.setText(course.getCourseTitle());
        instructorName.setText(course.getInstructorName());
    }

    @Override
    public void onMessage(int msgType, Object obj) {
        switch(msgType)
        {
            case MESSAGE_REQUEST_COURSE:
                activityCommunicator.onMessage(MESSAGE_RESPONSE_COURSE, buildCourse());
                break;
        }
    }
}
