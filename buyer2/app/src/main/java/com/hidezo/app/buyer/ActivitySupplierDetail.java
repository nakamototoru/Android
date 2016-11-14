package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 *
 */
public class ActivitySupplierDetail extends CustomAppCompatActivity {

    String mySupplierId = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // ツールバー初期化
        final String supplier_name = intent.getStringExtra("supplier_name");
        setNavigationBar(supplier_name,true);

        // HTTP GET
        final HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), this);

        // Progress
        openProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {

        // Progress
        closeProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivitySupplierDetail _self = this;
        final HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();
        if (responseFriend.parseJson(response)) {
            if (responseFriend.friendInfoList.size() == 0) {
                return;
            }

            for (final HDZFriendInfo object : responseFriend.friendInfoList) {
                if (object.id.equals(mySupplierId)) {

                    final ArrayList<HDZProfile> profileList = new ArrayList<>();
                    final HDZProfile pName = new HDZProfile("店舗名",object.name);
                    profileList.add(pName);
                    final HDZProfile pAddress = new HDZProfile("本社所在地",object.address);
                    profileList.add(pAddress);
                    final HDZProfile pMadd = new HDZProfile("メールアドレス",object.mail_addr);
                    profileList.add(pMadd);
                    final HDZProfile pMobile = new HDZProfile("携帯電話",object.mobile);
                    profileList.add(pMobile);
                    final HDZProfile pPhone = new HDZProfile("電話番号",object.tel);
                    profileList.add(pPhone);
                    final HDZProfile pMaster = new HDZProfile("代表者",object.minister);
                    profileList.add(pMaster);

                    //UIスレッド上で呼び出してもらう
                    this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            //リストビュー作成
                            final ArrayAdapterSupplierDetail adapter = new ArrayAdapterSupplierDetail(_self, profileList);
                            final ListView listView = (ListView) findViewById(R.id.listViewSupplierDetail);
                            listView.setAdapter(adapter);
                        }
                    });

                    break;
                }
            }
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
