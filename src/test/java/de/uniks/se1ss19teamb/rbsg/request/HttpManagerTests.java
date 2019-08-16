package de.uniks.se1ss19teamb.rbsg.request;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HttpManagerTests {

    private URI uri;
    private HttpManager httpManager;
    private Header[] headers;
    private Gson gson;
    private HttpEntity httpEntity;

    {
        try {
            uri = new URI("https://rbsg.uniks.de/api/user");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setupTests() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        httpManager = new HttpManager(httpClient);
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
    public void testHttpManagerPost() throws Exception {
        String managerResponse = httpManager.post(uri, headers, httpEntity).body;
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }

    @Test
    public void testHttpManagerGet() throws Exception {
        String managerResponse = httpManager.get(uri, headers).body;
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }

    @Test
    public void testHttpManagerDelete() throws Exception {
        String managerResponse = httpManager.delete(uri, headers, httpEntity).body;
        Assert.assertNotNull(managerResponse);
        Body responseBody = gson.fromJson(managerResponse, Body.class);
        Assert.assertEquals("Test Name", responseBody.name);
        Assert.assertEquals("Test Password", responseBody.password);
    }

    static class Body {
        String name = "Test Name";
        String password = "Test Password";
    }
}
