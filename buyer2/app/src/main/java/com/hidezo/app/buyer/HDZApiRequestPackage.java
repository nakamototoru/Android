package com.hidezo.app.buyer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public class HDZApiRequestPackage {

    public static class LoginCheck {

        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("login_check/store",callbacks);
        }
    }

    public static class Login {

        public void begin(final String id,final String uuid,final String password, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("password",password);

            request.beginPost("login/store", params, callbacks);
        }
    }

    public static class Friend {

        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("store/friend",callbacks);
        }
    }

    public static class Item {

        public void begin(final String id,final String uuid,final String supplier_id, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("store/item",callbacks);
        }
    }

    public static class OrderList {

        public void begin(final String id,final String uuid,final int page, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("page", String.valueOf(page));

            request.beginRequest("store/order_list",callbacks);
        }
    }

    public static class OrderDetail {

        public void begin(final String id,final String uuid,final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest("store/order_detail",callbacks);
        }
    }

    public static class Order {

        public void begin(final String id, final String uuid, final String supplier_id, final ArrayList<String> static_item, final ArrayList<String> dynamic_item, final String deliver_to, final String delivery_day, final String charge, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();

            /*
             let id: String
    let uuid: String
    let supplier_id: String
    let static_item: [String] // ID,個数　カンマ区切り
    let dynamic_item: [String] // ID,個数　カンマ区切り
    let deliver_to: String
    let delivery_day: String
    let charge: String
             */
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("id",id);
            params.put("uuid",uuid);

            request.beginPost("store/order", params, callbacks);
        }
    }

    public static class Message {

        public void begin(final String id,final String uuid,final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("order_no", order_no);

            request.beginRequest("store/message",callbacks);
        }
    }

    public static class AddMessage {

        public void begin(final String id,final String uuid,final String charge, final String message, final String order_no, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();

            /*
             let id: String
    let uuid: String
    let charge: String
    let message: String
    let order_no: String
             */
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("id",id);
            params.put("uuid",uuid);
            params.put("charge",charge);
            params.put("message",message);
            params.put("order_no",order_no);

            request.beginPost("store/add_message", params, callbacks);
        }
    }

}
