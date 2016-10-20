package com.hidezo.app.buyer;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
//import okhttp3.Headers;
//import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
//import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZClient {

    interface HDZCallbacks {
        void HDZClientComplete(String response,String apiName);
        void HDZClientError(String message);
    }

    private static final String _baseUrl = "https://dev-api.hidezo.co/"; // "https://api.hidezo.co/store/";
    private static final String _baseUrlRelease = "https://api.hidezo.co/";

    private static HDZCallbacks hdzCallbacks = null;

    static class Get {

        private void runAsync(final String url, HDZCallbacks callbacks, final String apiName) {

            hdzCallbacks = callbacks;
            //リクエスト開始
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientComplete(response.body().string(), apiName);
                    }
                }
            });
        }

        /**
         *
         * @param apiName : ApiName
         * @param params : Request Parameter Map
         * @param callbacks : Result After
         */
        void runAsync(final String apiName, final HashMap<String,String> params, HDZCallbacks callbacks) {

            String requestUrl;
            if (BuildConfig.DEBUG) {
                requestUrl = _baseUrl + apiName + "?";
            }
            else {
                requestUrl = _baseUrlRelease + apiName + "?";
            }
            int count = 0;
            for ( HashMap.Entry<String, String> e : params.entrySet() ) {
                if (count != 0) {
                    requestUrl += "&";
                }
                requestUrl += e.getKey() + "=" + e.getValue();
                count++;
            }
            Log.d("########",requestUrl);
            runAsync(requestUrl,callbacks,apiName);
        }

    }

    static class Post {

        void runAsync(final String apiName, final HashMap<String ,String> paramMap, HDZCallbacks callbacks) {

            hdzCallbacks = callbacks;

            String requestUrl;
            if (BuildConfig.DEBUG) {
                requestUrl = _baseUrl + apiName;
            }
            else {
                requestUrl = _baseUrlRelease + apiName;
            }

            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder().url(requestUrl);

            if (paramMap != null) {
                // Body
                FormBody.Builder postData = new FormBody.Builder();
                for(HashMap.Entry<String, String> e : paramMap.entrySet()) {
                    postData.add(e.getKey(),e.getValue());
                }
                builder.post(postData.build());
            }

            Request request = builder.build(); //new Request.Builder()

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientComplete(response.body().string(), apiName);
                    }
                }
            });

        }

        void runAsync(final String apiName, final FormBody.Builder postData, HDZCallbacks callbacks) {

            hdzCallbacks = callbacks;

            String requestUrl;
            if (BuildConfig.DEBUG) {
                requestUrl = _baseUrl + apiName;
            }
            else {
                requestUrl = _baseUrlRelease + apiName;
            }

            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder().url(requestUrl);

            // Body
            builder.post(postData.build());

            Request request = builder.build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientComplete(response.body().string(), apiName);
                    }
                }
            });
        }
    }
}
