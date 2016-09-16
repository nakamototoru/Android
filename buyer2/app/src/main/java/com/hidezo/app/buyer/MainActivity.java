package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.support.v4.app.FragmentTabHost;

// FragmentActivity implements FragmentTabHost.OnTabChangeListener
public class MainActivity extends AppCompatActivity implements HDZClientCallbacksGet {

    private static MainActivity _self;
    private static boolean isStarted = false;

    private HDZApiResponse responseLoginCheck = new HDZApiResponse();
    private HDZApiResponse responseLogin = new HDZApiResponse();

    // グローバル
    private AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        // HTTP GET
        HDZApiRequestPackage.LoginCheck req = new HDZApiRequestPackage.LoginCheck();
        req.begin( sGlobals.getUserId(), sGlobals.getUuid(), this);

//        initTabs();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void hdzClientCallbackGetComplete(String response,String apiname) {
        if (apiname.equals("login_check/store")) {
            if (responseLoginCheck.parseJson(response)) {
                if (responseLoginCheck.result) {

                    //ログインしている
                    // 画面遷移
                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                    startActivity(intent);
                }
                else {

                    //ログインを促す
                    sGlobals.openWarning("login_check/store","ログインを促す",this);
                }
            }
        }
        else if (apiname.equals("login/store")) {
            if (responseLogin.parseJson(response)) {
                if (responseLogin.result) {

                    //ログイン状態に
                    // 画面遷移
                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                    startActivity(intent);
                }
                else {
                    //ログイン失敗
                    sGlobals.openWarning("login/store","ログイン失敗",this);
                }
            }
        }
    }
    public void hdzClientCallbackGetError(String error) {
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (isStarted) {
//            return;
//        }
//        isStarted = true;
//
//        // 画面遷移
//        Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
//        startActivity(intent);
//    }



    protected void initTabs() {
        try {
            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();
            TabHost.TabSpec spec;

            // Tab1
            spec = tabHost.newTabSpec("Tab1")
                    .setIndicator("Home", ContextCompat.getDrawable(this, R.drawable.ic_home_white_36dp))
                    .setContent(R.id.linearLayout);
            tabHost.addTab(spec);

            // Tab2
            spec = tabHost.newTabSpec("Tab2")
                    .setIndicator("Event", ContextCompat.getDrawable(this, R.drawable.ic_event_white_36dp))
                    .setContent(R.id.linearLayout2);
            tabHost.addTab(spec);

//            // Tab3
//            spec = tabHost.newTabSpec("Tab3")
////                    .setIndicator("Event", ContextCompat.getDrawable(this, R.drawable.ic_event_white_36dp))
//                    .setContent(R.id.linearLayout3);
//            tabHost.addTab(spec);

            tabHost.setCurrentTab(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
