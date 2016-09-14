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
        /*
        let supplier_id: String
    let supplier_name: String
         */
        String supplier_id = "";
        String supplier_name = "";
    }

    public static class Category {
        String id = "";
        String name = "";
    }

    public static class StaticItem {
        Category category = new Category();
     /*
    let id: String
    let name: String
    let code: String
    let detail: String
    let image: NSURL
    let loading: Int
    let min_order_count: String
    let standard: String
    let price: String
    let scale: String
    let num_scale: [String]
         */
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
        /*
         let id: String
    let item_name: String
    let num_scale: [String]
    let price: String
         */
        String id = "";
        String item_name = "";
        String price = "";
        ArrayList<String> num_scale = new ArrayList<String>();
    }

    public static class DynamicItemInfo {
        /*
         let imagePath: [String]
    let text: String
    let lastUpdate: NSDate
         */
        String text = "";
        ArrayList<String> imagePath = new ArrayList<String>();
        String lastUpdate = "";
    }
}
