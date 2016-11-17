package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by dezamisystem on 2016/11/10.
 *
 */
class HDZApiResponseOrderdItem extends HDZApiResponse {

    ArrayList<HDZItemInfo.StaticItem> staticItemList = new ArrayList<>();

    @Override
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

//                Log.d("########",json_staticItem.toString());

                if ( !json.isNull("ordredStaticItem") ) {
                    final JSONArray json_staticItem = json.getJSONArray("ordredStaticItem");
                    for (int i = 0; i < json_staticItem.length(); i++) {

                        final HDZItemInfo.StaticItem item = new HDZItemInfo.StaticItem();
                        item.id = json_staticItem.getJSONObject(i).getString("id");
                        item.name = json_staticItem.getJSONObject(i).getString("name");
                        item.price = json_staticItem.getJSONObject(i).getString("price");
                        item.code = json_staticItem.getJSONObject(i).getString("code");
//                        if ( !json_staticItem.getJSONObject(i).isNull("image") ) {
//                            item.image = json_staticItem.getJSONObject(i).getString("image");
//                        }
                        item.detail = json_staticItem.getJSONObject(i).getString("detail");
                        item.loading = json_staticItem.getJSONObject(i).getString("loading");
                        item.min_order_count = json_staticItem.getJSONObject(i).getString("min_order_count");
                        item.scale = json_staticItem.getJSONObject(i).getString("scale");
                        item.standard = json_staticItem.getJSONObject(i).getString("standard");

                        final JSONArray json_num_scale = json_staticItem.getJSONObject(i).getJSONArray("num_scale");
                        for (int j = 0; j < json_num_scale.length(); j++) {
                            item.num_scale.add( json_num_scale.getString(j) );
                        }

                        final JSONObject json_category = json_staticItem.getJSONObject(i).getJSONObject("category");
                        item.category.id = json_category.getString("id");
                        item.category.name = json_category.getString("name");
                        item.category.isStatic = true;
//                        int img_flg = json_category.getInt("image_flg");
//                        if (img_flg != 0) {
                            // 画像無し
                            item.category.image_flg = true;
                            item.image = "";
//                        }

                        staticItemList.add(item);
                    }
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
