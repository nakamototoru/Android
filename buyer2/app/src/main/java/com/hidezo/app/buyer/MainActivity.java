package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

// FragmentActivity implements FragmentTabHost.OnTabChangeListener
public class MainActivity extends AppCompatActivity implements HDZClient.HDZCallbacks,AppGlobals.CheckLoginCallbacks {

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

        // ツールバー初期化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        // エディットテキスト
        TextView tvEditId = (TextView) findViewById(R.id.editTextUserId);
        tvEditId.setText(sGlobals.getUserId());
        TextView tvEditPass = (TextView) findViewById(R.id.editTextPassword);
        tvEditPass.setText("test");

//        // クリックイベントを取得したいボタン
//        Button button = (Button) findViewById(R.id.buttonLogin);
//        // ボタンに OnClickListener インターフェースを実装する
//        button.setOnClickListener(new View.OnClickListener() {
//
//            // クリック時に呼ばれるメソッド
//            @Override
//            public void onClick(View view) {
//
//                TextView tvPass = (TextView) findViewById(R.id.editTextPassword);
//                String password = tvPass.getText().toString();
//
//                // HTTP POST
//                HDZApiRequestPackage.Login req = new HDZApiRequestPackage.Login();
//                req.begin( sGlobals.getUserId(), sGlobals.getUuid(), password, _self);
//            }
//        });

        // ログインチェック
        sGlobals.checkLogin(this);

//        initTabs();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
//        if (apiname.equals("login_check/store")) {
//            if (responseLoginCheck.parseJson(response)) {
//                if (responseLoginCheck.result) {
//
//                    //ログインしている
//                    // 画面遷移
////                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
////                    startActivity(intent);
//                }
//                else {
//
//                    //ログインを促す
//                    sGlobals.openWarning("login_check/store","ログインを促す",this);
//                }
//            }
//        }

//        if (apiname.equals("login/store")) {
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
//            }
        }
    }
    public void HDZClientError(String error) {
    }

    /**
     * ログインチェック結果
     */
    public void responseLoginState(boolean isLogin) {

        if (isLogin) {
            // 画面遷移
            Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
            startActivity(intent);
        }
        else {
            // クリックイベントを取得したいボタン
            Button button = (Button) findViewById(R.id.buttonLogin);
            // ボタンに OnClickListener インターフェースを実装する
            button.setOnClickListener(new View.OnClickListener() {

                // クリック時に呼ばれるメソッド
                @Override
                public void onClick(View view) {

                    TextView tvPass = (TextView) findViewById(R.id.editTextPassword);
                    String password = tvPass.getText().toString();

                    // HTTP POST
                    HDZApiRequestPackage.Login req = new HDZApiRequestPackage.Login();
                    req.begin( sGlobals.getUserId(), sGlobals.getUuid(), password, _self);
                }
            });
        }
    }

    /**
     * ツールバー
     * @param menu menu
     * @return
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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



//    protected void initTabs() {
//        try {
//            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
//            tabHost.setup();
//            TabHost.TabSpec spec;
//
//            // Tab1
//            spec = tabHost.newTabSpec("Tab1")
//                    .setIndicator("Home", ContextCompat.getDrawable(this, R.drawable.ic_home_white_36dp))
//                    .setContent(R.id.linearLayout);
//            tabHost.addTab(spec);
//
//            // Tab2
//            spec = tabHost.newTabSpec("Tab2")
//                    .setIndicator("Event", ContextCompat.getDrawable(this, R.drawable.ic_event_white_36dp))
//                    .setContent(R.id.linearLayout2);
//            tabHost.addTab(spec);
//
////            // Tab3
////            spec = tabHost.newTabSpec("Tab3")
//////                    .setIndicator("Event", ContextCompat.getDrawable(this, R.drawable.ic_event_white_36dp))
////                    .setContent(R.id.linearLayout3);
////            tabHost.addTab(spec);
//
//            tabHost.setCurrentTab(0);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//    }

}
