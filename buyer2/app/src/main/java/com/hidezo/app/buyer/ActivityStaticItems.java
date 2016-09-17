package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityStaticItems extends AppCompatActivity implements HDZClient.HDZCallbacks {

    private static ActivityStaticItems _self;

    private String mySupplierId = "";
    private String myCategoryId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    // グローバル
    AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_items);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        myCategoryId = intent.getStringExtra("category_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        req.begin( sGlobals.getUserId(), sGlobals.getUuid(), mySupplierId, this);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
        if (responseItem.parseJson(response)) {
            if (responseItem.staticItemList != null && responseItem.staticItemList.size() > 0) {
                String name = responseItem.staticItemList.get(0).name;
                Log.d("########", name);
            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<HDZItemInfo.StaticItem> staticItems = new ArrayList<HDZItemInfo.StaticItem>();

                    // 静的商品
                    for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                        String cid =item.category.id;
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
                    listView.setAdapter(aastaticitem);
                }
            });
        }
    }
    public void HDZClientError(String message) {
        Log.d("########",message);
    }
}
