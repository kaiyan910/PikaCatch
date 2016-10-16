package com.crookk.pikaplus.core.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.crookk.pikaplus.core.ui.holder.Wrapper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, V extends View> extends RecyclerView.Adapter<Wrapper<V>> {

    protected List<T> mData = new ArrayList<T>();

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public final Wrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Wrapper<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public void setData(List<T> data) {

        mData = new ArrayList<>(data);

        notifyDataSetChanged();
    }
}