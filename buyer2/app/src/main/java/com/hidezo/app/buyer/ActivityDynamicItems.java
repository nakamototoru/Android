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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 *
 */
public class ActivityDynamicItems extends CustomAppCompatActivity {

    String mySupplierId = "";

    // リスト更新用に保持
    ArrayAdapterDynamicItem myAdapter = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        final Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        // TouchEvent
        final TextView tvOrderCheck = (TextView)findViewById(R.id.textViewButtonOrderCheck);
        tvOrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 遷移・注文履歴
                final Intent intent = new Intent( getApplication(), ActivityUserOrders.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
        final TextView tvHome = (TextView)findViewById(R.id.textViewButtonHome);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 遷移・カテゴリ一覧
                final Intent intent = new Intent( getApplication(), ActivityCategorys.class);
                intent.putExtra("supplier_id", mySupplierId);
                startActivity(intent);
            }
        });

        // ツールバー初期化
        setNavigationBar("新着",true);

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
    public void HDZClientComplete(final String response,final String apiName) {
        // Progress
        closeHttpProgressDialog();

        if ( checkLogOut(response) ) {
            return;
        }

        final ActivityDynamicItems _self = this;
        if ( apiName.equals(HDZApiRequestPackage.CheckDynamicItems.apiName) ) {
            // "store/check_dynamic_items"
            if (BuildConfig.DEBUG) {
                Log.d("Hidezo",response);
            }
        }
        else {
            final HDZApiResponseItem responseItem = new HDZApiResponseItem();
            if (responseItem.parseJson(response)) {
                final AppGlobals globals = (AppGlobals) _self.getApplication();

                // 表示リスト作成
                final ArrayList<HDZUserOrder> displayItemList = new ArrayList<>();
                HDZUserOrder.transFromDynamic(_self, responseItem.dynamicItemList, mySupplierId, displayItemList);

                //UIスレッド上で呼び出してもらう
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //リストビュー作成
                        myAdapter = new ArrayAdapterDynamicItem(_self, displayItemList);
                        final ListView listView = (ListView) findViewById(R.id.listViewDynamicItem);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            //行タッチイベント
                            @Override
                            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                                // ボタン場合分け
                                if (id == 1 || id == -1) {
                                    // 個数の増減
                                    final HDZUserOrder order = displayItemList.get(position);
                                    int count = Integer.parseInt(order.orderSize);
                                    count += (int)id;
                                    if (count == 0) {
                                        globals.deleteCart(order.supplierId, order.itemId);
                                        order.orderSize = AppGlobals.STR_ZERO;
                                        // カート更新
                                        reFleshListView();
                                    }
                                    else if (count <= 100) {
                                        final String numScale = String.valueOf(count);
                                        globals.replaceCart(order.supplierId, order.itemId, numScale, true);
                                        order.orderSize = numScale;
                                        // カート更新
                                        reFleshListView();
                                    }
                                }
                                else {
                                    // ピッカーの作成
                                    final ArrayList<String> pickerList = new ArrayList<>();
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
                                                        public void onClick(final DialogInterface dialog, final int id) {
                                                            // カート内容変更
                                                            final HDZUserOrder order = displayItemList.get(position);
                                                            final String numScale = pickerView.getTextSelected();
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
                                                        public void onClick(final DialogInterface dialog, final int which) {
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                }
                            }
                        });

                        final LinearLayout inflateLayout = null;
                        //ヘッダー追加
                        if (listView.getHeaderViewsCount() == 0) {
                            //
                            final View header = getLayoutInflater().inflate(R.layout.item_message_header,inflateLayout);
                            listView.addHeaderView(header, null, false); // タッチ無効

                            final String str = responseItem.dynamicItemInfo.lastUpdate;
                            final TextView tvCount = (TextView)findViewById(R.id.textViewCommentCount);
                            tvCount.setText(str);
                        }
                        // フッター
                        if (listView.getFooterViewsCount() == 0) {
                            //
                            final View footer = getLayoutInflater().inflate(R.layout.item_dynamic_item_footer,inflateLayout);
                            listView.addFooterView(footer, null ,false); // タッチ無効

                            final ImageView ivItem00 = (ImageView) findViewById(R.id.imageViewIcon00);
                            final ImageView ivItem01 = (ImageView) findViewById(R.id.imageViewIcon01);
                            final ImageView ivItem02 = (ImageView) findViewById(R.id.imageViewIcon02);
                            final ImageView ivItem03 = (ImageView) findViewById(R.id.imageViewIcon03);
                            final ImageView ivItem04 = (ImageView) findViewById(R.id.imageViewIcon04);
                            final ImageView ivItem05 = (ImageView) findViewById(R.id.imageViewIcon05);
                            final ArrayList<ImageView> imageList = new ArrayList<>();
                            imageList.add(ivItem00);
                            imageList.add(ivItem01);
                            imageList.add(ivItem02);
                            imageList.add(ivItem03);
                            imageList.add(ivItem04);
                            imageList.add(ivItem05);
                            final Context context = footer.getContext();
                            for (int i = 0; i < responseItem.dynamicItemInfo.imagePath.size() && i < 6; i++) {
                                final ImageView iv = imageList.get(i);
                                final String imageURL = responseItem.dynamicItemInfo.imagePath.get(i);
                                try {
                                    Picasso.with(context)
                                            .load(imageURL)
                                            .placeholder(R.drawable.sakana180)
                                            .error(R.drawable.sakana180)
                                            .into(iv);
                                } catch (final Exception e) {
                                    if (BuildConfig.DEBUG) {
                                        Log.d("## Picasso",e.getMessage());
                                    }
                                }
                            }

                            final TextView tvDescription = (TextView)findViewById(R.id.textViewDescription);
                            tvDescription.setText(responseItem.dynamicItemInfo.text);
                        }
                        listView.setAdapter(myAdapter);

                        // HTTP GET
                        final HDZApiRequestPackage.CheckDynamicItems req = new HDZApiRequestPackage.CheckDynamicItems();
                        req.begin(globals.getUserId(),globals.getUuid(),mySupplierId,null);
                    }
                });
            }
        }
    }

    /**
     * リストビューの更新処理。
     */
    public void reFleshListView() {
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 戻る実行
     */
    void onClickNavigationBack() {
        // 遷移・カテゴリ一覧
        final Intent intent = new Intent( getApplication(), ActivityCategorys.class);
        intent.putExtra("supplier_id", mySupplierId);
        startActivity(intent);
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
