package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;


/**
 * Created by dezami on 2016/09/13.
 * 取引先一覧リストビュー
 */
public class ActivitySuppliers extends CustomAppCompatActivity {

    private static ActivitySuppliers _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        _self = this;

        // ツールナビゲーションバー
        setNavigationBar("取引先一覧");

        // ゲット・取引先一覧
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), this);
   }

    @Override
    protected void onResume() {
        super.onResume();

        // ログインチェック
        if (!isLogin()) {
            // ログアウト促す
            AppGlobals globals = (AppGlobals) this.getApplication();
            globals.openAlertSessionOut(this);
        }
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
//    @Override
    public void HDZClientComplete(String response,String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        if ( responseFriend.parseJson(response) ) {
            if (responseFriend.friendInfoList.size() == 0) {
                return;
            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    ArrayAdapterSupplier aasupplier = new ArrayAdapterSupplier(_self, responseFriend.friendInfoList);
                    ListView listView = (ListView) findViewById(R.id.listViewSupplier);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ListView listView = (ListView)parent;
                            HDZFriendInfo friend = (HDZFriendInfo)listView.getItemAtPosition(position);
                            String supplier_id = friend.id;

                            // 画面遷移
                            if (id == 0) {
                                // カテゴリ一覧
                                Intent intent = new Intent( _self.getApplication(), ActivityCategorys.class);
                                intent.putExtra("supplier_id", supplier_id);
                                _self.startActivity(intent);
                            }
                            else {
                                // 取引先詳細
                                Intent intent = new Intent( _self.getApplication(), ActivitySupplierDetail.class);
                                intent.putExtra("supplier_id", supplier_id);
                                intent.putExtra("supplier_name", friend.name);
                                _self.startActivity(intent);
                            }
                        }
                    });
                    listView.setAdapter(aasupplier);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            //UIスレッド上で呼び出してもらう
            _self.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    new AlertDialog.Builder(_self)
                            .setTitle("ログアウトします")
                            .setMessage("よろしいですか？")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    AppGlobals globals = (AppGlobals) _self.getApplication();
                                    globals.setLoginState(false);

                                    // ログインフォーム画面遷移
                                    Intent intent = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent);
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
            return true;
        }

        if (id == R.id.action_orderes) {

            // 注文履歴画面遷移
            Intent intent = new Intent(getApplication(), ActivityOrderes.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
