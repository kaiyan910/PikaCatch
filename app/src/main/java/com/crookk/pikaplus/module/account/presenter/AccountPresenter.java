package com.crookk.pikaplus.module.account.presenter;

import com.crookk.pikaplus.core.presenter.BasePresenter;
import com.crookk.pikaplus.core.ui.view.LCEView;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.db.Account;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class AccountPresenter implements BasePresenter<LCEView<Account>> {

    @Bean
    DatabaseManager mDatabaseManager;

    private LCEView<Account> mView;

    @Background
    public void fetchAccounts() {

        if (mView == null) return;

        LogUtils.d(this, "fetchAccounts()");
        mView.setData(mDatabaseManager.queryAccounts());
    }

    @Override
    public void attach(LCEView<Account> view) {
        mView = view;
    }
}
