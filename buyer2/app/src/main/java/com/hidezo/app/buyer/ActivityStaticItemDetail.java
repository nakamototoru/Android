package com.hidezo.app.buyer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 *
 */
public class ActivityStaticItemDetail extends CustomAppCompatActivity {

    String myItemId = "";
    String myCategoryId = "";
    String imageURL = "";
    String mySupplierId = "";
    String myCategoryName = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_item_detail);

        final Intent intent = getIntent();
        myItemId = intent.getStringExtra("item_id");
        myCategoryId = intent.getStringExtra("category_id");
        mySupplierId = intent.getStringExtra("supplier_id");

        // ツールバー初期化
        final String title = "商品詳細";
        setNavigationBar(title,true);

        // HTTP GET
        final HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        final AppGlobals globals = (AppGlobals) this.getApplication();
        req.begin( globals.getUserId(), globals.getUuid(), mySupplierId, this);

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

        final ActivityStaticItemDetail _self = this;
        final HDZApiResponseItem responseItem = new HDZApiResponseItem();
        if (responseItem.parseJson(response)) {
            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 表示リスト
                    final ArrayList<HDZProfile> profileList = new ArrayList<>();
                    // 静的商品
                    for (final HDZItemInfo.StaticItem item : responseItem.staticItemList) {
                        final String cid = item.category.id;
                        final String iid = item.id;
                        if ( cid.equals(myCategoryId) && iid.equals(myItemId)) {
                            final HDZProfile pCode = new HDZProfile("商品コード",item.code);
                            profileList.add(pCode);
                            final HDZProfile pName = new HDZProfile("商品名",item.name);
                            profileList.add(pName);
                            final HDZProfile pPrice = new HDZProfile("価格",item.price);
                            profileList.add(pPrice);
                            final HDZProfile pStandard = new HDZProfile("規格",item.standard);
                            profileList.add(pStandard);
                            final HDZProfile pLoading = new HDZProfile("入り数",item.loading);
                            profileList.add(pLoading);
                            final HDZProfile pScale = new HDZProfile("単位",item.scale);
                            profileList.add(pScale);
                            final HDZProfile pMinOrderCount = new HDZProfile("最小注文本数",item.min_order_count);
                            profileList.add(pMinOrderCount);
                            imageURL = item.image;
                            break;
                        }
                    }
                    //リストビュー作成
                    final ArrayAdapterStaticItemDetail adapter = new ArrayAdapterStaticItemDetail(_self, profileList);
                    final ListView listView = (ListView) findViewById(R.id.listViewStaticItemDetail);

                    //ヘッダー追加
                    if (listView.getHeaderViewsCount() == 0) {
                        //
                        final LinearLayout inflateLayout = null;
                        final View header = getLayoutInflater().inflate(R.layout.item_static_item_detail_header,inflateLayout);
                        listView.addHeaderView(header, null, false); // タッチ無効

                        final ImageView ivItem = (ImageView) findViewById(R.id.imageViewItem);
                        final Context context = header.getContext();
                        try {
                            Picasso.with(context)
                                    .load(imageURL)
                                    //                .centerInside()
                                    .placeholder(R.drawable.sakana180)
                                    .error(R.drawable.sakana180)
                                    .into(ivItem);
                        } catch (final Exception e) {
                            if (BuildConfig.DEBUG) {
                                Log.d("## Picasso",e.getMessage());
                            }
                        }
                        ivItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                //UIスレッド上で呼び出してもらう
                                _self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 画像取得
                                        final ImageView ivDetail = new ImageView(_self);
                                        final Context ctx = ivDetail.getContext();
                                        try {
                                            Picasso.with(ctx)
                                                    .load(imageURL)
                                                    //                .centerInside()
                                                    .placeholder(R.drawable.sakana180)
                                                    .error(R.drawable.sakana180)
                                                    .into(ivDetail);
                                        } catch (final Exception e) {
                                            if (BuildConfig.DEBUG) {
                                                Log.d("## Picasso",e.getMessage());
                                            }
                                        }

                                        new AlertDialog.Builder(_self)
                                                .setTitle("商品画像")
                                                .setView(ivDetail)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialog, final int id) {
                                                    }
                                                })
                                                .show();
                                    }
                                });
                            }
                        });
                    }
                    listView.setAdapter(adapter);
                }
            });
        }
    }

    /**
     * 戻る実行
     */
    void onClickNavigationBack() {
        // 遷移・静的商品一覧
        final Intent intent = new Intent( getApplication(), ActivityStaticItems.class);
        intent.putExtra("supplier_id", mySupplierId);
        intent.putExtra("category_id", myCategoryId);
        intent.putExtra("category_name", myCategoryName);
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
