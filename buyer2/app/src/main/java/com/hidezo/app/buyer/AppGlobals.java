package com.hidezo.app.buyer;

import android.app.Application;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hidezo.app.buyer.model.Dau;
import com.hidezo.app.buyer.model.DauHelper;
import com.hidezo.app.buyer.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by dezami on 2016/09/14.
 *
 */
public class AppGlobals extends Application {
    //  implements HDZClient.HDZCallbacks

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

        Log.d("########",uuid);

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

    /**
     * USER ID
     * @param value user_id
     */
    public void setUserId(final String value) {

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
     * ログイン状態
     */
    public void setLoginState(final boolean isLogin) {

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        SharedPreferences.Editor ed = sp.edit();
        SharedPreferences.Editor result = ed.putBoolean("is_login",isLogin);
        result.commit();
    }
    public boolean getLoginState() {

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getBoolean("is_login",false);
    }

    /**
     * ログインチェック
     */
//    private static CheckLoginCallbacks sCheckLoginCallbacks;
//    public interface CheckLoginCallbacks {
//        void responseLoginState(boolean isLogin);
//    }
//    public void checkLogin(CheckLoginCallbacks callbacks) {
//        sCheckLoginCallbacks = callbacks;
//
//        HDZApiRequestPackage.LoginCheck req = new HDZApiRequestPackage.LoginCheck();
//        req.begin( getUserId(), getUuid(), this);
//    }
    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
//    public void HDZClientComplete(final String response,final String apiname) {
//        if (apiname.equals("login_check/store")) {
//
//            HDZApiResponseLoginCheck responseLoginCheck = new HDZApiResponseLoginCheck();
//
//            if (responseLoginCheck.parseJson(response)) {
//                if (responseLoginCheck.result) {
//
//                    //ログインしている
//                    sCheckLoginCallbacks.responseLoginState(true);
//                }
//                else {
//                    //ログインしていない
//                    sCheckLoginCallbacks.responseLoginState(false);
//                }
//            }
//        }
////        else if (apiname.equals("login/store")) {
////            if (responseLogin.parseJson(response)) {
////                if (responseLogin.result) {
////
////                    //ログイン状態に
////                    // 画面遷移
////                    Intent intent = new Intent(getApplication(), ActivitySuppliers.class);
////                    startActivity(intent);
////                }
////                else {
////                    //ログイン失敗
////                    sGlobals.openWarning("login/store","ログイン失敗",this);
////                }
////            }
////        }
//
//    }
//    public void HDZClientError(final String error) {
//    }

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
    public void openAlertSessionOut(final AppCompatActivity activity) {

        //UIスレッド上で呼び出してもらう
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(activity)
                        .setTitle("他のデバイスがログインしたかタイムアウトしました")
                        .setMessage("ログインしなおしてください")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                setLoginState(false);

                                // ログインフォーム画面遷移
                                Intent intent = new Intent(activity.getApplication(), MainActivity.class);
                                activity.startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
//    private static String numScaleTemp = "0";
//    public void openDialogNumScale(final AppCompatActivity activity, final String[] listNumScale, final ListView listView, final int position, final AdapterView<?> parent) {
//
//        //UIスレッド上で呼び出してもらう
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new AlertDialog.Builder(activity)
//
//                        .setTitle("選択")
//                        .setItems(listNumScale, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // 選択
//                                numScaleTemp = listNumScale[which];
//                            }
//                        })
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                // 転送
//                                Log.d("########",numScaleTemp);
//                                View targetView = listView.getChildAt(position);
//                                listView.getAdapter().getView(position,targetView,parent);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .show();
//            }
//        });
//
//    }

    /**
     * 注文カート
     */
    public static class CartCount {
        int count = 0;
    }

    public void createCart() {
        DBHelper dBHelper = null;
        try {
            // 一度クリエイトすれば次からはスキップされる
            dBHelper = new DBHelper(getApplicationContext());
        } catch(Exception e) {
            Log.d(DauHelper.TAG, e.getMessage());
        }
    }
    public void selectCart() {
        Dau dau = DauHelper.getDau(getApplicationContext(), "1");
        if (dau != null) {
            Log.d(DauHelper.TAG, dau.toString());
        }
        List<Dau> list = DauHelper.getDauList(getApplicationContext());
        Log.d(DauHelper.TAG, " list result : " + list.size());
    }
    public void insertCart() {
        ContentValues contentValues = new ContentValues();
        // "カラム名","値"
        contentValues.put(Dau.COL.get(0) ,"2");
        contentValues.put(Dau.COL.get(1), "20160601");
        contentValues.put(Dau.COL.get(2), "7000");
        contentValues.put(Dau.COL.get(3), "7200");
        contentValues.put(Dau.COL.get(4), System.currentTimeMillis());
        contentValues.put(Dau.COL.get(5), System.currentTimeMillis());
        long result = DauHelper.insert(getApplicationContext(), contentValues);
        Log.d(DauHelper.TAG, " insert result : " + result);
    }
    public void updateCart() {
        ContentValues contentValues = new ContentValues();
        // "カラム名","値"
        contentValues.put(Dau.COL.get(0) ,"2");
        contentValues.put(Dau.COL.get(1), "20160601");
        contentValues.put(Dau.COL.get(2), "7300");
        contentValues.put(Dau.COL.get(3), "8000");
        contentValues.put(Dau.COL.get(9), System.currentTimeMillis());
        long result = DauHelper.update(getApplicationContext(), contentValues, "1");
        Log.d(DauHelper.TAG, " update result : " + result);
    }
    public void deleteCart() {
        long result = DauHelper.delete(getApplicationContext(), "2");
        Log.d(DauHelper.TAG, " delete result : " + result);
    }
}
