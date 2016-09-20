package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
public class ActivitySuppliers extends AppCompatActivity implements HDZClient.HDZCallbacks,AppGlobals.CheckLoginCallbacks {

    private static ActivitySuppliers _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    //メンバー変数
//    private RadioGroup mRadioGroup;
//    private RadioButton mRadioButton;

    // グローバル
    AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        // ツールバー初期化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        // HTTP GET
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        req.begin( sGlobals.getUserId(), sGlobals.getUuid(), this);

   }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
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
                                _self.startActivity(intent);
                            }
                        }
                    });
                    listView.setAdapter(aasupplier);
                }
            });

        }
    }
    public void HDZClientError(String message) {
        Log.d("########",message);
    }

    /**
     * ログインチェック結果
     */
    public void responseLoginState(boolean isLogin) {

        if (!isLogin) {
            sGlobals.openWarning("別デバイスでログインされた","TODO:ログアウト処理",this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
