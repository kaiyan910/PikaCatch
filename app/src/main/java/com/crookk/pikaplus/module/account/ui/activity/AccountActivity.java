package com.crookk.pikaplus.module.account.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.ui.activity.ListActivity;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.AccountManager;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.module.account.event.RefreshAccountEvent;
import com.crookk.pikaplus.module.account.presenter.AccountPresenter;
import com.crookk.pikaplus.module.account.ui.adapter.AccountAdapter;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_account)
@OptionsMenu(R.menu.menu_account)
public class AccountActivity extends ListActivity<Account> {

    @Bean
    AccountAdapter mAdapter;
    @Bean
    AccountPresenter mPresenter;
    @Bean
    DatabaseManager mDatabaseManager;
    @Bean
    AccountManager mAccountManager;

    @StringRes(R.string.account_title)
    String mStringTitle;
    @ColorRes(R.color.colorPrimaryDark)
    int mColorTheme;

    @Override
    protected void afterViews() {

        super.afterViews();

        setupBackNavigation();
        mViewList.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.attach(this);
        LogUtils.d(this, "afterViews()");
        mPresenter.fetchAccounts();
    }

    @Override
    public void finish() {
        LogUtils.d(this, "finish()");
        mAccountManager.refresh();
        super.finish();
    }

    @OptionsItem(R.id.menu_account_add)
    void add() {

        new MaterialDialog.Builder(this)
                .title(mStringTitle)
                .titleColor(mColorTheme)
                .canceledOnTouchOutside(false)
                .customView(R.layout.dialog_account, true)
                .positiveText(R.string.account_dialog_positive)
                .negativeText(R.string.account_dialog_negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        View view = dialog.getCustomView();

                        EditText username = (EditText) view.findViewById(R.id.username);
                        EditText password = (EditText) view.findViewById(R.id.password);

                        Account account = new Account();
                        account.setUsername(username.getText().toString());
                        account.setPassword(password.getText().toString());
                        account.setType(Constant.ACCOUNT_TYPE_PTC);
                        account.setEnabled(true);

                        mDatabaseManager.createAccount(account);

                        showLoading();
                        mPresenter.fetchAccounts();
                    }
                })
                .show();
    }

    @Override
    protected String getActivityTitle() {
        return mStringTitle;
    }

    @Override
    protected void onDataDownloaded() {

        LogUtils.d(this, "onDataDownloaded()");
        mAdapter.setData(mData);
        mViewList.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        LogUtils.d(this, "onRefresh()");
        mPresenter.fetchAccounts();
    }

    @Subscribe
    public void onRefreshRequest(RefreshAccountEvent event) {
        showLoading();
        mPresenter.fetchAccounts();
    }
}
