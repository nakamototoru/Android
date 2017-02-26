package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class ActivityUserOrdersFinish extends AppCompatActivity implements HDZClient.HDZCallbacks {

    private static final String TAG = "##OrderFinish";

    String mySupplierId = "";
    String myOrderNo = "";
//    boolean isFaxSend = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_finish);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        myOrderNo = intent.getStringExtra("order_no");

//        final ActivityUserOrdersFinish _self = this;
        final Button btn = (Button)findViewById(R.id.buttonGoOrders);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 注文履歴
                goOrderListActivity();
            }
        });

        // HTTP GET
//        final HDZApiRequestPackage.OrderMethod req = new HDZApiRequestPackage.OrderMethod();
//        final AppGlobals globals = (AppGlobals) this.getApplication();
//        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // FAX送信
        final HDZApiRequestPackage.sendFax req = new HDZApiRequestPackage.sendFax();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
//        final AppGlobals globals = (AppGlobals) this.getApplication();

        Log.d(TAG,response);

        if (apiName.equals(HDZApiRequestPackage.OrderMethod.apiName)) {
            // FAX送信方法取得
            final HDZApiResponseOrderMethod responseOrderMethod = new HDZApiResponseOrderMethod();
            if (responseOrderMethod.parseJson(response)) {

                Log.d(TAG,"Method = " + responseOrderMethod.method);

//                isFaxSend = responseOrderMethod.method.equals("fax");
//                if (isFaxSend) {
//                    // FAX送信
//                    final HDZApiRequestPackage.sendFax req = new HDZApiRequestPackage.sendFax();
//                    final AppGlobals globals = (AppGlobals) this.getApplication();
//                    req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);
//                }
            }
        }
        else {
            final HDZApiResponse responseApi = new HDZApiResponse();
            if (responseApi.parseJson(response)) {
                Log.d(TAG,"fax send complete");
            }
            else {
                Log.d(TAG,"cancel send fax");
            }
        }

    }
    public void HDZClientError(final String error) {
        // 警告
//        openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。");
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 戻るボタンを無効
            Log.d("Hidezo","戻るボタン無効");
            return false;
        }
        return true;
    }

    /**
     * 注文履歴画面へ
     */
    private void goOrderListActivity() {
        final Intent intent = new Intent(getApplication(), ActivityOrderes.class);
        intent.putExtra("supplier_id",mySupplierId);
        startActivity(intent);
    }

}
