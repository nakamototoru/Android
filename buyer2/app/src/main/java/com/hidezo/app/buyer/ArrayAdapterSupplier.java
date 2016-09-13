package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public class ArrayAdapterSupplier extends ArrayAdapter<HDZFriendInfo> {

    private ArrayList<HDZFriendInfo> friendInfoList = new ArrayList<HDZFriendInfo>();

    public ArrayAdapterSupplier(Context context, ArrayList<HDZFriendInfo> friends) {
        super(context, 0, friends);
        this.friendInfoList = friends;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        HDZFriendInfo friend = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvRow = (TextView) convertView.findViewById(R.id.tvRow);

        // Populate the data into the template view using the data object
        tvName.setText(friend.name);
        tvRow.setText( String.valueOf(position+1) );

        // Return the completed view to render on screen
        return convertView;
    }
}
