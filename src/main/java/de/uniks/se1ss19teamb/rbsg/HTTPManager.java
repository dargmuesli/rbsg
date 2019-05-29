package de.uniks.se1ss19teamb.rbsg;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import de.uniks.se1ss19teamb.rbsg.request.HTTPResponse;

import java.net.URI;

public class HTTPManager {

    private final HttpClient httpClient;

    public HTTPManager() {
        this.httpClient = HttpClients.createDefault();
    }

    public HTTPManager(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HTTPResponse get(URI uri, Header[] headers) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpGet httpGet = new HttpGet();

        httpGet.setURI(uri);
        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        HTTPResponse responseBody = getResponseBody(response);

        httpGet.releaseConnection();

        return responseBody;
    }

    public HTTPResponse post(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPost httpPost = new HttpPost();

        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);

        HttpResponse response = httpClient.execute(httpPost);
        HTTPResponse responseBody = getResponseBody(response);

        httpPost.releaseConnection();

        return responseBody;
    }


    public HTTPResponse delete(URI uri, Header[] headers, HttpEntity body) throws
            Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpDelete httpDelete = new HttpDelete();

        httpDelete.setURI(uri);
        httpDelete.setHeaders(headers);
        //httpDelete.setEntity(body);

        HttpResponse response = httpClient.execute(httpDelete);
        HTTPResponse responseBody = getResponseBody(response);

        httpDelete.releaseConnection();

        return responseBody;
    }

    private HTTPResponse getResponseBody(HttpResponse httpResponse) throws Exception {
        int status = httpResponse.getStatusLine().getStatusCode();
        String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
        String response = httpResponse.getEntity() != null
                ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : null;

        return new HTTPResponse(response, status, errorMessage);
        
    }
}
