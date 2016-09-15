package com.hidezo.app.buyer;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/14.
 *
 */
public class HDZItemInfo {

    public String attr_flg = "";
    public ArrayList<String> charge_list = new ArrayList<String>();
    public ArrayList<String> deliver_to_list = new ArrayList<String>();

    public static class Supplier {
        String supplier_id = "";
        String supplier_name = "";
    }

    public static class Category {
        String id = "";
        String name = "";
        boolean isStatic = false;
    }

    public static class StaticItem {
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

    public static class DynamicItem {
        String id = "";
        String item_name = "";
        String price = "";
        ArrayList<String> num_scale = new ArrayList<String>();
    }

    public static class DynamicItemInfo {
        String text = "";
        ArrayList<String> imagePath = new ArrayList<String>();
        String lastUpdate = "";
    }
}
