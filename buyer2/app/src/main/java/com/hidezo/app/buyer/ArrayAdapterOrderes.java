package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hidezo.app.buyer.CustomView.CircleView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class ArrayAdapterOrderes extends ArrayAdapter<HDZApiResponseOrderedList.OrderInfo> {

    ArrayAdapterOrderes(final Context context, final ArrayList<HDZApiResponseOrderedList.OrderInfo> list) {
        super(context, 0, list);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZApiResponseOrderedList.OrderInfo orderInfo = getItem(position);
        if (orderInfo == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        // Lookup view for data population
        final TextView tvOrderAt = (TextView) convertView.findViewById(R.id.textViewOrderAt);
        tvOrderAt.setText( orderInfo.order_at );

        final TextView tvRow = (TextView) convertView.findViewById(R.id.textViewOrderRow);
        tvRow.setText( String.valueOf(position+1) );

        final TextView tvSupplier = (TextView) convertView.findViewById(R.id.textViewSupplier);
        tvSupplier.setText( orderInfo.supplier_name );

        final TextView tvDeliverAt = (TextView) convertView.findViewById(R.id.textViewDeliverAt);
        final String strDeliverAt = orderInfo.deliver_at + "納品";
        tvDeliverAt.setText( strDeliverAt );

        // 通知バッジ表示
        final CircleView circleView = (CircleView)convertView.findViewById(R.id.viewForBadge);
        if (orderInfo.badgeCount > 0) {
            circleView.setColor(R.color.colorForBadge);
            final TextView tvCount = (TextView)convertView.findViewById(R.id.textViewBadgeCount);
            tvCount.setText( String.valueOf(orderInfo.badgeCount) );
        }
        else {
            circleView.setColor(R.color.colorForWhite);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
