package de.uniks.se1ss19teamb.rbsg.chat;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatLogTest {

    @Test
    public void testChat() throws IOException, InterruptedException {

        Chat chat = new Chat("Admin");
        chat.sendMessage("Hi ich bin der Vadim und ich kann Java nicht.");
        TimeUnit.SECONDS.sleep(1);
        chat.sendMessage("Hi Vadim, gut das du es erwähnst");
        chat.sendMessage("Es ist kein Problem, hir sind alle Hilfsbereit und können dir gerne Weiterhelfen =)");

        chat.sendMessage("Albert", "Ich würde gerne Fragen was mit dem Server los ist");

        Path path = Paths.get("stories/test.txt");

        chat.writeLog(path);

        File file = new File(String.valueOf(path));
        file.delete();
    }

}
