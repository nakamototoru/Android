package com.hidezo.app.buyer;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.hidezo.app.buyer.model.Dau;
import com.hidezo.app.buyer.model.DauHelper;
import com.hidezo.app.buyer.util.DBHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
        final String uuid = UUID.randomUUID().toString();

        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString("uuid", uuid);
        ed.apply();
        result.commit();

        Log.d("########",uuid);

        return uuid;
    }
    public String getUuid() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("uuid", "");
    }

    /**
     * USER ID
     * @param value user_id
     */
    public void setUserId(final String value) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString("user_id", value);
        ed.apply();
        result.commit();
    }
    public String getUserId() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("user_id", ""); // 6146740737615597570
    }

    /**
     * LOGIN ID
     */
    public void setLoginId(final String value) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString("login_id", value);
        ed.apply();
        result.commit();
    }
    public String getLoginId() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString("login_id", "");
    }

    /**
     * ログイン状態
     */
    public void setLoginState(final boolean isLogin) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putBoolean("is_login",isLogin);
        ed.apply();
        result.commit();
    }
    public boolean getLoginState() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getBoolean("is_login",false);
    }

    /**
     * ログアウトチェック
     */
    public boolean checkLogOut(final boolean result, final String message) {

        Log.d("## Global","checkLogOut");

        if (message.equals("データがありません")) {
            Log.d("##", "データがありません");
            return false;
        }
        else if (result) {
            Log.d("##", "result = true");
            return false;
        }
        return true;
    }

    /**
     * 注文前入力・メッセージ
     */
    private static final String keyMessage = "message";
    public void setOrderMessage(final String text) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString(keyMessage, text);
        ed.apply();
        result.commit();
    }
    public String getOrderMessage() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString(keyMessage, "");
    }

    /**
     * 注文前入力・納品日
     */
    private static final String keyDeliverDay = "deliver_day";
//    public static final List<String> deliverDayList = Collections.unmodifiableList(new LinkedList<String>() {
//        {
//            // 登録
//            add("最短納品日"); // 0
//            add("月曜日"); // 1
//            add("火曜日"); // 2
//            add("水曜日"); // 3
//            add("木曜日"); // 4
//            add("金曜日"); // 5
//            add("土曜日"); // 6
//            add("日曜日"); // 6
//        }
//    });
    public void setOrderDeliverDay(final String text) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString(keyDeliverDay, text);
        ed.apply();
        result.commit();
    }
    public String getOrderDeliverDay() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString(keyDeliverDay, "");
    }

    /**
     * 注文前入力・担当者
     */
    private static final String keyCharge = "charge";
    public void setOrderCharge(final String text) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString(keyCharge, text);
        ed.apply();
        result.commit();
    }
    public String getOrderCharge() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString(keyCharge, "");
    }

    /**
     * 注文前入力・配送先
     */
    private static final String keyDeliverPlace = "deliver_place";
    public void setOrderDeliverPlace(final String text) {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを保存する
        final SharedPreferences.Editor ed = sp.edit();
        final SharedPreferences.Editor result = ed.putString(keyDeliverPlace, text);
        ed.apply();
        result.commit();
    }
    public String getOrderDeliverPlace() {
        // インスタンスを取得する
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // データを取得する(第2引数はデフォルト値)
        return sp.getString(keyDeliverPlace, "");
    }

    /**
     * 注文前入力リセット
     */
    public void resetOrderInfoWithMessage(final boolean isMessageAlso) {
        setOrderCharge("");
        setOrderDeliverDay("");
        setOrderDeliverPlace("");
        if (isMessageAlso) {
            setOrderMessage("");
        }
    }

    /**
     * 注文カート操作
     */
    public void createCart() {
        final DBHelper dBHelper;
        try {
            // 一度クリエイトすれば次からはスキップされる
            dBHelper = new DBHelper(getApplicationContext());
            dBHelper.sendSuccess();
        } catch(final Exception e) {
            Log.d(DauHelper.TAG, e.getMessage());
        }
    }
    public Dau selectCartDau(final String supplier_id, final String item_id) {
        final Dau dau = DauHelper.getDau(getApplicationContext(), supplier_id, item_id);
        if (dau != null) {
            Log.d(DauHelper.TAG, dau.toString());
        }
        final List<Dau> list = DauHelper.getDauList(getApplicationContext());
        Log.d(DauHelper.TAG, " list result : " + list.size());

        return dau;
    }
    public List<Dau> selectCartList(final String supplier_id) {
        final List<Dau> list_dau = DauHelper.getDauList(getApplicationContext(),supplier_id);
        final List<Dau> response = new ArrayList<>();
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
    public long insertCart(final String supplier_id, final String item_id, final String order_size, final boolean is_dynamic) {
        final ContentValues contentValues = new ContentValues();
        // "カラム名","値"
        contentValues.put(Dau.getKeyId() ,UUID.randomUUID().toString());
        contentValues.put(Dau.getKeySupplierId(), supplier_id);
        contentValues.put(Dau.getKeyItemId(), item_id);
        contentValues.put(Dau.getKeyOrderSize(), order_size);
        contentValues.put(Dau.getKeyIsDYnamic(), is_dynamic);
        contentValues.put(Dau.getKeyCreatedAt(), System.currentTimeMillis());
        contentValues.put(Dau.getKeyUpdatedAt(), System.currentTimeMillis());
        final long result = DauHelper.insert(getApplicationContext(), contentValues);

        Log.d(DauHelper.TAG, " insert result : " + result);

        return result;
    }
    public long updateCart(final String supplier_id, final String item_id, final String order_size, final boolean is_dynamic) {
        final ContentValues contentValues = new ContentValues();
        // "カラム名","値"
        contentValues.put(Dau.getKeyOrderSize(), order_size);
        contentValues.put(Dau.getKeyIsDYnamic(), is_dynamic);
        contentValues.put(Dau.getKeyUpdatedAt(), System.currentTimeMillis());
        final long result = DauHelper.update(getApplicationContext(), contentValues, supplier_id, item_id);

        Log.d(DauHelper.TAG, " update result : " + result);

        return result;
    }
    public long replaceCart(final String supplier_id, final String item_id, final String order_size, final boolean is_dynamic) {

        final Dau dau = selectCartDau(supplier_id,item_id);
        final long result;
        if (dau == null) {
            // 新規
            result = insertCart(supplier_id,item_id,order_size, is_dynamic);
        }
        else {
            // 更新
            result = updateCart(supplier_id,item_id,order_size, is_dynamic);
        }
        return result;
    }

    public void deleteCart(final String supplier_id, final String item_id) {
        final long result = DauHelper.delete(getApplicationContext(), supplier_id,item_id);
        Log.d(DauHelper.TAG, " delete result : " + result);
    }

    public void deleteAllCart() {
        final long result = DauHelper.deleteAll(getApplicationContext());
        Log.d(DauHelper.TAG, " delete result : " + result);
    }
}
