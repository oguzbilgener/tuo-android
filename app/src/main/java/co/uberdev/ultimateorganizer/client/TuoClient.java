package co.uberdev.ultimateorganizer.client;

import co.uberdev.ultimateorganizer.core.CoreCrypto;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreJSON;

import java.io.IOException;

/**
* Created by ata on 4/9/14.
*/
public class TuoClient{


    boolean logged;
    private String publicKey;
    private String secretToken;

    public TuoClient(String publicKey, String secretToken)
    {



        this.publicKey = publicKey;
        this.secretToken = secretToken;



    }

    /**
     * Get ready to make a request to server
     * @param endpoint
     * @param requestData
     * @param httpMethod
     * @param loggedIn
     * @return
     */
    public APIRequest prepareRequest(String endpoint, String requestData, String httpMethod, boolean loggedIn)
    {



        if(loggedIn)
        {
            String signature = CoreCrypto.hmacSha1(requestData, secretToken);
            endpoint += "?"+ CoreDataRules.fields.request.public_key + "=" + publicKey + "&" + CoreDataRules.fields.request.signature + "=" + signature;
        }

        return new APIRequest(endpoint, requestData,httpMethod);
    }


    /**
     *Login with given parameters. Returs the result as an object.
     * @param email
     * @param password
     * @return
     * @throws IOException
     */
    public APIResult logIn(String email, String password) throws IOException
    {
        CoreJSON data = new CoreJSON();
        data.put(CoreDataRules.fields.login.email, email);
        data.put(CoreDataRules.fields.login.password, password);


        String jsonString = data.getAsJsonString();

        co.uberdev.ultimateorganizer.client.APIRequest request = prepareRequest("/auth/login", jsonString, "POST", false);
        return request.run();
    }


    /**
     * Register with given parameters. Returns the result as an object
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param schoolName
     * @param departmentName
     * @param birthday
     * @return
     * @throws IOException
     */
    public APIResult register(String email, String password, String firstName, String lastName, String schoolName, String departmentName, int birthday) throws IOException {
        CoreJSON data = new CoreJSON();
        data.put(CoreDataRules.fields.register.email, email);
        data.put(CoreDataRules.fields.register.password, password);
        data.put(CoreDataRules.fields.register.firstName, firstName);
        data.put(CoreDataRules.fields.register.lastName, lastName);
        data.put(CoreDataRules.fields.register.schoolName, schoolName);
        data.put(CoreDataRules.fields.register.departmentName, departmentName);
        data.put(CoreDataRules.fields.register.birthday, birthday);

        String jsonString = data.getAsJsonString();

        co.uberdev.ultimateorganizer.client.APIRequest request = prepareRequest("/auth/register", jsonString, "POST", false);

        return request.run();
    }


    public APIResult getTasks()
    {
        // hede hodo
        return null;
    }


}
