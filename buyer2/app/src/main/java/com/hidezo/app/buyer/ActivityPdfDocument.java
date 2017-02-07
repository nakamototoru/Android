package com.hidezo.app.buyer;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.HorizontalScrollView;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityPdfDocument extends CustomAppCompatActivity {

    private static final String TAG = "#ActPdfDocument";

    ArrayList<HDZApiResponseFaxDoc> displayList = new ArrayList<>();

    String myOrderNo = "";
    String faxNumber = "";
    ListView listViewPdfImage = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_document);

        // ツールナビゲーションバー
        setNavigationBar("注文書FAX送信",false);

        final Intent intent = getIntent();
        myOrderNo = intent.getStringExtra("order_no");

        // ゲット・注文履歴
        final AppGlobals globals = (AppGlobals) this.getApplication();
        final HDZApiRequestPackage.faxDoc req = new HDZApiRequestPackage.faxDoc();
        req.begin(globals.getUserId(), globals.getUuid(), myOrderNo, this );

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

        if (checkLogOut(response)) {
            return;
        }

        final ActivityPdfDocument _self = this;
        final HDZApiResponseFaxDoc responseFaxDoc = new HDZApiResponseFaxDoc();
        if (responseFaxDoc.parseJson(response)) {

            Log.d(TAG, response);

            faxNumber = responseFaxDoc.fax;

            displayList.add(responseFaxDoc);

            //UIスレッド上で呼び出してもらう
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //リストビュー作成
                    final ArrayAdapterPdfDocumentFirst adapter = new ArrayAdapterPdfDocumentFirst(_self, displayList);
                    final ListView listView = (ListView) findViewById(R.id.listViewPdfDocument);
                    listView.setAdapter(adapter);

                    _self.listViewPdfImage = listView;
                }
            });
        }
    }

    /**
     * PDFデータ受け取り
     * @param base64Binary Base64 string
     */
    public void respondBase64String(final String base64Binary) {

        if (base64Binary != null && base64Binary.length() > 0) {
            // FAX送信開始
            HDZSoapFax.doSendRequest(base64Binary,faxNumber,"TestFromAndroid",this);
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
        getMenuInflater().inflate(R.menu.menu_pdf_document, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fax_send) {
            // FAX送信の原稿を作成
            new HDZPdfManager.GenerationTask(listViewPdfImage,getApplicationContext(),this).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
