package com.hidezo.app.buyer;

import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiRequest {

    private final HashMap<String, String> mapParams = new HashMap<String,String>(); // 連想配列に格納

    void putKeyAndValue(final String key, final String value) {
        mapParams.put(key,value);
    }

    public HashMap<String, String> getParams() {
        return mapParams;
    }

    void beginRequest(final String apiname, HDZClient.HDZCallbacks callbacks) {

        final HDZClient.Get httpget = new HDZClient.Get();
        if (mapParams.size() == 0) {
            return;
        }
        httpget.runAsync(apiname, mapParams, callbacks);
    }

    void beginPost(final String apiname, final HashMap<String,String> paramMap, HDZClient.HDZCallbacks callbacks) {

        final HDZClient.Post httppost = new HDZClient.Post();
        if (paramMap.size() == 0) {
            return;
        }
        httppost.runAsync(apiname, paramMap, callbacks);
    }
}
