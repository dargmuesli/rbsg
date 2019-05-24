package de.uniks.se1ss19teamb.rbsg;

import com.google.gson.Gson;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;


public class HTTPManagerTests {

    class Body {
        String name = "Test Name";
        String password = "Test Password";
    }

    private URI uri;

    {
        try {
            uri = new URI("https://rbsg.uniks.de/api/user");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private HTTPManager httpManager;
    private Header[] headers;
    private Gson gson;
    private HttpEntity httpEntity;

    @Before
    public void setupTests() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        httpManager = new HTTPManager(httpClient);
        HttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");

        httpResponse.addHeader("TestHeader", "1");

        // A Header for the Method
        headers = new Header[1];
        headers[0] = httpResponse.getFirstHeader("TestHeader");

        // HTTP Body in gson
        gson = new Gson();
        String jsonString = gson.toJson(new Body());

        httpEntity = new StringEntity(jsonString);

        httpResponse.setEntity(httpEntity);

        when(httpClient.execute(any())).thenReturn(httpResponse);
    }

    @Test
    public void testHTTPManagerPost() throws Exception {
        String managerResponse = httpManager.post(uri, headers, httpEntity);
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }

    @Test
    public void testHTTPManagerGet() throws Exception {
        String managerResponse = httpManager.get(uri, headers);
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }

    @Test
    public void testHTTPManagerDelete() throws Exception {
        String managerResponse = httpManager.delete(uri, headers, httpEntity);
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }
}
