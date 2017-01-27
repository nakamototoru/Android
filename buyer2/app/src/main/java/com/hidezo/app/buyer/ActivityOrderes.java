package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * 注文履歴アクティビティ
 */
public class ActivityOrderes extends CustomAppCompatActivity {

    ArrayList<HDZordered> displayList = new ArrayList<>();
    boolean isFinishBadge = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderes);

        // ツールナビゲーションバー
        setNavigationBar("注文履歴",false);

        // ゲット・注文履歴
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiRequestPackage.OrderList req = new HDZApiRequestPackage.OrderList();
        req.begin(globals.getUserId(), globals.getUuid(), 1, this );

        // Progress
        openProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ActivityOrderes _self = this;
        // HTTP GET
        final HDZApiRequestPackage.Badge req = new HDZApiRequestPackage.Badge();
        final AppGlobals globals = (AppGlobals) _self.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), _self);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {
        // Progress
        closeProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityOrderes _self = this;
        if ( apiName.equals(HDZApiRequestPackage.Badge.apiName) ) {
            // "store/badge"

            Log.d("####",response);

            isFinishBadge = true;
            final HDZApiResponseBadge responseBadge = new HDZApiResponseBadge();
            if (responseBadge.parseJson(response)) {
                if (responseBadge.badgeMessageList.size() > 0) {
                    // バッジ情報追加
                    final ArrayList<HDZApiResponseBadge.MessageUp> badgeMessageList = responseBadge.badgeMessageList;
                    for (final HDZordered order : displayList) {
                        for (final HDZApiResponseBadge.MessageUp badge : badgeMessageList) {
                            if (badge.order_no.equals(order.order_no)) {
                                order.badgeCount = badge.messageCount;
                                break;
                            }
                        }
                    }
                    reFleshListView();
                }
            }
        }
        else {
            final HDZApiResponseOrderedList responseOrderedList = new HDZApiResponseOrderedList();
            if ( responseOrderedList.parseJson(response) ) {
                if (responseOrderedList.orderedList.size() == 0) {
                    Log.d("########","orderedList is ZERO");
                    return;
                }

                // 表示リスト
                displayList = responseOrderedList.orderedList;

                //UIスレッド上で呼び出してもらう
                this.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        //リストビュー作成
                        final ArrayAdapterOrderes adapter = new ArrayAdapterOrderes(_self, displayList); // responseOrderedList.orderedList
                        final ListView listView = (ListView) findViewById(R.id.listViewOrderes);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            //行タッチイベント
                            @Override
                            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                                final ListView listView = (ListView)parent;
                                final HDZordered order = (HDZordered)listView.getItemAtPosition(position);

                                // 画面遷移
                                final Intent intent = new Intent( _self.getApplication(), ActivityOrderDetail.class);
                                intent.putExtra("order_no", order.order_no);
                                intent.putExtra("supplier_name", order.supplier_name);
                                _self.startActivity(intent);
                            }
                        });
                        listView.setAdapter(adapter);

                        if (!isFinishBadge) {
                            // HTTP GET
                            final HDZApiRequestPackage.Badge req = new HDZApiRequestPackage.Badge();
                            final AppGlobals globals = (AppGlobals) _self.getApplication();
                            req.begin( globals.getUserId(), globals.getUuid(), _self);
                        }
                    }
                });

            }
        }
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.listViewOrderes);
                final ArrayAdapterOrderes adapter = (ArrayAdapterOrderes) listView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            openLogoutDialog();
            return true;
        }

        if (id == R.id.action_suppliers) {
            // 取引先一覧
            final Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
