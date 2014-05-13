package co.uberdev.ultimateorganizer.client;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import co.uberdev.ultimateorganizer.android.models.Course;
import co.uberdev.ultimateorganizer.android.models.Courses;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.core.Core;
import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreCourses;
import co.uberdev.ultimateorganizer.core.CoreNote;
import co.uberdev.ultimateorganizer.core.CoreNotes;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreTasks;
import co.uberdev.ultimateorganizer.core.CoreUser;


/**
 * Created by ata on 4/16/14.
 */
public class APIResult {

    public HttpResponse response;
    private String responseBody;

	public static final int RESPONSE_SUCCESS = 200;
	public static final int RESPONSE_UNAUTHORIZED = 401;

   public APIResult(HttpResponse response)
   {
       this.response = response;
       try {
           responseBody = EntityUtils.toString(response.getEntity());
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    public int getResponseCode(){ return response.getStatusLine().getStatusCode(); }

    public String getResponseBody() throws IOException { return responseBody; }


    //public isSuccess()


    //TODO make these not Core
    public CoreUser getAsUser() throws IOException { return CoreUser.fromJson(responseBody, CoreUser.class);        }

    public CoreNote getAsNote() throws IOException { return CoreNote.fromJson(responseBody, CoreNote.class);        }

    public CoreTask getAsTask() throws IOException { return CoreTask.fromJson(responseBody, CoreTask.class);        }

    public CoreNotes getAsNotes() throws IOException { return CoreNotes.fromJson(responseBody, CoreNotes.class);    }

    public CoreTasks getAsTasks() throws IOException { return CoreTasks.fromJson(responseBody, CoreTasks.class);    }

    public CoreCourse getAsCourse() throws IOException { return CoreCourse.fromJson(responseBody, CoreCourse.class);    }

    public CoreCourses getAsCourses() throws IOException { return CoreCourses.fromJson(responseBody, CoreCourses.class);    }

	public Courses getAsClientCourses() throws IOException { return Courses.fromJson(responseBody, Courses.class);    }

	public CoreTask[] getAsTaskArray() throws IOException { return Core.fromJson(responseBody, CoreTask[].class); }

	public CoreCourse[] getAsCourseArray() throws IOException { return Core.fromJson(responseBody, CoreCourse[].class); }

	public CoreUser[] getAsUserArray() throws IOException { return Core.fromJson(responseBody, CoreUser[].class); }

	public CoreNote[] getAsNoteArray() throws IOException { return Core.fromJson(responseBody, CoreNote[].class); }

	public Task[] getAsClientTaskArray() throws IOException { return Core.fromJson(responseBody, Task[].class); }

	public Course[] getAsClientCourseArray() throws IOException { return Core.fromJson(responseBody, Course[].class); }

}
