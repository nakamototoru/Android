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
 * Created by dezami on 2016/09/21.
 *
 */

class ArrayAdapterOrderes extends ArrayAdapter<HDZordered> {

//    public ArrayList<HDZordered> orderedList = new ArrayList<>(); // HDZordered

    public ArrayAdapterOrderes(Context context, ArrayList<HDZordered> orderes) {
        super(context, 0, orderes);
//        this.orderedList = orderes;
    }
//    @Override
//    public boolean isEnabled(int position) {
//        return false;  // ListView アイテムの選択を無効にする場合
//    }
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

//        tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("########","R.id.tvName");
//
//                ((ListView) parent).performItemClick(null, position, 99);
//            }
//        });

//        tvOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("########","R.id.tvOrder");
//
//                ((ListView) parent).performItemClick(null, position, 0);
//            }
//        });

        // Return the completed view to render on screen
        return convertView;
    }
}
