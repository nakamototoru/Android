package com.hidezo.app.buyer;

import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
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

    private static final int TouchEventDetail = 99;

    ArrayAdapterSupplier(final Context context, final ArrayList<HDZFriendInfo> list) {
        super(context, 0, list);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZFriendInfo friendInfo = getItem(position);
        if (friendInfo == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier, parent, false);
        }

        // Lookup view for data population
        final TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final TextView tvRow = (TextView) convertView.findViewById(R.id.tvRow);
        final TextView tvOrder = (TextView) convertView.findViewById(R.id.textViewOrder);

        // Populate the data into the template view using the data object
        tvRow.setText( String.valueOf(position+1) );

        tvName.setText(friendInfo.name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Log.d("########","R.id.tvName");
                ((ListView) parent).performItemClick(null, position, TouchEventDetail);
            }
        });

        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Log.d("########","R.id.tvOrder");
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        // 通知バッジ表示
        final CircleView circleView = (CircleView)convertView.findViewById(R.id.viewForBadge);
        if (friendInfo.badgeCount > 0) {
            circleView.setColor(R.color.colorForBadge);
            final TextView tvCount = (TextView)convertView.findViewById(R.id.textViewBadgeCount);
            tvCount.setText( String.valueOf(friendInfo.badgeCount) );
        }
        else {
            circleView.setColor(R.color.colorForWhite);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
