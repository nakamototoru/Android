package com.hidezo.app.buyer;

import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/24.
 *
 */

class HDZUserOrder {

    String id = "";
    String supplierId = "";
    String itemId = "";
    String orderSize = "";
    boolean isDynamic = false;
    ArrayList<String> numScale = new ArrayList<>();
    String itemName = "";
    String standard = "";
    String loading = "";
    String scale = "";
    String price = "";
    String image = "";

    public static void transFromDynamic(CustomAppCompatActivity activity, ArrayList<HDZItemInfo.DynamicItem> srcList, String supplier_id, ArrayList<HDZUserOrder> dstList) {
        final AppGlobals globals = (AppGlobals) activity.getApplication();
        // 表示リスト
        for (HDZItemInfo.DynamicItem src : srcList) {
            HDZUserOrder item = new HDZUserOrder();
            item.isDynamic = true;
            item.numScale = src.num_scale;
            item.itemName = src.item_name;
            item.supplierId = supplier_id;
            item.itemId = src.id;
            item.price = src.price;
            Dau dau = globals.selectCartDau(item.supplierId,item.itemId);
            if (dau != null) {
                item.orderSize = dau.order_size;
            }
            else {
                item.orderSize = AppGlobals.STR_ZERO;
            }
            dstList.add(item);
        }
    }

    public static void transFromStatic(CustomAppCompatActivity activity, ArrayList<HDZItemInfo.StaticItem> srcList, String supplier_id, ArrayList<HDZUserOrder> dstList) {
        final AppGlobals globals = (AppGlobals) activity.getApplication();
        // 表示リスト
        for (HDZItemInfo.StaticItem src : srcList) {
            HDZUserOrder item = new HDZUserOrder();
            item.numScale = src.num_scale;
            item.itemName = src.name;
            item.supplierId = supplier_id;
            item.itemId = src.id;
            item.price = src.price;
            item.standard = src.standard;
            item.loading = src.loading;
            item.scale = src.scale;
            item.image = src.image;
            Dau dau = globals.selectCartDau(item.supplierId,item.itemId);
            if (dau != null) {
                item.orderSize = dau.order_size;
            }
            else {
                item.orderSize = AppGlobals.STR_ZERO;
            }
            dstList.add(item);
        }
    }
}
