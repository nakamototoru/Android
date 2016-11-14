package com.hidezo.app.buyer;

import android.content.Context;
//import android.util.Log;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/27.
 *
 */
class ArrayAdapterUserOrderCheck extends ArrayAdapter<HDZProfile> {

    ArrayAdapterUserOrderCheck(final Context context, final ArrayList<HDZProfile> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(final int position) {
        return false;   // ListView アイテムの選択を無効にする場合
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_order_check, parent, false);
        }

        // Lookup view for data population
        final TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        tvTitle.setText(profileInfo.title);
        final TextView tvContent = (TextView) convertView.findViewById(R.id.textViewContent);
        tvContent.setText(profileInfo.content);

        // Touch Event
        final TextView tvBtnUpdate = (TextView) convertView.findViewById(R.id.textViewButtonUpdate);
        tvBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 親アクティビティへ
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
