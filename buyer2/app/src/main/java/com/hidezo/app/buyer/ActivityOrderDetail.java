package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateEncodingException;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import org.w3c.dom.Text;

/**
 *
 */
public class ActivityOrderDetail extends CustomAppCompatActivity {

    private static final String TAG = "#ActivityOrderDetail";

    String myOrderNo = "";
    String mySupplierName = "";
    String myCharge = "";

    ListView myListView = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        final Intent intent = getIntent();
        myOrderNo = intent.getStringExtra("order_no");

        // ツールナビゲーションバー
        mySupplierName = intent.getStringExtra("supplier_name");
        setNavigationBar(mySupplierName + "様宛",true);

        // HTTP GET
        final HDZApiRequestPackage.OrderDetail req = new HDZApiRequestPackage.OrderDetail();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);

        // Progress
        openHttpGetProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {
        // Progress
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityOrderDetail _self = this;
        final HDZApiResponseOrderDetail responseOrderDetail = new HDZApiResponseOrderDetail();
        if ( responseOrderDetail.parseJson(response) ) {

            myCharge = responseOrderDetail.orderInfo.charge;

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    final ArrayAdapterOrderDetail adapter = new ArrayAdapterOrderDetail(_self,responseOrderDetail.itemList);
                    final ListView listView = (ListView) findViewById(R.id.listViewOrderDetail);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                            //
//                            ListView listView = (ListView)parent;
//                            HDZordered order = (HDZordered)listView.getItemAtPosition(position);
//                            String order_no = order.order_no;
//
//                            // 画面遷移
//                            Intent intent = new Intent( _self.getApplication(), ActivityOrderDetail.class);
//                            intent.putExtra("order_no", order_no);
//                            _self.startActivity(intent);
                        }
                    });

                    // フッター
                    if (listView.getFooterViewsCount() == 0) {
                        //
                        final LinearLayout inflateLayout = null;
                        final View footer = getLayoutInflater().inflate(R.layout.item_order_detail_footer,inflateLayout);
                        listView.addFooterView(footer, null ,false); // タッチ無効

                        final String subTotal = responseOrderDetail.orderInfo.subTotal + "円";
                        final TextView tvSubTotal = (TextView)findViewById(R.id.textViewSubTotal);
                        tvSubTotal.setText(subTotal);

                        final String deliverFee = responseOrderDetail.orderInfo.deliveryFee + "円";
                        final TextView tvDeliverFee = (TextView)findViewById(R.id.textViewDeliverFee);
                        tvDeliverFee.setText(deliverFee);

                        final String total = responseOrderDetail.orderInfo.total + "円";
                        final TextView tvTotal = (TextView)findViewById(R.id.textViewTotal);
                        tvTotal.setText(total);

                        final String charge = responseOrderDetail.orderInfo.charge;
                        final TextView tvCharge = (TextView)findViewById(R.id.textViewCharge);
                        tvCharge.setText(charge);

                        final String deliverDay = responseOrderDetail.orderInfo.delivery_day + "納品";
                        final TextView tvDay = (TextView)findViewById(R.id.textViewDeliverDay);
                        tvDay.setText(deliverDay);

                        final String deliverTo = responseOrderDetail.orderInfo.deliver_to;
                        final TextView tvTo = (TextView)findViewById(R.id.textViewDeliverTo);
                        tvTo.setText(deliverTo);
                    }

                    listView.setAdapter(adapter);

                    myListView = listView;
                }
            });

        }
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_message) {
            // 画面遷移
            final Intent intent = new Intent( getApplication(), ActivityMessages.class);
            intent.putExtra("order_no", myOrderNo);
            intent.putExtra("supplier_name", mySupplierName);
            intent.putExtra("charge", myCharge);
            startActivity(intent);
            return true;
        }

        if (BuildConfig.DEBUG) {
            if (id == R.id.action_pdf) {
                // PDF出力テスト
//                new HDZPdfManager.GenerationTask(myListView, getApplicationContext(), this).execute();
                // PDFイメージアクティビティ
                final Intent intent = new Intent( getApplication(), ActivityPdfDocument.class);
                intent.putExtra("order_no", myOrderNo);
                startActivity(intent);
                return true;
            }

//            if (id == R.id.action_base64) {
//                String result = HDZPdfManager.getPdfBase64String(getApplicationContext());
//                Log.d(TAG, result);
//                return true;
//            }
//            if (id == R.id.action_soap) {
//                HDZSoapFax.getSendList();
//                return true;
//            }
        }

        return super.onOptionsItemSelected(item);
    }
}
