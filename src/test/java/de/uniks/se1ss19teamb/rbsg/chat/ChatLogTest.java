package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

public class ChatLogTest {

    @Test
    public void chatTest() throws IOException, InterruptedException {

        Chat chat = new Chat("testTeamB", "qwertz");
        TimeUnit.SECONDS.sleep(1);

        String message1 = "Hi ich bin der testTeamB und ich kann Java nicht.";
        chat.sendMessage(message1);
        TimeUnit.SECONDS.sleep(1);

        String message2 = "Hi testTeamB, gut das du es erwähnst";
        chat.sendMessage(message2);
        TimeUnit.SECONDS.sleep(1);

        String message3 = "Es ist kein Problem, hir sind alle Hilfsbereit und können dir"
                + " gerne Weiterhelfen =)";
        chat.sendMessage(message3);
        TimeUnit.SECONDS.sleep(1);

        String message4 = "Ich würde gerne Fragen was mit dem Server los ist";
        chat.sendMessage(message4, "Albert");
        TimeUnit.SECONDS.sleep(1);

        chat.disconnect();

        Path path = Paths.get("src/main/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");

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
