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
class ArrayAdapterEmpty extends ArrayAdapter<HDZApiResponse> {

    ArrayAdapterEmpty(final Context context, final ArrayList<HDZApiResponse> list) {
        super(context, 0, list);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final HDZApiResponse res = getItem(position);
        if (res == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_empty, parent, false);
        }

        final TextView tvMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
        tvMessage.setText(res.message);

        // Return the completed view to render on screen
        return convertView;
    }

}
