package de.uniks.se1ss19teamb.rbsg;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;

public class HTTPManager {

    public HTTPManager() {
        this.httpClient = HttpClients.createDefault();
    }

    private final CloseableHttpClient httpClient;

    public String get(URI uri, Header[] headers) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpGet httpGet = new HttpGet();

        httpGet.setURI(uri);
        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        //String responseBody = getResponseBody(execute(httpGet));

        httpGet.releaseConnection();

        // TODO: responseBody
        //return responseBody;
        return null;
    }

    public String post(URI uri, Header[] headers, HttpEntity body) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPost httpPost = new HttpPost();

        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);

        HttpResponse response = httpClient.execute(httpPost);
        //String responseBody = getResponseBody(execute(httpPost));

        httpPost.releaseConnection();

        // TODO: responseBody
        //return responseBody;
        return null;
    }


    public String delete(URI uri, Header[] headers, HttpEntity body) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpDelete httpDelete = new HttpDelete();

        httpDelete.setURI(uri);
        httpDelete.setHeaders(headers);
        //httpDelete.setEntity(body);

        HttpResponse response = httpClient.execute(httpDelete);
        //String responseBody = getResponseBody(execute(httpDelete));

        httpDelete.releaseConnection();

        //TODO: responseBody
        //return responseBody;
        return null;
    }
}
