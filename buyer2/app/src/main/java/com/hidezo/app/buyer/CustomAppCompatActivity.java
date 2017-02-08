package com.hidezo.app.buyer;

//import android.app.Activity;
//import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.ActionBar;
//import android.os.Bundle;
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
    // ,Application.ActivityLifecycleCallbacks

    private static final String TAG = "#CustomActivity";

    ProgressDialog progressDialog = null;
//    ProgressDialog progressNotification = null;

    @Override
    protected void onResume() {
        super.onResume();

        // ログインチェック
        if (!isLogin()) {
            // ログアウト促す
            openAlertSessionOut();
        }
    }

    /**
     * ログインチェック
     */
    public boolean isLogin() {
        final AppGlobals globals = (AppGlobals) this.getApplication();
        return globals.getLoginState();
    }

    /**
     * ツールバー初期化
     */
    protected void setNavigationBar(final String title, final boolean isBack) {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        // 戻るボタン
        if (isBack) {
            // UPナビゲーションを有効化する
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Log.d(TAG,"toolbar.onClick");

                    onClickNavigationBack();
                }
            });
        }
    }
    void onClickNavigationBack() {
        // サブクラスに実装
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
                            public void onClick(final DialogInterface dialog, final int id) {
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
                            public void onClick(final DialogInterface dialog, final int id) {
                                // ログアウト
                                final AppGlobals globals = (AppGlobals) getApplication();
                                globals.setLoginState(false);

                                // ログインフォーム画面遷移
                                final Intent intent = new Intent(getApplication(), MainActivity.class);
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
    public void openProgressDialog(final String title, final String message) {
        if (progressDialog == null) {
            final CustomAppCompatActivity _self = this;
            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(_self);
                    progressDialog.setTitle(title);
                    progressDialog.setMessage(message);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                }
            });
        }
    }
    public void closeProgressDialog() {
        if (progressDialog != null) {
//            final CustomAppCompatActivity _self = this;
            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            });
        }
    }

    void openHttpGetProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("データ取得中");
            progressDialog.setMessage("お待ち下さい");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }
    void openHttpPostProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("データ送信中");
            progressDialog.setMessage("お待ち下さい");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }
    void closeHttpProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    /**
     * 通知ダイアログ
     */
//    protected void openNotificationDialog(final String message) {
//        if (progressNotification == null) {
//            progressNotification = new ProgressDialog(this);
//            progressNotification.setMessage(message);
//            progressNotification.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, final int which) {
//                            progressNotification = null;
//                        }
//                    }
//            );
//            progressNotification.show();
//        }
//    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response,final String apiName) {
        // サブクラスで定義
    }
    public void HDZClientError(final String message) {
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
    boolean checkLogOut(final String response) {

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
                // ダイアログ
                new AlertDialog.Builder(_self)
                        .setTitle("ログアウトします")
                        .setMessage("よろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                                final AppGlobals globals = (AppGlobals)getApplication();
                                globals.resetOrderInfoWithMessage(true);
                                globals.setLoginState(false);

                                // ログインフォーム画面遷移
                                final Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);

                                // HTTP GET
                                final HDZApiRequestPackage.logOut req = new HDZApiRequestPackage.logOut();
                                req.begin(globals.getUserId(),globals.getUuid(),null);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * サブクラスで使用
     * @param base64data Base64 string
     */
    public void respondBase64String(final String base64data) {

    }

    // ActivityLifecycleCallbacksのメソッド郡
//    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//    }
//
//    public void onActivityPaused(Activity activity) {
//    }
//
//    public void onActivityDestroyed(Activity activity) {
//    }
//
//    public void onActivityStarted(Activity activity) {
//    }
//
//    public void onActivityResumed(Activity activity) {
//    }
//
//    public void onActivityStopped(Activity activity) {
//    }
//
//    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//    }
}
