package com.hidezo.app.buyer;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hidezo.app.buyer.CustomView.PickerView;

import java.util.ArrayList;
//import java.util.StringTokenizer;

//import java.util.ArrayList;

/**
 *
 */
public class ActivityDynamicItems extends CustomAppCompatActivity {

    private ActivityDynamicItems _self;

    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    private String numScaleSelected = "0";

    private ListView myListView = null;
    private int indexSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        _self = this;

        Intent intent = getIntent();
        String mySupplierId = intent.getStringExtra("supplier_id");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // TouchEvent
        TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("########","R.id.textViewButtonOrderCheck");

                Intent intent = new Intent( _self.getApplication(), ActivityUserOrders.class);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        setNavigationBar("新着");
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        if ( checkLogOut(response) ) {
            return;
        }

        if (responseItem.parseJson(response)) {
//            if (responseItem.dynamicItemList != null && responseItem.dynamicItemList.size() > 0) {
//                String name = responseItem.dynamicItemList.get(0).item_name;
//                Log.d("########", name);
//            }

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //リストビュー作成
                    ArrayAdapterDynamicItem adapter = new ArrayAdapterDynamicItem(_self, responseItem.dynamicItemList);
                    _self.myListView = (ListView) findViewById(R.id.listViewDynamicItem);
                    _self.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ArrayList<String> pickerList = new ArrayList<>();
                            pickerList.add("0");
                            pickerList.addAll(responseItem.dynamicItemList.get(position).num_scale);

                            final PickerView pickerView = new PickerView(_self);
                            pickerView.setData(pickerList);
                            pickerView.setOnSelectListener(new PickerView.onSelectListener() {
                                @Override
                                public void onSelect(String text) {
                                    _self.numScaleSelected = text;
                                }
                            });
                            indexSelected = 0;
                            int loop = 0;
                            for (String scale : pickerList) {
                                if (scale.equals(_self.numScaleSelected)) {
                                    indexSelected = loop;
                                    break;
                                }
                                loop++;
                            }
                            pickerView.setSelected(indexSelected);

                            //UIスレッド上で呼び出してもらう
                            _self.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    new AlertDialog.Builder(_self)
                                            .setTitle("選択")
                                            .setView(pickerView)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // TODO:カート更新
                                                    // 更新対象のViewを取得
//                                                    View targetView = _self.myListView.getChildAt(_self.indexSelected);
//                                                    // getViewで対象のViewを更新
//                                                    _self.myListView.getAdapter().getView(_self.indexSelected, targetView, _self.myListView);

                                                    _self.refleshListView();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .show();
                                }
                            });

                        }
                    });
                    myListView.setAdapter(adapter);

                }
            });
        }
    }
//    public void HDZClientError(String message) {
//        Log.d("########",message);
//    }

    /**
     * リストビューの更新処理。
     */
    public void refleshListView() {
//        ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
        if (myListView != null) {
            ArrayAdapterDynamicItem adapter = (ArrayAdapterDynamicItem) myListView.getAdapter();
            adapter.notifyDataSetChanged();

            Log.d("########","refleshListView");
        }
//        adapter.refleshItemList(hogeItemList);
//        adapter.refleshAll();
//        adapter.getFilter().filter(s);//フィルタ実行。ソートもここで実行する。再描画も担当する
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
