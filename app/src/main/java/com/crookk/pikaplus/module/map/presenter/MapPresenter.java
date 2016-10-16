package com.crookk.pikaplus.module.map.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.Preference;
import com.crookk.pikaplus.Preference_;
import com.crookk.pikaplus.core.presenter.BasePresenter;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.bean.FileManager;
import com.crookk.pikaplus.local.bean.PokemonManager;
import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.module.map.model.PokemonMarker;
import com.crookk.pikaplus.module.map.service.MapService;
import com.crookk.pikaplus.module.map.ui.view.MapView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EBean
public class MapPresenter implements BasePresenter<MapView> {

    @Bean
    MapService mMapService;
    @Bean
    DatabaseManager mDatabaseManager;
    @Bean
    PokemonManager mPokemonManager;
    @Bean
    FileManager mFileManager;

    @Pref
    Preference_ mPreference;

    private MapView mView;
    private Long mLastRequestTime = 0L;
    private List<Pokemon> mBlacklist = new ArrayList<>();

    @AfterInject
    void afterInject() {
        mLastRequestTime = mPreference.lastRequestTime().get();
        LogUtils.d(this, "Last Request Time=[%d]", mLastRequestTime);
    }

    @Override
    public void attach(MapView view) {
        mView = view;
        createBlacklist();
    }

    @Background
    public void fetchPokemonFromServer() {

        if (mView == null) throw new RuntimeException("no view is attached to MapPresenter");

        SpawnResultWrapper result = mMapService.getRawData(mLastRequestTime);

        if (result != null) {

            mLastRequestTime = result.getTime();
            mPreference.edit().lastRequestTime().put(mLastRequestTime).apply();
            List<Spawn> spawnList = Spawn.convert(result.getSpawnResultList());

            LogUtils.d(this, "Fetched Pokemon from server=[%d]", spawnList.size());

            if (spawnList.size() > 0) {
                mDatabaseManager.insert(spawnList);
            }

            mView.onPokemonFound(filter(spawnList), mLastRequestTime * 1000L, true);
        } else {
            mView.onPokemonFound(new ArrayList<Spawn>(), new Date().getTime(), true);
        }
    }

    @Background
    public void checkIV(PokemonMarker marker) {

        List<Spawn> spawnList = new ArrayList<>();

        spawnList.add(marker.getSpawn());

        mPokemonManager.checkIndividualValue(spawnList);
    }

    @Background
    public void saveSnapshot(Bitmap bitmap) {

        if (mView == null) throw new RuntimeException("no view is attached to MapPresenter");

        File file = mFileManager.saveBitmap(bitmap);

        if (file != null) {

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            mView.onSnapshotSaved(intent);
        }
    }

    @Background
    public void fetchPokemonFromDatabase() {

        if (mView == null) throw new RuntimeException("no view is attached to MapPresenter");

        List<Spawn> spawnList = mDatabaseManager.querySpawns();

        LogUtils.d(this, "Fetched Pokemon from database=[%d]", spawnList.size());

        mView.onPokemonFound(spawnList, new Date().getTime(), false);
    }

    public void refresh() {
        createBlacklist();
    }

    private List<Spawn> filter(List<Spawn> spawnList) {

        List<Spawn> result = new ArrayList<>();

        for(Spawn spawn : spawnList) {

            if(!mBlacklist.contains(spawn.getPokemon())) {
                result.add(spawn);
            }
        }

        return result;
    }

    private void createBlacklist() {

        mBlacklist.clear();
        mBlacklist = mDatabaseManager.queryBlacklistPokemonList();
    }
}
