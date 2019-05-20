package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.HTTPManager;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.net.URI;


public class HTTPManagerTests {

    @Test
    public void postTest() throws Exception {
        HTTPManager httpManager = new HTTPManager();
        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost("rbsg.uniks.de")
                .setPath("/api/user")
                .build();

        JSONObject json = new JSONObject();
        json.put("name", "Test");
        json.put("password", "Test");
        json.toString();

        StringEntity httpEntity = new StringEntity(json.toString());

        String responseBody = httpManager.post(uri, null, httpEntity);



    }
}
