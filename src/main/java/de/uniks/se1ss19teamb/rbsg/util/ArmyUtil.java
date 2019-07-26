package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.CreateArmyRequest;
import de.uniks.se1ss19teamb.rbsg.request.UpdateArmyRequest;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArmyUtil {
    private static final Logger logger = LogManager.getLogger();

    public static String saveToServer(Army currentArmy) {
        String currentArmyName = currentArmy.getName();
        String currentArmyId = currentArmy.getId();
        List<Unit> currentArmyUnits = currentArmy.getUnits();

        if (currentArmyName == null) {
            NotificationHandler.getInstance().sendError("You have to give the army a name!",
                logger);
            return null;
        }

        if (currentArmyUnits.size() < 10) {
            NotificationHandler.getInstance().sendError("You need at least ten units!", logger);
            return null;
        }

        if (currentArmyId == null) {
            CreateArmyRequest req = new CreateArmyRequest(currentArmyName, currentArmyUnits,
                LoginController.getUserKey());
            req.sendRequest();

            if (req.getSuccessful()) {
                NotificationHandler.getInstance().sendSuccess("The Army was saved.", logger);
                currentArmy.setId(req.getData());
            }
        } else {
            UpdateArmyRequest req = new UpdateArmyRequest(currentArmyId, currentArmyName, currentArmyUnits,
                LoginController.getUserKey());
            req.sendRequest();

            if (req.getSuccessful()) {
                NotificationHandler.getInstance().sendSuccess("The Army was updated.", logger);
            }
        }
        return currentArmy.getId();
    }
}
