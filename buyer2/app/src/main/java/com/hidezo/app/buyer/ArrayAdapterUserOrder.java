package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/25.
 *
 */

class ArrayAdapterUserOrder extends ArrayAdapter<HDZUserOrder> {

    public ArrayAdapterUserOrder(Context context, ArrayList<HDZUserOrder> userOrders) {
        super(context, 0, userOrders);
//        this.profileList = profiles;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_order, parent, false);
        }
        // Get the data item for this position
        HDZUserOrder userOrder = getItem(position);
        if (userOrder == null) {
            return convertView;
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewName);
        tvTitle.setText(userOrder.itemName);
        TextView tvContent = (TextView) convertView.findViewById(R.id.textViewCount);
        tvContent.setText(userOrder.orderSize);

        // Return the completed view to render on screen
        return convertView;
    }

}
