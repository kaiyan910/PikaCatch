package com.crookk.pikaplus.module.map.service;

import com.crookk.pikaplus.core.bean.RetrofitClient;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.IOException;

import retrofit2.Response;

@EBean(scope = EBean.Scope.Singleton)
public class MapService {

    @Bean
    RetrofitClient mClient;

    public SpawnResultWrapper getRawData(Long lastRequestTime) {

        try {

            Response<SpawnResultWrapper> response = mClient.getApi().getData(lastRequestTime).execute();

            if(response.code() == 200) {
                return response.body();
            }

        } catch (IOException e) {
            LogUtils.e(this, e);
        }

        return null;
    }

}
