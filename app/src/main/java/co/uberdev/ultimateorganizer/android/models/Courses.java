package co.uberdev.ultimateorganizer.android.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.core.CoreCourses;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by dunkuCoder on 08/05/14.
 */
public class Courses extends CoreCourses implements CoreSelectable {
    private transient SQLiteDatabase db;

    public Courses(SQLiteDatabase db) {
        this.db = db;
    }

    public Courses() {
        this(null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public String getTableName() {
        return CoreDataRules.tables.courses;
    }

    @Override
    public boolean loadFromDb(String sqlCriteria, String[] params, int limit) {
        try {
            if (db != null) {
                String sqlString = "SELECT * FROM " + CoreDataRules.tables.courses;
                if (params.length > 0)
                    sqlString += " WHERE " + sqlCriteria;

                Cursor loader = db.rawQuery(sqlString, params);

                if (!loader.moveToFirst())
                    return true; // no rows in the db, still valid

                while (!loader.isAfterLast()) {
                    Course course = new Course();
                    course.setLocalId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.courses.localId))); // local id = sqlite id
                    course.setId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.courses.id)));
                    course.setOwnerId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.courses.ownerId)));
                    course.setCourseTitle(loader.getString(loader.getColumnIndex(CoreDataRules.columns.courses.title)));
                    course.setCourseSemester(loader.getString(loader.getColumnIndex(CoreDataRules.columns.courses.semester)));
                    course.setDepartmentCode(loader.getString(loader.getColumnIndex(CoreDataRules.columns.courses.departmentCode)));
                    course.setCourseCode(loader.getString(loader.getColumnIndex(CoreDataRules.columns.courses.courseCode)));
                    course.setSectionCode(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.courses.sectionCode)));
                    course.setInstructorName(loader.getString(loader.getColumnIndex(CoreDataRules.columns.courses.instructor_name)));
					course.setCourseColor(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.courses.color)));

                    this.add(course);

                    loader.moveToNext();
                }

                loader.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: parse tags as Tag objects

        return false;
    }

    @Override
    public boolean loadFromDb(int limit) {
        return false;
    }

    @Override
    public boolean loadFromDb() {
        return false;
    }


    /**
     * Loads all the courses to be shown in CoursesListFragment
     *
     * @return
     */
//    public boolean loadAllCourses()
//    {
////        String sqlCriteria = " 1 = 1 ORDER BY " +CoreDataRules.columns.courses.departmentCode;
//
//        String[] params = new String[] { Integer.toString(Task.STATE_ACTIVE), Integer.toString(Task.STATE_COMPLETED) };
//
//        return loadFromDb(sqlCriteria, params, 0);
//    }
    public boolean loadAllCourses() {
        String sqlCriteria = "";

        String[] params = new String[] {};

        return loadFromDb(sqlCriteria, params, 0);
    }

    public ArrayList<Course> toCourseArrayList()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        for(int i=0; i<size(); i++)
        {
            list.add((Course) get(i));
        }
        return list;
    }
}