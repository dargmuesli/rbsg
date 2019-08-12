package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.request.AbstractDataRestRequest;
import de.uniks.se1ss19teamb.rbsg.request.AbstractRestRequest;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;

public class RequestUtil {
    public static boolean request(AbstractRestRequest request) {
        request.sendRequest();

        if (!request.getSuccessful()) {
            NotificationHandler.sendError(request.getErrorMessage(), LogManager.getLogger());
            return false;
        }

        return true;
    }

    public static <T> Optional<T> request(AbstractDataRestRequest<T> request) {
        request.sendRequest();

        if (!request.getSuccessful()) {
            LogManager.getLogger().error(request.getErrorMessage());
            return Optional.empty();
        }

        return Optional.of(request.getData());
    }
}
