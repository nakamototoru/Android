package com.hidezo.app.buyer;

import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.StringTokenizer;

/**
 *
 */
public class ActivityCategorys extends CustomAppCompatActivity {

    private String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        final ActivityCategorys _self = this;

        // TouchEvent
        TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( _self.getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });

        // ゲット・商品一覧
        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");
        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // ツールバー初期化
        setNavigationBar("カテゴリ一覧");
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityCategorys _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<HDZItemInfo.Category> categorys = new ArrayList<>(); // HDZItemInfo.Category

                    // 動的商品
                    if (responseItem.dynamicItemList.size() > 0) {
                        HDZItemInfo.Category object = new HDZItemInfo.Category();
                        object.name = "新着";
                        categorys.add(object);
                    }

                    // 静的商品
                    HashMap<String,String> hashmap = new HashMap<>(); // String, String
                    for (int i = 0; i < responseItem.staticItemList.size(); i++) {
                        HDZItemInfo.StaticItem item = responseItem.staticItemList.get(i);

                        String cid = item.category.id;
                        // keyが存在しているか確認
                        if ( !hashmap.containsKey(cid) ){
                            // 存在しないなら登録
                            categorys.add( item.category );
                            hashmap.put(cid,item.category.name);
                        }
//                        else {
//                            // すでにある＝なにもしない
//                        }
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
                                // 動的商品リストビュー
                                Intent intent = new Intent( _self.getApplication(), ActivityDynamicItems.class);
                                intent.putExtra("supplier_id",_self.mySupplierId);
                                _self.startActivity(intent);
                            }
                            else if (position < listView.getCount() ) {
                                // 静的商品リストビュー
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);
                                intent.putExtra("supplier_id", _self.mySupplierId);
                                intent.putExtra("category_id", category.id);
                                intent.putExtra("category_name", category.name);
                                _self.startActivity(intent);
                            }
                        }
                    });
                    listView.setAdapter(aacategory);
                }
            });
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
