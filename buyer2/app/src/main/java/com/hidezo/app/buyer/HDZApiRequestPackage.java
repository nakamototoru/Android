package com.hidezo.app.buyer;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiRequestPackage {

    private static final String TAG = "#RequestPackage";
//    static class LoginCheck {
//        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {
//
//            HDZApiRequest request = new HDZApiRequest();
//            request.putKeyAndValue("id", id);
//            request.putKeyAndValue("uuid", uuid);
//
//            request.beginRequest("login_check/store",callbacks);
//        }
//    }

    static class Login {
        public static final String apiName = "store/login/attempt";

        public void begin(final String login_id,final String uuid,final String pass, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();

            final HashMap<String,String> params = new HashMap<>();
            params.put("login_id",login_id);
            params.put("uuid",uuid);
            params.put("pass",pass);
            params.put("device_div","2"); // for Android

            request.beginPost(apiName, params, callbacks);
        }
    }

    static class Friend {
        public void begin(final String id,final String uuid, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("store/friend",callbacks);
        }
    }

    static class Item {
        public void begin(final String id,final String uuid,final String supplier_id, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("store/item",callbacks);
        }
    }

    static class OrderdItem {
        public static final String apiName = "store/orderd_item";
        public void begin(final String id,final String uuid,final String supplier_id, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest(apiName,callbacks);
        }
    }

    static class OrderList {
        public void begin(final String id,final String uuid,final int page, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("page", String.valueOf(page));

            request.beginRequest("store/order_list",callbacks);
        }
    }

    static class OrderDetail {
        public void begin(final String id,final String uuid,final String order_no, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest("store/order_detail",callbacks);
        }
    }

    static class Order {
        public static final String apiName = "store/order";
        public void begin(final String id,final String uuid,final String supplier_id,final ArrayList<String> dynamic_items,final ArrayList<String> static_items,final String delivery_day,final String charge,final String deliver_to, final HDZClient.HDZCallbacks callbacks) {
            //
            final HDZApiRequest request = new HDZApiRequest();

            final HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("supplier_id",supplier_id);
            params.put("delivery_day",delivery_day);
            params.put("deliver_to",deliver_to);
            params.put("charge",charge);

            Log.d(TAG,"delivery_day = " + delivery_day);

            request.beginOrder(apiName, params, dynamic_items, static_items, callbacks); // "store/order"
        }
    }

    static class Message {
        public static final String apiName = "store/message";
        public void begin(final String id,final String uuid,final String order_no, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest(apiName,callbacks); // "store/message"
        }
    }

    static class AddMessage {
        public static final String apiName = "store/add_message";
        public void begin(final String id,final String uuid,final String charge, final String message,final String order_no, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();

            final HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("charge",charge);
            params.put("message",message);
            params.put("order_no",order_no);

            request.beginPost(apiName, params, callbacks);
        }
    }

    static class Badge {
        public static final String apiName = "store/badge";
        public void begin(final String id,final String uuid, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.beginRequest(apiName,callbacks); // "store/badge"
        }
    }

    static class CheckDynamicItems {
        public static final String apiName = "store/check_dynamic_items";
        public void begin(final String id,final String uuid,final String supplier_id, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();

            final HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("supplier_id",supplier_id);

            request.beginPost(apiName, params, callbacks);
        }
    }

    static class faxDoc {
        public static final String apiName = "store/faxdoc";
        public void begin(final String id,final String uuid, final String order_no, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);
            request.beginRequest(apiName,callbacks);
        }
    }

    static class OrderMethod {
        public static final String apiName = "store/order_method";
        public void begin(final String id,final String uuid, final String supplier_id, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);
            request.beginRequest(apiName,callbacks);
        }
    }

    static class sendDeviceToken {
        public static final String apiName = "store/device_token";
        public void begin(final String id,final String uuid, final String device_token, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();

            final HashMap<String,String> params = new HashMap<>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("device_token",device_token);
            params.put("device_flg","2"); // Androidは２を送る

            request.beginPost(apiName, params, callbacks);
        }
    }

    static class logOut {
        public void begin(final String id,final String uuid, final HDZClient.HDZCallbacks callbacks) {
            final HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.beginRequest("logout",callbacks); // store/
        }
    }

}
