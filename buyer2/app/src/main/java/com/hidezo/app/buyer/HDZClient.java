package com.hidezo.app.buyer;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public class HDZClient {

    public static final String _baseUrl = "https://dev-api.hidezo.co/"; // "https://api.hidezo.co/store/";
    public static final String _baseUrlRelease = "https://api.hidezo.co/";

    private static HDZClientCallbacksGet _myCallbacks = null;

    public static class Get {

        private void runAsync(final String url, HDZClientCallbacksGet callbacks, final String apiname) {

            _myCallbacks = callbacks;
            //リクエスト開始
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    _myCallbacks.hdzClientCallbackGetError("Error:HDZClient.runAsync");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    _myCallbacks.hdzClientCallbackGetComplete(response.body().string(),apiname);
                }
            });
        }

        /**
         *
         * @param apiname : ApiName
         * @param params : Request Parameter Map
         * @param callbacks : Result After
         */
        public void runAsync(final String apiname, final HashMap<String,String> params, HDZClientCallbacksGet callbacks) {

            String requestUrl;
            if (BuildConfig.DEBUG) {
                requestUrl = _baseUrl + apiname + "?";
            }
            else {
                requestUrl = _baseUrlRelease + apiname + "?";
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
            runAsync(requestUrl,callbacks,apiname);
        }

    }

    public static class Post {

        public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

        public void run() throws Exception {
            String postBody = ""
                    + "Releases\n"
                    + "--------\n"
                    + "\n"
                    + " * _1.0_ May 6, 2013\n"
                    + " * _1.1_ June 15, 2013\n"
                    + " * _1.2_ August 11, 2013\n";

            Request request = new Request.Builder()
                    .url("https://api.github.com/markdown/raw")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }
}
