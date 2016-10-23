package com.hidezo.app.buyer;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by dezami on 2016/09/16.
 *
 */
class ArrayAdapterDynamicItem extends ArrayAdapter<HDZUserOrder> {

    ArrayAdapterDynamicItem(Context context, ArrayList<HDZUserOrder> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZUserOrder item = getItem(position);
        if (item == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dynamic_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText( item.itemName );

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        tvPrice.setText( item.price );

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewCount);
        tvCount.setText( item.orderSize );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Touch Event
//        TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
//        tvBtnPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 親アクティビティへ
//                ((ListView) parent).performItemClick(null, position, -1);
//            }
//        });
        // カート操作ボタン
        if ( item.numScale.size() < 100) {
            // 分数操作
            // 変更したいレイアウトを取得する
            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layoutOrderCountDynamic);
            // レイアウトのビューをすべて削除する
            layout.removeAllViews();
            // レイアウトを変更する
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.layout_order_fraction, layout);

            // Touch Event
            TextView tvBtnUpdate = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
            tvBtnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 変更ダイアログ
                    ((ListView) parent).performItemClick(null, position, 999);
                }
            });
        }
        else {
            // 整数操作
            // 変更したいレイアウトを取得する
            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layoutOrderCountDynamic);
            // レイアウトのビューをすべて削除する
            layout.removeAllViews();
            // レイアウトを変更する
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.layout_order_decimal, layout);

            // Touch Event
            TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonPlus);
            tvBtnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 操作制限
                    int count = Integer.parseInt(item.orderSize);
                    if (count < 100) {
                        // 変更処理
                        ((ListView) parent).performItemClick(null, position, 1);
                    }
                }
            });
            TextView tvBtnMinus = (TextView)convertView.findViewById(R.id.textViewButtonMinus);
            tvBtnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 操作制限
                    int count = Integer.parseInt(item.orderSize);
                    if (count > 0) {
                        // 変更処理
                        ((ListView) parent).performItemClick(null, position, -1);
                    }
                }
            });
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
