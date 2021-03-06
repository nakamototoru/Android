package com.hidezo.app.buyer;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZApiResponseLoginCheck extends HDZApiResponse {

    String status = "";

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                if ( !json.isNull("status") ) {
                    status = json.getString("status");
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
