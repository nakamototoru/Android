package com.hidezo.app.buyer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.List;

//import com.hidezo.app.buyer.CustomView.FiltableArrayAdapter;

/**
 * Created by dezami on 2016/09/16.
 *
 */
class ArrayAdapterDynamicItem extends ArrayAdapter<HDZItemInfo.DynamicItem> {

    ArrayList<HDZItemInfo.DynamicItem> dynamicItemList = new ArrayList<>();

    ArrayList<AppGlobals.CartCount> countInCartList = new ArrayList<>();

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

        // Get the data item for this position
        HDZItemInfo.DynamicItem dynamicItem = getItem(position);
        if (dynamicItem == null) {
            return convertView;
        }

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

    /**
     * リストをリフレッシュ
     */
    public void refleshRow(int position) {
    }
//    public void refleshItemList(ArrayList<HDZItemInfo.DynamicItem> itemList){
//        dynamicItemList.clear();
//        dynamicItemList = new ArrayList<>(itemList);
//    }
//    void refleshAll() {
//
//    }

//    public class TestFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            List<HDZItemInfo.DynamicItem> items = new ArrayList<>();
//
//            // getCount及びgetItemはAdapterのメソッド
//            for(int i = 0, size = getCount(); i < size; i++) {
//                HDZItemInfo.DynamicItem data = getItem(i);
//                if((data.getId() != null && data.getId().contains(constraint))
////                        || (data.getText() != null && data.getText().contains(constraint))
//                        )
//                {
//                    items.add(data);
//                }
//            }
//
//            FilterResults r = new FilterResults();
//            r.count = items.size();
//            r.values = items;
//
//            return r;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            // Adapterのメソッドでデータの内容を更新する
//            if(results.count > 0) {
//                List<HDZItemInfo.DynamicItem> items = (List<HDZItemInfo.DynamicItem>) results.values;
//
//                clear();
//                addAll(items);
//                notifyDataSetChanged();
//
//            } else {
//                notifyDataSetInvalidated();
//            }
//        }
//
//    }

}
