package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;

import com.hidezo.app.buyer.model.Dau;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ActivityUserOrdersExec extends CustomAppCompatActivity {

    String mySupplierId = "";
    String myOrderNo = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_exec);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        final ActivityUserOrdersExec _self = this;
        final AppGlobals globals = (AppGlobals) this.getApplication();

        final ArrayList<String> static_items = new ArrayList<>();
        final ArrayList<String> dynamic_items = new ArrayList<>();
        final List<Dau> cartList = globals.selectCartList(mySupplierId);
        for (final Dau dau : cartList) {
            if (dau.order_size.equals(AppGlobals.STR_ZERO)) {
                continue;
            }
            // 商品チェック
            final String dst = dau.item_id + "," + dau.order_size;
            if (dau.is_dynamic) {
                dynamic_items.add(dst);
            }
            else {
                static_items.add(dst);
            }
        }
        // HTTP POST
        final HDZApiRequestPackage.Order req = new HDZApiRequestPackage.Order();
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, dynamic_items,static_items, globals.getOrderDeliverDay(), globals.getOrderCharge(), globals.getOrderDeliverPlace(), _self );

        // Progress
        openPostProgressDialog();
    }
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 戻るボタンを無効
            Log.d("####","戻るボタン無効");
            return false;
        }
        return true;
    }

    /**
     * 画面遷移
     */
    void goOrdersFinish() {
        // 画面遷移
        final Intent intent = new Intent(getApplication(), ActivityUserOrdersFinish.class);
        intent.putExtra("supplier_id",mySupplierId);
        startActivity(intent);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {

        final AppGlobals globals = (AppGlobals) this.getApplication();
        if (apiName.equals(HDZApiRequestPackage.Order.apiName)) {
            // "store/order"
            // 注文処理
            final HDZApiResponseOrderResult responseOrderResult = new HDZApiResponseOrderResult();
            if (responseOrderResult.parseJson(response)) {
                if (responseOrderResult.result) {
                    // 注文成功
                    myOrderNo = responseOrderResult.orderNo;

                    if ( globals.getOrderMessage().equals("") ) {
                        // Progress
                        closeProgressDialog();

                        // 記憶値クリア
                        globals.deleteAllCart();
                        globals.resetOrderInfoWithMessage(true);

                        // 画面遷移
                        goOrdersFinish();
                    }
                    else {
                        // メッセージ送信
                        // HTTP POST
                        final HDZApiRequestPackage.AddMessage req = new HDZApiRequestPackage.AddMessage();
                        req.begin( globals.getUserId(), globals.getUuid(), globals.getOrderCharge(), globals.getOrderMessage(), myOrderNo, this);
                    }
                }
                else {
                    // Progress
                    closeProgressDialog();

                    //注文失敗
                    openAlertFailed();
                }
            }
            else {
                // Progress
                closeProgressDialog();

                //注文失敗
                openAlertFailed();
            }
        }
        else {
            // Progress
            closeProgressDialog();

            final HDZApiResponse responseResult = new HDZApiResponse();
            if (responseResult.parseJson(response)) {
                Log.d("## OrderExec","Completed");
            }
            else {
                Log.d("## OrderExec","Error");
            }

            // 記憶値クリア
            globals.deleteAllCart();
            globals.resetOrderInfoWithMessage(true);

            // 画面遷移
            goOrdersFinish();
        }
    }

    /**
     * 失敗ダイアログ
     */
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
                            public void onClick(final DialogInterface dialog, final int id) {
                                // 画面遷移
                                final Intent intent = new Intent( getApplication(), ActivityUserOrdersCheck.class);
                                intent.putExtra("supplier_id", mySupplierId);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
