package com.crookk.pikaplus;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.bean.Foreground;
import com.crookk.pikaplus.module.map.service.BackgroundService_;
import com.google.android.gms.maps.model.LatLngBounds;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EApplication
public class Pika extends MultiDexApplication implements Foreground.Listener {

    @Bean
    DatabaseManager mDataManager;
    @Bean
    OttoBus mOttoBus;
    @Pref
    Preference_ mPreference;

    LatLngBounds mMapBounds;

    @AfterInject
    void afterInject() {

        houseKeeping();

        Foreground.init(this);
        Foreground.get().addListener(this);
    }

    @Background
    void houseKeeping() {
        mDataManager.deleteExpired();
    }

    @Override
    public void onBecameForeground() {
        BackgroundService_.intent(this).stop();
    }

    @Override
    public void onBecameBackground() {
        if (mPreference.background().get()) {
            LogUtils.d(this, "Started background service");
            BackgroundService_.intent(this).start();
        }
    }


    public LatLngBounds getMapBounds() {
        return mMapBounds;
    }

    public void setMapBounds(LatLngBounds mMapBounds) {
        this.mMapBounds = mMapBounds;
    }
}
