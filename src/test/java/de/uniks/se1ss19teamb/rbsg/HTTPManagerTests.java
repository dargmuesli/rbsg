package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.HTTPManager;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.Mockito.*;


public class HTTPManagerTests {

    /*

    // used in last method
    @InjectMocks
    HTTPManager httpManager = new HTTPManager();
    @Mock
    CloseableHttpClient httpClient;
    // not used after



    @Mock
    HttpPost httpPost;
    @Mock
    HttpResponse httpResponse;

     */

    /*
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    */

    URI uri;

    {
        try {
            uri = new URI("https://rbsg.uniks.de/api/user");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    JSONObject json = new JSONObject();

    /*
    @Test
    public void testExecute() throws Exception {
        json.put("name", "TestUsername");
        json.put("password", "TestPassword");

        HTTPManager httpManager = new HTTPManager();
        HTTPManager spiedManager = Mockito.spy(httpManager);
        Mockito.when(spiedManager.executePost(httpPost))
                .thenReturn(httpResponse);
        String response = httpManager.post(uri, null, new StringEntity(json.toString()));
        httpClient = HttpClients.createDefault();
        Assert.assertNotNull(response);
        System.out.println(response.toString());
    }


    @Test
    public void postTest() throws Exception {
        HTTPManager httpManager = new HTTPManager();
        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost("rbsg.uniks.de")
                .setPath("/api/user")
                .build();



        StringEntity httpEntity = new StringEntity(json.toString());

        String responseBody = httpManager.post(uri, null, httpEntity);
    }
    */


    /*
    @Test
    public void testExecutePost() throws Exception {
        // Mock the CloseableHttpClient so you can control its return.. done
        // create the return value for the httpClient.execute(httpPost) Method.. done

        HttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        httpResponse.addHeader("TestHeader", "1");


        // HTTP Body json
        JSONObject json = new JSONObject();
        json.put("name", "Test");
        json.put("password", "Test");
        json.toString();

        // A Header for the Method
        Header[] headers = new Header[1];
        headers[0] = httpResponse.getFirstHeader("TestHeader");

        // building the HttpGet Object to pass it to the Mockito Rule
        HttpPost httpPost = mock(HttpPost.class);
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        //HttpResponse httpResponse = mock(HttpResponse.class);
        /*
        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        HttpEntity httpEntity = new StringEntity(json.toString());
        httpPost.setEntity(httpEntity);
        */
/*
        // Mocking rule
        when(httpClient.execute(httpPost)).thenReturn(httpResponse);

        String response = httpManager.post(uri, headers, httpEntity);

    }

    */

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

    // Test if Mocking works with the HttpClient.execute() Method. Looks like it does..
    @Test
    public void testExecuteMocking() {
        HttpClient httpClient = mock(HttpClient.class);
        HttpGet httpGet = mock(HttpGet.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        HttpPost httpPost = mock(HttpPost.class);

        HTTPManager httpManager = new HTTPManager(httpClient);

        // Direct call httpClient.execute(httpGet)
        try {
            when(httpClient.execute(httpGet)).thenReturn(httpResponse);
            when(httpClient.execute(httpPost)).thenReturn(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            verify(httpClient).execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(response);
        System.out.println("Direct call on mocked object: " + response.toString());

        // Try to pass httpPost in executePost() of HTTPManager
        // which returns httpClient.execute(httpPost).
        // Not working. Executes real httpClient.execute(httpPost) method.
        HttpResponse response1 = null;
        String httpManagerResponse = null;
        try {
            response1 = httpManager.executePost(httpPost);
            httpManagerResponse = httpManager.post(uri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(response1);
        Assert.assertNotNull(httpManagerResponse);
        System.out.println(response1.toString());
        try {
            verify(httpClient).execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
