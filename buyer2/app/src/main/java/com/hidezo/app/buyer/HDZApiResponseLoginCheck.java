package com.hidezo.app.buyer;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZApiResponseLoginCheck extends HDZApiResponse {

    public String status = "";

    @Override
    public boolean parseJson(final String str_json) {
        boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(str_json);

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
