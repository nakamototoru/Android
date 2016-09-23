package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.content.ContextCompat;
//import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.TabHost;
//import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements HDZClient.HDZCallbacks {

    private static MainActivity _self;

    private HDZApiResponse responseLogin = new HDZApiResponse();

    // グローバル
    private AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _self = this;

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        // エディットテキスト
        TextView tvEditId = (TextView) findViewById(R.id.editTextUserId);
        tvEditId.setText(sGlobals.getUserId());
        TextView tvEditPass = (TextView) findViewById(R.id.editTextPassword);
        tvEditPass.setText("test");

        // クリックイベントを取得したいボタン
        Button button = (Button) findViewById(R.id.buttonLogin);
        // ボタンに OnClickListener インターフェースを実装する
        button.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {

                // ログイン処理
                TextView tvPass = (TextView) findViewById(R.id.editTextPassword);
                String password = tvPass.getText().toString();
                String uuid = sGlobals.createUuid();
                // HTTP POST
                HDZApiRequestPackage.Login req = new HDZApiRequestPackage.Login();
                req.begin( sGlobals.getUserId(), uuid, password, _self);
            }
        });

        // ツールバー初期化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ログインチェック
        if (sGlobals.getLoginState()) {
            // 画面遷移
            Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
            startActivity(intent);
        }
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        if (apiName.equals("login/store")) {
            if (responseLogin.parseJson(response)) {
                if (responseLogin.result) {

                    sGlobals.setLoginState(true);
                    // 画面遷移
                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                    startActivity(intent);
                }
                else {
                    //ログイン失敗
                    sGlobals.setLoginState(false);

                    sGlobals.openWarning("ログインできません","IDかパスワードが違います",this);
                }
            }
        }
    }
    public void HDZClientError(String error) {
        AppGlobals globals = (AppGlobals) this.getApplication();
        // 警告
        globals.openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。",this);
    }


    /**
     * ツールバー
     * @param menu menu
     * @return menu on
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
