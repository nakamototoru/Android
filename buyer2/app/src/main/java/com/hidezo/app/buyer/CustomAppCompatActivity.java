package com.hidezo.app.buyer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

/**
 * Created by dezami on 2016/09/21.
 *
 */
public class CustomAppCompatActivity extends AppCompatActivity implements HDZClient.HDZCallbacks {

    ProgressDialog progressDialog = null;

    @Override
    protected void onResume() {
        super.onResume();

        // ログインチェック
        if (!isLogin()) {
            // ログアウト促す
//            AppGlobals globals = (AppGlobals) this.getApplication();
            openAlertSessionOut();
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
    protected void setNavigationBar(final String title, boolean isBack) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
//        if (isBack) {
//            toolbar.setNavigationIcon(R.drawable.ic_event_white_36dp);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //do something you want
//                    finish();
//                }
//            });
//        }
        setSupportActionBar(toolbar);

        if (isBack) {
            // UPナビゲーションを有効化する
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do something you want
                    finish();
                }
            });

        }
    }

    /**
     * ワーニング
     */
    public void openWarning(final String title, final String message) {
        final CustomAppCompatActivity _self = this;

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });
    }
    public void openAlertSessionOut() {
        final CustomAppCompatActivity _self = this;

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle("アクセスエラー")
                        .setMessage("他の端末でお客様のアカウントにログインしたか、サーバーの不具合でログアウトされました。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // ログアウト
                                AppGlobals globals = (AppGlobals) getApplication();
                                globals.setLoginState(false);

                                // ログインフォーム画面遷移
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }


    /**
     * プログレスダイアログ
     */
    protected void openProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("データ取得中");
            progressDialog.setMessage("お待ち下さい");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }
    protected void openPostProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("データ送信中");
            progressDialog.setMessage("お待ち下さい");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }
    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
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

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

//        AppGlobals globals = (AppGlobals) this.getApplication();
        // 警告
        openWarning("アクセスエラー","ネットワークにアクセス出来ませんでしたので時間を置いて再試行して下さい。");

        // ログアウトチェック
//        globals.openAlertSessionOut(this);
    }

    /**
     * ログアウトチェック
     */
    boolean checkLogOut(String response) {

        final HDZApiResponse apiRes = new HDZApiResponse();

        final AppGlobals globals = (AppGlobals) this.getApplication();

        if (!apiRes.parseJson(response)) {
            // アクセスエラー
            openAlertSessionOut();
            return true;
        }

        // ログアウトチェック
        if (globals.checkLogOut( apiRes.result, apiRes.message )) {
            openAlertSessionOut();
            return true;
        }

        // ログアウトしていない
        return false;
    }
    void openLogoutDialog() {
        final CustomAppCompatActivity _self = this;

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(_self)
                        .setTitle("ログアウトします")
                        .setMessage("よろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                AppGlobals globals = (AppGlobals)getApplication();
                                globals.resetOrderInfoWithMessage(true);
                                globals.setLoginState(false);

                                // ログインフォーム画面遷移
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }
}
