package co.uberdev.ultimateorganizer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.StringEntity;



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

    //TODO: HTTPS!!!!!!


    public APIResult run() throws  IOException{

        //CloseableHttpClient client = HttpClients.createDefault();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

        post.setEntity(new StringEntity(requestBody, ContentType.create("application/json")));
        HttpResponse response = client.execute(post);



        System.out.println(URL+ httpMethod + requestBody);

        APIResult result = new APIResult(response);

        System.out.println(result.getResponseCode());



        return result;
    }


}
