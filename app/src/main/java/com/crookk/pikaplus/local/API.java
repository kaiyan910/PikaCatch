package com.crookk.pikaplus.local;

import com.crookk.pikaplus.local.model.api.SpawnResult;
import com.crookk.pikaplus.local.model.api.SpawnResultWrapper;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("mobile")
    Call<SpawnResultWrapper> getData(@Query("data_from") Long lastRequestTime);
}
