package com.crookk.pikaplus.module.account.ui.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.module.account.event.RefreshAccountEvent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EViewGroup(R.layout.holder_account)
public class AccountListHolder extends RelativeLayout implements View.OnLongClickListener {

    @ViewById(R.id.username)
    TextView mViewUsername;
    @ViewById(R.id.password)
    TextView mViewPassword;
    @ViewById(R.id.enable)
    SwitchCompat mViewEnable;

    @Bean
    DatabaseManager mDatabaseManager;
    @Bean
    OttoBus mOttoBus;

    @StringRes(R.string.account_holder_delete_message)
    String mStringDeleteMessage;

    private Account mAccount;

    public AccountListHolder(Context context) {
        super(context);
    }

    public void bind(Account account) {
        mAccount = account;

        mViewUsername.setText(mAccount.getUsername());
        mViewPassword.setText(convertPassword(mAccount.getPassword()));
        mViewEnable.setChecked(account.isEnabled());

        setOnLongClickListener(this);
    }

    @CheckedChange(R.id.enable)
    void enableChanged(boolean enabled) {

        mAccount.setEnabled(enabled);
        mDatabaseManager.updateAccount(mAccount);
    }

    private String convertPassword(String password) {

        String output = "";

        for(int i=0; i<password.length(); i++) {
            output += "*";
        }

        return output;
    }

    @Override
    public boolean onLongClick(View v) {


        new MaterialDialog.Builder(getContext())
                .content(String.format(mStringDeleteMessage, mAccount.getUsername()))
                .positiveText(R.string.account_holder_delete_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        mDatabaseManager.deleteAccount(mAccount);
                        mOttoBus.post(new RefreshAccountEvent());
                    }
                })
                .negativeText(R.string.account_holder_delete_no)
                .show();

        return true;
    }
}
