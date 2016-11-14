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

    ArrayAdapterOrderDetail(final Context context, final ArrayList<HDZOrderDetail.Item> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZOrderDetail.Item orderItem = getItem(position);
        if (orderItem == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_detail, parent, false);
        }

        // Lookup view for data population
        final TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        final TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewName);
        tvTitle.setText(orderItem.name);

        final TextView tvStandard = (TextView) convertView.findViewById(R.id.textViewStandard);
        final String str_standard = "(" + orderItem.standard + "・" + orderItem.loading + "/" + orderItem.scale + ")";
        tvStandard.setText(str_standard);

        final TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        final String str_price = "価格：" + orderItem.price + "円";
        tvPrice.setText(str_price);

        final TextView tvCount = (TextView) convertView.findViewById(R.id.textViewOrderCount);
        final String str_count = "数量：" + orderItem.order_num + orderItem.scale;
        tvCount.setText(str_count);

        // Return the completed view to render on screen
        return convertView;
    }

}
