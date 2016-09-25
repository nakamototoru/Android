package com.hidezo.app.buyer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.hidezo.app.buyer.CustomView.PickerView;

import java.util.ArrayList;
//import java.util.HashMap;

public class ActivityStaticItems extends CustomAppCompatActivity {

    private static ActivityStaticItems _self;

    private HDZApiResponseItem responseItem = new HDZApiResponseItem();
    private ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();

    private String myCategoryId = "";
    private String mySupplierId = "";
//    private String myCategoryName = "";

    private ListView myListView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_items);

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        myCategoryId = intent.getStringExtra("category_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // TouchEvent
        TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("########","R.id.textViewButtonOrderCheck");

                Intent intent = new Intent( _self.getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        String title = intent.getStringExtra("category_name");
        setNavigationBar(title);
    }


    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        if (responseItem.parseJson(response)) {
            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final ArrayList<HDZItemInfo.StaticItem> staticItems = new ArrayList<>(); // HDZItemInfo.StaticItem
                    // 静的商品
                    for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                        String cid = item.category.id;
                        if ( cid.equals(myCategoryId) ) {
                            staticItems.add(item);
                        }
                    }

                    // 表示リスト作成
                    HDZUserOrder.transFromStatic(_self, responseItem.staticItemList, mySupplierId, _self.displayItemList);

                    //リストビュー作成
                    ArrayAdapterStaticItem adapter = new ArrayAdapterStaticItem(_self, _self.displayItemList);
                    myListView = (ListView) findViewById(R.id.listViewStaticItem);
                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                            if (id == 0) {
                                // 遷移・詳細画面
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItemDetail.class);
                                intent.putExtra("supplier_id", _self.mySupplierId);
                                intent.putExtra("item_id", staticItems.get(position).id);
                                intent.putExtra("category_id", _self.myCategoryId);
                                _self.startActivity(intent);
                            }
                            else {
                                // ピッカーの作成
                                ArrayList<String> pickerList = new ArrayList<>();
                                pickerList.add( AppGlobals.STR_ZERO );
                                pickerList.addAll(responseItem.staticItemList.get(position).num_scale);
                                final CustomPickerView pickerView = new CustomPickerView(_self, pickerList, _self.displayItemList.get(position).orderSize);

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

                                                        HDZUserOrder order = _self.displayItemList.get(position);
                                                        final AppGlobals globals = (AppGlobals) _self.getApplication();
                                                        String numScale = pickerView.getTextSelected();
                                                        if (numScale.equals(AppGlobals.STR_ZERO)) {
                                                            globals.deleteCart(order.supplierId, order.itemId);
                                                        }
                                                        else {
                                                            globals.replaceCart(order.supplierId, order.itemId, numScale);
                                                        }
                                                        order.orderSize = pickerView.getTextSelected();
                                                        // カート更新
                                                        _self.refleshListView();
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
                    myListView.setAdapter(adapter);
                }
            });
        }
    }
//    public void HDZClientError(String message) {
//        Log.d("########",message);
//    }

    /**
     * リストビューの更新処理。
     */
    public void refleshListView() {
        if (myListView != null) {
            ArrayAdapterStaticItem adapter = (ArrayAdapterStaticItem) myListView.getAdapter();
            adapter.notifyDataSetChanged();

            Log.d("########","refleshListView");
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
