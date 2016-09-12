package com.hidezo.app.buyer;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import okhttp3.Callback;
import okhttp3.Call;

import java.util.HashMap;

import android.util.Log;

/**
 *
 */
public class MyOkhttpGet {

    private final OkHttpClient client = new OkHttpClient();
    private final String _baseUrl = "https://api.hidezo.co/store/";

    private MyOkhttpCallbacks _myCallbacks = null;

    public void runAsync(final String url, MyOkhttpCallbacks callbacks) {

        _myCallbacks = callbacks;

        //リクエスト開始
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                _myCallbacks.myOkhttpCallbackError("Error:MyOkhttpGet.runAsync");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                _myCallbacks.myOkhttpCallbackComplete(response.body().string());
            }

        });
    }

    /**
     *
     * @param apiname : ApiName
     * @param params : Request Parameter Map
     * @param callbacks : Result After
     */
    public void runAsync(final String apiname, final HashMap<String,String> params, MyOkhttpCallbacks callbacks) {

        String requestUrl = _baseUrl + apiname + "?";

        // foreach()みたいな動作にする
        // Map.Entry<keyの型, 値の型>とする
        int count = 0;
        for ( HashMap.Entry<String, String> e : params.entrySet() ) {
            if (count != 0) {
                requestUrl += "&";
            }
            requestUrl += e.getKey() + "=" + e.getValue();

            count++;
        }

        Log.d("########",requestUrl);
        runAsync(requestUrl,callbacks);
    }

}