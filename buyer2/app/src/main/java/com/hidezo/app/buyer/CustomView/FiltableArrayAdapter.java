package com.hidezo.app.buyer.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.Collection;
import java.util.List;

/**
 * Created by dezami on 2016/09/24.
 *
 */

public abstract class FiltableArrayAdapter<T> extends ArrayAdapter<T> {

    private ListFilter<T> _filter;

    public FiltableArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FiltableArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context,resource,textViewResourceId);
    }

    public FiltableArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public FiltableArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FiltableArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public FiltableArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public final Filter getFilter() {
        _filter = getFilterImpl();
        return _filter;
    }

    public boolean isFiltered() {
        return _filter != null;
    }

    @Override
    public final void add(T object) {
        if(_filter == null || _filter.add(object)) {
            super.add(object);
        }
    }

    @SuppressLint("NewApi")
    public final void addAll(Collection<? extends T> collection) {

        if(_filter == null) {
            if(Build.VERSION.SDK_INT >= 11) {
                super.addAll(collection);
            } else {
                for(T data : collection) {
                    super.add(data);
                }
            }

            return;
        }

        for(T data : collection) {
            if(_filter.add(data)) super.add(data);
        }
    }

    @SuppressLint("NewApi")
    public final void addAll(T... items) {
        if(_filter == null) {
            if(Build.VERSION.SDK_INT >= 11) {
                super.addAll(items);
            } else {
                for(T data : items) {
                    super.add(data);
                }
            }

            return;
        }

        for(T data : items) {
            if(_filter.add(data)) super.add(data);
        }

    }

    @Override
    public final void clear() {
        if(_filter != null) {
            _filter.dispose();
            _filter = null;
        }

        super.clear();
    }

    protected final void dataClear() {
        super.clear();
    }

    @Override
    public final void insert(T object, int index) {
        if(_filter == null || _filter.add(object, index)) {
            super.insert(object, index);
        }
    }

    @Override
    public final void remove(T object) {
        if(_filter != null) _filter.remove(object);
        super.remove(object);
    }

    public final ListFilter<T> getCurrentFilter() {
        return _filter;
    }

    public final void resetFilter() {
        if(_filter == null) {
            Log.w("FiltableArrayAdapter", "filter is not found.");
            return;
        }

        List<T> datas = _filter.getOriginalDatas();
        this.clear();

        for(T data : datas) super.add(data);
    }

    protected abstract ListFilter<T> getFilterImpl();
}