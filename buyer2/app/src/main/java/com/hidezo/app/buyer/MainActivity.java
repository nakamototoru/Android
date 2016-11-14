package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * ログインフォーム画面・最初に呼び出される
 */
public class MainActivity extends AppCompatActivity implements HDZClient.HDZCallbacks {

    String myUserId = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity _self = this;

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        final AppGlobals globals = (AppGlobals) this.getApplication();

        // データベース作成
        globals.createCart();

        // エディットテキスト
        final TextView tvEditId = (TextView) findViewById(R.id.editTextUserId);
        tvEditId.setText(globals.getUserId());

        // クリックイベントを取得したいボタン
        final Button button = (Button) findViewById(R.id.buttonLogin);
        // ボタンに OnClickListener インターフェースを実装する
        button.setOnClickListener(new View.OnClickListener() {
            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(final View view) {
                // ログイン処理
                final TextView tvId = (TextView) findViewById(R.id.editTextUserId);
                myUserId = tvId.getText().toString();
                final TextView tvPass = (TextView) findViewById(R.id.editTextPassword);
                final String password = tvPass.getText().toString();
                final String uuid = globals.createUuid();
                // HTTP POST
                final HDZApiRequestPackage.Login req = new HDZApiRequestPackage.Login();
                req.begin( myUserId, uuid, password, _self);
            }
        });

        // ツールバー初期化
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ログインチェック
        if (globals.getLoginState()) {
            // 画面遷移
            final Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
            startActivity(intent);
        }
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {

        if (apiName.equals("login/store")) {
            // ログイン処理
            final AppGlobals globals = (AppGlobals) this.getApplication();
            final HDZApiResponse responseLogin = new HDZApiResponse();
            if (responseLogin.parseJson(response)) {
                if (responseLogin.result) {
                    // ログイン
                    globals.setUserId(myUserId);
                    globals.setLoginState(true);
                    globals.resetOrderInfoWithMessage(true);
                    // 画面遷移
                    final Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                    startActivity(intent);
                }
                else {
                    //ログイン失敗
                    globals.setLoginState(false);
                    openWarning("エラー",responseLogin.message);
                }
            }
            else {
                //ログイン失敗
                globals.setLoginState(false);
                openWarning("エラー",responseLogin.message);
            }
        }
    }
    public void HDZClientError(final String error) {
        // 警告
        openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。");
    }

    /**
     * ワーニング
     */
    public void openWarning(final String title, final String message) {
        final MainActivity _self = this;
        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * ツールバー
     * @param menu menu
     * @return menu on
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
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
