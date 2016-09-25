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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hidezo.app.buyer.util.DBOpenHelper;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An Model class for Dau
 */
public class Dau implements Parcelable {

//    public static final String TAG = "## Dau";

    // table name
//    public static final String TABLE_NAME = "dau_hidezo_buyer";
    public static String getTableName() {
        return DBOpenHelper.TABLE_NAME;
    }

    // table name aliases
//    public static final String TABLE_NAME_OMISSION = "d";
    public static String getTableNameOmission() {
        return "d";
    }

    // column list constant
//    public static final List<String> COL = Collections.unmodifiableList(new LinkedList<String>() {
//        {
//            // カラム名登録
//            add("id"); // 0
//            add("supplier_id"); // 1
//            add("item_id"); // 2
//            add("order_size"); // 3
//            add("created_at"); // 4
//            add("updated_at"); // 5
//            add("deleted_at"); // 6
//        }
//    });

    public static String getKeyId() {
        return "id";
    }
    public static String getKeySupplierId() {
        return "supplier_id";
    }
    public static String getKeyItemId() {
        return "item_id";
    }
    public static String getKeyOrderSize() {
        return "order_size";
    }
    public static String getKeyCreatedAt() {
        return "created_at";
    }
    public static String getKeyUpdatedAt() {
        return "updated_at";
    }
    public static String getKeyDeletedAt() {
        return "deleted_at";
    }
    public static int getIndexId() {
        return 0;
    }
    public static int getIndexSupplierId() {
        return 1;
    }
    public static int getIndexItemId() {
        return 2;
    }
    public static int getIndexOrderSize() {
        return 3;
    }
    public static int getIndexCreatedAt() {
        return 4;
    }
    public static int getIndexUpdatedAt() {
        return 5;
    }
    public static int getIndexDeletedAt() {
        return 6;
    }

    // id = UUID
    public String id;

    public String supplier_id = "";
    public String item_id = "";
    public String order_size = "";

    public String created_at = "";
    public String updated_at = "";
    public String deleted_at = "";

    /**
     * コンストラクタ
     */
    public Dau() {}
    public static final Parcelable.Creator<Dau> CREATOR = new Parcelable.Creator<Dau>() {

        public Dau createFromParcel(Parcel in) {
            return new Dau(in);
        }
        public Dau[] newArray(int size) {
            return new Dau[size];
        }
    };

    private Dau(Parcel in) {
        id = in.readString();
        supplier_id = in.readString();
        item_id = in.readString();
        order_size = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        deleted_at = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(supplier_id);
        out.writeString(item_id);
        out.writeString(order_size);
        out.writeString(created_at);
        out.writeString(updated_at);
        out.writeString(deleted_at);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Dau [");
        str.append(" id=" + id);
        if (!TextUtils.isEmpty(supplier_id)) {
            str.append(", supplier_id=" + supplier_id);
        }
        if (!TextUtils.isEmpty(item_id)) {
            str.append(", item_id=" + item_id);
        }
        if (!TextUtils.isEmpty(order_size)) {
            str.append(", order_size=" + order_size);
        }
        str.append(", created_at=" + created_at);
        str.append(", updated_at=" + updated_at);
        str.append(", deleted_at=" + deleted_at);
        str.append("]");
        return str.toString();
    }
}