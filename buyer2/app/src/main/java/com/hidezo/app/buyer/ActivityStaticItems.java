package com.hidezo.app.buyer;

//import android.app.DatePickerDialog;
//import android.app.Dialog;
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
//import java.util.HashMap;

public class ActivityStaticItems extends CustomAppCompatActivity {

//    private static ActivityStaticItems _self;
//    private HDZApiResponseItem responseItem = new HDZApiResponseItem();
//    private ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();

    private String myCategoryId = "";
    private String mySupplierId = "";
//    private String myCategoryName = "";
//    private ListView myListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_items);

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        myCategoryId = intent.getStringExtra("category_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // TouchEvent
        final ActivityStaticItems _self = this;
        TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        final ActivityStaticItems _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {

            final AppGlobals globals = (AppGlobals) _self.getApplication();

            // 商品選別
            final ArrayList<HDZItemInfo.StaticItem> staticItems = new ArrayList<>();
            // 静的商品
            for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                if ( myCategoryId.equals(item.category.id) ) {
                    staticItems.add(item);
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
                    ArrayAdapterStaticItem adapter = new ArrayAdapterStaticItem(_self, displayItemList);
                    ListView listView = (ListView) findViewById(R.id.listViewStaticItem);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                            if (id == 0) {
                                // 遷移・詳細画面
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItemDetail.class);
                                intent.putExtra("supplier_id", mySupplierId);
                                intent.putExtra("item_id", displayItemList.get(position).id);
                                intent.putExtra("category_id", myCategoryId);
                                _self.startActivity(intent);
                            }
                            else {
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
        ListView listView = (ListView) findViewById(R.id.listViewStaticItem);
        ArrayAdapterStaticItem adapter = (ArrayAdapterStaticItem) listView.getAdapter();
        adapter.notifyDataSetChanged();
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
