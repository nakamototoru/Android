package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZApiResponseOrderDetail extends HDZApiResponse {

    public HDZOrderDetail.Info orderInfo = new HDZOrderDetail.Info();
    public ArrayList<HDZOrderDetail.Item> itemList = new ArrayList<>();

    @Override
    public boolean parseJson(final String strJson) {
        boolean isSuccess = super.parseJson(strJson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strJson);

                orderInfo.attr_flg = json.getString("attr_flg");
                orderInfo.deliveryFee = json.getString("deliveryFee");
                orderInfo.subTotal = json.getString("subtotal");
                orderInfo.total = json.getString("total");

                // order
                if ( !json.isNull("order") ) {
                    JSONObject json_order = json.getJSONObject("order");
                    orderInfo.order_no = json_order.getString("order_no");
                    orderInfo.deliver_to = json_order.getString("deliver_to");
                    orderInfo.delivery_day = json_order.getString("delivery_day");
                    orderInfo.charge = json_order.getString("charge");
                    orderInfo.order_at = json_order.getString("order_at");
                }
                // item dynamic
                if ( !json.isNull("dynamicItemList") ) {
                    JSONArray json_dynamics = json.getJSONArray("dynamicItemList");
                    for (int i = 0; i < json_dynamics.length(); i++) {
                        HDZOrderDetail.Item item = new HDZOrderDetail.Item();
                        item.id = json_dynamics.getJSONObject(i).getString("id");
                        item.name = json_dynamics.getJSONObject(i).getString("name");
                        item.price = json_dynamics.getJSONObject(i).getString("price");
                        item.order_num = json_dynamics.getJSONObject(i).getString("order_num");
                        item.code = json_dynamics.getJSONObject(i).getString("code");
                        itemList.add(item);
                    }
                }
                // item static
                if ( !json.isNull("staicItemList") ) {
                    JSONArray json_statics = json.getJSONArray("staicItemList");
                    for (int i = 0; i < json_statics.length(); i++) {
                        HDZOrderDetail.Item item = new HDZOrderDetail.Item();
                        item.id = json_statics.getJSONObject(i).getString("id");
                        item.name = json_statics.getJSONObject(i).getString("name");
                        item.price = json_statics.getJSONObject(i).getString("price");
                        item.order_num = json_statics.getJSONObject(i).getString("order_num");
                        item.standard = json_statics.getJSONObject(i).getString("standard");
                        item.scale = json_statics.getJSONObject(i).getString("scale");
                        item.loading = json_statics.getJSONObject(i).getString("loading");
                        item.code = json_statics.getJSONObject(i).getString("code");
                        itemList.add(item);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
