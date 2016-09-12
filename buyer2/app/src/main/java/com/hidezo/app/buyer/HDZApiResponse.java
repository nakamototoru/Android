package com.hidezo.app.buyer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class HDZApiResponse {

    public String message = "";
    public boolean result = false;

    public boolean parseJson(final String strjson) {

        result = false;
        try {
            JSONObject json = new JSONObject(strjson);
            result = json.getBoolean("result");
            message = json.getString("message");
            if (!result) {
                Log.d("########",message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
