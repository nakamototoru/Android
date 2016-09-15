package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/15.
 *
 */
public class ArrayAdapterStaticItem extends ArrayAdapter<HDZItemInfo.StaticItem> {

    ArrayList<HDZItemInfo.StaticItem> staticItemList = new ArrayList<HDZItemInfo.StaticItem>();

    public ArrayAdapterStaticItem(Context context, ArrayList<HDZItemInfo.StaticItem> items) {
        super(context, 0, items);
        this.staticItemList = items;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZItemInfo.StaticItem staticItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_static_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText(staticItem.name);

        // Return the completed view to render on screen
        return convertView;
    }

}
