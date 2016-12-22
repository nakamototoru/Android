package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/30.
 *
 */
class ArrayAdapterMessages extends ArrayAdapter<HDZApiResponseMessage.Detail> {

    ArrayAdapterMessages(final Context context, final ArrayList<HDZApiResponseMessage.Detail> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final HDZApiResponseMessage.Detail detail = getItem(position);
        if (detail == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        // Content
        // row
        final TextView tvRow = (TextView)convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position + 1) );

        // comment
        final TextView tvMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
        tvMessage.setText(detail.message);

        // textViewSupplierName
        final TextView tvSupplier = (TextView)convertView.findViewById(R.id.textViewSupplierName);
        tvSupplier.setText(detail.nameOfSender);

        // textViewDate
        final TextView tvDate = (TextView)convertView.findViewById(R.id.textViewDate);
        tvDate.setText(detail.posted_at);

        // Return the completed view to render on screen
        return convertView;
    }
}
