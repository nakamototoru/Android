package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.sax.StartElementListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityUserOrdersCheck extends CustomAppCompatActivity {

    private String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_check);

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // ツールバー初期化
        setNavigationBar("注文前入力");
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response, String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityUserOrdersCheck _self = this;
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {

            final ArrayList<HDZProfile> profileList = new ArrayList<>();

            HDZProfile pMessage = new HDZProfile("メッセージ", globals.getOrderMessage() );
            profileList.add(pMessage);

            HDZProfile pDate = new HDZProfile("納品日", globals.getOrderDeliverDay() );
            profileList.add(pDate);

            String strCharge = globals.getOrderCharge();
            if ( strCharge.equals("") ) {
                strCharge = responseItem.itemInfo.charge_list.get(0);
            }
            HDZProfile pCharge = new HDZProfile("担当者", strCharge );
            profileList.add(pCharge);

            String strDeliverTo = globals.getOrderDeliverPlace();
            if ( strDeliverTo.equals("") ) {
                strDeliverTo = "選択なし";
            }
//            if ( responseItem.itemInfo.deliver_to_list.size() > 0 ) {
//                strDeliverTo = responseItem.itemInfo.deliver_to_list.get(0);
//            }
            HDZProfile pDeliverTo = new HDZProfile("配達先",strDeliverTo);
            profileList.add(pDeliverTo);

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    ArrayAdapterUserOrderCheck adapter = new ArrayAdapterUserOrderCheck(_self, profileList);
                    ListView listView = (ListView) findViewById(R.id.listViewUserOrdersCheck);
                    listView.setAdapter(adapter);
                }
            });

        }
    }

    /**
     * ツールバー
     *
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            // ログインフォーム画面遷移
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
