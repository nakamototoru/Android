package com.hidezo.app.buyer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/17.
 *
 */
public class ArrayAdapterSupplierDetail extends ArrayAdapter<HDZProfile> {

    private ArrayList<HDZProfile> profileList = new ArrayList<HDZProfile>();

    public ArrayAdapterSupplierDetail(Context context, ArrayList<HDZProfile> profiles) {
        super(context, 0, profiles);
        this.profileList = profiles;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZProfile profileInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier_detail, parent, false);
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        tvTitle.setText(profileInfo.title);
        TextView tvContent = (TextView) convertView.findViewById(R.id.textViewContent);
        tvContent.setText(profileInfo.content);

        // Return the completed view to render on screen
        return convertView;
    }

}
