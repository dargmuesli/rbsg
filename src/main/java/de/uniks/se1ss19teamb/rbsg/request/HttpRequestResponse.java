package de.uniks.se1ss19teamb.rbsg.request;

public class HttpRequestResponse {

    public String body;
    private int status;
    private String errorMsg;

    public HttpRequestResponse(String body, int status, String errorMsg) {
        this.body = body;
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
