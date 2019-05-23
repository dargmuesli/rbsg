package de.uniks.se1ss19teamb.rbsg;

import com.google.gson.Gson;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;


public class HTTPManagerTests {

    class Body {
        public String name = "Test";
        public String password = "Test";
    }

    private URI uri;

    {
        try {
            uri = new URI("https://rbsg.uniks.de/api/user");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHTTPManagerPost() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        HTTPManager httpManager = new HTTPManager(httpClient);
        HttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");

        httpResponse.addHeader("TestHeader", "1");

        // A Header for the Method
        Header[] headers = new Header[1];
        headers[0] = httpResponse.getFirstHeader("TestHeader");

        // HTTP Body in gson
        Gson gson = new Gson();
        String jsonString = gson.toJson(new Body());

        Body body = gson.fromJson(jsonString, Body.class);
        System.out.println("Body name: " + body.name);
        System.out.println("Gson produced String: " + jsonString);


        HttpEntity httpEntity = new StringEntity(jsonString);

        httpResponse.setEntity(httpEntity);

        when(httpClient.execute(any())).thenReturn(httpResponse);

        String managerResponse = httpManager.post(uri, headers, httpEntity);
        Assert.assertNotNull(managerResponse);
        verify(httpClient).execute(any());
        Body responeBody = gson.fromJson(managerResponse, Body.class);
        System.out.println("Responsename: " + responeBody.name);
        Assert.assertTrue(managerResponse.contains("name"));

    }

    @Test
    public void testHTTPManagerGet() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        HTTPManager httpManager = new HTTPManager(httpClient);
        HttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");

        httpResponse.addHeader("TestHeader", "1");

        // A Header for the Method
        Header[] headers = new Header[1];
        headers[0] = httpResponse.getFirstHeader("TestHeader");

        // HTTP Body json
        JSONObject json = new JSONObject();
        json.put("name", "Test");
        json.put("password", "Test");
        json.toString();

        HttpEntity httpEntity = new StringEntity(json.toString());

        httpResponse.setEntity(httpEntity);

        when(httpClient.execute(any())).thenReturn(httpResponse);

        String managerResponse = httpManager.get(uri, headers);
        Assert.assertNotNull(managerResponse);
        verify(httpClient).execute(any());

    }

    @Test
    public void testHTTPManagerDelete() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        HTTPManager httpManager = new HTTPManager(httpClient);
        HttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");

        httpResponse.addHeader("TestHeader", "1");

        // A Header for the Method
        Header[] headers = new Header[1];
        headers[0] = httpResponse.getFirstHeader("TestHeader");

        // HTTP Body json
        JSONObject json = new JSONObject();
        json.put("name", "Test");
        json.put("password", "Test");
        json.toString();

        HttpEntity httpEntity = new StringEntity(json.toString());

        httpResponse.setEntity(httpEntity);

        when(httpClient.execute(any())).thenReturn(httpResponse);

        String managerResponse = httpManager.delete(uri, headers, httpEntity);
        Assert.assertNotNull(managerResponse);
        verify(httpClient).execute(any());

    }
}
