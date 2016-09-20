package com.hidezo.app.buyer;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by dezami on 2016/09/14.
 *
 */
public class AppGlobals extends Application implements HDZClient.HDZCallbacks {

    /**
     * UUID
     * @return uuid
     */
    public String createUuid() {

        String uuid = UUID.randomUUID().toString();

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        SharedPreferences.Editor ed = sp.edit();
        SharedPreferences.Editor result = ed.putString("uuid", uuid);
        result.commit();

        return uuid;
    }
//    public void setUuid(String value) {
//
//        // インスタンスを取得する
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        // データを保存する
//        SharedPreferences.Editor ed = sp.edit();
//        SharedPreferences.Editor result = ed.putString("uuid", value);
//        result.commit();
//
////        sp.edit().putString("uuid", value);//.commit();
//    }
    public String getUuid() {

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("uuid", "F0CAC7E8-D81C-4667-BE8B-588869EF5D25");
    }
    public void removeUuid() {
        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        SharedPreferences.Editor ed = sp.edit();
        SharedPreferences.Editor result = ed.putString("uuid", "");
        result.commit();
    }

    /**
     * USER ID
     * @param value user_id
     */
    public void setUserId(String value) {
        //
        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        SharedPreferences.Editor ed = sp.edit();
        SharedPreferences.Editor result = ed.putString("user_id", value);
        result.commit();
    }
    public String getUserId() {

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("user_id", "6146740737615597570");
    }

    /**
     * ログインチェック
     */
    private static CheckLoginCallbacks sCheckLoginCallbacks;
    public interface CheckLoginCallbacks {
        void responseLoginState(boolean isLogin);
    }
    public void checkLogin(CheckLoginCallbacks callbacks) {
        sCheckLoginCallbacks = callbacks;

        HDZApiRequestPackage.LoginCheck req = new HDZApiRequestPackage.LoginCheck();
        req.begin( getUserId(), getUuid(), this);
    }
    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiname) {
        if (apiname.equals("login_check/store")) {

            HDZApiResponse responseLoginCheck = new HDZApiResponse();

            if (responseLoginCheck.parseJson(response)) {
                if (responseLoginCheck.result) {

                    //ログインしている
                    sCheckLoginCallbacks.responseLoginState(true);
                }
                else {
                    //ログインしていない
                    sCheckLoginCallbacks.responseLoginState(false);
                }
            }
        }
//        else if (apiname.equals("login/store")) {
//            if (responseLogin.parseJson(response)) {
//                if (responseLogin.result) {
//
//                    //ログイン状態に
//                    // 画面遷移
//                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
//                    startActivity(intent);
//                }
//                else {
//                    //ログイン失敗
//                    sGlobals.openWarning("login/store","ログイン失敗",this);
//                }
//            }
//        }

    }
    public void HDZClientError(String error) {
    }

    /**
     * ALERT DIALOG
     * @param title title
     * @param activity activity
     */
    public void openWarning(final String title, final String message, final AppCompatActivity activity) {

        //UIスレッド上で呼び出してもらう
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(activity)
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

    /**
     *
     */
    public static class CartCount {
        int count = 0;
    }

}
