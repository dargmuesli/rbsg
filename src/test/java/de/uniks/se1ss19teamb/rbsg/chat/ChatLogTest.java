package de.uniks.se1ss19teamb.rbsg.chat;

import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.ChatLogEntry;
import de.uniks.se1ss19teamb.rbsg.sockets.*;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatLogTest {

    private ChatSocket chatSocket;

    @Before
    public void prepareClient() {
        chatSocket = mock(ChatSocket.class);

        when(chatSocket.getUserName()).thenReturn("me");
    }

    private void setupSocket(Chat chat, String msg, String from) {
        doAnswer(invocation -> {
            chat.chatMessageHandler.handle(msg, from, false);
            return null;
        }).when(chatSocket).sendMessage(any());
    }

    @Test
    public void chatTest() throws IOException {
        Path testChatLogPath = Paths.get("src/test/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");
        Chat chat = new Chat(chatSocket, testChatLogPath);

        chat.deleteLog();

        ChatLogEntry chatLogEntry1 = new ChatLogEntry("message1", chatSocket.getUserName());
        setupSocket(chat, chatLogEntry1.message, chatLogEntry1.sender);
        chat.sendMessage(chatLogEntry1.message);

        ChatLogEntry chatLogEntry2 = new ChatLogEntry("message2", chatSocket.getUserName(), "receiver2");
        setupSocket(chat, chatLogEntry2.message, chatLogEntry2.sender);
        chat.sendMessage(chatLogEntry2.message, chatLogEntry2.receiver);

        ChatLogEntry chatLogEntry3 = new ChatLogEntry("message3", chatSocket.getUserName());
        setupSocket(chat, chatLogEntry3.message, chatLogEntry3.sender);
        chat.sendMessage(chatLogEntry3.message);

        chat.disconnect();

        // Open the file
        FileInputStream fstream = new FileInputStream(String.valueOf(testChatLogPath));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        ArrayList<ChatLogEntry> loggedEntries = new ArrayList<>();
        while ((strLine = br.readLine()) != null) {
            // deserialize chatLog
            ChatLogEntry entry = SerializeUtils.deserialize(strLine, ChatLogEntry.class);
            loggedEntries.add(entry);
        }
        //Close the input stream
        fstream.close();

        Assert.assertEquals(chatLogEntry1.message, loggedEntries.get(0).message);
        Assert.assertEquals(chatLogEntry1.sender, loggedEntries.get(0).sender);
        Assert.assertEquals(chatLogEntry1.receiver, loggedEntries.get(0).receiver);

        Assert.assertEquals(chatLogEntry2.message, loggedEntries.get(1).message);
        Assert.assertEquals(chatLogEntry2.sender, loggedEntries.get(1).sender);
        Assert.assertEquals(chatLogEntry2.receiver, loggedEntries.get(1).receiver);

        Assert.assertEquals(chatLogEntry3.message, loggedEntries.get(2).message);
        Assert.assertEquals(chatLogEntry3.sender, loggedEntries.get(2).sender);
        Assert.assertEquals(chatLogEntry3.receiver, loggedEntries.get(2).receiver);

        chat.deleteLog();
    }
}
