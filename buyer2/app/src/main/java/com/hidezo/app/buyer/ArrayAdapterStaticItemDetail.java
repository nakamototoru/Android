package com.hidezo.app.buyer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hidezo.app.buyer.HDZItemInfo;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/23.
 * 
 */

public class ArrayAdapterStaticItemDetail extends ArrayAdapter<HDZProfile> {

    ArrayList<HDZProfile> staticItemDetailList = new ArrayList<>();

    ArrayAdapterStaticItemDetail(Context context, ArrayList<HDZProfile> items) {
        super(context, 0, items);
        this.staticItemDetailList = items;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZProfile profileInfo = getItem(position);
        if (profileInfo == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_static_item_detail, parent, false);
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        tvTitle.setText(profileInfo.title);
        TextView tvContent = (TextView) convertView.findViewById(R.id.textViewContent);
        tvContent.setText(profileInfo.content);


//        TextView tvContent = (TextView) convertView.findViewById(R.id.textViewContent);
//
//
//        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
//        String str_price = "単価：" + staticItem.price + "円";
//        tvPrice.setText(str_price);
//
//        TextView tvStandard = (TextView) convertView.findViewById(R.id.textViewStandard);
//        String str_standard = "(" + staticItem.standard + "・" + staticItem.loading + "/" + staticItem.scale + ")";
//        tvStandard.setText( str_standard );
//
//
//        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
//        tvRow.setText( String.valueOf(position+1) );

//        TextView tvBtnMinus = (TextView)convertView.findViewById(R.id.textViewButtonMinus);
//        tvBtnMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Log.d("########","R.id.tvBtnMinus");
//
//                if (countInCartList.get(position).count > 0) {
//                    countInCartList.get(position).count--;
//                    ((ListView) parent).performItemClick(null, position, -1);
//                }
//            }
//        });

        // Return the completed view to render on screen
        return convertView;
    }
}
