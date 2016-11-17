package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZApiResponseOrderedList extends HDZApiResponse {

    ArrayList<HDZordered> orderedList = new ArrayList<>();

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                if ( !json.isNull("order_list") ) {
                    final JSONArray orderList = json.getJSONArray("order_list");
                    if (orderList.length() > 0) {
                        for (int i = 0; i < orderList.length(); i++) {
                            final HDZordered info = new HDZordered();
                            info.order_no = orderList.getJSONObject(i).getString("order_no");
                            info.supplier = orderList.getJSONObject(i).getString("supplier");
                            info.supplier_name = orderList.getJSONObject(i).getString("supplier_name");
                            info.order_at = orderList.getJSONObject(i).getString("order_at");
                            info.deliver_at = orderList.getJSONObject(i).getString("deliver_at");

                            orderedList.add(info);
                        }
                    }
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
