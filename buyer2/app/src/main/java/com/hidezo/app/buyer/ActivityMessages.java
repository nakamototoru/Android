package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 */
public class ActivityMessages extends CustomAppCompatActivity {

    String myOrderNo = "";
    String mySupplierName = "";
    String myCharge = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final Intent intent = getIntent();
        myOrderNo = intent.getStringExtra("order_no");
        myCharge = intent.getStringExtra("charge");

        final AppGlobals globals = (AppGlobals) this.getApplication();
        final ActivityMessages _self = this;

        // ツールナビゲーションバー
        mySupplierName = intent.getStringExtra("supplier_name");
        setNavigationBar(mySupplierName + "様宛",true);

        // Touch Event
        final TextView tvBtnSend = (TextView)findViewById(R.id.textViewButtonSendComment);
        tvBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("## Messages","TODO: Send Comment");

                // テキストエディット
                final EditText editText = new EditText(ActivityMessages.this);
                editText.setLines(8);
                editText.setText( globals.getOrderMessage() );
                //UIスレッド上で呼び出してもらう
                _self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(_self)
                                .setTitle("メッセージ入力")
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        // メッセージ送信
                                        final String content = editText.getText().toString();
                                        globals.setOrderMessage(content);
                                        // HTTP POST
                                        final HDZApiRequestPackage.AddMessage req = new HDZApiRequestPackage.AddMessage();
                                        req.begin( globals.getUserId(), globals.getUuid(), myCharge, globals.getOrderMessage(), myOrderNo, _self);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, final int which) {
                                    }
                                })
                                .show();
                    }
                });

            }
        });
        final TextView tvBtnHome = (TextView)findViewById(R.id.textViewButtonHome);
        tvBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 注文履歴画面遷移
                final Intent intent = new Intent(getApplication(), ActivityOrderes.class);
                startActivity(intent);
            }
        });

        // HTTP GET
        final HDZApiRequestPackage.Message req = new HDZApiRequestPackage.Message();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);

        // Progress
        openProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {

        // Progress
        closeProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final AppGlobals globals = (AppGlobals) this.getApplication();
        final ActivityMessages _self = this;
        if (apiName.equals(HDZApiRequestPackage.Message.apiName)) {
            // "store/message"
            final HDZApiResponseMessage responseMessage = new HDZApiResponseMessage();
            if ( responseMessage.parseJson(response) ) {
                //UIスレッド上で呼び出してもらう
                this.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        //リストビュー作成
                        final ArrayAdapterMessages adapter = new ArrayAdapterMessages(_self,responseMessage.messageList);
                        final ListView listView = (ListView) findViewById(R.id.listViewMessage);
                        //ヘッダー追加
                        if (listView.getHeaderViewsCount() == 0) {
                            //
                            final LinearLayout inflateLayout = null;
                            final View header = getLayoutInflater().inflate(R.layout.item_message_header,inflateLayout);
                            listView.addHeaderView(header, null, false); // タッチ無効

                            final int count = responseMessage.messageList.size();
                            final String str = String.valueOf(count) + "件のコメントがあります。";
                            final TextView tvCount = (TextView)findViewById(R.id.textViewCommentCount);
                            tvCount.setText(str);
                        }

                        listView.setAdapter(adapter);
                    }
                });

            }
        }
        else if (apiName.equals(HDZApiRequestPackage.AddMessage.apiName)) {
            // "store/add_message"
            // メッセージリセット
            globals.setOrderMessage("");
            // HTTP GET
            final HDZApiRequestPackage.Message req = new HDZApiRequestPackage.Message();
            req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this);
        }
    }

    /**
     * ツールバー
     * @param menu menu
     * @return result
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
