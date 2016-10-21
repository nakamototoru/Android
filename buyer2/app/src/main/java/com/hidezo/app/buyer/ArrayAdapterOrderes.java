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
class ArrayAdapterOrderes extends ArrayAdapter<HDZordered> {

    ArrayAdapterOrderes(Context context, ArrayList<HDZordered> list) {
        super(context, 0, list);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZordered orderInfo = getItem(position);
        if (orderInfo == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        // Lookup view for data population
        TextView tvOrderAt = (TextView) convertView.findViewById(R.id.textViewOrderAt);
        tvOrderAt.setText( orderInfo.order_at );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewOrderRow);
        tvRow.setText( String.valueOf(position+1) );

        TextView tvSupplier = (TextView) convertView.findViewById(R.id.textViewSupplier);
        tvSupplier.setText( orderInfo.supplier_name );

        TextView tvDeliverAt = (TextView) convertView.findViewById(R.id.textViewDeliverAt);
        tvDeliverAt.setText( orderInfo.deliver_at );

        // 通知バッジ表示
        CircleView circleView = (CircleView)convertView.findViewById(R.id.viewForBadge);
        if (orderInfo.badgeCount > 0) {
            circleView.setColor(R.color.colorForBadge);
            TextView tvCount = (TextView)convertView.findViewById(R.id.textViewBadgeCount);
            tvCount.setText( String.valueOf(orderInfo.badgeCount) );
        }
        else {
            circleView.setColor(R.color.colorForWhite);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
