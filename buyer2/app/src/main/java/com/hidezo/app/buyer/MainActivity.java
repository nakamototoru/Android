package com.hidezo.app.buyer;

import android.content.Intent;
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

    private String myUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity _self = this;

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        final AppGlobals globals = (AppGlobals) this.getApplication();

        // データベース作成
        globals.createCart();

        // エディットテキスト
        TextView tvEditId = (TextView) findViewById(R.id.editTextUserId);
        tvEditId.setText(globals.getUserId());

        // クリックイベントを取得したいボタン
        Button button = (Button) findViewById(R.id.buttonLogin);
        // ボタンに OnClickListener インターフェースを実装する
        button.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {

                // ログイン処理
                TextView tvId = (TextView) findViewById(R.id.editTextUserId);
                myUserId = tvId.getText().toString();
                TextView tvPass = (TextView) findViewById(R.id.editTextPassword);
                String password = tvPass.getText().toString();
                String uuid = globals.createUuid();
                // HTTP POST
                HDZApiRequestPackage.Login req = new HDZApiRequestPackage.Login();
                req.begin( myUserId, uuid, password, _self);
            }
        });

        // ツールバー初期化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ログインチェック
        if (globals.getLoginState()) {
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
            // ログイン処理
            final AppGlobals globals = (AppGlobals) this.getApplication();
            final HDZApiResponse responseLogin = new HDZApiResponse();
            if (responseLogin.parseJson(response)) {
                if (responseLogin.result) {

                    globals.setUserId(myUserId);
                    globals.setLoginState(true);
                    // 画面遷移
                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
                    startActivity(intent);
                }
                else {
                    //ログイン失敗
                    globals.setLoginState(false);
                    globals.openWarning("エラー",responseLogin.message,this);
                }
            }
            else {
                //ログイン失敗
                globals.setLoginState(false);
                globals.openWarning("エラー",responseLogin.message,this);
            }
        }
    }
    public void HDZClientError(String error) {
        final AppGlobals globals = (AppGlobals) this.getApplication();
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
