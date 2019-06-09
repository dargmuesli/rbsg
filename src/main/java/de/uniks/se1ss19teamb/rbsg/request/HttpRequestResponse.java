package de.uniks.se1ss19teamb.rbsg.request;

public class HttpRequestResponse {
    
    public HttpRequestResponse(String body, int status, String errorMsg) {
        this.body = body;
        this.status = status;
        this.errorMsg = errorMsg;
    }
    
    private int status;
    
    private String errorMsg;
    
    public String body;
}
