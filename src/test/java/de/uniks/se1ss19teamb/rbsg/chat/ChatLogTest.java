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
        Path testChatLogPath = Paths.get("src/test/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");
        Chat chat = new Chat("testTeamB", "qwertz", testChatLogPath);

        chat.deleteLog();

        String message1 = "message1";
        chat.sendMessage(message1);

        String message2 = "message2";
        chat.sendMessage(message2);

        String message3 = "message3";
        chat.sendMessage(message3);
        TimeUnit.SECONDS.sleep(1);

        String message4 = "message4";
        chat.sendMessage(message4, "receiver");

        String message5 = "message5";
        chat.addToChatLog(message5, "sender");

        chat.disconnect();

        // Open the file
        FileInputStream fstream = new FileInputStream(String.valueOf(testChatLogPath));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        ArrayList<ChatLogEntry> chatLogEntries = new ArrayList<>();
        while ((strLine = br.readLine()) != null) {
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
        Assert.assertEquals(message5, chatLogEntries.get(4).message);

        chat.deleteLog();
    }
}
