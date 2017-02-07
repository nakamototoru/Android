package com.hidezo.app.buyer;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezamisystem2 on 2017/02/07.
 *
 */
class HDZApiResponseOrderMethod extends HDZApiResponse {

    String method = "";

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                method = json.getString("method");

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
