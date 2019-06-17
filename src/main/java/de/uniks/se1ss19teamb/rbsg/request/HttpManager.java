package de.uniks.se1ss19teamb.rbsg.request;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


class HttpManager {

    private final HttpClient httpClient;

    public HttpManager() {
        this.httpClient = HttpClients.createDefault();
    }

    public HttpManager(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpRequestResponse get(URI uri, Header[] headers) throws
        Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpGet httpGet = new HttpGet();

        httpGet.setURI(uri);
        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        HttpRequestResponse responseBody = getResponseBody(response);

        httpGet.releaseConnection();

        return responseBody;
    }

    public HttpRequestResponse post(URI uri, Header[] headers, HttpEntity body) throws
        Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPost httpPost = new HttpPost();

        httpPost.setURI(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);

        HttpResponse response = httpClient.execute(httpPost);
        HttpRequestResponse responseBody = getResponseBody(response);

        httpPost.releaseConnection();

        return responseBody;
    }

    public HttpRequestResponse put(URI uri, Header[] headers, HttpEntity body) throws
        Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpPut httpPut = new HttpPut();

        httpPut.setURI(uri);
        httpPut.setHeaders(headers);
        httpPut.setEntity(body);

        HttpResponse response = httpClient.execute(httpPut);
        HttpRequestResponse responseBody = getResponseBody(response);

        httpPut.releaseConnection();

        return responseBody;
    }


    public HttpRequestResponse delete(URI uri, Header[] headers, HttpEntity body) throws
        Exception {
        assert (uri != null);
        assert (!uri.toString().equals(""));

        final HttpDelete httpDelete = new HttpDelete();

        httpDelete.setURI(uri);
        httpDelete.setHeaders(headers);
        //httpDelete.setEntity(body);

        HttpResponse response = httpClient.execute(httpDelete);
        HttpRequestResponse responseBody = getResponseBody(response);

        httpDelete.releaseConnection();

        return responseBody;
    }

    private HttpRequestResponse getResponseBody(HttpResponse httpResponse) throws Exception {
        int status = httpResponse.getStatusLine().getStatusCode();
        String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
        String response = httpResponse.getEntity() != null
            ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : null;

        return new HttpRequestResponse(response, status, errorMessage);

    }
}
