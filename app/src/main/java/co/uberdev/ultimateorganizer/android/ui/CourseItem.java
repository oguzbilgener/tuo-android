package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by cagdass on 24/04/14.
 */
public class CourseItem
{
    private TextView courseName;
    private TextView courseDescription;
    private TextView courseInstructor;
    private String itemId;

    public CourseItem()
    {
        courseName = new TextView(null);
        courseDescription = new TextView(null);
        courseInstructor = new TextView(null);
        itemId = "";
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setCourseName(String courseName)
    {
        this.courseName.setText(courseName);
    }

    public void setCourseDescription(String courseDescription)
    {
        this.courseDescription.setText(courseDescription);
    }

    public void setCourseInstructor(String courseInstructor)
    {
        this.courseInstructor.setText(courseInstructor);
    }

    public String getCourseName()
    {
        return (String) courseName.getText();
    }

    public String getCourseDescription()
    {
        return (String) courseDescription.getText();
    }

    public String getCourseInstructor()
    {
        return (String) courseInstructor.getText();
    }
}
