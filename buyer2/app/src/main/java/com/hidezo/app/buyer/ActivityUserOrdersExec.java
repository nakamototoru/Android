package com.hidezo.app.buyer;

//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;
import java.util.List;

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
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, static_items, dynamic_items, globals.getOrderDeliverDay(), globals.getOrderCharge(), globals.getOrderDeliverPlace(), _self );
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        // 注文処理
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiResponseOrderResult responseOrderResult = new HDZApiResponseOrderResult();

        Log.d("## Order",response);

        if (responseOrderResult.parseJson(response)) {
            if (responseOrderResult.result) {
                // 注文成功
                // 画面遷移
                Intent intent = new Intent(getApplication(), ActivityCategorys.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
            else {
                //注文失敗
                globals.openWarning("エラー",responseOrderResult.message,this);
            }
        }
        else {
            //注文失敗
            globals.openWarning("エラー",responseOrderResult.message,this);
        }
    }
//    public void HDZClientError(String error) {
//        final AppGlobals globals = (AppGlobals) this.getApplication();
//        // 警告
//        globals.openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。",this);
//    }

}
