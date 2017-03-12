package com.hidezo.app.buyer;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class ActivityCategorys extends CustomAppCompatActivity {

    String mySupplierId = "";

    ArrayList<HDZItemInfo.Category> displayList = new ArrayList<>();
    boolean isFinishBadge = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // TouchEvent
        final TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 遷移・注文履歴
                final Intent intent = new Intent( getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
        final TextView tvTop = (TextView)findViewById(R.id.textViewButtonTop);
        tvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 遷移・取引先一覧
                final Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        setNavigationBar("カテゴリ一覧",true);

        // ゲット・商品一覧
        // HTTP GET
        final HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // Progress Start
        openHttpGetProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
        // Progress End
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityCategorys _self = this;
        if ( apiName.equals(HDZApiRequestPackage.Badge.apiName) ) {
            // "store/badge"
            isFinishBadge = true;

            if (BuildConfig.DEBUG) {
                Log.d("####",response);
            }

            final HDZApiResponseBadge responseBadge = new HDZApiResponseBadge();
            if (responseBadge.parseJson(response)) {
                if (responseBadge.badgeSupplierList.size() > 0) {
                    // バッジ情報追加
                    final ArrayList<HDZApiResponseBadge.SupplierUp> badgeSupplierList = responseBadge.badgeSupplierList;
                    for (final HDZApiResponseBadge.SupplierUp badge : badgeSupplierList) {
                        if (mySupplierId.equals(badge.supplierId)) {
                            if (displayList != null && displayList.size() > 0) {
                                final HDZItemInfo.Category category = displayList.get(0);
                                if (!category.isStatic) {
                                    category.badgeCount = 1;
                                }
                            }
                            break;
                        }
                    }
                    reFleshListView();
                }
            }
        }
        else {
            final HDZApiResponseItem responseItem = new HDZApiResponseItem();
            if (responseItem.parseJson(response)) {

                // 表示リスト作成
                // 動的商品
                if (responseItem.dynamicItemList.size() > 0) {
                    final HDZItemInfo.Category object = new HDZItemInfo.Category();
                    object.name = "新着";
                    displayList.add(object);
                }

                // 静的商品
                final HashMap<String,String> hashmap = new HashMap<>(); // String, String
                for (int i = 0; i < responseItem.staticItemList.size(); i++) {
                    final HDZItemInfo.StaticItem item = responseItem.staticItemList.get(i);

                    final String cid = item.category.id;
                    // keyが存在しているか確認
                    if ( !hashmap.containsKey(cid) ){
                        // 存在しないなら登録
                        displayList.add( item.category );
                        hashmap.put(cid,item.category.name);
                    }
                }

                // 履歴から注文
                final HDZItemInfo.Category categoryHistory = new HDZItemInfo.Category();
                categoryHistory.name = "履歴から注文";
                categoryHistory.isStatic = true;
                categoryHistory.isHistory = true;
                displayList.add(categoryHistory);

                //UIスレッド上で呼び出してもらう
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //リストビュー作成
                        final ArrayAdapterCategory adapter = new ArrayAdapterCategory(_self, displayList);
                        final ListView listView = (ListView) findViewById(R.id.listViewCategory);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            //行タッチイベント
                            @Override
                            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                                final ListView listView = (ListView) parent;
                                final HDZItemInfo.Category category = (HDZItemInfo.Category)listView.getItemAtPosition(position);

                                if (!category.isStatic) {
                                    // 動的商品リストビュー
                                    final Intent intent = new Intent( _self.getApplication(), ActivityDynamicItems.class);
                                    intent.putExtra("supplier_id",_self.mySupplierId);
                                    _self.startActivity(intent);
                                }
                                else if (category.isHistory) {
                                    if (BuildConfig.DEBUG) {
                                        Log.d("##--##","履歴から注文");
                                    }
                                    final Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);
                                    intent.putExtra("supplier_id", _self.mySupplierId);
                                    intent.putExtra("category_id", category.id);
                                    intent.putExtra("category_name", category.name);
                                    intent.putExtra("history_flag", true);
                                    _self.startActivity(intent);
                                }
                                else if (position < listView.getCount() ) {
                                    // 静的商品リストビュー
                                    final Intent intent = new Intent( _self.getApplication(), ActivityStaticItems.class);
                                    intent.putExtra("supplier_id", _self.mySupplierId);
                                    intent.putExtra("category_id", category.id);
                                    intent.putExtra("category_name", category.name);
                                    _self.startActivity(intent);
                                }
                            }
                        });
                        listView.setAdapter(adapter);

                        if (!isFinishBadge) {
                            // HTTP GET
                            final HDZApiRequestPackage.Badge req = new HDZApiRequestPackage.Badge();
                            final AppGlobals globals = (AppGlobals) _self.getApplication();
                            req.begin( globals.getUserId(), globals.getUuid(), _self);
                        }
                    }
                });
            }
        }
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.listViewCategory);
                final ArrayAdapterCategory adapter = (ArrayAdapterCategory) listView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 戻る実行
     */
    void onClickNavigationBack() {
        // 遷移・取引先一覧
        final Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
        startActivity(intent);
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
