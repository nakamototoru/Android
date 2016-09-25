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

    /**
     * 定数
     */
    public static final String STR_ZERO = "0";

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
    public String getUuid() {

        // インスタンスを取得する
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("uuid", ""); // F0CAC7E8-D81C-4667-BE8B-588869EF5D25
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
        return sp.getString("user_id", ""); // 6146740737615597570
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
     * ログアウトチェック
     */
    public boolean checkLogOut(boolean result, String message) {

        if (message.equals("データがありません")) {
            return false;
        }
        else if (result) {
            return false;
        }
        return true;
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
    public void openAlertSessionOut(final AppCompatActivity activity) {

        //UIスレッド上で呼び出してもらう
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle("アクセスエラー")
                        .setMessage("他の端末でお客様のアカウントにログインしたか、サーバーの不具合でログアウトされました。")
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

    /**
     * 注文カート
     */
    public static class CartCount {
        int count = 0;
    }

    public void createCart() {
        DBHelper dBHelper;
        try {
            // 一度クリエイトすれば次からはスキップされる
            dBHelper = new DBHelper(getApplicationContext());
            dBHelper.sendSuccess();
        } catch(Exception e) {
            Log.d(DauHelper.TAG, e.getMessage());
        }
    }
    public Dau selectCartDau(final String supplier_id, final String item_id) {
        Dau dau = DauHelper.getDau(getApplicationContext(), supplier_id, item_id);
        if (dau != null) {
            Log.d(DauHelper.TAG, dau.toString());
        }
        List<Dau> list = DauHelper.getDauList(getApplicationContext());
        Log.d(DauHelper.TAG, " list result : " + list.size());

        return dau;
    }
    public List<Dau> selectCartList(final String supplier_id) {
        List<Dau> list_dau = DauHelper.getDauList(getApplicationContext(),supplier_id);
        List<Dau> response = new ArrayList<>();
        response.addAll(list_dau);
        return response;
    }
//    public void selectCartAll() {
//        List<Dau> list_dau = DauHelper.getDauList(getApplicationContext());
//        if (list_dau.size() > 0) {
//            for (Dau dau : list_dau) {
//                Log.d(DauHelper.TAG, dau.toString());
//            }
//        }
//        Log.d(DauHelper.TAG, " list result : " + list_dau.size());
//    }
    public long insertCart(final String supplier_id, final String item_id, final String order_size) {
        ContentValues contentValues = new ContentValues();
        // "カラム名","値"
        contentValues.put(Dau.getKeyId() ,UUID.randomUUID().toString());
        contentValues.put(Dau.getKeySupplierId(), supplier_id);
        contentValues.put(Dau.getKeyItemId(), item_id);
        contentValues.put(Dau.getKeyOrderSize(), order_size);
        contentValues.put(Dau.getKeyCreatedAt(), System.currentTimeMillis());
        contentValues.put(Dau.getKeyUpdatedAt(), System.currentTimeMillis());
        long result = DauHelper.insert(getApplicationContext(), contentValues);

        Log.d(DauHelper.TAG, " insert result : " + result);

        return result;
    }
    public long updateCart(final String supplier_id, final String item_id, final String order_size) {
        ContentValues contentValues = new ContentValues();
        // "カラム名","値"
//        contentValues.put(Dau.getKeyId() ,"2");
//        contentValues.put(Dau.getKeySupplierId(), "20160601");
//        contentValues.put(Dau.getKeyItemId(), "7300");
        contentValues.put(Dau.getKeyOrderSize(), order_size);
        contentValues.put(Dau.getKeyUpdatedAt(), System.currentTimeMillis());
        long result = DauHelper.update(getApplicationContext(), contentValues, supplier_id, item_id);

        Log.d(DauHelper.TAG, " update result : " + result);

        return result;
    }
    public long replaceCart(final String supplier_id, final String item_id, final String order_size) {

        Dau dau = selectCartDau(supplier_id,item_id);
        long result;
        if (dau == null) {
            // 新規
            result = insertCart(supplier_id,item_id,order_size);
        }
        else {
            // 更新
            result = updateCart(supplier_id,item_id,order_size);
        }
        return result;
    }

    public void deleteCart(final String supplier_id, final String item_id) {
        long result = DauHelper.delete(getApplicationContext(), supplier_id,item_id);
        Log.d(DauHelper.TAG, " delete result : " + result);
    }
}
