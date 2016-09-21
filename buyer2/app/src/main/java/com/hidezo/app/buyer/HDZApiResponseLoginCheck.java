package com.hidezo.app.buyer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/21.
 *
 */

class HDZApiResponseLoginCheck extends HDZApiResponse {

    String status = "";

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                if ( !json.isNull("status") ) {
                    status = json.getString("status");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
