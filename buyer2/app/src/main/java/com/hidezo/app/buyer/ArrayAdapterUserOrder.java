package com.hidezo.app.buyer;

import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.StringTokenizer;

/**
 * Created by dezami on 2016/09/25.
 *
 */
class ArrayAdapterUserOrder extends ArrayAdapter<HDZUserOrder> {

    ArrayAdapterUserOrder(Context context, ArrayList<HDZUserOrder> userOrders) {
        super(context, 0, userOrders);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZUserOrder userOrder = getItem(position);
        if (userOrder == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_order, parent, false);
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewName);
        tvTitle.setText(userOrder.itemName);
        TextView tvContent = (TextView) convertView.findViewById(R.id.textViewCount);
        tvContent.setText(userOrder.orderSize);
        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText(String.valueOf(position+1));

        // Touch Event
        TextView tvBtnDelete = (TextView) convertView.findViewById(R.id.textViewButtonDelete);
        tvBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 親アクティビティへ
                ((ListView) parent).performItemClick(null, position, -1);
            }
        });
        TextView tvBtnUpdate = (TextView) convertView.findViewById(R.id.textViewButtonUpdate);
        tvBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 親アクティビティへ
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
