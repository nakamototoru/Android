package com.hidezo.app.buyer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZApiResponseOrderDetail extends HDZApiResponse {

    public HDZOrderDetail orderDetail = new HDZOrderDetail();

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                // order
                if ( !json.isNull("order") ) {
//                     json_order = json.getJSONArray("order");
                    JSONObject json_order = json.getJSONObject("order");
                    orderDetail.order.order_no = json_order.getString("order_no");

//                    if (friendList.length() > 0) {
//                        for (int i = 0; i < friendList.length(); i++) {
//                            HDZOrderDetail info = new HDZOrderDetail();
//                            info.order_no = friendList.getJSONObject(i).getString("order_no");
//                            info.supplier = friendList.getJSONObject(i).getString("supplier");
//                            info.supplier_name = friendList.getJSONObject(i).getString("supplier_name");
//                            info.order_at = friendList.getJSONObject(i).getString("order_at");
//                            info.deliver_at = friendList.getJSONObject(i).getString("deliver_at");
//
//                            orderedList.add(info);
//                        }
//                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
