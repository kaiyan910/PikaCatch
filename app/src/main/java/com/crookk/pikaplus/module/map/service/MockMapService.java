package com.crookk.pikaplus.module.map.service;

import com.crookk.pikaplus.local.model.api.SpawnResult;
import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@EBean
public class MockMapService implements MapService {

    public static final int BASE_MOCK_NUMBER = 10;
    public static final Long MOCK_EXPIRED_TIME = 90000L;

    @Override
    public SpawnResultWrapper getRawData(Long lastRequestTime, LatLngBounds bounds) {

        SpawnResultWrapper wrapper = new SpawnResultWrapper();

        wrapper.setTime(new Date().getTime());

        Random random = new Random();

        int mockNumber = random.nextInt(BASE_MOCK_NUMBER) + 1;

        List<SpawnResult> resultList = new ArrayList<>();

        for (int i=0; i<mockNumber; i++) {

            SpawnResult result = new SpawnResult();

            LatLng randomLocation = getRandomLocation(bounds);

            result.setExpireTime(new Date().getTime() + MOCK_EXPIRED_TIME);
            result.setLatitude(randomLocation.latitude);
            result.setLongitude(randomLocation.longitude);
            result.setPokemonId(random.nextInt(152));
            result.setSpawnId(random.nextLong());
            result.setSpawnType(1);

            resultList.add(result);
        }

        wrapper.setSpawnResultList(resultList);

        return wrapper;
    }

    private LatLng getRandomLocation(LatLngBounds bounds) {

        double lat_min   = bounds.southwest.latitude;
        double lat_range = bounds.northeast.latitude - lat_min;
        double lng_min   = bounds.southwest.longitude;
        double lng_range = bounds.northeast.longitude - lng_min;

        return new LatLng(lat_min + (Math.random() * lat_range), lng_min + (Math.random() * lng_range));
    }
}
