package com.hidezo.app.buyer;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by dezami on 2016/09/21.
 *
 */
public class CustomAppCompatActivity extends AppCompatActivity implements AppGlobals.CheckLoginCallbacks {

    // グローバル
//    AppGlobals sGlobals;

    /**
     * ログインチェック結果
     */
    public void responseLoginState(boolean isLogin) {

        if (isLogin) {
            // HTTP GET
        }
        else {
            AppGlobals globals = (AppGlobals) this.getApplication();
            globals.openWarning("別デバイスでログインされた","TODO:ログアウト処理",this);
        }
    }

    /**
     * ツールバー初期化
     */
    public void setNavigationBar(final String title) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

}
