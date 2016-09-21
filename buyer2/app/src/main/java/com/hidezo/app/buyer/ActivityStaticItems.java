package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.HashMap;

public class ActivityStaticItems extends CustomAppCompatActivity {

    private static ActivityStaticItems _self;

//    private String mySupplierId = "";
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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        String title = intent.getStringExtra("category_name");
//        toolbar.setTitle(title);
//        setSupportActionBar(toolbar);
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

                            if (id == 0) {
                                //遷移
                                Intent intent = new Intent( _self.getApplication(), ActivityStaticItemDetail.class);
//                                intent.putExtra("supplier_id",_self.mySupplierId);
                                _self.startActivity(intent);
                            }
                            else {
                                ListView listView = (ListView) parent;
                                try {
                                    View targetView = listView.getChildAt(position);
                                    listView.getAdapter().getView(position,targetView,parent);
                                } catch (Exception e) {
                                    Log.d("########","Failed : ListView reflesh");
                                }
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
