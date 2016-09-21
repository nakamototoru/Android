package com.hidezo.app.buyer;

import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/16.
 *
 */
class ArrayAdapterDynamicItem extends ArrayAdapter<HDZItemInfo.DynamicItem> {

    ArrayList<HDZItemInfo.DynamicItem> dynamicItemList = new ArrayList<HDZItemInfo.DynamicItem>();

    ArrayList<AppGlobals.CartCount> countInCartList = new ArrayList<AppGlobals.CartCount>();

    public ArrayAdapterDynamicItem(Context context, ArrayList<HDZItemInfo.DynamicItem> items) {
        super(context, 0, items);
        this.dynamicItemList = items;

        for (int i = 0; i < this.dynamicItemList.size(); i++) {
            AppGlobals.CartCount object = new AppGlobals.CartCount();
            countInCartList.add(object);
        }
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

//        Log.d("########","ArrayAdapterDynamicItem : getView");

        // Get the data item for this position
        HDZItemInfo.DynamicItem dynamicItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dynamic_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText(dynamicItem.item_name);

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        tvPrice.setText( dynamicItem.price );

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewCount);
        tvCount.setText( String.valueOf(countInCartList.get(position).count) );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Touch Event
        TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonPlus);
        tvBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("########","R.id.tvBtnPlus");
                if (countInCartList.get(position).count < 100) {
                    countInCartList.get(position).count++;
                    ((ListView) parent).performItemClick(null, position, 1);
                }
            }
        });

        TextView tvBtnMinus = (TextView)convertView.findViewById(R.id.textViewButtonMinus);
        tvBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("########","R.id.tvBtnMinus");
                if (countInCartList.get(position).count > 0) {
                    countInCartList.get(position).count--;
                    ((ListView) parent).performItemClick(null, position, -1);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * リストをリフレッシュ
     * @param itemList arraylist
     */
//    public void refleshItemList(ArrayList<HDZItemInfo.DynamicItem> itemList){
//        sourceList.clear();
//        sourceList = new ArrayList<HogeItem>(itemList);
//    }

    /**
     * num_scaleを連結
     */
//    public String bindNumScale(ArrayList<String> list) {
//
//        String response = "";
//        int count = 0;
//        for (String str : list) {
//            if (count != 0) {
//                response += ",";
//            }
//            response += str;
//            count++;
//        }
//        return response;
//    }
}
