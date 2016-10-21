package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/10/21.
 *
 */

class HDZApiResponseBadge extends HDZApiResponse {

    public static class SupplierUp {
        String supplierId = "";
    }
    public static class MessageUp {
        String order_no = "";
        int messageCount = 0;
    }

    public ArrayList<SupplierUp> badgeSupplierList = new ArrayList<>();
    public ArrayList<MessageUp> badgeMessageList = new ArrayList<>();

    @Override
    public boolean parseJson(final String str_json) {
        boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(str_json);

                if ( !json.isNull("supplierUp") ) {
                    JSONObject json_supplier_up = json.getJSONObject("supplierUp");
                    JSONArray supplierList = json_supplier_up.getJSONArray("supplierUpList");
                    if (supplierList.length() > 0) {
                        for (int i = 0; i < supplierList.length(); i++) {
                            SupplierUp object = new SupplierUp();
                            object.supplierId = supplierList.getJSONObject(i).getString("supplierId");
                            if (object.supplierId.equals("")) {
                                break; // データなし＝中断
                            }

                            badgeSupplierList.add(object);
                        }
                    }
                }

                if ( !json.isNull("messageUp") ) {
                    JSONObject json_message_up = json.getJSONObject("messageUp");
                    JSONArray messageList = json_message_up.getJSONArray("messageUpList");
                    if (messageList.length() > 0) {
                        for (int i = 0; i < messageList.length(); i++) {
                            MessageUp object = new MessageUp();
                            object.order_no = messageList.getJSONObject(i).getString("order_no");
                            object.messageCount = messageList.getJSONObject(i).getInt("messageCount");
                            if (object.messageCount == 0 || object.order_no.equals("")) {
                                break; // データなし＝中断
                            }

                            badgeMessageList.add(object);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
