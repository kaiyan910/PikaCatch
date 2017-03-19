package com.crookk.pikaplus.module.map.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.Pika;
import com.crookk.pikaplus.Preference_;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.ui.activity.BaseActivity;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.core.utils.PermissionChecker;
import com.crookk.pikaplus.local.bean.FileManager;
import com.crookk.pikaplus.local.event.ConsoleLogEvent;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.local.ui.view.LogView;
import com.crookk.pikaplus.module.account.ui.activity.AccountActivity_;
import com.crookk.pikaplus.module.map.event.BroadcastCheckingEvent;
import com.crookk.pikaplus.module.map.event.PokemonExpiredEvent;
import com.crookk.pikaplus.module.map.event.UpdateSpawnEvent;
import com.crookk.pikaplus.module.map.model.PokemonMarker;
import com.crookk.pikaplus.module.map.model.PokemonMarker_;
import com.crookk.pikaplus.module.map.presenter.MapPresenter;
import com.crookk.pikaplus.module.map.ui.custom.MapWindowView;
import com.crookk.pikaplus.module.map.ui.custom.MapWindowView_;
import com.crookk.pikaplus.module.map.ui.view.MapView;
import com.crookk.pikaplus.module.pokemon.ui.activity.PokemonActivity_;
import com.entire.sammalik.samlocationandgeocoding.SamLocationRequestService;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.jraska.console.Console;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;

