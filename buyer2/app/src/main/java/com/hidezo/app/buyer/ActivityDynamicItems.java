package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hidezo.app.buyer.CustomView.PickerView;
import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;
//import java.util.StringTokenizer;

//import java.util.ArrayList;

/**
 *
 */
public class ActivityDynamicItems extends CustomAppCompatActivity {

    private ActivityDynamicItems _self;

    private HDZApiResponseItem responseItem = new HDZApiResponseItem();
    private ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();

    private String mySupplierId = "";

    private ListView myListView = null;
//    private int indexSelected = 0;
//    private String numScaleSelected = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

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
        setNavigationBar("新着");
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

                    final AppGlobals globals = (AppGlobals) _self.getApplication();
                    // 表示リスト
                    for (HDZItemInfo.DynamicItem src : responseItem.dynamicItemList) {
                        HDZUserOrder item = new HDZUserOrder();
                        item.itemName = src.item_name;
                        item.supplierId = responseItem.supplierInfo.supplier_id;
                        item.itemId = src.id;
                        item.price = src.price;
                        Dau dau = globals.selectCartDau(item.supplierId,item.itemId);
                        if (dau != null) {
                            item.orderSize = dau.order_size;
                        }
                        else {
                            item.orderSize = AppGlobals.STR_ZERO;
                        }
                        _self.displayItemList.add(item);
                    }

                    //リストビュー作成
                    ArrayAdapterDynamicItem adapter = new ArrayAdapterDynamicItem(_self, _self.displayItemList);
                    _self.myListView = (ListView) findViewById(R.id.listViewDynamicItem);
                    _self.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            // ピッカーの作成
                            ArrayList<String> pickerList = new ArrayList<>();
                            pickerList.add( AppGlobals.STR_ZERO );
                            pickerList.addAll(responseItem.dynamicItemList.get(position).num_scale);
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
                                                    globals.replaceCart(order.supplierId, order.itemId, pickerView.getTextSelected());
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
                    });
                    myListView.setAdapter(adapter);

                }
            });
        }
    }

    /**
     * リストビューの更新処理。
     */
    public void refleshListView() {
//        ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
        if (myListView != null) {
            ArrayAdapterDynamicItem adapter = (ArrayAdapterDynamicItem) myListView.getAdapter();
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
