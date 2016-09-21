package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 *
 */
public class ActivityOrderes extends CustomAppCompatActivity {

    private static ActivityOrderes _self;
    private HDZApiResponseOrderedList responseOrderedList = new HDZApiResponseOrderedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderes);

        _self = this;

        // ツールナビゲーションバー
        setNavigationBar("注文履歴");

        // ログインチェック
        if (isLogin()) {

            // ゲット・注文履歴
            AppGlobals globals = (AppGlobals) this.getApplication();
            HDZApiRequestPackage.OrderList req = new HDZApiRequestPackage.OrderList();
            req.begin(globals.getUserId(), globals.getUuid(), 1, this );
        }
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiname) {
        if ( responseOrderedList.parseJson(response) ) {
            if (responseOrderedList.orderedList.size() == 0) {
                Log.d("########","orderedList is ZERO");
                return;
            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    ArrayAdapterOrderes aasupplier = new ArrayAdapterOrderes(_self, responseOrderedList.orderedList);
                    ListView listView = (ListView) findViewById(R.id.listViewOrderes);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ListView listView = (ListView)parent;
                            HDZordered order = (HDZordered)listView.getItemAtPosition(position);
                            String order_no = order.order_no;

                            // 画面遷移
                            Intent intent = new Intent( _self.getApplication(), ActivityOrderDetail.class);
                            intent.putExtra("order_no", order_no);
                            _self.startActivity(intent);
                        }
                    });
                    listView.setAdapter(aasupplier);
                }
            });

        }
    }

}
