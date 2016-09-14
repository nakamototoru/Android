package com.hidezo.app.buyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

public class ActivityCategorys extends AppCompatActivity implements HDZClientCallbacksGet {

    private static ActivityCategorys _self;

    private String mySupplierId = "";
    private HDZApiResponseItem responseItem = new HDZApiResponseItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        _self = this;

        Intent intent = getIntent();
        String supplier_id = intent.getStringExtra("supplier_id");
        mySupplierId = supplier_id;

        // HTTP GET
        HDZApiRequestPackage.Item req = new HDZApiRequestPackage.Item();
        req.begin("6146740737615597570","955F40F8-563B-40A0-BB26-EBF7412DC3E7",mySupplierId,this);

    }

    /**
     * HDZClientCallbacksGet
     * データ取得時
     */
    public void hdzClientCallbackGetComplete(String response) {
        if (responseItem.parseJson(response)) {
            if (responseItem.supplierInfo != null) {
                String name = responseItem.supplierInfo.supplier_name;
                Log.d("########",name);
            }
        }
    }
    public void hdzClientCallbackGetError(String message) {
        Log.d("########",message);
    }

}
