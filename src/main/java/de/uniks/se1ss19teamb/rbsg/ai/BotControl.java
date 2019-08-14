package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.ui.BotManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.logging.LogManager;

public class BotControl {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private static ArrayList<BotUser> botUsers = new ArrayList<>();

    public static void createBotUser(int number, BotSelectionController botSelectionController) {
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
        //Just info for dev. Will be deleted later
        NotificationHandler.getInstance().sendInfo(loginUserRequest.getData(), logger);
        NotificationHandler.getInstance().sendInfo("Bot Username: "+ botUser.getBotUserName() +
            "\nBot Password: " + botUser.getBotUserPassword(), logger);
        botSelectionController.setCheckBoxName(botUser.getBotUserName());
    }
}
