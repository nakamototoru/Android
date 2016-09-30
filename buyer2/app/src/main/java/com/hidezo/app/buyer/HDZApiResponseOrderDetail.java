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

                /*
  "order": {
    "order_no": "6147055737622237186-00039",
    "deliver_to": "静岡",
    "delivery_day": "明日",
    "charge": "中本",
    "order_at": "08/17 16:34"
  },
    "attr_flg": "2",
  "deliveryFee": "1200",
  "subtotal": "1800",
  "total": "3000",
  "staicItemList": [
    {
      "id": "117",
      "name": "芋焼酎　赤霧島1820ml むらさきまさり",
      "price": "1500",
      "order_num": "1",
      "standard": "1800ml",
      "scale": "本",
      "loading": "1",
      "code": "a000-1234"
    }
  ],
  "dynamicItemList": [
    {
      "id": "116",
      "name": "山口県の宇部かまぼこ",
      "price": "400,300,250",
      "order_num": "1",
      "code": "001-01"
    }
  ],
     */
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
