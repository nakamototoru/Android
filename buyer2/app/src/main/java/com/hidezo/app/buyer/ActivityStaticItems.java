package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
//import android.text.method.CharacterPickerDialog;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

//import com.hidezo.app.buyer.CustomView.PickerView;

import java.util.ArrayList;

/**
 *
 */
public class ActivityStaticItems extends CustomAppCompatActivity {

    String myCategoryId = "";
    String mySupplierId = "";
    boolean myHistoryFlag = false;
    String myCategoryName = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_items);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        myCategoryId = intent.getStringExtra("category_id");
        myHistoryFlag = intent.getBooleanExtra("history_flag",false);

        // TouchEvent
        final TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 画面遷移
                final Intent intent = new Intent( getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
        final TextView tvHome = (TextView)findViewById(R.id.textViewButtonHome);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent( getApplication(), ActivityCategorys.class);
                intent.putExtra("supplier_id", mySupplierId);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        myCategoryName = intent.getStringExtra("category_name");
        setNavigationBar(myCategoryName,true);

        // HTTP GET
        if (myHistoryFlag) {
            // 履歴から
            final HDZApiRequestPackage.OrderdItem req = new HDZApiRequestPackage.OrderdItem();
            final AppGlobals globals = (AppGlobals) this.getApplication();
            req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, this);
        }
        else {
            final HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
            final AppGlobals globals = (AppGlobals) this.getApplication();
            req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, this);
        }

        // Progress
        openHttpGetProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
        // Progress
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityStaticItems _self = this;
        final AppGlobals globals = (AppGlobals) _self.getApplication();

        // 商品選別
        final ArrayList<HDZItemInfo.StaticItem> staticItems = new ArrayList<>();

        if (apiName.equals(HDZApiRequestPackage.OrderdItem.apiName)) {
            // 履歴から注文
            final HDZApiResponseOrderdItem responseOrderdItem = new HDZApiResponseOrderdItem();
            if (responseOrderdItem.parseJson(response)) {
                // 履歴商品
                for (final HDZItemInfo.StaticItem item : responseOrderdItem.staticItemList) {
                    staticItems.add(item);
                }
            }
            else {
                return;
            }
        }
        else {
            final HDZApiResponseItem responseItem = new HDZApiResponseItem();
            if (responseItem.parseJson(response)) {
                // 静的商品
                for (final HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                    if ( myCategoryId.equals(item.category.id) ) {
                        staticItems.add(item);
                    }
                }
            }
            else {
                return;
            }
        }

        // 表示リスト作成
        final ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();
        HDZUserOrder.transFromStatic(_self, staticItems, mySupplierId, displayItemList);

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //リストビュー作成
                final ArrayAdapterStaticItem adapter = new ArrayAdapterStaticItem(_self, displayItemList);
                final ListView listView = (ListView) findViewById(R.id.listViewStaticItem);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //タッチイベント
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                        if (id == 0) {
                            // 遷移・詳細画面
                            final Intent intent = new Intent( _self.getApplication(), ActivityStaticItemDetail.class);
                            intent.putExtra("supplier_id", mySupplierId);
                            intent.putExtra("item_id", displayItemList.get(position).id);
                            intent.putExtra("category_id", myCategoryId);
                            intent.putExtra("category_name", myCategoryName);
                            _self.startActivity(intent);
                        }
                        else if (id == 1 || id == -1) {
                            // 個数の増減
                            final HDZUserOrder order = displayItemList.get(position);
                            int count = Integer.parseInt(order.orderSize);
                            count += (int)id;
                            if (count == 0) {
                                globals.deleteCart(order.supplierId, order.itemId);
                                order.orderSize = AppGlobals.STR_ZERO;
                                // カート更新
                                reFleshListView();
                            }
                            else if (count <= 100) {
                                final String numScale = String.valueOf(count);
                                globals.replaceCart(order.supplierId, order.itemId, numScale, false);
                                order.orderSize = numScale;
                                // カート更新
                                reFleshListView();
                            }
                        }
                        else {
                            // 分数指定
                            // ピッカーの作成
                            final ArrayList<String> pickerList = new ArrayList<>();
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
                                                public void onClick(final DialogInterface dialog, final int id) {

                                                    final HDZUserOrder order = displayItemList.get(position);
                                                    final String numScale = pickerView.getTextSelected();
                                                    if (numScale.equals(AppGlobals.STR_ZERO)) {
                                                        globals.deleteCart(order.supplierId, order.itemId);
                                                    }
                                                    else {
                                                        globals.replaceCart(order.supplierId, order.itemId, numScale, false);
                                                    }
                                                    order.orderSize = pickerView.getTextSelected();
                                                    // カート更新
                                                    reFleshListView();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, final int which) {
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

    /**
     * 戻る実行
     */
    void onClickNavigationBack() {
        // カテゴリ一覧
        final Intent intent = new Intent( getApplication(), ActivityCategorys.class);
        intent.putExtra("supplier_id", mySupplierId);
        startActivity(intent);
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        final ListView listView = (ListView) findViewById(R.id.listViewStaticItem);
        final ArrayAdapterStaticItem adapter = (ArrayAdapterStaticItem) listView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
