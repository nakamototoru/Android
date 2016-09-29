package com.hidezo.app.buyer;

//import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;

import com.hidezo.app.buyer.model.Dau;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ActivityUserOrdersExec extends CustomAppCompatActivity {

    private String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_exec);

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        final ActivityUserOrdersExec _self = this;
        final AppGlobals globals = (AppGlobals) this.getApplication();

        ArrayList<String> static_items = new ArrayList<>();
        ArrayList<String> dynamic_items = new ArrayList<>();
        List<Dau> cartList = globals.selectCartList(mySupplierId);
        for (Dau dau : cartList) {
            if (dau.order_size.equals(AppGlobals.STR_ZERO)) {
                continue;
            }
            // 商品チェック
            String dst = dau.item_id + "," + dau.order_size;
            if (dau.is_dynamic) {
                dynamic_items.add(dst);
            }
            else {
                static_items.add(dst);
            }
        }
        // HTTP POST
        HDZApiRequestPackage.Order req = new HDZApiRequestPackage.Order();
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, dynamic_items,static_items, globals.getOrderDeliverDay(), globals.getOrderCharge(), globals.getOrderDeliverPlace(), _self );
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        // 注文処理
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiResponseOrderResult responseOrderResult = new HDZApiResponseOrderResult();
        if (responseOrderResult.parseJson(response)) {
            if (responseOrderResult.result) {

                globals.deleteAllCart();
                globals.setOrderCharge("");
                globals.setOrderDeliverDay("");
                globals.setOrderDeliverPlace("");

                // 注文成功
                openAlertCompleted();
            }
            else {
                //注文失敗
                openAlertFailed();
            }
        }
        else {
            //注文失敗
            openAlertFailed();
        }
    }
    public void HDZClientError(String error) {
        openAlertFailed();
    }

    /**
     * ALERT DIALOG
     */
    public void openAlertCompleted() {

        final ActivityUserOrdersExec _self = this;
        final String title = "注文完了";
        final String message = "注文が完了しました。";

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // 画面遷移
                                Intent intent = new Intent(getApplication(), ActivityCategorys.class);
                                intent.putExtra("supplier_id",mySupplierId);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    public void openAlertFailed() {

        final ActivityUserOrdersExec _self = this;
        final String title = "エラー";
        final String message = "注文が出来ませんでした。";

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // 画面遷移
                                Intent intent = new Intent( _self.getApplication(), ActivityUserOrdersCheck.class);
                                intent.putExtra("supplier_id", mySupplierId);
                                _self.startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
