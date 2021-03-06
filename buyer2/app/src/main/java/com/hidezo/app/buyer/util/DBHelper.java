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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
import android.util.Log;
import com.hidezo.app.buyer.BuildConfig;

/**
 * Created by msuzuki on 2016/06/24.
 *
 */
public class DBHelper {

    private static final String TAG = "## DBHelper";

    public SQLiteDatabase db = null;
    private final DBOpenHelper dbOpenHelper;

    public DBHelper(final Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

//    /**
//     * Databaseが削除できればtrue。できなければfalse
//     * @param context application_context
//     * @return result
//     */
//    public boolean isDatabaseDelete(final Context context) {
//        boolean result = false;
//        if (this.db != null) {
//            final File file = context.getDatabasePath(dbOpenHelper.getDatabaseName());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                result = this.db.deleteDatabase(file);
//            }
//        }
//        return result;
//    }

    public void sendSuccess() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "sendSuccess");
        }
    }

}
