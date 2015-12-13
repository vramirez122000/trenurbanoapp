package com.trenurbanoapp.webapi;

import org.apache.http.HttpMessage;

/**
 * Created by victor on 12/12/15.
 */
@FunctionalInterface
public interface RequestCallback {
    void doWithRequest(HttpMessage request);
}
