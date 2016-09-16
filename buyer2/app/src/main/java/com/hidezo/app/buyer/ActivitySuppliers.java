package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Created by dezami on 2016/09/13.
 * 取引先一覧リストビュー
 */
public class ActivitySuppliers extends AppCompatActivity implements HDZClientCallbacksGet {

    private static ActivitySuppliers _self;
    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    //メンバー変数
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;

    // グローバル
    AppGlobals sGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        // getApplication()でアプリケーションクラスのインスタンスを拾う
        sGlobals = (AppGlobals) this.getApplication();

        _self = this;

        // HTTP GET
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        req.begin( sGlobals.getUserId(), sGlobals.getUuid(), this);


        //radioGroupとリソースのradioGroupを結び付ける
        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroupSuppliersTabbar);

        //radioButtonとチェックされているボタンを結び付ける
        mRadioButton = (RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);

                    // ラジオボタンのテキストを取得
                    String text = radioButton.getText().toString();

//                    Log.d("########", text);

                    sGlobals.openWarning("ラジオボタンのテキストを取得", text, _self);
                }

            }
        });

   }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void hdzClientCallbackGetComplete(String response,String apiname) {
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
                            else {
                                // 取引先詳細
                                Intent intent = new Intent( _self.getApplication(), ActivitySupplierDetail.class);
                                intent.putExtra("supplier_id", supplier_id);
                                _self.startActivity(intent);
                            }
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
