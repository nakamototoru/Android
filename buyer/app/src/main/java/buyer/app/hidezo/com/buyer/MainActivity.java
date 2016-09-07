package buyer.app.hidezo.com.buyer;

import java.util.ArrayList;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // !!!: 追加
        populateUsersList();

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
}
