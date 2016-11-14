package com.hidezo.app.buyer;

import android.util.Log;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiResponse {

    public String message = "";
    public boolean result = false;

    public boolean parseJson(final String str_json) {

        result = false;
        try {
            final JSONObject json = new JSONObject(str_json);
            result = json.getBoolean("result");
            message = json.getString("message");
            if (!result) {
                Log.d("########",message);
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
