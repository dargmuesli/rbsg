package de.uniks.se1ss19teamb.rbsg.chat;

import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.ChatHistoryEntry;
import de.uniks.se1ss19teamb.rbsg.sockets.*;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatHistoryTest {

    private ChatSocket chatSocket;

    @Before
    public void prepareClient() {
        chatSocket = mock(ChatSocket.class);
        LoginController.setUserName("me");
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

        chat.deleteHistory();

        ChatHistoryEntry chatHistoryEntry1 = new ChatHistoryEntry("message1", LoginController.getUserName());
        setupSocket(chat, chatHistoryEntry1.message, chatHistoryEntry1.sender);
        chat.sendMessage(chatHistoryEntry1.message);

        ChatHistoryEntry chatHistoryEntry2
            = new ChatHistoryEntry("message2", LoginController.getUserName(), "receiver2");
        setupSocket(chat, chatHistoryEntry2.message, chatHistoryEntry2.sender);
        chat.sendMessage(chatHistoryEntry2.message, chatHistoryEntry2.receiver);

        ChatHistoryEntry chatHistoryEntry3 = new ChatHistoryEntry("message3", LoginController.getUserName());
        setupSocket(chat, chatHistoryEntry3.message, chatHistoryEntry3.sender);
        chat.sendMessage(chatHistoryEntry3.message);

        chat.disconnect();

        // Open the file
        FileInputStream fstream = new FileInputStream(String.valueOf(testChatLogPath));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        ArrayList<ChatHistoryEntry> loggedEntries = new ArrayList<>();
        while ((strLine = br.readLine()) != null) {
            // deserialize chatLog
            ChatHistoryEntry entry = SerializeUtil.deserialize(strLine, ChatHistoryEntry.class);
            loggedEntries.add(entry);
        }
        //Close the input stream
        fstream.close();

        Assert.assertEquals(chatHistoryEntry1.message, loggedEntries.get(0).message);
        Assert.assertEquals(chatHistoryEntry1.sender, loggedEntries.get(0).sender);
        Assert.assertEquals(chatHistoryEntry1.receiver, loggedEntries.get(0).receiver);

        Assert.assertEquals(chatHistoryEntry2.message, loggedEntries.get(1).message);
        Assert.assertEquals(chatHistoryEntry2.sender, loggedEntries.get(1).sender);
        Assert.assertEquals(chatHistoryEntry2.receiver, loggedEntries.get(1).receiver);

        Assert.assertEquals(chatHistoryEntry3.message, loggedEntries.get(2).message);
        Assert.assertEquals(chatHistoryEntry3.sender, loggedEntries.get(2).sender);
        Assert.assertEquals(chatHistoryEntry3.receiver, loggedEntries.get(2).receiver);

        chat.deleteHistory();
    }
}
