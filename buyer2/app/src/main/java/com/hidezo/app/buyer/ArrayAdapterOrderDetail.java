package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/21.
 *
 */
class ArrayAdapterOrderDetail extends ArrayAdapter<HDZOrderDetail.Item> {

    ArrayAdapterOrderDetail(Context context, ArrayList<HDZOrderDetail.Item> items) {
        super(context, 0, items);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZOrderDetail.Item orderItem = getItem(position);
        if (orderItem == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_detail, parent, false);
        }

        // Lookup view for data population
        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewName);
        tvTitle.setText(orderItem.name);

        TextView tvStandard = (TextView) convertView.findViewById(R.id.textViewStandard);
        String str_standard = "(" + orderItem.standard + "・" + orderItem.loading + "/" + orderItem.scale + ")";
        tvStandard.setText(str_standard);

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        String str_price = "価格：" + orderItem.price + "円";
        tvPrice.setText(str_price);

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewOrderCount);
        String str_count = "数量：" + orderItem.order_num + orderItem.scale;
        tvCount.setText(str_count);

        // Return the completed view to render on screen
        return convertView;
    }

}
