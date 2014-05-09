package co.uberdev.ultimateorganizer.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.uberdev.ultimateorganizer.android.util.Utils;


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
		StringEntity se = new StringEntity(requestBody);

		post.setEntity(se);

		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(post);


//        Utils.log.d(URL+ httpMethod + requestBody);

        APIResult result = new APIResult(response);

        Utils.log.d(String.valueOf(result.getResponseCode()));

        return result;
    }

	private static String convertInputStreamToString(InputStream inputStream) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

}
