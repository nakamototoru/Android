package com.hidezo.app.buyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class ActivitySuppliers extends AppCompatActivity implements MyOkhttpCallbacks {

    private HDZApiResponseFriend responseFriend = new HDZApiResponseFriend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        // !!!: 追加
        populateUsersList();

        // HTTP TEST
        HDZApiRequestPackage.Friend req = new HDZApiRequestPackage.Friend();
        req.begin("6146740737615597570","955F40F8-563B-40A0-BB26-EBF7412DC3E7",this);

//        HDZApiRequest request = new HDZApiRequest();
//        request.putKeyAndValue("id", "6146740737615597570");
//        request.putKeyAndValue("uuid", "955F40F8-563B-40A0-BB26-EBF7412DC3E7");
////        request.putKeyAndValue("page", "1");
//        request.beginRequest("friend",this); //

//        httpget.runAsync("order_list", request.getParams(), this);
    }

    private void populateUsersList() {

        // Construct the data source
        ArrayList<Supplier> arrayOfUsers = Supplier.getSuppliers();

        // Create the adapter to convert the array to views
        CustomSupplierAdapter adapter = new CustomSupplierAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listViewSupplier);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String country = adapter.getItem(position);

                new AlertDialog.Builder(ActivitySuppliers.this)
                        .setTitle( String.valueOf(position) )
//                        .setMessage(_strsample)
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    /**
     * MyOkhttpCallbacks
     */
    public void myOkhttpCallbackComplete(String response) {

//        Log.d("########",response);

        if ( responseFriend.parseJson(response) ) {
            if (responseFriend.friendInfoList.size() > 0) {
                String name = responseFriend.friendInfoList.get(0).name;
                Log.d("########",name);
            }
        }

//        try {
//            JSONObject json = new JSONObject(response);
//
//            boolean result = json.getBoolean("result");
//            if (result) {
//                JSONArray friendList = json.getJSONArray("friendList");
//                if (friendList != null && friendList.length() > 0) {
//                    String supplier_name = friendList.getJSONObject(0).getString("name");
//                    Log.d("########",supplier_name);
//                }
//            }
//            else {
//                Log.d("########","result = false");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
    public void myOkhttpCallbackError(String error) {

        Log.d("########",error);
    }
}
