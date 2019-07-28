package de.uniks.se1ss19teamb.rbsg.request;

public class HttpRequestResponse {

    public String body;

    public HttpRequestResponse(String body, int status, String errorMsg) {
        this.body = body;
    }
}
