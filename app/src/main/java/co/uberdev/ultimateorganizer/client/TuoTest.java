package co.uberdev.ultimateorganizer.client;

import co.uberdev.ultimateorganizer.core.*;


import java.io.IOException;

/**
 * Created by ata on 4/16/14.
 */
public class TuoTest {

    public static void main(String[] args) throws IOException {

        TuoClient client = new TuoClient(null,null);





        //APIResult register = client.register("ata@gmail.com", "toptop", "Onur", "Okumus", "METU", "CS", 1);



        APIResult login  = client.logIn("ata@gmail.com", "toptop");

        //System.out.print(login.getResponseCode());

        CoreUser onurUser = login.getAsUser();


        TuoClient client1 = new TuoClient(onurUser.getPublicKey(),onurUser.getSecretToken());



        CoreNote coreNote = new CoreNote(2);

        coreNote.setAttachment(new CoreAttachment());
        coreNote.setContent("lelele");

        CoreTask coreTask = new CoreTask(3);
        coreTask.setCourse(new CoreCourse());
        coreTask.setOwner(new CoreUser());




        //" client1.insertTask(coreTask);
        //client1.removeTask(coreTask);
        //client1.updateTask(coreTask);


        //client1.insertNote(coreNote);
        //client1.removeNote(coreNote);
        //client1.updateNote(coreNote);




        //System.out.println(login.getAsUser().getPublicKey());
        //System.out.println(addTask.getResponseCode());


    }



}
