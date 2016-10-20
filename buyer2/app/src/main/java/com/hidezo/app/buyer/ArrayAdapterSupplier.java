package com.hidezo.app.buyer;

import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hidezo.app.buyer.CustomView.CircleView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class ArrayAdapterSupplier extends ArrayAdapter<HDZFriendInfo> {

    ArrayAdapterSupplier(Context context, ArrayList<HDZFriendInfo> list) {
        super(context, 0, list);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZFriendInfo friendInfo = getItem(position);
        if (friendInfo == null) {
            return convertView;
        }

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
//                Log.d("########","R.id.tvName");
                ((ListView) parent).performItemClick(null, position, 99);
            }
        });

        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("########","R.id.tvOrder");
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        // 通知バッジ表示
        CircleView circleView = (CircleView)convertView.findViewById(R.id.viewForBadge);
        if (circleView != null) {
            circleView.setColor(R.color.colorForBadge);
        }
        else {
            Log.d("####","circleView is Null.");
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
