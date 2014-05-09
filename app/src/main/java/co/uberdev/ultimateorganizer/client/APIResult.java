package co.uberdev.ultimateorganizer.client;



import co.uberdev.ultimateorganizer.core.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


/**
 * Created by ata on 4/16/14.
 */
public class APIResult {

    public HttpResponse response;
    private String responseBody;

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

}
