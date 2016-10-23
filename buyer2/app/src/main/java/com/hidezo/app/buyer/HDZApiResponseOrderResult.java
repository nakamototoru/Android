package com.hidezo.app.buyer;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/28.
 *
 */
class HDZApiResponseOrderResult extends HDZApiResponse {

    public String orderNo = "";

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                orderNo = json.getString("order_no");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
