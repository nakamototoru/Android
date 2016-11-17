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

    HDZOrderDetail.Info orderInfo = new HDZOrderDetail.Info();
    ArrayList<HDZOrderDetail.Item> itemList = new ArrayList<>();

    @Override
    public boolean parseJson(final String strJson) {
        final boolean isSuccess = super.parseJson(strJson);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(strJson);

                orderInfo.attr_flg = json.getString("attr_flg");
                orderInfo.deliveryFee = json.getString("deliveryFee");
                orderInfo.subTotal = json.getString("subtotal");
                orderInfo.total = json.getString("total");

                // order
                if ( !json.isNull("order") ) {
                    final JSONObject json_order = json.getJSONObject("order");
                    orderInfo.order_no = json_order.getString("order_no");
                    orderInfo.deliver_to = json_order.getString("deliver_to");
                    orderInfo.delivery_day = json_order.getString("delivery_day");
                    orderInfo.charge = json_order.getString("charge");
                    orderInfo.order_at = json_order.getString("order_at");
                }
                // item dynamic
                if ( !json.isNull("dynamicItemList") ) {
                    final JSONArray json_dynamics = json.getJSONArray("dynamicItemList");
                    for (int i = 0; i < json_dynamics.length(); i++) {
                        final HDZOrderDetail.Item item = new HDZOrderDetail.Item();
                        item.id = json_dynamics.getJSONObject(i).getString("id");
                        item.name = json_dynamics.getJSONObject(i).getString("name");
                        item.price = json_dynamics.getJSONObject(i).getString("price");
                        item.order_num = json_dynamics.getJSONObject(i).getString("order_num");
                        item.code = json_dynamics.getJSONObject(i).getString("code");
                        item.isDynamc = true;
                        itemList.add(item);
                    }
                }
                // item static
                if ( !json.isNull("staicItemList") ) {
                    final JSONArray json_statics = json.getJSONArray("staicItemList");
                    for (int i = 0; i < json_statics.length(); i++) {
                        final HDZOrderDetail.Item item = new HDZOrderDetail.Item();
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


            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
