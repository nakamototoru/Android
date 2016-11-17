package com.hidezo.app.buyer;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/14.
 *
 */
class HDZItemInfo {

    String attr_flg = "";
    ArrayList<String> charge_list = new ArrayList<>();
    ArrayList<String> deliver_to_list = new ArrayList<>();

    static class Supplier {
        String supplier_id = "";
        String supplier_name = "";
    }

    static class Category {
        String id = "";
        String name = "";
        boolean isStatic = false;
        int badgeCount = 0;
        boolean isHistory = false;
        boolean image_flg = false;
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
        ArrayList<String> num_scale = new ArrayList<>();
    }

    static class DynamicItem {
        String id = "";
        String item_name = "";
        String price = "";
        ArrayList<String> num_scale = new ArrayList<>();
        String change_date = "";

        public String getId() {
            return this.id;
        }
        public void setId(final String str) {
            this.id = str;
        }
    }

    static class DynamicItemInfo {
        String text = "";
        ArrayList<String> imagePath = new ArrayList<>();
        String lastUpdate = "";
    }
}
