package com.trenurbanoapp.webapi;

/**
 * Created by victor on 6/16/14.
 */
public class LoginResponse extends ResponseBase {

    private String token;
    private String assetsHash;

    public LoginResponse(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }

    public LoginResponse(String token, String assetsHash) {
        super(200, "OK");
        this.token = token;
        this.assetsHash = assetsHash;
    }

    public String getToken() {
        return token;
    }

    public String getAssetsHash() {
        return assetsHash;
    }
}
