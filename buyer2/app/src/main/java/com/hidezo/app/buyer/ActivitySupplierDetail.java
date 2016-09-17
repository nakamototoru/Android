package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivitySupplierDetail extends AppCompatActivity implements HDZClient.HDZCallbacks {

    private static ActivitySupplierDetail _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    private String mySupplierId = "";

    // グローバル
    AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        req.begin( sGlobals.getUserId(), sGlobals.getUuid(), this);
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

//                    Log.d("########",object.name);

                    final ArrayList<HDZProfile> profileList = new ArrayList<HDZProfile>();
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
                            ArrayAdapterSupplierDetail aasupplier = new ArrayAdapterSupplierDetail(_self, profileList);
                            ListView listView = (ListView) findViewById(R.id.listViewSupplierDetail);
                            listView.setAdapter(aasupplier);
                        }
                    });

                    break;
                }
            }
        }
    }
    public void HDZClientError(String message) {
        Log.d("########",message);
    }

}
