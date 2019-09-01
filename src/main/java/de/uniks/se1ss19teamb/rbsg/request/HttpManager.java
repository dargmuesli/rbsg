package de.uniks.se1ss19teamb.rbsg.request;

import java.io.IOException;
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


public class HttpManager {

    private final HttpClient httpClient;

    HttpManager() {
        this.httpClient = HttpClients.createDefault();
    }

    public HttpManager(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Forms a GET request to the specified URI with given headers.
     *
     * @param uri          The request's endpoint.
     * @param headers      The request's headers.
     * @return             The server's response.
     * @throws IOException In case the request fails or the response body could not be retrieved.
     */
    public HttpRequestResponse get(URI uri, Header[] headers) throws IOException {
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

    /**
     * Forms a POST request to the specified URI with given headers.
     *
     * @param uri          The request's endpoint.
     * @param headers      The request's headers.
     * @param body         The request's body.
     * @return             The server's response.
     * @throws IOException In case the request fails or the response body could not be retrieved.
     */
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

    /**
     * Forms a PUT request to the specified URI with given headers.
     *
     * @param uri          The request's endpoint.
     * @param headers      The request's headers.
     * @param body         The request's body.
     * @return             The server's response.
     * @throws IOException In case the request fails or the response body could not be retrieved.
     */
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

    /**
     * Forms a DELETE request to the specified URI with given headers.
     *
     * @param uri          The request's endpoint.
     * @param headers      The request's headers.
     * @param body         The request's body.
     *                     Exists just in case the server suddenly demands one.
     * @return             The server's response.
     * @throws IOException In case the request fails or the response body could not be retrieved.
     */
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

    private HttpRequestResponse getResponseBody(HttpResponse httpResponse) throws IOException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
        String response = httpResponse.getEntity() != null
            ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : null;

        return new HttpRequestResponse(response, status, errorMessage);
    }
}
