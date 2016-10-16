package com.crookk.pikaplus.module.map.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.Pika;
import com.crookk.pikaplus.Preference_;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.local.receiver.BackgroundReceiver_;
import com.crookk.pikaplus.local.utils.ResourcesLoader;
import com.crookk.pikaplus.module.map.ui.activity.MapActivity_;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;


@EService
public class BackgroundService extends Service {

    public static final int SLEEP = 30000;

    @App
    Pika mPika;

    @Bean
    DatabaseManager mDatabaseManager;

    @Bean
    MapService mMapService;

    @SystemService
    Vibrator mVibrator;
    @SystemService
    ConnectivityManager mConnectivityManager;

    @Pref
    Preference_ mPreference;

    @StringRes(R.string.service_found)
    String mStringServiceFound;
    @StringRes(R.string.service_location)
    String mStringServiceLocation;

    private Ringtone mRingtone;
    private boolean run = true;
    private Long mLastRequestTime;
    private LatLngBounds mBounds;
    private List<Integer> mTrackingList = new ArrayList<>();

    @AfterInject
    void afterInject() {
        mLastRequestTime = mPreference.lastRequestTime().get();
        mBounds = mPika.getMapBounds();
        mTrackingList = mDatabaseManager.getTrackingList();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        request();

        if (intent != null) BackgroundReceiver_.completeWakefulIntent(intent);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        run = false;
    }

    @Background
    void request() {

        if (!run) return;

        try {

            if (hasNetwork()) {

                SpawnResultWrapper result = mMapService.getRawData(mLastRequestTime);
                mLastRequestTime = result.getTime();
                mPreference.edit().lastRequestTime().put(result.getTime()).apply();

                LogUtils.d(this, "Background found=[%d]", result.getSpawnResultList().size());

                if (result.getSpawnResultList().size() > 0) {

                    List<Spawn> spawnList = Spawn.convert(result.getSpawnResultList());
                    mDatabaseManager.insert(spawnList);

                    for (Spawn spawn : spawnList) {

                        if(mTrackingList.contains(spawn.getPokemon().getId()) && withinBounds(spawn)) {

                            LogUtils.d(this, "Notification name=[%1$s] location=[%2$f,%3$f]",
                                    spawn.getPokemon().getName(getApplicationContext()),
                                    spawn.getLatitude(),
                                    spawn.getLongitude());

                            createNotification(spawn);
                        }
                    }
                }
            }

            Thread.sleep(SLEEP);

            request();

        } catch (InterruptedException e) {
            LogUtils.e(this, e);
        }
    }

    private boolean withinBounds(Spawn spawn) {

        return mBounds.contains(new LatLng(spawn.getLatitude(), spawn.getLongitude()));
    }

    private boolean hasNetwork() {

        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private void createNotification(Spawn spawn) {

        Intent intent = new Intent(this, MapActivity_.class);

        intent.putExtra(Constant.ALERT_SPAWN, spawn);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int iconId = ResourcesLoader.getDrawableResources(getApplicationContext(), spawn.getPokemon().getId());


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(iconId)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), iconId))
                .setContentTitle(String.format(mStringServiceFound, spawn.getPokemon().getName(getApplicationContext()), spawn.getTimeLeft(getApplicationContext())))
                .setContentText(String.format(mStringServiceLocation, spawn.getLatitude(), spawn.getLongitude()));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(spawn.getPokemon().getId(), mBuilder.build());

        playNotificationSound();

        mVibrator.vibrate(500);
    }

    private void playNotificationSound() {

        try {

            if (!mRingtone.isPlaying()) {
                mRingtone.play();
            }

        } catch (Exception e) {

            LogUtils.e(this, e);
        }
    }
}
