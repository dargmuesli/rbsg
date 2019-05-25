package de.uniks.se1ss19teamb.rbsg.chat;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatLogTest {

    @Test
    public void testChat() throws IOException, InterruptedException {
        Chat chat = new Chat();
        TimeUnit.SECONDS.sleep(5);
        Chat.Message message1 = new Chat.Message("Erste nachricht", "Vadim", new Date());
        Chat.Message message2 = new Chat.Message("Zweite nachricht", "Christoph", new Date());
        Chat.Message message3 = new Chat.Message("Dritte nachricht", "Albert", new Date());
        Chat.Message message4 = new Chat.Message("Vierte nachricht", "Clemens", new Date());

        chat.writeLog("test.txt");

        //File file = new File("./test.txt");
        //file.delete();
    }

}
