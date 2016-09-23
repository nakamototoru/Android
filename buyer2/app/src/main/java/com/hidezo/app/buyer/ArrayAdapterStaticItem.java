package com.hidezo.app.buyer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/15.
 *
 */
class ArrayAdapterStaticItem extends ArrayAdapter<HDZItemInfo.StaticItem> {

    ArrayList<HDZItemInfo.StaticItem> staticItemList = new ArrayList<>();

    ArrayList<AppGlobals.CartCount> countInCartList = new ArrayList<>();

    ArrayAdapterStaticItem(Context context, ArrayList<HDZItemInfo.StaticItem> items) {
        super(context, 0, items);
        this.staticItemList = items;

        for (int i = 0; i < this.staticItemList.size(); i++) {
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
        HDZItemInfo.StaticItem staticItem = getItem(position);
        if (staticItem == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_static_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText(staticItem.name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });
        ImageView ivItem = (ImageView) convertView.findViewById(R.id.imageViewItem);
        ivItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        String str_price = "単価：" + staticItem.price + "円";
        tvPrice.setText(str_price);

        TextView tvStandard = (TextView) convertView.findViewById(R.id.textViewStandard);
        String str_standard = "(" + staticItem.standard + "・" + staticItem.loading + "/" + staticItem.scale + ")";
        tvStandard.setText( str_standard );

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewCount);
        tvCount.setText( String.valueOf(countInCartList.get(position).count) );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Touch Event
        TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
        tvBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("########","R.id.tvBtnPlus");

//                if (countInCartList.get(position).count < 100) {
//                    countInCartList.get(position).count++;
//                    ((ListView) parent).performItemClick(null, position, 1);
//                }
                // TODO: 変更ダイアログ
                Log.d("########","R.id.tvBtn・TODO: 変更ダイアログ");

                ((ListView) parent).performItemClick(null, position, -1);
            }
        });

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
