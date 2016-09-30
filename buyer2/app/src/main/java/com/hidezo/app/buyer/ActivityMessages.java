package com.hidezo.app.buyer;

//import android.support.v7.app.AppCompatActivity;
//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


/**
 *
 */
public class ActivityMessages extends CustomAppCompatActivity {

    String myOrderNo = "";
    String mySupplierName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Intent intent = getIntent();
        myOrderNo = intent.getStringExtra("order_no");

        // HTTP GET
        HDZApiRequestPackage.Message req = new HDZApiRequestPackage.Message();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);

        // ツールナビゲーションバー
        mySupplierName = intent.getStringExtra("supplier_name");
        setNavigationBar(mySupplierName + "様宛");
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityMessages _self = this;
        final HDZApiResponseMessage responseMessage = new HDZApiResponseMessage();
        if ( responseMessage.parseJson(response) ) {

//            Log.d("########",response);

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    ArrayAdapterMessages adapter = new ArrayAdapterMessages(_self,responseMessage.messageList);
                    ListView listView = (ListView) findViewById(R.id.listViewMessage);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        //行タッチイベント
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        }
//                    });

                    //ヘッダー追加
                    if (listView.getHeaderViewsCount() == 0) {
                        //
                        View header = getLayoutInflater().inflate(R.layout.item_message_header,null);
                        listView.addHeaderView(header, null, false); // タッチ無効

                        int count = responseMessage.messageList.size();
                        String str = String.valueOf(count) + "件のコメントがあります。";
                        TextView tvCount = (TextView)findViewById(R.id.textViewCommentCount);
                        tvCount.setText(str);
                    }

                    listView.setAdapter(adapter);
                }
            });

        }
    }

    /**
     * ツールバー
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
