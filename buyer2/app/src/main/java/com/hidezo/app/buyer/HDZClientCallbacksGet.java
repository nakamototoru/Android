package com.hidezo.app.buyer;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public interface HDZClientCallbacksGet {

    void hdzClientCallbackGetComplete(String response);
    void hdzClientCallbackGetError(String message);

}
