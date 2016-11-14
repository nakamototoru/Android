package com.hidezo.app.buyer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/28.
 *
 */
class HDZApiResponseOrderResult extends HDZApiResponse {

    public String orderNo = "";

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                orderNo = json.getString("order_no");

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
