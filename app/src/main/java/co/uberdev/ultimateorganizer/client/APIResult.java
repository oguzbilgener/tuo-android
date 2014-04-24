package co.uberdev.ultimateorganizer.client;

import co.uberdev.ultimateorganizer.core.CoreJSON;
import co.uberdev.ultimateorganizer.core.CoreUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by ata on 4/16/14.
 */
public class APIResult {

    public HttpResponse response;

   public APIResult(HttpResponse response)
   {
    this.response = response;
   }

    public int getResponseCode()
    {
        return response.getStatusLine().getStatusCode() ;
    }

    //public isSuccess()


    //TODO make this user
    public CoreUser getAsUser() throws IOException {
        return CoreUser.fromJson(EntityUtils.toString(response.getEntity()), CoreUser.class);
    }
}
