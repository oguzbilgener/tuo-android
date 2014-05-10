package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by dunkuCoder on 08/05/1014.
 */
public class Course extends CoreCourse implements CoreStorable
{
    private transient SQLiteDatabase db;


    public Course(SQLiteDatabase db)
    {
        super();
        this.db = db;
    }

    public Course()
    {
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
    public boolean insert()
    {
        if(db != null)
        {
            try {
                String insertSql = "INSERT INTO " + getTableName() + " (" +
                        CoreDataRules.columns.courses.id + ", " +
                        CoreDataRules.columns.courses.ownerId + ", " +
                        CoreDataRules.columns.courses.title + ", " +
                        CoreDataRules.columns.courses.semester + ", " +
                        CoreDataRules.columns.courses.departmentCode + ", " +
                        CoreDataRules.columns.courses.courseCode + ", " +
                        CoreDataRules.columns.courses.sectionCode + ", " +
                        CoreDataRules.columns.courses.instructor_name + " " +
                        ") VALUES (?,?,?,?,?,?,?,?)";

                int n = 1;
                SQLiteStatement ss = db.compileStatement(insertSql);
                ss.bindLong(n++, getId());
                ss.bindLong(n++, getOwnerId());
                ss.bindString(n++, getCourseTitle());
                ss.bindString(n++, getCourseSemester());
                ss.bindString(n++, getDepartmentCode());
                ss.bindString(n++, getCourseCode());
                ss.bindLong(n++, getSectionCode());
                ss.bindString(n++, getInstructorName());

                ss.execute();
                ss.close();

                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean update()
    {
        if(db != null)
        {
            try
            {
                int n = 1;
                String updateSql = "UPDATE " + CoreDataRules.tables.tasks + " SET " +
                        CoreDataRules.columns.courses.id + " = ?, " +
                        CoreDataRules.columns.courses.ownerId + " = ?, " +
                        CoreDataRules.columns.courses.title + " = ?, " +
                        CoreDataRules.columns.courses.semester + " = ?, " +
                        CoreDataRules.columns.courses.departmentCode + " = ?, " +
                        CoreDataRules.columns.courses.courseCode + " = ?, " +
                        CoreDataRules.columns.courses.sectionCode + " = ?, " +
                        CoreDataRules.columns.courses.instructor_name + " = ? " +
                        " WHERE " + CoreDataRules.columns.courses.localId + " = ?";

                SQLiteStatement ss = db.compileStatement(updateSql);
                ss.bindLong(n++, getId());
                ss.bindLong(n++, getOwnerId());
                ss.bindString(n++, getCourseTitle());
                ss.bindString(n++, getCourseSemester());
                ss.bindString(n++, getDepartmentCode());
                ss.bindString(n++, getCourseCode());
                ss.bindLong(n++, getSectionCode());
                ss.bindString(n++, getInstructorName());

                ss.execute();
                ss.close();

                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean remove()
    {
        if(db != null && localId > 0)
        {
            String removeSql = "DELETE FROM "+CoreDataRules.tables.courses+" WHERE id = "+localId;
            // TODO: complete this.
        }
        return false;
    }
}
