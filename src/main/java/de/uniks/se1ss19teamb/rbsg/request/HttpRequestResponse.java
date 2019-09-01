package de.uniks.se1ss19teamb.rbsg.request;

public class HttpRequestResponse {

    public String body;
    private int status;
    private String errorMsg;

    /**
     * Constructor for a http request's respnse with individual field values.
     *
     * @param body     The response's body.
     * @param status   The response's status code.
     * @param errorMsg The response's error message.
     */
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
