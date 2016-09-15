package com.hidezo.app.buyer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public class ArrayAdapterSupplier extends ArrayAdapter<HDZFriendInfo> {

    private ArrayList<HDZFriendInfo> friendInfoList = new ArrayList<HDZFriendInfo>();

    public ArrayAdapterSupplier(Context context, ArrayList<HDZFriendInfo> friends) {
        super(context, 0, friends);
        this.friendInfoList = friends;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZFriendInfo friendInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvRow = (TextView) convertView.findViewById(R.id.tvRow);
        TextView tvOrder = (TextView) convertView.findViewById(R.id.textViewOrder);

        // Populate the data into the template view using the data object
        tvRow.setText( String.valueOf(position+1) );

        tvName.setText(friendInfo.name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("########","R.id.tvName");

                ((ListView) parent).performItemClick(null, position, 99);
            }
        });

        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("########","R.id.tvOrder");

                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        /*
        // クリックイベントを取得したいボタン
        Button button = (Button)convertView.findViewById(R.id.buttonItemList);
        // ボタンに OnClickListener インターフェースを実装する
        button.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                Log.d("########","R.id.buttonItemList");
            }
        });
        */

        // Return the completed view to render on screen
        return convertView;
    }

}
