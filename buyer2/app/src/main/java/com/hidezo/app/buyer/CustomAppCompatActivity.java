package com.hidezo.app.buyer;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by dezami on 2016/09/21.
 *
 */
public class CustomAppCompatActivity extends AppCompatActivity implements HDZClient.HDZCallbacks {

    @Override
    protected void onResume() {
        super.onResume();

        // ログインチェック
        if (!isLogin()) {
            // ログアウト促す
            AppGlobals globals = (AppGlobals) this.getApplication();
            globals.openAlertSessionOut(this);
        }
    }

    /**
     * ログインチェック
     */
    public boolean isLogin() {
        AppGlobals globals = (AppGlobals) this.getApplication();
        return globals.getLoginState();
    }

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
    public void HDZClientComplete(String response,String apiName) {
        // サブクラスで定義
    }
    public void HDZClientError(String message) {
        Log.d("########",message);

        AppGlobals globals = (AppGlobals) this.getApplication();
        // 警告
        globals.openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。",this);

        // ログアウトチェック
//        globals.openAlertSessionOut(this);
    }

    /**
     * ログアウトチェック
     */
    public boolean checkLogOut(String response) {

        final HDZApiResponse apiRes = new HDZApiResponse();

        final AppGlobals globals = (AppGlobals) this.getApplication();

        if (!apiRes.parseJson(response)) {
            // アクセスエラー
            globals.openAlertSessionOut(this);
            return true;
        }

        // ログアウトチェック
        if (globals.checkLogOut( apiRes.result, apiRes.message )) {
            globals.openAlertSessionOut(this);
            return true;
        }

        // ログアウトしていない
        return false;
    }
}
