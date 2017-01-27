package com.hidezo.app.buyer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezamisystem2 on 2016/12/03.
 *
 */
class HDZApiResponseLogin extends HDZApiResponse {

    public String id = "";

    public boolean parseJson(final String str_json) {

        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);
                if ( !json.isNull("id") ) {
                    id = json.getString("id");
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
