package de.uniks.se1ss19teamb.rbsg.chat;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatLogTest {

    @Test
    public void testChat() throws IOException, InterruptedException {
        Chat chat = new Chat("My Name");
        //TimeUnit.SECONDS.sleep(5);



        chat.writeLog("test.txt");

        //File file = new File("./test.txt");
        //file.delete();
    }

}
