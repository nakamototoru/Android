package com.hidezo.app.buyer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */
public class ActivityDynamicItems extends CustomAppCompatActivity {

    private String mySupplierId = "";

    private ArrayAdapterDynamicItem myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // TouchEvent
        TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
        TextView tvHome = (TextView)findViewById(R.id.textViewButtonHome);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplication(), ActivityCategorys.class);
                intent.putExtra("supplier_id", mySupplierId);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        setNavigationBar("新着");

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin(globals.getUserId(), globals.getUuid(), mySupplierId, this);

        // Progress
        openProgressDialog();
    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void HDZClientComplete(String response,String apiName) {

        // Progress
        closeProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityDynamicItems _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {
            final AppGlobals globals = (AppGlobals) _self.getApplication();

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 表示リスト作成
                    final ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();
                    HDZUserOrder.transFromDynamic(_self, responseItem.dynamicItemList, mySupplierId, displayItemList);

                    //リストビュー作成
                    myAdapter = new ArrayAdapterDynamicItem(_self, displayItemList);
                    ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //行タッチイベント
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            // ボタン場合分け
                            if (id == 1 || id == -1) {
                                // 個数の増減
                                HDZUserOrder order = displayItemList.get(position);
                                int count = Integer.parseInt(order.orderSize);
                                count += (int)id;
                                if (count == 0) {
                                    globals.deleteCart(order.supplierId, order.itemId);
                                    order.orderSize = AppGlobals.STR_ZERO;
                                    // カート更新
                                    reFleshListView();
                                }
                                else if (count <= 100) {
                                    String numScale = String.valueOf(count);
                                    globals.replaceCart(order.supplierId, order.itemId, numScale, true);
                                    order.orderSize = numScale;
                                    // カート更新
                                    reFleshListView();
                                }
                            }
                            else {
                                // ピッカーの作成
                                ArrayList<String> pickerList = new ArrayList<>();
                                pickerList.add( AppGlobals.STR_ZERO );
                                pickerList.addAll(responseItem.dynamicItemList.get(position).num_scale);
                                final CustomPickerView pickerView = new CustomPickerView(_self, pickerList, displayItemList.get(position).orderSize);

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
                                                        // カート内容変更
                                                        HDZUserOrder order = displayItemList.get(position);
//                                                        final AppGlobals globals = (AppGlobals) _self.getApplication();
                                                        String numScale = pickerView.getTextSelected();
                                                        if (numScale.equals(AppGlobals.STR_ZERO)) {
                                                            globals.deleteCart(order.supplierId, order.itemId);
                                                        }
                                                        else {
                                                            globals.replaceCart(order.supplierId, order.itemId, numScale, true);
                                                        }
                                                        order.orderSize = pickerView.getTextSelected();
                                                        // カート更新
                                                        reFleshListView();
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
                        }
                    });

                    //ヘッダー追加
                    if (listView.getHeaderViewsCount() == 0) {
                        //
                        View header = getLayoutInflater().inflate(R.layout.item_message_header,null);
                        listView.addHeaderView(header, null, false); // タッチ無効

                        String str = responseItem.dynamicItemInfo.lastUpdate;
                        TextView tvCount = (TextView)findViewById(R.id.textViewCommentCount);
                        tvCount.setText(str);
                    }
                    // フッター
                    if (listView.getFooterViewsCount() == 0) {
                        //
                        View footer = getLayoutInflater().inflate(R.layout.item_dynamic_item_footer,null);
                        listView.addFooterView(footer, null ,false); // タッチ無効

                        ImageView ivItem00 = (ImageView) findViewById(R.id.imageViewIcon00);
                        ImageView ivItem01 = (ImageView) findViewById(R.id.imageViewIcon01);
                        ImageView ivItem02 = (ImageView) findViewById(R.id.imageViewIcon02);
                        ImageView ivItem03 = (ImageView) findViewById(R.id.imageViewIcon03);
                        ImageView ivItem04 = (ImageView) findViewById(R.id.imageViewIcon04);
                        ImageView ivItem05 = (ImageView) findViewById(R.id.imageViewIcon05);
                        ArrayList<ImageView> imageList = new ArrayList<>();
                        imageList.add(ivItem00);
                        imageList.add(ivItem01);
                        imageList.add(ivItem02);
                        imageList.add(ivItem03);
                        imageList.add(ivItem04);
                        imageList.add(ivItem05);
                        Context context = footer.getContext();
                        for (int i = 0; i < responseItem.dynamicItemInfo.imagePath.size() && i < 6; i++) {
                            ImageView iv = imageList.get(i);
                            String imageURL = responseItem.dynamicItemInfo.imagePath.get(i);
                            try {
                                Picasso.with(context)
                                        .load(imageURL)
                                        .placeholder(R.drawable.sakana180)
                                        .error(R.drawable.sakana180)
                                        .into(iv);
                            } catch (Exception e) {
                                Log.d("## Picasso",e.getMessage());
                            }
                        }

                        TextView tvDescription = (TextView)findViewById(R.id.textViewDescription);
                        tvDescription.setText(responseItem.dynamicItemInfo.text);
                    }

                    listView.setAdapter(myAdapter);
                }
            });
        }
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
//        ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
//        ArrayAdapterDynamicItem adapter = (ArrayAdapterDynamicItem) listView.getAdapter();
//        adapter.notifyDataSetChanged();

        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
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
