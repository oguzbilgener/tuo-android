package co.uberdev.ultimateorganizer.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;



/**
* Created by ata on 4/9/14.
*/
public class APIRequest {

    private final String v1 = "http://api.ultimateapp.co:9000/v1";
    private String URL;
    String httpMethod;
    String requestBody;

    public APIRequest(String URL, String requestBody, String httpMethod){
        this.URL = v1 + URL;
        this.httpMethod = httpMethod;
        this.requestBody = requestBody;

    }

    // TODO: build a regular json object for login and use the normal request method
    //TODO: HTTPS!!!!!!


    public APIResult run() throws  IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

//        post.setEntity(new StringEntity(requestBody, ContentType.create("application/json")));
        HttpResponse response = client.execute(post);



        System.out.println(URL+ httpMethod + requestBody);

        APIResult result = new APIResult(response);


        return result;
    }

    public HttpResponse request()
    {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

        return null;
    }

}
