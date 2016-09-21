package com.hidezo.app.buyer;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by dezami on 2016/09/21.
 *
 */
public class CustomAppCompatActivity extends AppCompatActivity implements HDZClient.HDZCallbacks {
    // AppGlobals.CheckLoginCallbacks,

    /**
     * ログインチェック
     */
//    public void checkLogin() {
//        AppGlobals globals = (AppGlobals) this.getApplication();
//        globals.checkLogin(this);
//    }
    public boolean isLogin() {
        AppGlobals globals = (AppGlobals) this.getApplication();
        return globals.getLoginState();
    }

    /**
     * ログインチェック結果
     */
//    public void responseLoginState(boolean isLogin) {
//
//        if (!isLogin) {
//            AppGlobals globals = (AppGlobals) this.getApplication();
////            globals.openWarning("別デバイスでログインされた","TODO:ログアウト処理",this);
//            globals.openAlertSessionOut(this);
//        }
//        else {
//            onLoginPassed();
//        }
//    }
    /**
     * ログイン状態の時の処理
     */
//    public void onLoginPassed() {
//        // サブクラスで定義
//    }

    /**
     * ツールバー初期化
     */
    protected void setNavigationBar(final String title) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
        // サブクラスで定義
    }
    public void HDZClientError(String message) {
        Log.d("########",message);

        // ログアウト
//        AppGlobals globals = (AppGlobals) this.getApplication();
//        globals.openAlertSessionOut(this);
    }

}
