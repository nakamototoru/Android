package buyer.app.hidezo.com.buyer;

import java.util.ArrayList;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.widget.ListView;

import android.util.Log;
import android.app.AlertDialog;
import java.io.IOException;
import android.content.DialogInterface;

/**
 MainActivity Class : First Vision
 */
public class MainActivity extends AppCompatActivity implements MyOkhttpCallbacks {

    private String _strsample = "";

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
        String url = "https://api.hidezo.co/store/order_list?id=6146740737615597570&page=1&uuid=955F40F8-563B-40A0-BB26-EBF7412DC3E7";
        httpget.runAsync(url,this);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("GetResult")
//                .setMessage(_strsample)
//                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton( "No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .show();
    }

    private void populateUsersList() {

        // Construct the data source
        ArrayList<Supplier> arrayOfUsers = Supplier.getSuppliers();

        // Create the adapter to convert the array to views
        CustomSupplierAdapter adapter = new CustomSupplierAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }


    /**
     *
     */
    public void myOkhttpCallbackComplete(String response) {

//        System.out.println(response);
        Log.d("########",response);
        _strsample = response;

    }
    public void myOkhttpCallbackError(String error) {

        Log.d("########",error);
    }

}
