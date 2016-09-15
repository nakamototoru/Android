package com.hidezo.app.buyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dezami on 2016/09/13.
 * 取引先一覧リストビュー
 */
public class ActivitySuppliers extends AppCompatActivity implements HDZClientCallbacksGet {

    private static ActivitySuppliers _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        _self = this;

        // HTTP GET
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        req.begin("6146740737615597570","955F40F8-563B-40A0-BB26-EBF7412DC3E7",this);
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void hdzClientCallbackGetComplete(String response) {
        if ( responseFriend.parseJson(response) ) {
            if (responseFriend.friendInfoList.size() == 0) {
                return;
            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    ArrayAdapterSupplier aasupplier = new ArrayAdapterSupplier(_self, responseFriend.friendInfoList);
                    ListView listView = (ListView) findViewById(R.id.listViewSupplier);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ListView listView = (ListView)parent;
                            HDZFriendInfo friend = (HDZFriendInfo)listView.getItemAtPosition(position);
                            String supplier_id = friend.id;

                            // 画面遷移
                            if (id == 0) {
                                // カテゴリ一覧
                                Intent intent = new Intent( _self.getApplication(), ActivityCategorys.class);
                                intent.putExtra("supplier_id", supplier_id);
                                _self.startActivity(intent);
                            }

                            /*
                            new AlertDialog.Builder(ActivitySuppliers._self)
                                    .setTitle( String.valueOf(position) )
                                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                                    */
                        }
                    });

                    listView.setAdapter(aasupplier);
                }
            });

        }
    }
    public void hdzClientCallbackGetError(String message) {
        Log.d("########",message);
    }

}
