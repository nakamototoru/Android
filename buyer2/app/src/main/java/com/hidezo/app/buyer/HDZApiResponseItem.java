package com.hidezo.app.buyer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/14.
 *
 */
public class HDZApiResponseItem extends HDZApiResponse {

    public HDZItemInfo.Supplier supplierInfo = null;
    public ArrayList<HDZItemInfo.StaticItem> staticItemList = new ArrayList<HDZItemInfo.StaticItem>();
    public ArrayList<HDZItemInfo.DynamicItem> dynamicItemList = new ArrayList<HDZItemInfo.DynamicItem>();
    public HDZItemInfo.DynamicItemInfo dynamicItemInfo = null;

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                JSONObject json_supplier = json.getJSONObject("supplier");
                supplierInfo = new HDZItemInfo.Supplier();
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

//                        item.num_scale = new ArrayList<String>();
                        JSONArray json_num_scale = json_staticItem.getJSONObject(i).getJSONArray("num_scale");
                        for (int j = 0; j < json_num_scale.length(); j++) {
                            item.num_scale.add( json_num_scale.getString(j) );
                        }

//                        item.category = new HDZItemInfo.Category();
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

//                        item.num_scale = new ArrayList<String>();
                        JSONArray json_num_scale = json_dynamicItem.getJSONObject(i).getJSONArray("num_scale");
                        for (int j = 0; j < json_num_scale.length(); j++) {
                            item.num_scale.add(json_num_scale.getString(j));
                        }

                        dynamicItemList.add(item);
                    }
                }

                if ( !json.isNull("dynamicItemInfo") ) {
                    JSONArray dynamivItemInfo = json.getJSONArray("dynamicItemInfo");
                    if (dynamivItemInfo.length() >= 1) {

                    }
                }
/*
                JSONArray friendList = json.getJSONArray("friendList");
                if (friendList != null) {
                    if (friendList.length() > 0) {
                        for (int i = 0; i < friendList.length(); i++) {
                            HDZFriendInfo info = new HDZFriendInfo();
                            info.id = friendList.getJSONObject(i).getString("id");
                            info.name = friendList.getJSONObject(i).getString("name");
                            info.address = friendList.getJSONObject(i).getString("address");
                            info.mail_addr = friendList.getJSONObject(i).getString("mail_addr");
                            info.minister = friendList.getJSONObject(i).getString("minister");
                            info.mobile = friendList.getJSONObject(i).getString("mobile");
                            info.tel = friendList.getJSONObject(i).getString("tel");

                            friendInfoList.add(info);
                        }
                    }
                }
*/
//                else {
////                    friendList = new JSONArray();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
