package com.crookk.pikaplus.core.bean;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.local.API;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class RetrofitClient {

    private Retrofit mRetrofit;
    private API mApi;

    @StringRes(R.string.api)
    String mStringApi;

    public RetrofitClient() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mStringApi)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        mApi = mRetrofit.create(API.class);
    }

    public API getApi() {
        return mApi;
    }

    public void setApi(API api) {
        this.mApi = api;
    }
}
