package com.crookk.pikaplus.module.map.ui.view;

import android.content.Intent;

import com.crookk.pikaplus.core.ui.view.BaseView;
import com.crookk.pikaplus.local.model.db.Spawn;

import java.util.Date;
import java.util.List;

public interface MapView extends BaseView {
    void onPokemonFound(List<Spawn> spawnList, Long lastUpdateTime, boolean closeLoad);
    void onSnapshotSaved(Intent intent);
}
