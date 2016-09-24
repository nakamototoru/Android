/**
 * Copyright (C) 2016 Programming Java Android Development Project
 * Programming Java is
 *
 *      http://java-lang-programming.com/
 *
 * Model Generator version : 1.3.1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hidezo.app.buyer.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hidezo.app.buyer.BuildConfig;
import com.hidezo.app.buyer.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * An ModelHelper class for DauHelper
 *
 */
public class DauHelper {

    public static final String TAG = "DauHelper";

    /**
     * return Dau List
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @return the list objects of rows, null otherwise.
     */
    public static List<Dau> getDauList(final Context context) {
        List<Dau> list = new ArrayList<Dau>();
        Dau dau = null;
        Cursor c = null;
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(context);

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(0) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(1) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(2) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(3) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(4) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(5) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(6) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(7) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(8) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(9));
            sql.append(" from ");
            sql.append(" " + Dau.getTableName() + " " + Dau.getTableNameOmission());

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "sql" + sql.toString());
            }

            c = dBHelper.db.rawQuery(sql.toString(), null);

            boolean isResult = c.moveToFirst();

            while (isResult) {
                dau = new Dau();
                dau.id = c.getInt(0);
//                dau.dau_date = c.getString(1);
//                dau.opening_price = c.getString(2);
//                dau.high_price = c.getString(3);
//                dau.low_price = c.getString(4);
//                dau.closing_price = c.getString(5);
//                dau.change_price = c.getString(6);
                dau.deleted_at = c.getString(7);
                dau.created_at = c.getString(8);
                dau.updated_at = c.getString(9);
                list.add(dau);
                isResult = c.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "error occured!! cause : " + e.getMessage());
        } finally {

            if (c != null) {
                c.close();
            }

            if (dBHelper != null) {
                dBHelper.cleanup();
            }
        }
        return list;
    }

    /**
     * return Dau
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @param id Dau's id
     * @return the object of rows affected if id is passed in, null otherwise.
     */
    public static Dau getDau(final Context context, final String id) {
        Dau dau = null;
        Cursor c = null;
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(context);

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(0) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(1) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(2) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(3) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(4) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(5) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(6) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(7) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(8) + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.COL.get(9));
            sql.append(" from ");
            sql.append(" " + Dau.getTableName() + " " + Dau.getTableNameOmission());
            sql.append(" where " + Dau.getTableNameOmission() + "." + Dau.COL.get(0) + "=\"" +  id + "\"");
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "sql" + sql.toString());
            }

            c = dBHelper.db.rawQuery(sql.toString(), null);

            boolean isResult = c.moveToFirst();

            if (isResult) {
                dau = new Dau();
                dau.id = c.getInt(0);
//                dau.dau_date = c.getString(1);
//                dau.opening_price = c.getString(2);
//                dau.high_price = c.getString(3);
//                dau.low_price = c.getString(4);
//                dau.closing_price = c.getString(5);
//                dau.change_price = c.getString(6);
                dau.deleted_at = c.getString(7);
                dau.created_at = c.getString(8);
                dau.updated_at = c.getString(9);
                isResult = c.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "error occured!! cause : " + e.getMessage());
        } finally {

            if (c != null) {
                c.close();
            }

            if (dBHelper != null) {
                dBHelper.cleanup();
            }
        }
        return dau;
    }

    /**
     * Return max id + 1 if not 1
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @return maxId of id
     */
    public static int getMaxId(final Context context) {
        Cursor c = null;
        DBHelper dbhelper = null;
        int maxId = 0;
        try {
            String sql = "select coalesce(id, 0) from " + Dau.getTableName() + " order by id desc limit 1 offset 0";
            dbhelper = new DBHelper(context);
            c = dbhelper.db.rawQuery(sql, null);
            int numRows = c.getCount();
            c.moveToFirst();
            if (0 < numRows) {
                maxId = c.getInt(0);
                c.moveToNext();
            }
            maxId++;
        } catch (Exception e) {
            Log.v(TAG, TAG, e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (dbhelper != null) {
                dbhelper.cleanup();
            }
            return maxId;
        }
    }

    /**
     * Return ContentValues
     *
     * @return contentValues
     */
    public static ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        // TODO something
        return contentValues;
    }

    /**
     * update
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @param contentValues key&value
     * @param id is primary key
     * @return the number of rows affected
     */
    public static long update(final Context context, final ContentValues contentValues, String id) {
        DBHelper dBHelper = new DBHelper(context);
        long result = dBHelper.db.update(Dau.getTableName(), contentValues, Dau.COL.get(0) + "=" + id, null);
        dBHelper.cleanup();
        return result;
    }

    /**
     * insert
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @param contentValues key&value
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public static long insert(final Context context, final ContentValues contentValues) {
        DBHelper dBHelper = new DBHelper(context);
        long result = dBHelper.db.insert(Dau.getTableName(), null, contentValues);
        dBHelper.cleanup();
        return result;
    }

    /**
     * delete
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @param id is primary key
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public static long delete(final Context context, String id) {
        DBHelper dBHelper = new DBHelper(context);
        int result = dBHelper.db.delete(Dau.getTableName(), Dau.COL.get(0) + "=" + id, null);
        dBHelper.cleanup();
        return result;
    }
}