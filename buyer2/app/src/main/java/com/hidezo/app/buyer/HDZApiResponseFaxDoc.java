package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezamisystem2 on 2017/02/04.
 *
 */

class HDZApiResponseFaxDoc extends HDZApiResponse {

    String store_name = "";
    String store_code = "";
    String store_address = "";
    String order_at = "";
    String deliver_at = "";
    String comment = "";
    String supplier_name = "";
    String fax = "";

    static class ItemInfo {
        String id = "";
        String name = "";
        String size = "";
    }

    ArrayList<ItemInfo> itemInfoArrayList = new ArrayList<>();

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                final JSONObject json_faxdoc = json.getJSONObject("faxdoc");
                store_name = json_faxdoc.getString("store_name");
                store_code = json_faxdoc.getString("store_code");
                store_address = json_faxdoc.getString("store_address");
                order_at = json_faxdoc.getString("order_at");
                deliver_at = json_faxdoc.getString("deliver_at");
                comment = json_faxdoc.getString("comment");
                supplier_name = json_faxdoc.getString("supplier_name");
                fax = json_faxdoc.getString("fax");
                final JSONArray item_list = json_faxdoc.getJSONArray("item_list");
                if (item_list.length() > 0) {
                    for (int i = 0; i < item_list.length(); i++) {
                        final ItemInfo object = new ItemInfo();
                        object.id = item_list.getJSONObject(i).getString("id");
                        object.name = item_list.getJSONObject(i).getString("name");
                        object.size = item_list.getJSONObject(i).getString("size");
                        itemInfoArrayList.add(object);
                    }
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
