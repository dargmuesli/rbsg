package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.request.AbstractDataRestRequest;
import de.uniks.se1ss19teamb.rbsg.request.AbstractRestRequest;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;

public class RequestUtil {

    /**
     * Sends a request and notifies about potential errors.
     * See {@link NotificationHandler#sendError}.
     *
     * @param request The request to send.
     * @return        Indicator that shows whether the request succeeded.
     */
    public static boolean request(AbstractRestRequest request) {
        request.sendRequest();

        if (!request.getSuccessful()) {
            NotificationHandler.sendError(request.getErrorMessage(), LogManager.getLogger());
            return false;
        }

        return true;
    }

    /**
     * Sends a data returning request and notifies about potential errors.
     * See {@link NotificationHandler#sendError}.
     *
     * @param request The request to send.
     * @param <T>     The data type that can be returned.
     * @return        Optionally the returned data.
     */
    public static <T> Optional<T> request(AbstractDataRestRequest<T> request) {
        request.sendRequest();

        if (!request.getSuccessful()) {
            LogManager.getLogger().error(request.getErrorMessage());
            return Optional.empty();
        }

        return Optional.of(request.getData());
    }
}
