package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

//import java.util.List;
//import android.util.Log;

/**
 * Created by dezami on 2016/09/16.
 *
 */
class ArrayAdapterDynamicItem extends ArrayAdapter<HDZUserOrder> {

    ArrayAdapterDynamicItem(Context context, ArrayList<HDZUserOrder> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZUserOrder item = getItem(position);
        if (item == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dynamic_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText( item.itemName );

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        tvPrice.setText( item.price );

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewCount);
        tvCount.setText( item.orderSize );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Touch Event
        TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
        tvBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 親アクティビティへ
                ((ListView) parent).performItemClick(null, position, -1);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