@EActivity(R.layout.activity_map)
public class MapActivity extends BaseActivity implements MapView,
        OnMapReadyCallback {

    public static final int REQ_FILTER = 12300;

    @App
    Pika mPika;

    @ViewById(R.id.pokeball)
    ImageView mViewPokeBall;
    @ViewById(R.id.multiple_actions)
    FloatingActionsMenu mViewActions;
    @ViewById(R.id.log_view)
    LogView mViewLog;

    @Bean
    MapPresenter mMapPresenter;
    @Pref
    Preference_ mPreference;
    @Bean
    FileManager mFileManager;

    @StringRes(R.string.map_iv_checking)
    String mStringIVChecking;
    @StringRes(R.string.map_service_on)
    String mStringServiceOn;
    @StringRes(R.string.map_service_off)
    String mStringServiceOff;

    @DimensionPixelSizeRes(R.dimen.logView_item_height)
    int mDimenLogHeight;

    private SamLocationRequestService mLocationService;
    private GoogleMap mGoogleMap;
    private Location mLocation;

    private Map<Marker, PokemonMarker> mMarkerMap = new HashMap<>();
    private Map<String, Spawn> mSpawnMap = new HashMap<>();

    // only one request to server is allowed
    private boolean mLastRequestFinished = true;
    // once the activity is down it change to false
    private boolean mRunning = true;
    // check if the map has initialized
    private boolean mInit = false;
    // check if pokemon is initialized from database
    private boolean mBaseLoad = false;
    // check if MapActivity is down because of activity change
    private boolean mInAppChange = false;

    private boolean mShowLog = false;

    private PokemonMarker mFocusedMarker;

    private Spawn mSpawnAlert;

    @Override
    protected void afterViews() {
        super.afterViews();
        onNewIntent(getIntent());
        mMapPresenter.attach(this);
        showPokeBall(true);
        obtainLocation();
        setupMap();

        mViewLog.setTranslationY(mDimenLogHeight);
    }

    @Override
    public void onNewIntent(Intent intent) {

        Bundle extra = intent.getExtras();

        LogUtils.d(this, "onNewIntent");
        if (extra != null && extra.containsKey(Constant.ALERT_SPAWN)) {
            LogUtils.d(this, "onNewIntent with ALERT_SPAWN");
            mSpawnAlert = extra.getParcelable(Constant.ALERT_SPAWN);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mInAppChange = false;
        mRunning = true;

        if (!PermissionChecker.check(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mInAppChange = true;
            checkPermission();
        } else {
            mFileManager.init();
        }

        LogUtils.d(this, "onResume");
        if (mInit) {

            search();
            broadcastMarkerCheck();

            if (mSpawnAlert != null) {
                LogUtils.d(this, "SpawnAlert location=[%1$f, %2$f]", mSpawnAlert.getLatitude(), mSpawnAlert.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mSpawnAlert.getLatitude(), mSpawnAlert.getLongitude()), 16));
                mSpawnAlert = null;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mRunning = false;
        if(mGoogleMap != null) {
            mPika.setMapBounds(mGoogleMap.getProjection().getVisibleRegion().latLngBounds);
        }
        if (!mInAppChange) cleanMap();
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        if (PermissionChecker.check(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //noinspection MissingPermission
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setBuildingsEnabled(false);
        mGoogleMap.setIndoorEnabled(false);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mFocusedMarker = null;
            }
        });
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                PokemonMarker pokemonMarker = mMarkerMap.get(marker);

                mFocusedMarker = pokemonMarker;

                MapWindowView window = MapWindowView_.build(MapActivity.this);
                window.bind(pokemonMarker);

                return window;
            }
        });

        if (mSpawnAlert != null) {
            LogUtils.d(this, "Alert spawn=[%s]", mSpawnAlert.getPokemon().getName(this));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mSpawnAlert.getLatitude(), mSpawnAlert.getLongitude()), 16));
            mSpawnAlert = null;
        } else if (mLocation != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 14));
        } else {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Constant.DEFAULT_LATITUDE, Constant.DEFAULT_LONGITUDE), 14));
        }

        mInit = true;

        search();
        broadcastMarkerCheck();

        showPokeBall(false);
    }

    @OnActivityResult(REQ_FILTER)
    public void onFilterResult(int responseCode, Intent data) {
        mMapPresenter.refresh();
        cleanMap();
        search();
    }

    @UiThread
    @Override
    public void onPokemonFound(List<Spawn> spawnList, Long lastUpdateTime, boolean closeLoad) {

        write(String.format(Locale.getDefault(), "Pokemon Found=[%d]", spawnList.size()));


        for (Spawn spawn : spawnList) {

            mSpawnMap.put(spawn.getId(), spawn);

            PokemonMarker marker = PokemonMarker_.getInstance_(this);
            marker.setSpawn(spawn);
            Marker gMarker = marker.createMarker(this, mGoogleMap);

            mMarkerMap.put(gMarker, marker);
        }

        if (closeLoad) {
            mLastRequestFinished = true;
            showPokeBall(false);
        }
    }

    @UiThread
    @Override
    public void onSnapshotSaved(Intent intent) {

        showPokeBall(false);
        startActivityForResult(intent, 0);
    }

    @UiThread(delay = 1000)
    void broadcastMarkerCheck() {

        if (mMarkerMap.size() > 0) {
            mOttoBus.post(new BroadcastCheckingEvent());
        }

        if (mRunning) broadcastMarkerCheck();
    }

    @UiThread(delay = 2000)
    void setupMap() {

        try {

            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map_container, mapFragment, null).commitAllowingStateLoss();
            mapFragment.getMapAsync(this);

        } catch (IllegalStateException e) {
            LogUtils.e(this, e);
        }
    }

    @Subscribe
    public void onSpawnUpdated(UpdateSpawnEvent event) {

        if (mSpawnMap.containsKey(event.getSpawn().getId())) {

            Spawn spawn = mSpawnMap.get(event.getSpawn().getId());

            spawn.setAttack(event.getSpawn().getAttack());
            spawn.setDefense(event.getSpawn().getDefense());
            spawn.setStamina(event.getSpawn().getStamina());
            spawn.setMove1(event.getSpawn().getMove1());
            spawn.setMove2(event.getSpawn().getMove2());

            mSpawnMap.put(spawn.getId(), spawn);
        }
    }

    @Subscribe
    public void onPokemonExpired(PokemonExpiredEvent event) {

        mMarkerMap.remove(event.getMarker().getMarker());
        mSpawnMap.remove(event.getMarker().getSpawn().getId());
        event.getMarker().getMarker().remove();
    }

    @Subscribe
    public void onMessageReceived(ConsoleLogEvent event) {
        write(event.getMessage());
    }

    @Click(R.id.log)
    void log() {

        mViewActions.collapse();
        if (!mShowLog) {
            mShowLog = true;
            mViewLog.animate().translationY(0).start();
        } else {
            mShowLog = false;
            mViewLog.animate().translationY(mDimenLogHeight).start();
        }
    }

    @Click(R.id.account)
    void account() {
        mViewActions.collapse();
        mInAppChange = true;
        AccountActivity_.intent(this).start();
    }

    @Click(R.id.check_iv)
    void checkIV() {
        mViewActions.collapse();
        if (mFocusedMarker != null) {
            mMapPresenter.checkIV(mFocusedMarker);
            Toast.makeText(this, mStringIVChecking, Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.share)
    void share() {

        mViewActions.collapse();
        showPokeBall(true);

        mGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                mMapPresenter.saveSnapshot(bitmap);
            }
        });
    }

    @Click(R.id.service)
    void service() {

        mViewActions.collapse();
        boolean service = mPreference.background().get();

        if (service) {
            Toast.makeText(this, mStringServiceOff, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, mStringServiceOn, Toast.LENGTH_SHORT).show();
        }

        mPreference.edit().background().put(!service).apply();
    }

    @Click(R.id.search)
    void search() {
        mViewActions.collapse();

        if (!mBaseLoad) {
            mMapPresenter.fetchPokemonFromDatabase();
            mBaseLoad = true;
        }

        if (mLastRequestFinished && mRunning) {
            showPokeBall(true);
            mLastRequestFinished = false;
            mMapPresenter.fetchPokemonFromServer(mGoogleMap.getProjection().getVisibleRegion().latLngBounds);
        }
    }

    @Click(R.id.filter)
    void filter() {
        mViewActions.collapse();
        mInAppChange = true;
        PokemonActivity_.intent(this).startForResult(REQ_FILTER);
    }

    private void obtainLocation() {

        mLocationService = new SamLocationRequestService(this);
        mLocationService.executeService(new SamLocationRequestService.SamLocationListener() {
            @Override
            public void onLocationUpdate(Location location, Address address) {
                mLocation = location;
                mLocationService.stopLocationUpdates();
            }
        });
    }

    private void showPokeBall(boolean show) {

        if (show && mViewPokeBall.getVisibility() == View.GONE) {
            mViewPokeBall.setVisibility(View.VISIBLE);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
            mViewPokeBall.startAnimation(rotation);
        } else {
            mViewPokeBall.clearAnimation();
            mViewPokeBall.setVisibility(View.GONE);
        }
    }

    private void checkPermission() {

        PermissionManager.with(this)
                .permission(
                        PermissionEnum.ACCESS_COARSE_LOCATION, PermissionEnum.ACCESS_FINE_LOCATION,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE, PermissionEnum.READ_EXTERNAL_STORAGE)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(UserResponse response) {
                        finish();
                    }
                })
                .callback(new FullCallback() {
                    @Override
                    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied,
                                       ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {

                        if (permissionsGranted.contains(PermissionEnum.WRITE_EXTERNAL_STORAGE)) {
                            mFileManager.init();
                        }
                    }
                })
                .ask();
    }

    private void cleanMap() {

        LogUtils.d(this, "cleanMap()");

        mMarkerMap.clear();
        mSpawnMap.clear();
        mGoogleMap.clear();

        mBaseLoad = false;
    }

    private void write(String message) {
        mViewLog.add(message);
    }
}
