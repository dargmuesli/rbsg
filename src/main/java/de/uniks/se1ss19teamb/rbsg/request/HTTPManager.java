package de.uniks.se1ss19teamb.rbsg.request;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class HTTPManager {

    private final HttpClient httpClient;

    public HTTPManager() {
        this.httpClient = HttpClients.createDefault();
    }

    public HTTPManager(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HTTPRequestResponse get(URI uri, Header[] headers) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpGet httpGet = new HttpGet();

        httpGet.setURI(uri);
        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        HTTPRequestResponse responseBody = getResponseBody(response);

        httpGet.releaseConnection();

        return responseBody;
    }

    public HTTPRequestResponse post(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPost httpPost = new HttpPost();

        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);

        HttpResponse response = httpClient.execute(httpPost);
        HTTPRequestResponse responseBody = getResponseBody(response);

        httpPost.releaseConnection();

        return responseBody;
    }


    public HTTPRequestResponse delete(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpDelete httpDelete = new HttpDelete();

        httpDelete.setURI(uri);
        httpDelete.setHeaders(headers);
        //httpDelete.setEntity(body);

        HttpResponse response = httpClient.execute(httpDelete);
        HTTPRequestResponse responseBody = getResponseBody(response);

        httpDelete.releaseConnection();

        return responseBody;
    }

    private HTTPRequestResponse getResponseBody(HttpResponse httpResponse) throws Exception {
        int status = httpResponse.getStatusLine().getStatusCode();
        String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
        String response = httpResponse.getEntity() != null
                ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : null;

        return new HTTPRequestResponse(response, status, errorMessage);
        
    }
}
