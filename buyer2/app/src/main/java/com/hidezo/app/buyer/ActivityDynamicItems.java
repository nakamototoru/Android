package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityDynamicItems extends AppCompatActivity implements HDZClient.HDZCallbacks {

    private static ActivityDynamicItems _self;

    private String mySupplierId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    // グローバル
    AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        // ツールバー初期化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        req.begin(sGlobals.getUserId(), sGlobals.getUuid(), mySupplierId, this);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
        if (responseItem.parseJson(response)) {
            if (responseItem.dynamicItemList != null && responseItem.dynamicItemList.size() > 0) {
                String name = responseItem.dynamicItemList.get(0).item_name;
                Log.d("########", name);
            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                    ArrayList<HDZItemInfo.DynamicItem> dynamicItems = new ArrayList<HDZItemInfo.DynamicItem>();
//
//                    // 動的商品
//                    for (HDZItemInfo.DynamicItem item : responseItem.dynamicItemList) {
//
//                    }

                    //リストビュー作成
                    ArrayAdapterDynamicItem aadynamicitem = new ArrayAdapterDynamicItem(_self, responseItem.dynamicItemList);
                    ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            ListView listView = (ListView) parent;
//                            HDZItemInfo.Category category = (HDZItemInfo.Category)listView.getItemAtPosition(position);
//
//                            if (!category.isStatic) {
//                                // 動的商品
//                            }
//                            else if (position < listView.getCount() ) {
//                                // 静的商品リストビュー
//                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);
//
//                                String supplier_id = _self.mySupplierId;
//                                String category_id = category.id;
//
//                                intent.putExtra("supplier_id", supplier_id);
//                                intent.putExtra("category_id", category_id);
//                                _self.startActivity(intent);
//                            }
                        }
                    });
                    listView.setAdapter(aadynamicitem);

                }
            });
        }
    }
    public void HDZClientError(String message) {
        Log.d("########",message);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
