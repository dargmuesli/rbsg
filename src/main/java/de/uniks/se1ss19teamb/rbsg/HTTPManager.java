package de.uniks.se1ss19teamb.rbsg;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class HTTPManager {

    public HTTPManager() {
        this.httpClient = HttpClients.createDefault();
    }

    public HTTPManager(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private final HttpClient httpClient;

    public String get(URI uri, Header[] headers) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpGet httpGet = new HttpGet();

        httpGet.setURI(uri);
        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = getResponseBody(response);

        httpGet.releaseConnection();

        return responseBody;
    }

    public String post(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPost httpPost = new HttpPost();

        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = getResponseBody(response);

        httpPost.releaseConnection();

        return responseBody;
    }


    public String delete(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpDelete httpDelete = new HttpDelete();

        httpDelete.setURI(uri);
        httpDelete.setHeaders(headers);
        //httpDelete.setEntity(body);

        HttpResponse response = httpClient.execute(httpDelete);
        String responseBody = getResponseBody(response);

        httpDelete.releaseConnection();

        return responseBody;
    }

    private String getResponseBody(HttpResponse httpResponse) throws Exception {
        int status = httpResponse.getStatusLine().getStatusCode();
        String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
        String response = httpResponse.getEntity() != null
                ? EntityUtils.toString(httpResponse.getEntity(),"UTF-8") : null;

        if(status >= 200 && status < 300){
            return response;
        }else if(status >= 400){
            throw new Exception(errorMessage);
        }else{
            return response;
        }
    }

    // outsource the HttpClient.execute() Method for better testing
    public HttpResponse executePost(HttpPost httpPost) throws IOException {
        return httpClient.execute(httpPost);

    }
}
