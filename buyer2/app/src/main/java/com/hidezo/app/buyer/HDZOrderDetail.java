package com.hidezo.app.buyer;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZOrderDetail {

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

    static class Info {
        String attr_flg = "";
        String deliveryFee = "";
        String subTotal = "";
        String total = "";
        String order_no = "";
        String deliver_to = "";
        String delivery_day = "";
        String charge = "";
        String order_at = "";
    }

    static class Item {
        String id = "";
        String name = "";
        String price = "";
        String order_num = "";
        String code = "";
        String standard = "";
        String scale = "";
        String loading = "";
        boolean isDynamc = false;
    }

}
