package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.sax.StartElementListener;
import android.support.v7.app.AlertDialog;
//import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//import com.hidezo.app.buyer.model.Dau;
//import org.w3c.dom.Text;
import java.util.ArrayList;

/**
 *
 */
public class ActivityUserOrdersCheck extends CustomAppCompatActivity {

    private static final String TAG = "##UserOrdersCheck";

    String mySupplierId = "";
    String myDeliverDay = "";
    String myCharge = "";
    String myDeliverPlace = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_check);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // ツールバー初期化
        setNavigationBar("注文前入力",true);

        // HTTP GET
        final HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // Progress
        openHttpGetProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(final String response, final String apiName) {
        // Progress
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityUserOrdersCheck _self = this;
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {

            final ArrayList<HDZProfile> profileList = new ArrayList<>();

            final HDZProfile pMessage = new HDZProfile("メッセージ", globals.getOrderMessage() );
            profileList.add(pMessage);

            // 注文情報
            // 納品日
            String strDeliverDay = globals.getOrderDeliverDay();
            if (strDeliverDay.equals("")) {
                strDeliverDay = responseItem.itemInfo.delivery_day_list.get(0);

//                Log.d(TAG,"strDeliverDay = " + strDeliverDay);
            }
            globals.setOrderDeliverDay(strDeliverDay);
            final HDZProfile pDate = new HDZProfile("納品日", strDeliverDay);
            profileList.add(pDate);

            // 担当者
            String strCharge = globals.getOrderCharge();
            if ( strCharge.equals("") ) {
                strCharge = responseItem.itemInfo.charge_list.get(0);
            }
            globals.setOrderCharge(strCharge);
            final HDZProfile pCharge = new HDZProfile("担当者", strCharge);
            profileList.add(pCharge);

            // 配達先
            final ArrayList<String> deliverPlaceList = new ArrayList<>();
            deliverPlaceList.add("選択なし");
            deliverPlaceList.addAll(responseItem.itemInfo.deliver_to_list);

            String strDeliverTo = globals.getOrderDeliverPlace();
            if ( strDeliverTo.equals("") ) {
                strDeliverTo = deliverPlaceList.get(0);
            }
            globals.setOrderDeliverPlace(strDeliverTo);
            final HDZProfile pDeliverTo = new HDZProfile("配達先",strDeliverTo);
            profileList.add(pDeliverTo);

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    //リストビュー作成
                    final ArrayAdapterUserOrderCheck adapter = new ArrayAdapterUserOrderCheck(_self, profileList);
                    final ListView listView = (ListView) findViewById(R.id.listViewUserOrdersCheck);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                            if (position == 0) {
                                // テキストエディット
                                final EditText editText = new EditText(ActivityUserOrdersCheck.this);
                                editText.setLines(8);
                                editText.setText( globals.getOrderMessage() );
//                                editText.setText("メッセージ入力");
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

                                                        final String content = editText.getText().toString();
                                                        globals.setOrderMessage(content);
                                                        profileList.get(position).content = content;
                                                        reFleshListView();
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
                            else {
                                // ピッカーの作成
                                final ArrayList<String> pickerList = new ArrayList<>();
                                // 場合分け
                                switch (position) {
                                    case 1: // 納品日
                                        pickerList.addAll( responseItem.itemInfo.delivery_day_list );
                                        break;
                                    case 2: // 担当者
                                        pickerList.addAll( responseItem.itemInfo.charge_list );
                                        break;
                                    case 3: // 配達先
                                        pickerList.addAll( deliverPlaceList );
                                        break;
                                    default:
                                        break;
                                }
                                final CustomPickerView pickerView = new CustomPickerView(_self, pickerList, globals.getOrderDeliverDay());

                                //UIスレッド上で呼び出してもらう
                                _self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(_self)
                                                .setTitle("選択")
                                                .setView(pickerView)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialog, final int id) {

                                                        final String content = pickerView.getTextSelected();
                                                        switch (position) {
                                                            case 1: // 納品日
                                                                globals.setOrderDeliverDay(content);
                                                                break;
                                                            case 2: // 担当者
                                                                globals.setOrderCharge(content);
                                                                break;
                                                            case 3: // 配達先
                                                                globals.setOrderDeliverPlace(content);
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                        profileList.get(position).content = content;
                                                        reFleshListView();
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
                        }
                    });
                    listView.setAdapter(adapter);
                }
            });

            // 注文実行ボタン
            final TextView txBtnOrderExec = (TextView)findViewById(R.id.textViewButtonOrderExec);
            txBtnOrderExec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

//                    Log.d(TAG, "Day = " + globals.getOrderDeliverDay());

                    openAlertOrderExec();
                }
            });
        }
    }
    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        //
        final ListView listView = (ListView) findViewById(R.id.listViewUserOrdersCheck);
        final ArrayAdapterUserOrderCheck adapter = (ArrayAdapterUserOrderCheck) listView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    /**
     * ALERT DIALOG
     */
    public void openAlertOrderExec() {

        final ActivityUserOrdersCheck _self = this;
        final String title = "注文を実行します";
        final String message = "よろしいですか？";

        //UIスレッド上で呼び出してもらう
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_self)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                                // 画面遷移
                                final Intent intent = new Intent(getApplication(), ActivityUserOrdersExec.class);
                                intent.putExtra("supplier_id",mySupplierId);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                            }
                        })
                        .show();
            }
        });

    }

    /**
     * ツールバー
     *
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
            // ログインフォーム画面遷移
            final Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
