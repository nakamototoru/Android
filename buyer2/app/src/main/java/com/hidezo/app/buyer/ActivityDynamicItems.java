package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.graphics.Canvas;
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

//import com.hidezo.app.buyer.CustomView.PickerView;
//import com.hidezo.app.buyer.model.Dau;

import java.util.ArrayList;
//import java.util.StringTokenizer;

//import java.util.ArrayList;

/**
 *
 */
public class ActivityDynamicItems extends CustomAppCompatActivity {

    private String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        final ActivityDynamicItems _self = this;

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
                Intent intent = new Intent( getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
        TextView tvHome = (TextView)findViewById(R.id.textViewButtonHome);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplication(), ActivityCategorys.class);
                intent.putExtra("supplier_id", mySupplierId);
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

        final ActivityDynamicItems _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {
            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();

                    // 表示リスト作成
                    HDZUserOrder.transFromDynamic(_self, responseItem.dynamicItemList, mySupplierId, displayItemList);

                    //リストビュー作成
                    ArrayAdapterDynamicItem adapter = new ArrayAdapterDynamicItem(_self, displayItemList);
                    ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            // ピッカーの作成
                            ArrayList<String> pickerList = new ArrayList<>();
                            pickerList.add( AppGlobals.STR_ZERO );
                            pickerList.addAll(responseItem.dynamicItemList.get(position).num_scale);
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
                                                    final AppGlobals globals = (AppGlobals) _self.getApplication();
                                                    String numScale = pickerView.getTextSelected();
                                                    if (numScale.equals(AppGlobals.STR_ZERO)) {
                                                        globals.deleteCart(order.supplierId, order.itemId);
                                                    }
                                                    else {
                                                        globals.replaceCart(order.supplierId, order.itemId, numScale, true);
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
        ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
        ArrayAdapterDynamicItem adapter = (ArrayAdapterDynamicItem) listView.getAdapter();
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
