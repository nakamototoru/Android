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

//    private static final String _baseUrl = "https://dev-api.hidezo.co/";
//    private static final String _baseUrlRelease = "https://api.hidezo.co/";

    private static HDZCallbacks hdzCallbacks = null;

    private static String getBaseUrl() {

        final String _baseUrl = "https://dev-api.hidezo.co/";
        final String _baseUrlRelease = "https://api.hidezo.co/";

        if (BuildConfig.DEBUG) {
            return _baseUrl;
        }
        return _baseUrlRelease;
    }
    private static String getRequestUrl(final String apiName) {
        return getBaseUrl() + apiName + "?";
    }

    static class Get {

        /**
         *
         * @param url request url
         * @param callbacks result callback
         * @param apiName api name
         */
        private void runAsync(final String url, final HDZCallbacks callbacks, final String apiName) {

            hdzCallbacks = callbacks;
            //リクエスト開始
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
                    }
                }
                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

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
        void runAsync(final String apiName, final HashMap<String,String> params, final HDZCallbacks callbacks) {

            String requestUrl = getRequestUrl(apiName); //getBaseUrl() + apiName + "?";
//            if (BuildConfig.DEBUG) {
//                requestUrl = _baseUrlRelease + apiName + "?";
//            }
//            else {
//                requestUrl = _baseUrlRelease + apiName + "?";
//            }
            int count = 0;
            for ( final HashMap.Entry<String, String> e : params.entrySet() ) {
                if (count != 0) {
                    requestUrl += "&";
                }
                requestUrl += e.getKey() + "=" + e.getValue();
                count++;
            }
            Log.d("Client",requestUrl);
            runAsync(requestUrl,callbacks,apiName);
        }

    }

    static class Post {

        /**
         *
         * @param apiName api name
         * @param postData post data
         * @param callbacks callback
         */
        void runAsync(final String apiName, final FormBody.Builder postData, final HDZCallbacks callbacks) {

            hdzCallbacks = callbacks;

            final String requestUrl = getRequestUrl(apiName); //getBaseUrl() + apiName + "?";
//            if (BuildConfig.DEBUG) {
//                requestUrl = _baseUrlRelease + apiName;
//            }
//            else {
//                requestUrl = _baseUrlRelease + apiName;
//            }
            Log.d("Client",requestUrl);

            final OkHttpClient client = new OkHttpClient();
            final Request.Builder builder = new Request.Builder().url(requestUrl);

            // Body
            builder.post(postData.build());

            final Request request = builder.build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
                    }
                }
                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    if (hdzCallbacks != null) {
                        hdzCallbacks.HDZClientComplete(response.body().string(), apiName);
                    }
                }
            });
        }

        /**
         *
         * @param apiName api name
         * @param paramMap param hash map
         * @param callbacks callback
         */
        void runAsync(final String apiName, final HashMap<String ,String> paramMap, final HDZCallbacks callbacks) {

            // Body
            final FormBody.Builder postData = new FormBody.Builder();
            for(final HashMap.Entry<String, String> e : paramMap.entrySet()) {
                postData.add(e.getKey(),e.getValue());
            }
            runAsync(apiName,postData,callbacks);

//            hdzCallbacks = callbacks;
//
//            final String requestUrl = getRequestUrl(apiName); //getBaseUrl() + apiName + "?";
//            Log.d("Client",requestUrl);
//
//            final OkHttpClient client = new OkHttpClient();
//            final Request.Builder builder = new Request.Builder().url(requestUrl);
//
//            if (paramMap != null) {
//                // Body
//                final FormBody.Builder postData = new FormBody.Builder();
//                for(final HashMap.Entry<String, String> e : paramMap.entrySet()) {
//                    postData.add(e.getKey(),e.getValue());
//                }
//                builder.post(postData.build());
//            }
//
//            final Request request = builder.build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(final Call call, final IOException e) {
//                    if (hdzCallbacks != null) {
//                        hdzCallbacks.HDZClientError("Error:HDZClient.runAsync");
//                    }
//                }
//                @Override
//                public void onResponse(final Call call, final Response response) throws IOException {
//                    if (!response.isSuccessful()) {
//                        throw new IOException("Unexpected code " + response);
//                    }
//                    if (hdzCallbacks != null) {
//                        hdzCallbacks.HDZClientComplete(response.body().string(), apiName);
//                    }
//                }
//            });

        }

    }
}
