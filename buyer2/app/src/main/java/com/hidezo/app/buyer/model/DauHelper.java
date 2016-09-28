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

    public static final String TAG = "## DauHelper";

    /**
     * return Dau List
     *
     * @param context you should use ApplicationContext. ApplicationContext can get getApplicationContext().
     * @return the list objects of rows, null otherwise.
     */
    public static List<Dau> getDauList(final Context context) {
        List<Dau> list = new ArrayList<>();
        Dau dau;
        Cursor c = null;
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(context);

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeySupplierId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyItemId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyOrderSize() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyIsDYnamic() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyCreatedAt() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyUpdatedAt() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyDeletedAt());
            sql.append(" from ");
            sql.append(" " + Dau.getTableName() + " " + Dau.getTableNameOmission());

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "sql:" + sql.toString());
            }

            c = dBHelper.db.rawQuery(sql.toString(), null);

            boolean isResult = c.moveToFirst();

            while (isResult) {
                dau = new Dau();
                dau.id = c.getString( Dau.getIndexId() );
                dau.supplier_id = c.getString( Dau.getIndexSupplierId() );
                dau.item_id = c.getString( Dau.getIndexItemId() );
                dau.order_size = c.getString( Dau.getIndexOrderSize() );
                dau.is_dynamic = c.getInt( Dau.getIndexIsDynamic() ) != 0;
                dau.created_at = c.getString( Dau.getIndexCreatedAt() );
                dau.updated_at = c.getString( Dau.getIndexUpdatedAt() );
                dau.deleted_at = c.getString( Dau.getIndexDeletedAt() );
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
    public static List<Dau> getDauList(final Context context, final String supplier_id) {
        List<Dau> list = new ArrayList<>();
        Dau dau;
        Cursor c = null;
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(context);

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeySupplierId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyItemId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyOrderSize() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyIsDYnamic() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyCreatedAt() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyUpdatedAt() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyDeletedAt());
            sql.append(" from ");
            sql.append(" " + Dau.getTableName() + " " + Dau.getTableNameOmission());
            sql.append(" where " + Dau.getTableNameOmission() + "." + Dau.getKeySupplierId() + "=\"" +  supplier_id + "\"");

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "sql:" + sql.toString());
            }

            c = dBHelper.db.rawQuery(sql.toString(), null);

            boolean isResult = c.moveToFirst();

            while (isResult) {
                dau = new Dau();
                dau.id = c.getString( Dau.getIndexId() );
                dau.supplier_id = c.getString( Dau.getIndexSupplierId() );
                dau.item_id = c.getString( Dau.getIndexItemId() );
                dau.order_size = c.getString( Dau.getIndexOrderSize() );
                dau.is_dynamic = c.getInt( Dau.getIndexIsDynamic() ) != 0;
                dau.created_at = c.getString( Dau.getIndexCreatedAt() );
                dau.updated_at = c.getString( Dau.getIndexUpdatedAt() );
                dau.deleted_at = c.getString( Dau.getIndexDeletedAt() );
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
     * @return the object of rows affected if id is passed in, null otherwise.
     */
    public static Dau getDau(final Context context, final String supplier_id, final String item_id) {
        Dau dau = null;
        Cursor c = null;
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(context);

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyId() + ","); // COL.get(0)
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeySupplierId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyItemId() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyOrderSize() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyIsDYnamic() + ",");
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyCreatedAt() + ","); // COL.get(7)
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyUpdatedAt() + ","); // COL.get(8)
            sql.append(" " + Dau.getTableNameOmission() + "." + Dau.getKeyDeletedAt()); // COL.get(9)
            sql.append(" from ");
            sql.append(" " + Dau.getTableName() + " " + Dau.getTableNameOmission());
            sql.append(" where " + Dau.getTableNameOmission() + "." + Dau.getKeySupplierId() + "=" + supplier_id + " and " + Dau.getKeyItemId() + "=" + item_id);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "sql:" + sql.toString());
            }

            c = dBHelper.db.rawQuery(sql.toString(), null);

            boolean isResult = c.moveToFirst();

            if (isResult) {
                dau = new Dau();
                dau.id = c.getString( Dau.getIndexId() );
                dau.supplier_id = c.getString( Dau.getIndexSupplierId() );
                dau.item_id = c.getString( Dau.getIndexItemId() );
                dau.order_size = c.getString( Dau.getIndexOrderSize() );
                dau.is_dynamic = c.getInt( Dau.getIndexIsDynamic() ) != 0;
                dau.created_at = c.getString( Dau.getIndexCreatedAt() );
                dau.updated_at = c.getString( Dau.getIndexUpdatedAt() );
                dau.deleted_at = c.getString( Dau.getIndexDeletedAt() );
                isResult = c.moveToNext();
                if (isResult) {
                    Log.d(TAG, "moveToNext is Succeded");
                }
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
     * @return the number of rows affected
     */
    public static long update(final Context context, final ContentValues contentValues, String supplier_id, String item_id) {
        DBHelper dBHelper = new DBHelper(context);
        long result = dBHelper.db.update(Dau.getTableName(), contentValues, Dau.getKeySupplierId() + "=" + supplier_id + " and " + Dau.getKeyItemId() + "=" + item_id, null);
        dBHelper.cleanup();
        return result;
    }

    /**
     * insert
     *
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
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public static long delete(final Context context, String supplier_id, String item_id) {
        DBHelper dBHelper = new DBHelper(context);
        int result = dBHelper.db.delete(Dau.getTableName(), Dau.getKeySupplierId() + "=" + supplier_id + " and " + Dau.getKeyItemId() + "=" + item_id, null);
        dBHelper.cleanup();
        return result;
    }
}