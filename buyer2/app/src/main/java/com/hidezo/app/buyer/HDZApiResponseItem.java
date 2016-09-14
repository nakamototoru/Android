package com.hidezo.app.buyer;

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


                JSONArray staticItemList = json.getJSONArray("staticItem");
                if (staticItemList != null) {
                    for (int i = 0; i < staticItemList.length(); i++) {

                    }
                }
                JSONArray dynamicItemList = json.getJSONArray("dynamicItem");
                if (dynamicItemList != null) {
                    for (int i = 0; i < dynamicItemList.length(); i++) {

                    }
                }
                JSONArray dynamivItemInfo = json.getJSONArray("dynamicItemInfo");
                if (dynamivItemInfo != null) {
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
