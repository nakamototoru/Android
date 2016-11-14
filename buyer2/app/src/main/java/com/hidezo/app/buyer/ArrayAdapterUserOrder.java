package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/25.
 *
 */
class ArrayAdapterUserOrder extends ArrayAdapter<HDZUserOrder> {

    static final int TouchEventDialog = 999;

    ArrayAdapterUserOrder(final Context context, final ArrayList<HDZUserOrder> userOrders) {
        super(context, 0, userOrders);
    }
    @Override
    public boolean isEnabled(final int position) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_order, parent, false);
        }

        // Lookup view for data population
        final TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewName);
        tvTitle.setText(item.itemName);
        final TextView tvContent = (TextView) convertView.findViewById(R.id.textViewCount);
        tvContent.setText(item.orderSize);
        final TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText(String.valueOf(position+1));

        // Touch Event
        final TextView tvBtnDelete = (TextView) convertView.findViewById(R.id.textViewButtonDelete);
        tvBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 親アクティビティへ
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });

        // カート操作ボタン
        if ( item.numScale.size() < 100) {
            // 分数操作
            // 変更したいレイアウトを取得する
            final LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layoutOrderCountCart);
            // レイアウトのビューをすべて削除する
            layout.removeAllViews();
            // レイアウトを変更する
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.layout_order_fraction, layout);

            // Touch Event
            final TextView tvBtnUpdate = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
            tvBtnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    // 変更ダイアログ
                    ((ListView) parent).performItemClick(null, position, TouchEventDialog);
                }
            });
        }
        else {
            // 整数操作
            // 変更したいレイアウトを取得する
            final LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layoutOrderCountCart);
            // レイアウトのビューをすべて削除する
            layout.removeAllViews();
            // レイアウトを変更する
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.layout_order_decimal, layout);

            // Touch Event
            final TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonPlus);
            tvBtnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    // 操作制限
                    final int count = Integer.parseInt(item.orderSize);
                    if (count < 100) {
                        // 変更処理
                        ((ListView) parent).performItemClick(null, position, 1);
                    }
                }
            });
            final TextView tvBtnMinus = (TextView)convertView.findViewById(R.id.textViewButtonMinus);
            tvBtnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // 操作制限
                    final int count = Integer.parseInt(item.orderSize);
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
