package co.uberdev.ultimateorganizer.client;

import java.io.IOException;

import co.uberdev.ultimateorganizer.core.CoreCourse;
import co.uberdev.ultimateorganizer.core.CoreCrypto;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreJSON;
import co.uberdev.ultimateorganizer.core.CoreNote;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreUser;
import co.uberdev.ultimateorganizer.core.CoreUtils;

/**
 * Created by ata on 4/9/14.
 */
public class TuoClient{


    private String publicKey;
    private String secretToken;

    public TuoClient(String publicKey, String secretToken)
    {



        this.publicKey = publicKey;
        this.secretToken = secretToken;



    }

    /**
     * Get ready to make a request to server
     * @param endPoint
     * @param requestData
     * @param httpMethod
     * @param loggedIn
     * @return
     */
    public APIRequest prepareRequest(String endPoint, String requestData, String httpMethod, boolean loggedIn)
    {



        if(loggedIn)
        {
            String signature = CoreCrypto.hmacSha1(requestData, secretToken);
            endPoint += "?"+ CoreDataRules.fields.request.public_key + "=" + publicKey + "&" + CoreDataRules.fields.request.signature + "=" + signature;
        }

        return new APIRequest(endPoint, requestData,httpMethod);
    }


    /**
     *Login with given parameters. Returns the result as an object.
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

        APIRequest request = prepareRequest("/auth/login", jsonString, "POST", false);

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

        APIRequest request = prepareRequest("/auth/register", jsonString, "POST", false);

        return request.run();
    }


    public APIResult getTasks(CoreUser user) throws IOException
    {
		CoreJSON data = new CoreJSON();
		data.put(CoreDataRules.columns.courses.ownerId, user.getId());
		APIRequest request = prepareRequest("/users/tasks",data.getAsJsonString(), "POST", true);

		return request.run();
    }

	public APIResult getCourses(CoreUser user) throws IOException
	{
		APIRequest request = prepareRequest("/users/courses", user.asJsonString(), "POST", true);

		return request.run();
	}

    public APIResult insertCourse(CoreCourse toAdd) throws IOException
    {
        APIRequest request = prepareRequest("/courses/insert", toAdd.asJsonString(), "POST", true);

        return request.run();
    }

    public APIResult updateCourse(CoreCourse toUpdate) throws IOException
    {
        APIRequest request = prepareRequest("/courses/update", toUpdate.asJsonString(), "POST", true);

        return request.run();
    }

    public APIResult removeCourse(CoreCourse toRemove) throws IOException
    {
        APIRequest request = prepareRequest("/courses/remove", toRemove.asJsonString(), "POST", true);

        return request.run();
    }

	public APIResult listCourses(CoreJSON params) throws IOException
	{
		APIRequest request = prepareRequest("/courses/list", params.getAsJsonString(), "POST", true);

		return request.run();
	}

    public APIResult insertTask(CoreTask toAdd) throws IOException
    {

        APIRequest request = prepareRequest("/tasks/insert", toAdd.asJsonString(), "POST", true);

        return request.run();

    }

    public APIResult updateTask(CoreTask toUpdate) throws IOException
    {
        toUpdate.setLastModified(CoreUtils.getUnixTimestamp());
        APIRequest request = prepareRequest("/tasks/update", toUpdate.asJsonString(), "POST", true);

        return request.run();
    }

    public APIResult removeTask(CoreTask toRemove) throws IOException
    {
        APIRequest request = prepareRequest("/tasks/remove", toRemove.asJsonString(), "POST", true);

        return request.run();
    }

	public APIResult listTasks(CoreJSON params) throws IOException
	{
		APIRequest request = prepareRequest("/users/tasks", params.getAsJsonString(), "POST", true);

		return request.run();
	}

    public APIResult insertNote(CoreNote toAdd) throws IOException
    {
        APIRequest request = prepareRequest("/notes/insert", toAdd.asJsonString(), "POST", true);

        return request.run();

    }

    public APIResult updateNote(CoreNote toUpdate) throws IOException
    {
        toUpdate.setLastModified(CoreUtils.getUnixTimestamp());
        APIRequest request = prepareRequest("/notes/update", toUpdate.asJsonString(), "POST", true);


        return request.run();
    }

    public APIResult removeNote(CoreNote toRemove) throws IOException
    {
        APIRequest request = prepareRequest("/notes/remove", toRemove.asJsonString(), "POST", true);

        return request.run();
    }

	public APIResult listNotes(CoreJSON params) throws IOException
	{
		APIRequest request = prepareRequest("/notes/list", params.getAsJsonString(), "POST", true);

		return request.run();
	}

	public APIResult getFeed(CoreUser user) throws IOException
	{
		APIRequest request = prepareRequest("/users/feed", user.asJsonString(), "POST", true);

		return request.run();
	}

    public APIResult verifyTask(String msg) throws IOException {
        CoreJSON json = new CoreJSON();
        json.put("message", msg);
        APIRequest request = prepareRequest("/verify", json.getAsJsonString(), "POST", true);

        return request.run();
    }


}
