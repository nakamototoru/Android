package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hidezo.app.buyer.CustomView.PickerView;

import java.util.ArrayList;

/**
 *
 *
 */
public class ActivityStaticItemDetail extends CustomAppCompatActivity {

    private static ActivityStaticItemDetail _self;
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    private String myItemId = "";
    private String myCategoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_item_detail);

        _self = this;

        Intent intent = getIntent();
        myItemId = intent.getStringExtra("item_id");
        myCategoryId = intent.getStringExtra("category_id");
        String supplier_id = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), supplier_id, this);

        // ツールバー初期化
        String title = "商品詳細";
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

                    final ArrayList<HDZProfile> profileList = new ArrayList<>();

                    // 静的商品
                    for (HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                        String cid = item.category.id;
                        String iid = item.id;
                        if ( cid.equals(myCategoryId) && iid.equals(myItemId)) {
                            HDZProfile pCode = new HDZProfile("商品コード",item.code);
                            profileList.add(pCode);
                            HDZProfile pName = new HDZProfile("商品名",item.name);
                            profileList.add(pName);
                            HDZProfile pPrice = new HDZProfile("価格",item.price);
                            profileList.add(pPrice);
                            HDZProfile pStandard = new HDZProfile("規格",item.standard);
                            profileList.add(pStandard);
                            HDZProfile pLoading = new HDZProfile("入り数",item.loading);
                            profileList.add(pLoading);
                            HDZProfile pScale = new HDZProfile("単位",item.scale);
                            profileList.add(pScale);
                            HDZProfile pMinOrderCount = new HDZProfile("最小注文本数",item.min_order_count);
                            profileList.add(pMinOrderCount);

                            break;
                        }
                    }

                    //リストビュー作成
                    ArrayAdapterStaticItemDetail adapter = new ArrayAdapterStaticItemDetail(_self, profileList);
                    ListView listView = (ListView) findViewById(R.id.listViewStaticItemDetail);
                    listView.setAdapter(adapter);
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
