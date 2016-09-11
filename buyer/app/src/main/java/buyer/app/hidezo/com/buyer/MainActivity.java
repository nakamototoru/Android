package buyer.app.hidezo.com.buyer;

import java.util.ArrayList;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.util.Log;
import android.app.AlertDialog;
import java.io.IOException;
import android.content.DialogInterface;
import android.widget.Toast;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 MainActivity Class : First Vision
 */
public class MainActivity extends AppCompatActivity implements MyOkhttpCallbacks {

    private String _strsample = "";
//    ArrayAdapter<String> _arrayadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        // !!!: 追加
        populateUsersList();


        // HTTP TEST
        MyOkhttpGet httpget = new MyOkhttpGet();
//        String url = "https://api.hidezo.co/store/order_list?id=6146740737615597570&page=1&uuid=955F40F8-563B-40A0-BB26-EBF7412DC3E7";
//        httpget.runAsync(url,this);

        HashMap<String, String> params = new HashMap<String, String>(); // 連想配列に格納
        // データを連想配列に格納
        params.put("id", "6146740737615597570");
        params.put("uuid", "955F40F8-563B-40A0-BB26-EBF7412DC3E7");
        params.put("page", "1");
        httpget.runAsync("order_list", params, this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }

    private void populateUsersList() {

        // Construct the data source
        ArrayList<Supplier> arrayOfUsers = Supplier.getSuppliers();

        // Create the adapter to convert the array to views
        CustomSupplierAdapter adapter = new CustomSupplierAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String country = adapter.getItem(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle( String.valueOf(position) )
                        .setMessage(_strsample)
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

        Log.d("########",response);
        _strsample = response;

        try {
            JSONObject json = new JSONObject(response);

            boolean result = json.getBoolean("result");
            if (result = true) {
                JSONArray order_list = json.getJSONArray("order_list");
                if (order_list != null && order_list.length() > 0) {
                    String supplier_name = order_list.getJSONObject(0).getString("supplier_name");
                    Log.d("########",supplier_name);
                }
            }
            else {
                Log.d("########","result = false");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void myOkhttpCallbackError(String error) {

        Log.d("########",error);
    }

}
