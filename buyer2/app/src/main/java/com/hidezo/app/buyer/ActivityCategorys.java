package com.hidezo.app.buyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ActivityCategorys extends AppCompatActivity implements HDZClientCallbacksGet {

    private static ActivityCategorys _self;

    private String mySupplierId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
//        mySupplierId = supplier_id;

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        req.begin("6146740737615597570","955F40F8-563B-40A0-BB26-EBF7412DC3E7",mySupplierId,this);

    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void hdzClientCallbackGetComplete(final String response) {
        if (responseItem.parseJson(response)) {
//            if (responseItem.supplierInfo != null) {
//                String name = responseItem.supplierInfo.supplier_name;
//                Log.d("########",name);
//            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<HDZItemInfo.Category> categorys = new ArrayList<HDZItemInfo.Category>();

                    // 動的商品
                    if (responseItem.dynamicItemList.size() > 0) {
                        HDZItemInfo.Category object = new HDZItemInfo.Category();
                        object.name = "新着";
                        categorys.add(object);
                    }

                    // 静的商品
                    HashMap<String,String> hashmap = new HashMap<String, String>();
                    for (int i = 0; i < responseItem.staticItemList.size(); i++) {
                        HDZItemInfo.StaticItem item = responseItem.staticItemList.get(i);

                        String cid =item.category.id;
                        // keyが存在しているか確認
                        if ( hashmap.containsKey(cid) ){
                            // すでにある＝なにもしない
                        } else {
                            categorys.add( item.category );
                            hashmap.put(cid,item.category.name);
                        }
                    }

                    //リストビュー作成
                    ArrayAdapterCategory aacategory = new ArrayAdapterCategory(_self, categorys);
                    ListView listView = (ListView) findViewById(R.id.listViewCategory);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ListView listView = (ListView) parent;
                            HDZItemInfo.Category category = (HDZItemInfo.Category)listView.getItemAtPosition(position);

                            if (!category.isStatic) {
                                // 動的商品
                            }
                            else if (position < listView.getCount() ) {
                                // 静的商品リストビュー
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);

                                String supplier_id = _self.mySupplierId;
                                String category_id = category.id;
//                                for (int i = 0; i < responseItem.staticItemList.size(); i++) {
//                                    HDZItemInfo.StaticItem item = responseItem.staticItemList.get(i);
//                                    String cid = item.category.id;
//                                    if (cid == category.id) {
//                                        category_id = category.id;
//                                    }
//                                }

                                intent.putExtra("supplier_id", supplier_id);
                                intent.putExtra("category_id", category_id);
                                _self.startActivity(intent);
                            }
                        }
                    });
                    listView.setAdapter(aacategory);
                }
            });
        }
    }
    public void hdzClientCallbackGetError(String message) {
        Log.d("########",message);
    }

}
