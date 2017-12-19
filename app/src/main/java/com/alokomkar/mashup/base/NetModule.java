package com.alokomkar.mashup.base;

import android.app.Application;

import com.alokomkar.mashup.songs.SongsAPI;
import com.google.gson.Gson;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.GsonBuilder;

public class NetModule {

    private Application application;

    private Cache cache;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;


    // Constructor needs one parameter to instantiate.
    public NetModule(Application application) {
        this.application = application;
    }

    private Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    private OkHttpClient provideOkHttpClient(Cache cache) {
        // create an instance of OkLogInterceptor using a builder()
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    private Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        String BASE_URL = "http://starlord.hackerearth.com/";
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    private Cache getCache() {
        if( cache == null ) {
            cache = provideOkHttpCache(application);
        }
        return cache;
    }

    private OkHttpClient getOkHttpClient() {
        if( okHttpClient == null ) {
            okHttpClient = provideOkHttpClient(getCache());
        }
        return okHttpClient;
    }

    public Retrofit getRetrofit() {
        if( retrofit == null ) {
            retrofit = provideRetrofit(getOkHttpClient());
        }
        return retrofit;
    }

    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public SongsAPI getSongsAPI() {
        return getRetrofit().create(SongsAPI.class);
    }


}
