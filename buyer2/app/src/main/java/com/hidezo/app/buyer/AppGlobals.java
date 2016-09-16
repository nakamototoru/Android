package com.hidezo.app.buyer;

import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by dezami on 2016/09/14.
 *
 */
public class AppGlobals extends Application {

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
}
