/* *
 * Copyright (C) 2016 Programming Java Android Development Project
 * Programming Java is
 *
 *      http://java-lang-programming.com/
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
package com.hidezo.app.buyer.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.hidezo.app.buyer.BuildConfig;

/**
 * Created by msuzuki on 2016/06/24.
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private Context m_context;
    private static final String TAG = "## DBOpenHelper";
    private static final int DB_VERSION = 1;
    /**
     * DBファイル名
     */
    private static final String DATABASE_NAME = "hidezo_buyer_cart.db";
    public static final String TABLE_NAME = "cart";

    /**
     * コンストラクタ
     */
    public DBOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.m_context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate version : " + db.getVersion());
        }
//        this.execFileSQL(db, "create_table.sql"); // assetsフォルダにあるファイルを読む
        final String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id text,supplier_id text,item_id text,order_size text, is_dynamic integer, created_at text,updated_at text,deleted_at text);";
        try {
            db.execSQL(sql);
        } catch (final Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG,e.getMessage());
            }
        }
        // "CREATE TABLE IF NOT EXISTS dau (id text,supplier_id text,item_id text,order_size text,created_at text,updated_at text,deleted_at text);"
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onUpgrade version : " + db.getVersion());
            Log.d(TAG, "onUpgrade oldVersion : " + oldVersion);
            Log.d(TAG, "onUpgrade newVersion : " + newVersion);
        }
        // 1 → 2
//        if (oldVersion == 1 && newVersion == 2) {
//            this.execFileSQL(db, "change_table_1.0.3.sql"); // v1.0.0
//        }
//
//        if (oldVersion == 1 && newVersion == 3) {
//            this.execFileSQL(db, "change_table_1.0.3.sql"); // v1.0.0
//            this.execFileSQL(db, "create_table_1.2.0.sql"); // v1.0.0
//        }
//
//        if (oldVersion == 2 && newVersion == 3) {
//            this.execFileSQL(db, "create_table_1.2.0.sql"); // v1.0.0
//        }

        // 指定したテーブルのカラム構成をチェックし、
        // 同名のカラムについてはアップグレード後もデータを引き継ぎます。
        // 同名のカラムで型に互換性がない場合はエラーになるので注意。

    }
    @Override
    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

    /**
     * assetsフォルダのSQLファイルを実行する
     * @param db database
     * @param fileName file_name
     */
    private void execFileSQL(final SQLiteDatabase db, final String fileName){
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader reader = null;
        try {
            // 文字コード(UTF-8)を指定して、ファイルを読み込み
            in = m_context.getAssets().open(fileName);
            inReader = new InputStreamReader(in, "UTF-8");
            reader = new BufferedReader(inReader);

            // ファイル内の全ての行を処理
            String s;
            while((s = reader.readLine()) != null){
                // 先頭と末尾の空白除去
                s = s.trim();

                // 文字が存在する場合（空白行は処理しない）
                if (0 < s.length()){
                    // SQL実行
                    db.execSQL(s);
                }
            }
        } catch (final IOException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG,e.getMessage());
            }
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inReader != null) {
                try {
                    inReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * insertを実行します
     * @param context application_context
     * @param cvList contentvalues_list
     * @return boolean
     */
    public static boolean insertTransactionExcute(final Context context, final String tableName, final ArrayList<ContentValues> cvList) {

        // テーブルデータ削除
        DBHelper dbhelper = null;
        try {
            dbhelper = new DBHelper(context);
            // トランザクション開始
            dbhelper.db.beginTransaction();

//            long result = -1;
            for (final ContentValues cv : cvList) {
                final long result = dbhelper.db.insert(tableName, null, cv);
                if (result == -1) {
                    return false;
                }
            }

            // コミット
            dbhelper.db.setTransactionSuccessful();

        } catch (final SQLException e) {
            e.printStackTrace();
            return false;
        } finally {

            if (dbhelper != null) {
                dbhelper.cleanup();
            }
        }

        return true;
    }
}
