package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/16.
 *
 */
public class ArrayAdapterDynamicItem extends ArrayAdapter<HDZItemInfo.DynamicItem> {

    ArrayList<HDZItemInfo.DynamicItem> dynamicItemList = new ArrayList<HDZItemInfo.DynamicItem>();

    public ArrayAdapterDynamicItem(Context context, ArrayList<HDZItemInfo.DynamicItem> items) {
        super(context, 0, items);
        this.dynamicItemList = items;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZItemInfo.DynamicItem dynamicItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dynamic_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText(dynamicItem.item_name);

        TextView tvNumScale = (TextView) convertView.findViewById(R.id.textViewNumScale);
        tvNumScale.setText( String.valueOf(dynamicItem.num_scale.size()) );

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        tvPrice.setText(dynamicItem.price);

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Return the completed view to render on screen
        return convertView;
    }
}
