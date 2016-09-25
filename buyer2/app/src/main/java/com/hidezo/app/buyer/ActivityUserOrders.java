package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ActivityUserOrders extends CustomAppCompatActivity {

    private ActivityUserOrders _self;

    private String mySupplierId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // ツールバー初期化
        setNavigationBar("注文確認");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response, String apiName) {

        if (checkLogOut(response)) {
            return;
        }

        if (responseItem.parseJson(response)) {

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // 表示リスト
                    ArrayList<HDZUserOrder> userOrders = new ArrayList<>();

                    AppGlobals globals = (AppGlobals) _self.getApplication();
                    List<Dau> cartList = globals.selectCartList(mySupplierId);
                    for (Dau dau : cartList) {
                        // 動的商品チェック
                        for (HDZItemInfo.DynamicItem item : responseItem.dynamicItemList) {
                            if (item.id.equals(dau.item_id)) {
                                HDZUserOrder order = new HDZUserOrder();
                                order.supplierId = dau.supplier_id;
                                order.itemId = dau.item_id;
                                order.isDynamic = true;
                                userOrders.add(order);
                                break;
                            }
                        }
                        // 静的商品チェック
                        for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                            if (item.id.equals(dau.item_id)) {
                                HDZUserOrder order = new HDZUserOrder();
                                order.supplierId = dau.supplier_id;
                                order.itemId = dau.item_id;
                                userOrders.add(order);
                                break;
                            }
                        }
                    }

                    if (userOrders.size() == 0) {
                        // カート空の場合
                        ArrayList<HDZApiResponse> emptyList = new ArrayList<>();
                        HDZApiResponse object = new HDZApiResponse();
                        object.message = "カートが空です";
                        emptyList.add(object);
                        ArrayAdapterEmpty emptyAdapter = new ArrayAdapterEmpty(_self,emptyList);
                        ListView listView = (ListView) findViewById(R.id.listViewUserOrders);
                        listView.setAdapter(emptyAdapter);
                        return;
                    }

                    //リストビュー作成
                    ArrayAdapterUserOrder adapter = new ArrayAdapterUserOrder(_self, userOrders);
                    ListView listView = (ListView) findViewById(R.id.listViewUserOrders);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        //行タッチイベント
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            ListView listView = (ListView) parent;
//                            HDZItemInfo.Category category = (HDZItemInfo.Category)listView.getItemAtPosition(position);
//
//                            if (!category.isStatic) {
//                                // 動的商品リストビュー
//                                Intent intent = new Intent( _self.getApplication(), ActivityDynamicItems.class);
//                                intent.putExtra("supplier_id",_self.mySupplierId);
//                                _self.startActivity(intent);
//                            }
//                            else if (position < listView.getCount() ) {
//                                // 静的商品リストビュー
//                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);
//                                intent.putExtra("supplier_id", _self.mySupplierId);
//                                intent.putExtra("category_id", category.id);
//                                intent.putExtra("category_name", category.name);
//                                _self.startActivity(intent);
//                            }
//                        }
//                    });
                    listView.setAdapter(adapter);
                }
            });
        }
    }

    /**
     * ツールバー
     *
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            // ログインフォーム画面遷移
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ActivityUserOrders Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
