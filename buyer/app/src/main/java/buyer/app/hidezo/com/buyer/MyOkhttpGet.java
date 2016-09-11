package buyer.app.hidezo.com.buyer;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import okhttp3.Callback;
import okhttp3.Call;

import org.json.JSONObject;

import android.util.Log;

/**
 * Created by dezami on 2016/09/11.
 */
public class MyOkhttpGet {

    private final OkHttpClient client = new OkHttpClient();

    private MyOkhttpCallbacks _myCallbacks = null;

    //  throws Exception
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

}