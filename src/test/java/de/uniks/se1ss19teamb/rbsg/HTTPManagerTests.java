package de.uniks.se1ss19teamb.rbsg;

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

    URI uri;

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

        // HTTP Body json
        JSONObject json = new JSONObject();
        json.put("name", "Test");
        json.put("password", "Test");
        json.toString();

        HttpEntity httpEntity = new StringEntity(json.toString());

        httpResponse.setEntity(httpEntity);

        when(httpClient.execute(any())).thenReturn(httpResponse);

        String managerResponse = httpManager.post(uri, headers, httpEntity);
        Assert.assertNotNull(managerResponse);
        verify(httpClient).execute(any());

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
