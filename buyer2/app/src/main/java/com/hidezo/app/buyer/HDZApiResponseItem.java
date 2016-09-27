package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/14.
 *
 */
class HDZApiResponseItem extends HDZApiResponse {

    public HDZItemInfo itemInfo = new HDZItemInfo();
    public HDZItemInfo.Supplier supplierInfo = new HDZItemInfo.Supplier();
    public ArrayList<HDZItemInfo.StaticItem> staticItemList = new ArrayList<>();
    public ArrayList<HDZItemInfo.DynamicItem> dynamicItemList = new ArrayList<>();
    public HDZItemInfo.DynamicItemInfo dynamicItemInfo = new HDZItemInfo.DynamicItemInfo();

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                itemInfo.attr_flg = json.getString("attr_flg");
                JSONArray json_charge_list = json.getJSONArray("charge_list");
                for (int i = 0; i < json_charge_list.length(); i++) {
                    itemInfo.charge_list.add( json_charge_list.getString(i) );
                }
                JSONArray json_deliver_to_list = json.getJSONArray("deliver_to_list");
                for (int i = 0; i < json_deliver_to_list.length(); i++) {
                    itemInfo.deliver_to_list.add( json_deliver_to_list.getString(i) );
                }

                JSONObject json_supplier = json.getJSONObject("supplier");
                supplierInfo.supplier_id = json_supplier.getString("supplier_id");
                supplierInfo.supplier_name = json_supplier.getString("supplier_name");

//                Log.d("########",json_staticItem.toString());

                if ( !json.isNull("staticItem") ) {
                    JSONArray json_staticItem = json.getJSONArray("staticItem");
                    for (int i = 0; i < json_staticItem.length(); i++) {

                        HDZItemInfo.StaticItem item = new HDZItemInfo.StaticItem();
                        item.id = json_staticItem.getJSONObject(i).getString("id");
                        item.name = json_staticItem.getJSONObject(i).getString("name");
                        item.price = json_staticItem.getJSONObject(i).getString("price");
                        item.code = json_staticItem.getJSONObject(i).getString("code");
                        item.image = json_staticItem.getJSONObject(i).getString("image");
                        item.detail = json_staticItem.getJSONObject(i).getString("detail");
                        item.loading = json_staticItem.getJSONObject(i).getString("loading");
                        item.min_order_count = json_staticItem.getJSONObject(i).getString("min_order_count");
                        item.scale = json_staticItem.getJSONObject(i).getString("scale");
                        item.standard = json_staticItem.getJSONObject(i).getString("standard");

                        JSONArray json_num_scale = json_staticItem.getJSONObject(i).getJSONArray("num_scale");
                        for (int j = 0; j < json_num_scale.length(); j++) {
                            item.num_scale.add( json_num_scale.getString(j) );
                        }

                        JSONObject json_category = json_staticItem.getJSONObject(i).getJSONObject("category");

//                        Log.d("########",json_category.toString());

                        item.category.id = json_category.getString("id");
                        item.category.name = json_category.getString("name");
                        item.category.isStatic = true;

                        staticItemList.add(item);
                    }
                }

                if ( !json.isNull("dynamicItem") ) {
                    JSONArray json_dynamicItem = json.getJSONArray("dynamicItem");
                    for (int i = 0; i < json_dynamicItem.length(); i++) {

                        HDZItemInfo.DynamicItem item = new HDZItemInfo.DynamicItem();
                        item.id = json_dynamicItem.getJSONObject(i).getString("id");
                        item.item_name = json_dynamicItem.getJSONObject(i).getString("item_name");
                        item.price = json_dynamicItem.getJSONObject(i).getString("price");

                        JSONArray json_num_scale = json_dynamicItem.getJSONObject(i).getJSONArray("num_scale");
                        for (int j = 0; j < json_num_scale.length(); j++) {
                            item.num_scale.add(json_num_scale.getString(j));
                        }

                        dynamicItemList.add(item);
                    }
                }

                if ( !json.isNull("dynamicItemInfo") ) {
                    JSONArray json_dynamiciteminfo = json.getJSONArray("dynamicItemInfo");
                    if (json_dynamiciteminfo.length() >= 1) {
                        dynamicItemInfo.text = json_dynamiciteminfo.getJSONObject(0).getString("text");
                        dynamicItemInfo.lastUpdate = json_dynamiciteminfo.getJSONObject(0).getString("lastUpdate");
                        JSONArray json_imagepath = json_dynamiciteminfo.getJSONObject(0).getJSONArray("imagePath");
                        for (int i = 0; i < json_imagepath.length(); i++) {
                            dynamicItemInfo.imagePath.add( json_imagepath.getString(i) );
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
