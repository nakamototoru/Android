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

    private String myCategoryId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_items);

        _self = this;

        Intent intent = getIntent();
        String mySupplierId = intent.getStringExtra("supplier_id");
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
                Log.d("########","R.id.textViewButtonOrderCheck");

                Intent intent = new Intent( _self.getApplication(), ActivityUserOrders.class);
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
    public void HDZClientComplete(String response,String apiname) {
        if (responseItem.parseJson(response)) {
//            if (responseItem.staticItemList != null && responseItem.staticItemList.size() > 0) {
//                String name = responseItem.staticItemList.get(0).name;
//                Log.d("########", name);
//            }

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

                    //リストビュー作成
                    ArrayAdapterStaticItem aastaticitem = new ArrayAdapterStaticItem(_self, staticItems);
                    ListView listView = (ListView) findViewById(R.id.listViewStaticItem);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (id == 0) {
                                //遷移
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItemDetail.class);
                                _self.startActivity(intent);
                            }
                            else {
                                ArrayList<String> pickerList = new ArrayList<>();
                                pickerList.add("0");
                                pickerList.addAll(staticItems.get(position).num_scale);

                                final PickerView pickerView = new PickerView(_self);
                                pickerView.setData( pickerList );
                                pickerView.setSelected(0);

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

                                                    }
                                                })
                                                .show();
                                    }
                                });
                            }
                        }
                    });
                    listView.setAdapter(aastaticitem);
                }
            });
        }
    }
//    public void HDZClientError(String message) {
//        Log.d("########",message);
//    }

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
