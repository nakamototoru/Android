package com.hidezo.app.buyer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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
public class ArrayAdapterCategory extends ArrayAdapter<HDZItemInfo.Category> {

    ArrayList<HDZItemInfo.Category> categoryArrayList = new ArrayList<HDZItemInfo.Category>();

    public ArrayAdapterCategory(Context context, ArrayList<HDZItemInfo.Category> categorys) {
        super(context, 0, categorys);
        this.categoryArrayList = categorys;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZItemInfo.Category categoryInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewCategoryName);
        tvName.setText(categoryInfo.name);

        // Return the completed view to render on screen
        return convertView;
    }

}
