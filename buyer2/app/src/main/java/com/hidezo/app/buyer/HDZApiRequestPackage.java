package com.hidezo.app.buyer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiRequestPackage {

    static class LoginCheck {
        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("login_check/store",callbacks);
        }
    }

    static class Login {
        public void begin(final String id,final String uuid,final String password, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();

            HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("password",password);

            request.beginPost("login/store", params, callbacks);
        }
    }

    static class Friend {
        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("store/friend",callbacks);
        }
    }

    static class Item {
        public void begin(final String id,final String uuid,final String supplier_id, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("store/item",callbacks);
        }
    }

     static class OrderList {
        public void begin(final String id,final String uuid,final int page, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("page", String.valueOf(page));

            request.beginRequest("store/order_list",callbacks);
        }
    }

    static class OrderDetail {
        public void begin(final String id,final String uuid,final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest("store/order_detail",callbacks);
        }
    }

    static class Order {
        public void begin(final String id,final String uuid,final String supplier_id,final ArrayList<String> dynamic_items,final ArrayList<String> static_items,final String delivery_day,final String charge,final String deliver_to, HDZClient.HDZCallbacks callbacks) {
            //
            HDZApiRequest request = new HDZApiRequest();

            HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("supplier_id",supplier_id);
            params.put("delivery_day",delivery_day);
            params.put("deliver_to",deliver_to);
            params.put("charge",charge);

            request.beginOrder("store/order", params, dynamic_items, static_items, callbacks);
        }
    }

    static class Message {
        public void begin(final String id,final String uuid,final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest("store/message",callbacks);
        }
    }

    static class AddMessage {
        public void begin(final String id,final String uuid,final String charge, final String message,final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();

            HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("charge",charge);
            params.put("message",message);
            params.put("order_no",order_no);

            request.beginPost("store/add_message", params, callbacks);
        }
    }

    static class Badge {
        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("store/badge",callbacks);
        }
    }

    static class CheckDynamicItems {
        public void begin(final String id,final String uuid,final String supplier_id, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("store/check_dynamic_items",callbacks);
        }
    }

    static class sendDeviceToken {
        public void begin(final String id,final String uuid, final String device_token, HDZClient.HDZCallbacks callbacks) {
            HDZApiRequest request = new HDZApiRequest();

            HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("device_token",device_token);
            params.put("device_flg","2"); // Androidは２を送る

            request.beginPost("store/device_token", params, callbacks);
        }
    }

}
