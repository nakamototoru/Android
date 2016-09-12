package com.hidezo.app.buyer;

import java.util.HashMap;

/**
 *
 */
public class HDZApiRequest {

    private final HashMap<String, String> mapParams = new HashMap<String,String>(); // 連想配列に格納

    private final MyOkhttpGet httpget = new MyOkhttpGet();

    public void putKeyAndValue(final String key, final String value) {
        mapParams.put(key,value);
    }

    public HashMap<String, String> getParams() {
        return mapParams;
    }

    public void beginRequest(final String apiname, MyOkhttpCallbacks callbacks) {
        if (mapParams.size() == 0) {
            return;
        }
        httpget.runAsync(apiname, mapParams, callbacks);
    }
}
