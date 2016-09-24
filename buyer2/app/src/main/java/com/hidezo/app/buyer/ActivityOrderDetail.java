package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivityOrderDetail extends CustomAppCompatActivity {

    private static ActivityOrderDetail _self;
    private HDZApiResponseOrderDetail responseOrderDetail = new HDZApiResponseOrderDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        _self = this;

        Intent intent = getIntent();
        String order_no = intent.getStringExtra("order_no");

        // HTTP GET
        HDZApiRequestPackage.OrderDetail req = new HDZApiRequestPackage.OrderDetail();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), order_no, this);

        String supplier_name = intent.getStringExtra("supplier_name");
        // ツールナビゲーションバー
        setNavigationBar(supplier_name + "様宛");

    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
//    @Override
    public void HDZClientComplete(final String response, final String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        if ( responseOrderDetail.parseJson(response) ) {

            Log.d("########",response);

//            //UIスレッド上で呼び出してもらう
//            this.runOnUiThread(new Runnable(){
//                @Override
//                public void run(){
//                    //リストビュー作成
//                    ArrayAdapterOrd aasupplier = new ArrayAdapterOrderes(_self, responseOrderDetail.orderedList);
//                    ListView listView = (ListView) findViewById(R.id.listViewOrderes);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        //行タッチイベント
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            ListView listView = (ListView)parent;
//                            HDZordered order = (HDZordered)listView.getItemAtPosition(position);
//                            String order_no = order.order_no;
//
//                            // 画面遷移
//                            Intent intent = new Intent( _self.getApplication(), ActivityOrderDetail.class);
//                            intent.putExtra("order_no", order_no);
//                            _self.startActivity(intent);
//                        }
//                    });
//                    listView.setAdapter(aasupplier);
//                }
//            });

        }
    }
}
