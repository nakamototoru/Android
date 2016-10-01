package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 *
 */
public class ActivityOrderDetail extends CustomAppCompatActivity {

    String myOrderNo = "";
    String mySupplierName = "";
    String myCharge = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        myOrderNo = intent.getStringExtra("order_no");

        // HTTP GET
        HDZApiRequestPackage.OrderDetail req = new HDZApiRequestPackage.OrderDetail();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);

        // ツールナビゲーションバー
        mySupplierName = intent.getStringExtra("supplier_name");
        setNavigationBar(mySupplierName + "様宛");
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {

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
                    ArrayAdapterOrderDetail adapter = new ArrayAdapterOrderDetail(_self,responseOrderDetail.itemList);
                    ListView listView = (ListView) findViewById(R.id.listViewOrderDetail);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                        View footer = getLayoutInflater().inflate(R.layout.item_order_detail_footer,null);
                        listView.addFooterView(footer, null ,false); // タッチ無効

                        String subTotal = responseOrderDetail.orderInfo.subTotal + "円";
                        TextView tvSubTotal = (TextView)findViewById(R.id.textViewSubTotal);
                        tvSubTotal.setText(subTotal);

                        String deliverFee = responseOrderDetail.orderInfo.deliveryFee + "円";
                        TextView tvDeliverFee = (TextView)findViewById(R.id.textViewDeliverFee);
                        tvDeliverFee.setText(deliverFee);

                        String total = responseOrderDetail.orderInfo.total + "円";
                        TextView tvTotal = (TextView)findViewById(R.id.textViewTotal);
                        tvTotal.setText(total);

                        String charge = responseOrderDetail.orderInfo.charge;
                        TextView tvCharge = (TextView)findViewById(R.id.textViewCharge);
                        tvCharge.setText(charge);

                        String deliverDay = responseOrderDetail.orderInfo.delivery_day;
                        TextView tvDay = (TextView)findViewById(R.id.textViewDeliverDay);
                        tvDay.setText(deliverDay);

                        String deliverTo = responseOrderDetail.orderInfo.deliver_to;
                        TextView tvTo = (TextView)findViewById(R.id.textViewDeliverTo);
                        tvTo.setText(deliverTo);
                    }

                    listView.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        final ActivityOrderDetail _self = this;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_message) {

            // 画面遷移
            Intent intent = new Intent( getApplication(), ActivityMessages.class);
            intent.putExtra("order_no", myOrderNo);
            intent.putExtra("supplier_name", mySupplierName);
            intent.putExtra("charge", myCharge);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
