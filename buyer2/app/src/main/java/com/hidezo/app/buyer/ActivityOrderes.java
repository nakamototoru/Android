package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 *
 */
public class ActivityOrderes extends CustomAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderes);

        // ツールナビゲーションバー
        setNavigationBar("注文履歴");

        // ゲット・注文履歴
        AppGlobals globals = (AppGlobals) this.getApplication();
        HDZApiRequestPackage.OrderList req = new HDZApiRequestPackage.OrderList();
        req.begin(globals.getUserId(), globals.getUuid(), 1, this );

        // Progress
        openProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
//    @Override
    public void HDZClientComplete(final String response, final String apiName) {

        // Progress
        closeProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityOrderes _self = this;
        final HDZApiResponseOrderedList responseOrderedList = new HDZApiResponseOrderedList();
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

                            // 画面遷移
                            Intent intent = new Intent( _self.getApplication(), ActivityOrderDetail.class);
                            intent.putExtra("order_no", order.order_no);
                            intent.putExtra("supplier_name", order.supplier_name);
                            _self.startActivity(intent);
                        }
                    });
                    listView.setAdapter(aasupplier);
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
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final ActivityOrderes _self = this;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //UIスレッド上で呼び出してもらう
//            _self.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    new AlertDialog.Builder(_self)
//                            .setTitle("ログアウトします")
//                            .setMessage("よろしいですか？")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    AppGlobals globals = (AppGlobals) _self.getApplication();
//                                    globals.setLoginState(false);
//
//                                    // ログインフォーム画面遷移
//                                    Intent intent = new Intent(getApplication(), MainActivity.class);
//                                    startActivity(intent);
//                                }
//                            })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .show();
//                }
//            });
            openLogoutDialog();
            return true;
        }

        if (id == R.id.action_suppliers) {
            // 取引先一覧
            Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
