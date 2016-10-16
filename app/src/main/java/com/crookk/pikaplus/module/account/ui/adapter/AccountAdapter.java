package com.crookk.pikaplus.module.account.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.crookk.pikaplus.core.ui.adapter.BaseAdapter;
import com.crookk.pikaplus.core.ui.holder.Wrapper;
import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.module.account.ui.holder.AccountListHolder;
import com.crookk.pikaplus.module.account.ui.holder.AccountListHolder_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class AccountAdapter extends BaseAdapter<Account, AccountListHolder> {

    @RootContext
    Context mContext;

    @Override
    protected AccountListHolder onCreateItemView(ViewGroup parent, int viewType) {
        return AccountListHolder_.build(mContext);
    }

    @Override
    public void onBindViewHolder(Wrapper<AccountListHolder> holder, int position) {
        AccountListHolder item = holder.getView();
        item.bind(mData.get(position));
    }
}
