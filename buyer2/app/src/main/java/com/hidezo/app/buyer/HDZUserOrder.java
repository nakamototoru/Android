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

    public void getFromDynamic(final HDZItemInfo.DynamicItem src, final String supplier_id) {

        this.id = src.id;
        this.isDynamic = true;
        this.numScale = src.num_scale;
        this.itemName = src.item_name;
        this.supplierId = supplier_id;
        this.itemId = src.id;
        this.price = src.price;
    }

    public static void transFromDynamic(final CustomAppCompatActivity activity, final ArrayList<HDZItemInfo.DynamicItem> srcList, final String supplier_id, final ArrayList<HDZUserOrder> dstList) {
        final AppGlobals globals = (AppGlobals) activity.getApplication();
        // 表示リスト
        for (final HDZItemInfo.DynamicItem src : srcList) {
            final HDZUserOrder item = new HDZUserOrder();
            item.getFromDynamic(src, supplier_id);
            // カート内容
            final Dau dau = globals.selectCartDau(item.supplierId,item.itemId);
            if (dau != null) {
                item.orderSize = dau.order_size;
            }
            else {
                item.orderSize = AppGlobals.STR_ZERO;
            }
            dstList.add(item);
        }
    }

    public void getFromStatic(final HDZItemInfo.StaticItem src, final String supplier_id) {

        this.id = src.id;
        this.numScale = src.num_scale;
        this.itemName = src.name;
        this.supplierId = supplier_id;
        this.itemId = src.id;
        this.price = src.price;
        this.standard = src.standard;
        this.loading = src.loading;
        this.scale = src.scale;
        this.image = src.image;
    }

    public static void transFromStatic(final CustomAppCompatActivity activity, final ArrayList<HDZItemInfo.StaticItem> srcList, final String supplier_id, final ArrayList<HDZUserOrder> dstList) {
        final AppGlobals globals = (AppGlobals) activity.getApplication();
        // 表示リスト
        for (final HDZItemInfo.StaticItem src : srcList) {
            final HDZUserOrder item = new HDZUserOrder();
            item.getFromStatic(src, supplier_id);
            // カート内容
            final Dau dau = globals.selectCartDau(item.supplierId,item.itemId);
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
