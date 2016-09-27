package com.hidezo.app.buyer;

//import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/14.
 *
 */
class HDZItemInfo {

    public String attr_flg = "";
    public ArrayList<String> charge_list = new ArrayList<>();
    public ArrayList<String> deliver_to_list = new ArrayList<>();

    static class Supplier {
        String supplier_id = "";
        String supplier_name = "";
    }

    static class Category {
        String id = "";
        String name = "";
        boolean isStatic = false;
    }

    static class StaticItem {
        Category category = new Category();
        String id ="";
        String name = "";
        String code = "";
        String detail = "";
        String image = "";
        String min_order_count = "";
        String standard = "";
        String price = "";
        String scale = "";
        String loading = "";
        ArrayList<String> num_scale = new ArrayList<String>();
    }

    static class DynamicItem {
        String id = "";
        String item_name = "";
        String price = "";
        ArrayList<String> num_scale = new ArrayList<>();

        public String getId() {
            return this.id;
        }
        public void setId(String str) {
            this.id = str;
        }
    }

    static class DynamicItemInfo {
        String text = "";
        ArrayList<String> imagePath = new ArrayList<String>();
        String lastUpdate = "";
    }
}
