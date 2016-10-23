package com.hidezo.app.buyer;

//import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class HDZOrderDetail {

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
