package com.hidezo.app.buyer;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiRequest {

    private final HashMap<String, String> mapParams = new HashMap<>(); // 連想配列に格納

    void putKeyAndValue(final String key, final String value) {
        mapParams.put(key,value);
    }

//    public HashMap<String, String> getParams() {
//        return mapParams;
//    }

    void beginRequest(final String apiName, HDZClient.HDZCallbacks callbacks) {

        final HDZClient.Get httpGet = new HDZClient.Get();
        if (mapParams.size() == 0) {
            return;
        }
        httpGet.runAsync(apiName, mapParams, callbacks);
    }

    void beginPost(final String apiName, final HashMap<String,String> paramMap, HDZClient.HDZCallbacks callbacks) {

        final HDZClient.Post httpPost = new HDZClient.Post();
        if (paramMap.size() == 0) {
            return;
        }

        if (BuildConfig.DEBUG) {
            for (HashMap.Entry<String,String> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                Log.d("## Order", key + ":" + val);
            }
        }

        httpPost.runAsync(apiName, paramMap, callbacks);
    }
}
