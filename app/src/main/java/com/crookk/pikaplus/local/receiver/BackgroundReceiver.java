package com.crookk.pikaplus.local.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.Preference_;
import com.crookk.pikaplus.module.map.service.BackgroundService_;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EReceiver
public class BackgroundReceiver extends WakefulBroadcastReceiver {

    @Pref
    Preference_ preference;

    private Context context;

    @ReceiverAction(actions = {
            Intent.ACTION_BOOT_COMPLETED
    })
    void startOnBoot(Intent intent) {
        if(preference.background().get()) {
            Intent service = new Intent(context, BackgroundService_.class);
            startWakefulService(context, service);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // empty override in compile time
        this.context = context;
    }
}
