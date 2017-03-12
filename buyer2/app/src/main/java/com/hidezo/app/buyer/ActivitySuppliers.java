package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * Created by dezami on 2016/09/13.
 * 取引先一覧リストビュー
 */
public class ActivitySuppliers extends CustomAppCompatActivity {

    ArrayList<HDZFriendInfo> displayList = new ArrayList<>();
    boolean isFinishBadge = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        // ツールナビゲーションバー
        setNavigationBar("取引先一覧",false);

        // ゲット・取引先一覧
        final HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), this);

        // Progress
        openHttpGetProgressDialog();
   }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
        // Progress
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivitySuppliers _self = this;
        if ( apiName.equals(HDZApiRequestPackage.Badge.apiName) ) {
            isFinishBadge = true;

            if (BuildConfig.DEBUG) {
                Log.d("####",response);
            }

            final HDZApiResponseBadge responseBadge = new HDZApiResponseBadge();
            if (responseBadge.parseJson(response)) {
                if (responseBadge.badgeSupplierList.size() > 0) {
                    // バッジ情報追加
                    final ArrayList<HDZApiResponseBadge.SupplierUp> badgeSupplierList = responseBadge.badgeSupplierList;
                    for (final HDZFriendInfo friend : displayList) {
                        for (final HDZApiResponseBadge.SupplierUp badge : badgeSupplierList) {
                            if (badge.supplierId.equals(friend.id)) {
                                friend.badgeCount = 1;
                                break;
                            }
                        }
                    }
                    reFleshListView();
                }
            }
        }
        else {
            final HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();
            if ( responseFriend.parseJson(response) ) {
                if (responseFriend.friendInfoList.size() > 0) {
                    // データ保存
                    displayList = responseFriend.friendInfoList;
                    //UIスレッド上で呼び出してもらう
                    this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            //リストビュー作成
                            final ArrayAdapterSupplier adapter = new ArrayAdapterSupplier(_self, displayList); // responseFriend.friendInfoList
                            final ListView listView = (ListView) findViewById(R.id.listViewSupplier);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                //行タッチイベント
                                @Override
                                public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                                    // 行情報
                                    final ListView listView = (ListView)parent;
                                    final HDZFriendInfo friend = (HDZFriendInfo)listView.getItemAtPosition(position);
                                    final String supplier_id = friend.id;
                                    // 画面遷移
                                    if (id == 0) {
                                        // カテゴリ一覧
                                        final Intent intent = new Intent( _self.getApplication(), ActivityCategorys.class);
                                        intent.putExtra("supplier_id", supplier_id);
                                        _self.startActivity(intent);
                                    }
                                    else {
                                        // 取引先詳細
                                        final Intent intent = new Intent( _self.getApplication(), ActivitySupplierDetail.class);
                                        intent.putExtra("supplier_id", supplier_id);
                                        intent.putExtra("supplier_name", friend.name);
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
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.listViewSupplier);
                final ArrayAdapterSupplier adapter = (ArrayAdapterSupplier) listView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

//        final ActivitySuppliers _self = this;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //UIスレッド上で呼び出してもらう
            openLogoutDialog();
            return true;
        }

        if (id == R.id.action_orderes) {
            // 注文履歴画面遷移
            final Intent intent = new Intent(getApplication(), ActivityOrderes.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
