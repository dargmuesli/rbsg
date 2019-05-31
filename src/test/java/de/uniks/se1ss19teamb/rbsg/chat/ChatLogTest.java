package de.uniks.se1ss19teamb.rbsg.chat;

import com.google.gson.stream.JsonReader;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatLogTest {

    @Test
    public void testChat() throws IOException, InterruptedException {

        Chat chat = new Chat("Admin");
        String message1 = "Hi ich bin der Vadim und ich kann Java nicht.";
        chat.sendMessage(message1);
        TimeUnit.SECONDS.sleep(1);
        String message2 = "Hi Vadim, gut das du es erwähnst";
        chat.sendMessage(message2);
        String message3 = "Es ist kein Problem, hir sind alle Hilfsbereit und können dir gerne Weiterhelfen =)";
        chat.sendMessage(message3);

        String message4 = "Ich würde gerne Fragen was mit dem Server los ist";
        chat.sendMessage("Albert", message4);

        Path path = Paths.get("stories/test.txt");

        chat.writeLog(path);

        // Open the file
        FileInputStream fstream = new FileInputStream(String.valueOf(path));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        ArrayList<ChatLogEntry> chatLogEntries = new ArrayList<ChatLogEntry>();
        while ((strLine = br.readLine()) != null)   {
            // deserialize chatLog
            ChatLogEntry entry = SerializeUtils.deserialize(strLine, ChatLogEntry.class);
            chatLogEntries.add(entry);
        }
        //Close the input stream
        fstream.close();

        Assert.assertEquals(message1, chatLogEntries.get(0).message);
        Assert.assertEquals(message2, chatLogEntries.get(1).message);
        Assert.assertEquals(message3, chatLogEntries.get(2).message);
        Assert.assertEquals(message4, chatLogEntries.get(3).message);

        File file = new File(String.valueOf(path));
        file.delete();
    }

}
