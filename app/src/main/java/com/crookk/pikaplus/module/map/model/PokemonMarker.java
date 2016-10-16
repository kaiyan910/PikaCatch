package com.crookk.pikaplus.module.map.model;

import android.content.Context;

import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.local.utils.ResourcesLoader;
import com.crookk.pikaplus.module.map.event.BroadcastCheckingEvent;
import com.crookk.pikaplus.module.map.event.PokemonExpiredEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class PokemonMarker {

    @RootContext
    Context context;

    @Bean
    OttoBus mOttoBus;

    private Spawn spawn;
    private Marker marker;

    @AfterInject
    void afterInject() {
        mOttoBus.register(this);
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public void setSpawn(Spawn spawn) {
        this.spawn = spawn;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Subscribe
    public void onBroadcastReceived(BroadcastCheckingEvent event) {

        if (spawn.isExpired()) {
            LogUtils.d(this, "%s is expired at [%2$f, %3$f]", spawn.getPokemon().getName(context), spawn.getLatitude(), spawn.getLongitude());
            mOttoBus.post(new PokemonExpiredEvent(this));
            mOttoBus.unregister(this);
        }
    }

    public Marker createMarker(Context context, GoogleMap map) {

        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(spawn.getLatitude(), spawn.getLongitude()));
        options.title(spawn.getPokemon().getName(context));
        options.icon(BitmapDescriptorFactory.fromResource(ResourcesLoader.getDrawableResources(context, spawn.getPokemon().getId())));
        options.infoWindowAnchor(0.5F, 0.3F);
        options.anchor(0.5F, 0.5F);

        marker = map.addMarker(options);

        return marker;
    }
}
