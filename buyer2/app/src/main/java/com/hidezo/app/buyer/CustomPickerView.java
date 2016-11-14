package com.hidezo.app.buyer;

import android.content.Context;

import com.hidezo.app.buyer.CustomView.PickerView;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/25.
 *
 */

class CustomPickerView extends PickerView {

    private CustomPickerView _self;

    public ArrayList<String> dataList = new ArrayList<>();

    private String textSelected = "";
    public String getTextSelected() {
        return this.textSelected;
    }

    private int indexSelected = 0;
//    public int getIndexSelected() {
//        return this.indexSelected;
//    }

    public CustomPickerView(final Context context) {
        super(context);
        _self = this;
    }

    public CustomPickerView(final Context context, final ArrayList<String> list, final String textTarget) {
        super(context);
        _self = this;

        this.dataList = list;

        this.setData(list);

        int loop = 0;
        for (final String scale : dataList) {
            if (scale.equals(textTarget)) {
                _self.indexSelected = loop;
                break;
            }
            loop++;
        }
        this.setSelected(_self.indexSelected);

        this.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(final String text) {
                _self.textSelected = text;
            }
        });

    }

}
