package com.alokomkar.mashup.songs;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Alok Omkar on 2017-12-16.
 */

public interface SongsAPI {

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
    @GET("studio")
    Observable<ArrayList<Songs>> getSongs();
}
