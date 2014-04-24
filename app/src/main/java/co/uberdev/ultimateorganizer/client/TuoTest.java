package co.uberdev.ultimateorganizer.client;

import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by ata on 4/16/14.
 */
public class TuoTest {

    public static void main(String[] args) throws IOException {

        TuoClient client = new TuoClient(null,null);

        APIResult register = client.register("onurokumus@gmail.com", "toptop", "Onur", "Okumus", "METU", "CS", 1);

        APIResult login  = client.logIn("onurokumus@gmail.com", "toptop");

       // System.out.println(EntityUtils.toString(login.response.getEntity()));

        System.out.print(login.getAsUser().getEmailAddress());


        TuoClient loggedClient = new TuoClient(login.getAsUser().getPublicKey(), login.getAsUser().getSecretToken());


    }



}
