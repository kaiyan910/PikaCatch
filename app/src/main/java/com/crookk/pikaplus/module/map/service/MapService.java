package com.crookk.pikaplus.module.map.service;

import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Kaiyan on 19/3/2017.
 */

public interface MapService {
    SpawnResultWrapper getRawData(Long lastRequestTime, LatLngBounds bounds);
}
