package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import org.apache.http.Header;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

public abstract class AbstractRestRequest implements RestRequest {

    private static final String url = "https://rbsg.uniks.de/api";
    private static final Logger logger = LogManager.getLogger();
    static HttpManager httpManager = new HttpManager();
    private JsonObject response = null;
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    protected abstract JsonObject buildJson();

    protected abstract String getHttpMethod(); //"get", "post", "delete", "put"

    protected abstract String getEndpoint(); //"/user", "/user/login", etc.

    protected abstract String getUserToken();

    @Override
    public void sendRequest() throws ParseException {
        HttpRequestResponse result = null;
        String token = getUserToken();
        try {
            switch (getHttpMethod()) {
                case "get":
                    result = httpManager.get(new URI(url + getEndpoint()), token == null
                        ? null : new Header[]{new BasicHeader("userKey", token)});
                    break;
                case "post":
                    result = httpManager.post(new URI(url + getEndpoint()), token == null
                            ? null : new Header[]{new BasicHeader("userKey", token)},
                        new StringEntity(buildJson().toString()));
                    break;
                case "delete":
                    result = httpManager.delete(new URI(url + getEndpoint()), token == null
                        ? null : new Header[]{new BasicHeader("userKey", token)}, null);
                    break;
                case "put":
                    result = httpManager.put(new URI(url + getEndpoint()), token == null
                        ? null : new Header[]{new BasicHeader("userKey", token)}, null);
                    break;
                default:
                    throw new MethodNotSupportedException("Method not Supported: " + getHttpMethod());
            }
        } catch (Exception e) {
            notificationHandler.sendError("Fehler bei einer Webanfrage!", logger, e);
        } finally {
            JsonParser parser = new JsonParser();
            response = (JsonObject) parser.parse(result == null ? "" : result.body);

            //TODO Handle result status codes
        }
    }

    @Override
    public void sendRequest(RestRequestResponseHandler callback) throws ParseException {
        sendRequest();

        callback.handle(response);
    }

    @Override
    public JsonObject getResponse() {
        return response;
    }
}