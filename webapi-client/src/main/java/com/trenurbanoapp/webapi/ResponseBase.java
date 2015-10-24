package com.trenurbanoapp.webapi;

/**
 * Created by victor on 6/15/14.
 */
public class ResponseBase {

    private int statusCode;
    private String statusMessage;

    public ResponseBase(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isError() {
        return statusCode != 200;
    }
}
