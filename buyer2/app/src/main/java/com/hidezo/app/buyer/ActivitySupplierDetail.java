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

    private static ActivitySupplierDetail _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    private String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), this);

        // ツールバー初期化
        String supplier_name = intent.getStringExtra("supplier_name");
        setNavigationBar(supplier_name);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response, String apiname) {
        if (responseFriend.parseJson(response)) {
            if (responseFriend.friendInfoList.size() == 0) {
                return;
            }

            for (HDZFriendInfo object : responseFriend.friendInfoList) {
                if (object.id.equals(mySupplierId)) {

                    final ArrayList<HDZProfile> profileList = new ArrayList<>();
                    HDZProfile pName = new HDZProfile("店舗名",object.name);
                    profileList.add(pName);
                    HDZProfile pAddress = new HDZProfile("本社所在地",object.address);
                    profileList.add(pAddress);
                    HDZProfile pMadd = new HDZProfile("メールアドレス",object.mail_addr);
                    profileList.add(pMadd);
                    HDZProfile pMobile = new HDZProfile("携帯電話",object.mobile);
                    profileList.add(pMobile);
                    HDZProfile pPhone = new HDZProfile("電話番号",object.tel);
                    profileList.add(pPhone);
                    HDZProfile pMaster = new HDZProfile("代表者",object.minister);
                    profileList.add(pMaster);

                    //UIスレッド上で呼び出してもらう
                    this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            //リストビュー作成
                            ArrayAdapterSupplierDetail adapter = new ArrayAdapterSupplierDetail(_self, profileList);
                            ListView listView = (ListView) findViewById(R.id.listViewSupplierDetail);
                            listView.setAdapter(adapter);
                        }
                    });

                    break;
                }
            }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
