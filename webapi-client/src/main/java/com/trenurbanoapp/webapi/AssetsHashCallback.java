package com.trenurbanoapp.webapi;

/**
 * Created by victor on 6/25/14.
 */
public interface AssetsHashCallback {

    void execute(String token, String assetsHash, WebApiRestClient restClient);

}
