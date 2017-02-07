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
    boolean isFaxSend = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_finish);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        final Button btn = (Button)findViewById(R.id.buttonGoOrders);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 画面遷移
//                if (isFaxSend) {
//                    // PDFイメージアクティビティ
//                    final Intent intent = new Intent(getApplication(), ActivityPdfDocument.class);
//                    intent.putExtra("order_no", myOrderNo);
//                    startActivity(intent);
//                }
//                else {
//                    // 注文履歴
//                    final Intent intent = new Intent(getApplication(), ActivityOrderes.class);
//                    intent.putExtra("supplier_id",mySupplierId);
//                    startActivity(intent);
//                }

                // 注文履歴
                final Intent intent = new Intent(getApplication(), ActivityOrderes.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);

            }
        });

        // HTTP GET
        final HDZApiRequestPackage.OrderMethod req = new HDZApiRequestPackage.OrderMethod();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
//        final AppGlobals globals = (AppGlobals) this.getApplication();

        // FAX送信処理
        final HDZApiResponseOrderMethod responseOrderMethod = new HDZApiResponseOrderMethod();
        if (responseOrderMethod.parseJson(response)) {

            Log.d(TAG,"Method = " + responseOrderMethod.method);

            isFaxSend = responseOrderMethod.method.equals("fax");
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
}
