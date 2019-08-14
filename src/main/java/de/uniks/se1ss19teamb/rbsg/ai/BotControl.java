package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.logging.LogManager;

public class BotControl {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    static ArrayList<BotUser> botUsers = new ArrayList<>();

    public static void createBotUser(int number) {
        BotUser botUser = new BotUser();
        CreateTemporaryUserRequest ctur = new CreateTemporaryUserRequest();
        ctur.sendRequest();
        botUser.setBotUserName(ctur.getData().get(0));
        botUser.setBotUserPassword(ctur.getData().get(1));
        LoginUserRequest loginUserRequest = new LoginUserRequest(botUser.getBotUserName(),
            botUser.getBotUserPassword());
        loginUserRequest.sendRequest();
        botUser.setBotUserKey(loginUserRequest.getData());
        botUsers.add(number, botUser);
        NotificationHandler.getInstance().sendInfo(loginUserRequest.getData(), logger);
    }
}
