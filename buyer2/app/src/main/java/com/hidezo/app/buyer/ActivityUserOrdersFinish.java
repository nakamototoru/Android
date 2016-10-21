package com.hidezo.app.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class ActivityUserOrdersFinish extends AppCompatActivity {

    String mySupplierId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_finish);

        Intent intent = getIntent();
        mySupplierId = intent.getStringExtra("supplier_id");

        Button btn = (Button)findViewById(R.id.buttonGoOrders);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 画面遷移
                Intent intent = new Intent(getApplication(), ActivityOrderes.class);
                intent.putExtra("supplier_id",mySupplierId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 戻るボタンを無効
            Log.d("Hidezo","戻るボタン無効");
            return false;
        }
        return true;
    }
}
