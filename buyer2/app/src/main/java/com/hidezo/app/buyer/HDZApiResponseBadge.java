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

    static class SupplierUp {
        String supplierId = "";
    }
    static class MessageUp {
        String order_no = "";
        int messageCount = 0;
    }

    public ArrayList<SupplierUp> badgeSupplierList = new ArrayList<>();
    public ArrayList<MessageUp> badgeMessageList = new ArrayList<>();

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                if ( !json.isNull("supplierUp") ) {
                    final JSONObject json_supplier_up = json.getJSONObject("supplierUp");
                    final JSONArray supplierList = json_supplier_up.getJSONArray("supplierUpList");
                    if (supplierList.length() > 0) {
                        for (int i = 0; i < supplierList.length(); i++) {
                            final SupplierUp object = new SupplierUp();
                            object.supplierId = supplierList.getJSONObject(i).getString("supplierId");
                            if (object.supplierId.equals("")) {
                                break; // データなし＝中断
                            }

                            badgeSupplierList.add(object);
                        }
                    }
                }

                if ( !json.isNull("messageUp") ) {
                    final JSONObject json_message_up = json.getJSONObject("messageUp");
                    final JSONArray messageList = json_message_up.getJSONArray("messageUpList");
                    if (messageList.length() > 0) {
                        for (int i = 0; i < messageList.length(); i++) {
                            final MessageUp object = new MessageUp();
                            object.order_no = messageList.getJSONObject(i).getString("order_no");
                            object.messageCount = messageList.getJSONObject(i).getInt("messageCount");
                            if (object.messageCount == 0 || object.order_no.equals("")) {
                                break; // データなし＝中断
                            }

                            badgeMessageList.add(object);
                        }
                    }
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
