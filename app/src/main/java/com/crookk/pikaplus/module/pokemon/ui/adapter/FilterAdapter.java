package com.crookk.pikaplus.module.pokemon.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.crookk.pikaplus.core.ui.adapter.BaseAdapter;
import com.crookk.pikaplus.core.ui.holder.Wrapper;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.module.pokemon.listener.OnFilterCheckChangeListener;
import com.crookk.pikaplus.module.pokemon.ui.holder.FilterHolder;
import com.crookk.pikaplus.module.pokemon.ui.holder.FilterHolder_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class FilterAdapter extends BaseAdapter<Pokemon, FilterHolder> {

    @RootContext
    Context context;

    private OnFilterCheckChangeListener onFilterCheckChangeListener;

    @Override
    protected FilterHolder onCreateItemView(ViewGroup parent, int viewType) {
        return FilterHolder_.build(context);
    }

    @Override
    public void onBindViewHolder(Wrapper<FilterHolder> holder, int position) {
        FilterHolder item = holder.getView();
        item.bind(mData.get(position), onFilterCheckChangeListener);
    }

    public void setOnFilterCheckChangeListener(OnFilterCheckChangeListener onFilterCheckChangeListener) {
        this.onFilterCheckChangeListener = onFilterCheckChangeListener;
    }
}
