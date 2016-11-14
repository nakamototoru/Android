package com.hidezo.app.buyer;

import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/17.
 *
 */
class ArrayAdapterSupplierDetail extends ArrayAdapter<HDZProfile> {

    ArrayAdapterSupplierDetail(final Context context, final ArrayList<HDZProfile> list) {
        super(context, 0, list);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZProfile profileInfo = getItem(position);
        if (profileInfo == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier_detail, parent, false);
        }

        // Lookup view for data population
        final TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        tvTitle.setText(profileInfo.title);
        final TextView tvContent = (TextView) convertView.findViewById(R.id.textViewContent);
        tvContent.setText(profileInfo.content);

        // Return the completed view to render on screen
        return convertView;
    }

}
