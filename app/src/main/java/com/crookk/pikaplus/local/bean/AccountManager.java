package com.crookk.pikaplus.local.bean;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.core.utils.MathUtils;
import com.crookk.pikaplus.local.exception.NoAccountException;
import com.crookk.pikaplus.local.model.db.Account;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class AccountManager {

    @Bean
    DatabaseManager mDatabaseManager;

    List<Account> mAccountList = new ArrayList<>();

    @AfterInject
    void afterInject() {
        mAccountList = mDatabaseManager.getEnabledAccountList();
    }

    public synchronized Account request(Double latitude, Double longitude) throws NoAccountException {

        if (mAccountList.size() == 0) {

            throw new NoAccountException();

        } else {

            Account usableAccount = null;
            float shortestDistance = Float.MAX_VALUE;

            for (Account account : mAccountList) {

                if (account.canWork()) {

                    if(account.getLastLocationLatitude() == 0 && account.getLastLocationLatitude() == 0) {
                        usableAccount = account;
                        break;
                    }

                    float distance = MathUtils.distanceTo(account.getLastLocationLatitude(), account.getLastLocationLongitude(), latitude, longitude);

                    LogUtils.d(this, "[%1$s] distance=[%2$f]", account.getUsername(), distance);

                    if (shortestDistance > distance) {
                        shortestDistance = distance;
                        usableAccount = account;
                    }
                }
            }

            return usableAccount;
        }
    }

    public void refresh() {
        mAccountList.clear();
        mAccountList = mDatabaseManager.getEnabledAccountList();
    }
}
