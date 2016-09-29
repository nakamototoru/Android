package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ActivityUserOrders extends CustomAppCompatActivity {

    private String mySupplierId = "";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

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
    public void HDZClientComplete(final String response, String apiName) {

        final AppGlobals globals = (AppGlobals) this.getApplication();

        if (checkLogOut(response)) {
            return;
        }

        final ActivityUserOrders _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {

            final ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 表示リスト
                    List<Dau> cartList = globals.selectCartList(mySupplierId);
                    for (Dau dau : cartList) {
                        if (dau.order_size.equals(AppGlobals.STR_ZERO)) {
                            continue;
                        }
                        boolean isFound = false;
                        // 動的商品チェック
                        for (HDZItemInfo.DynamicItem item : responseItem.dynamicItemList) {
                            if (mySupplierId.equals(dau.supplier_id) && item.id.equals(dau.item_id)) {
                                HDZUserOrder order = new HDZUserOrder();
                                order.getFromDynamic(item, mySupplierId);
                                order.orderSize = dau.order_size;
                                displayItemList.add(order);
                                isFound = true;
//                                Log.d("## NAME",item.item_name);
                                break;
                            }
                        }
                        if (!isFound) {
                            // 静的商品チェック
                            for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                                if (mySupplierId.equals(dau.supplier_id) && item.id.equals(dau.item_id)) {
                                    HDZUserOrder order = new HDZUserOrder();
                                    order.getFromStatic(item, mySupplierId);
                                    order.orderSize = dau.order_size;
                                    displayItemList.add(order);
//                                    Log.d("## NAME",item.name);
                                    break;
                                }
                            }
                        }
                    }

                    if (displayItemList.size() == 0) {
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

                    // 注文確定ボタンを有効に
                    TextView tvOrderDecide = (TextView) findViewById(R.id.textViewButtonOrderDecide);
                    tvOrderDecide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 遷移
                            Intent intent = new Intent( _self.getApplication(), ActivityUserOrdersCheck.class);
                            intent.putExtra("supplier_id", mySupplierId);
                            _self.startActivity(intent);
                        }
                    });

                    //リストビュー作成
                    ArrayAdapterUserOrder adapter = new ArrayAdapterUserOrder(_self, displayItemList);
                    ListView listView = (ListView) findViewById(R.id.listViewUserOrders);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                            // 場合分け
                            if (id == -1) {
                                // カート削除
                                HDZUserOrder order = displayItemList.get(position);
                                globals.deleteCart(order.supplierId, order.itemId);
                                displayItemList.remove(position);
                                // カート更新
                                reFleshListView();
                            }
                            else if (id == 0) {
                                // ピッカーの作成
                                ArrayList<String> pickerList = new ArrayList<>();
                                pickerList.add( AppGlobals.STR_ZERO );
                                pickerList.addAll(displayItemList.get(position).numScale);
                                final CustomPickerView pickerView = new CustomPickerView(_self, pickerList, displayItemList.get(position).orderSize);

                                //UIスレッド上で呼び出してもらう
                                _self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(_self)
                                                .setTitle("選択")
                                                .setView(pickerView)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
//                                                    Log.d("## Cart","POS[" + String.valueOf(position) + "]SELECTED = " + String.valueOf(pickerView.getIndexSelected()));

                                                        HDZUserOrder order = displayItemList.get(position);
//                                                        final AppGlobals globals = (AppGlobals) _self.getApplication();
                                                        String numScale = pickerView.getTextSelected();
                                                        if (numScale.equals(AppGlobals.STR_ZERO)) {
                                                            //削除
                                                            globals.deleteCart(order.supplierId, order.itemId);
                                                            displayItemList.remove(position);
                                                        }
                                                        else {
                                                            globals.replaceCart(order.supplierId, order.itemId, numScale, order.isDynamic);
                                                        }
                                                        order.orderSize = pickerView.getTextSelected();
                                                        // カート更新
                                                        reFleshListView();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .show();
                                    }
                                });

                            }


                        }
                    });
                    listView.setAdapter(adapter);
                }
            });
        }
    }
    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        //
        ListView listView = (ListView) findViewById(R.id.listViewUserOrders);

        final AppGlobals globals = (AppGlobals) this.getApplication();

        // 表示リスト
        List<Dau> cartList = globals.selectCartList(mySupplierId);

        if (cartList.size() <= 0) {
            // カート空の場合
            ArrayList<HDZApiResponse> emptyList = new ArrayList<>();
            HDZApiResponse object = new HDZApiResponse();
            object.message = "カートが空です";
            emptyList.add(object);
            ArrayAdapterEmpty emptyAdapter = new ArrayAdapterEmpty(this,emptyList);
            listView.setAdapter(emptyAdapter);
            // 注文確定ボタンを無効に
            TextView tvOrderDecide = (TextView) findViewById(R.id.textViewButtonOrderDecide);
            tvOrderDecide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        else {
            ArrayAdapterUserOrder adapter = (ArrayAdapterUserOrder) listView.getAdapter();
            adapter.notifyDataSetChanged();
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
