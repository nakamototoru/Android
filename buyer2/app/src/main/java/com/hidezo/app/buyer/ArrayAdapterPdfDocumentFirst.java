package com.hidezo.app.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//import com.hidezo.app.buyer.CustomView.CircleView;

import java.util.ArrayList;

/**
 * Created by dezamisystem2 on 2017/02/05.
 *
 */
class ArrayAdapterPdfDocumentFirst extends ArrayAdapter<HDZApiResponseFaxDoc> {

    private int countPage = 0;

    ArrayAdapterPdfDocumentFirst(final Context context, final ArrayList<HDZApiResponseFaxDoc> list) {
        super(context, 0, list);

        countPage = list.size();
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        final HDZApiResponseFaxDoc faxDoc = getItem(position);
        if (faxDoc == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pdf_document_first, parent, false);
        }

        // Lookup view for data population
        final TextView tvSupplier = (TextView) convertView.findViewById(R.id.textViewPdfSupplierName);
        final String textSupplierName = faxDoc.supplier_name + "御中";
        tvSupplier.setText( textSupplierName );

        final TextView tvPage = (TextView) convertView.findViewById(R.id.textViewPdfPage);
        final int page_number = position + 1;
        final String textPage = String.valueOf(page_number) + "枚目／" + String.valueOf(countPage) + "n枚中";
        tvPage.setText(textPage);

        final TextView tvStoreName = (TextView)convertView.findViewById(R.id.textViewPdfStoreName);
        final String textStoreName = "＜お届け先＞" + faxDoc.store_name + "(" + faxDoc.store_code + ")";
        tvStoreName.setText(textStoreName);

        final TextView tvOrderAt = (TextView)convertView.findViewById(R.id.textViewPdfOrderAt);
        final String textOrderAt = "注文日時：" + faxDoc.order_at;
        tvOrderAt.setText(textOrderAt);

        final TextView tvStoreAddress = (TextView)convertView.findViewById(R.id.textViewPdfStoreAddress);
        final String textStoreAddress = "＜注文住所＞" + faxDoc.store_address;
        tvStoreAddress.setText(textStoreAddress);

        final TextView tvDeliverAt = (TextView)convertView.findViewById(R.id.textViewPdfDeliverAt);
        final String textDeliverAt = "納品希望日" + faxDoc.deliver_at;
        tvDeliverAt.setText(textDeliverAt);

        // Return the completed view to render on screen
        return convertView;
    }
}
