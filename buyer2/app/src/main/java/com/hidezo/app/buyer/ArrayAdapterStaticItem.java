package com.hidezo.app.buyer;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import org.w3c.dom.Text;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/15.
 *
 */
class ArrayAdapterStaticItem extends ArrayAdapter<HDZUserOrder> {

    ArrayAdapterStaticItem(Context context, ArrayList<HDZUserOrder> items) {
        super(context, 0, items);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;  // ListView アイテムの選択を無効にする場合
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        HDZUserOrder item = getItem(position);
        if (item == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_static_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        tvName.setText(item.itemName);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 詳細画面
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });
        ImageView ivItem = (ImageView) convertView.findViewById(R.id.imageViewItem);
        ivItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 詳細画面
                ((ListView) parent).performItemClick(null, position, 0);
            }
        });
        Context context = this.getContext();
        try {
            Picasso.with(context)
                    .load(item.image)
//                .centerInside()
                    .placeholder(R.drawable.sakana180)
                    .error(R.drawable.sakana180)
                    .into(ivItem);

        } catch (Exception e) {
            Log.d("## Picasso",e.getMessage());
        }

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        String str_price = "単価：" + item.price + "円";
        tvPrice.setText(str_price);

        TextView tvStandard = (TextView) convertView.findViewById(R.id.textViewStandard);
        String str_standard = "(" + item.standard + "・" + item.loading + "/" + item.scale + ")";
        tvStandard.setText( str_standard );

        TextView tvCount = (TextView) convertView.findViewById(R.id.textViewCount);
        tvCount.setText( item.orderSize );

        TextView tvRow = (TextView) convertView.findViewById(R.id.textViewRow);
        tvRow.setText( String.valueOf(position+1) );

        // Touch Event
        TextView tvBtnPlus = (TextView)convertView.findViewById(R.id.textViewButtonUpdate);
        tvBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 変更ダイアログ
                ((ListView) parent).performItemClick(null, position, -1);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
