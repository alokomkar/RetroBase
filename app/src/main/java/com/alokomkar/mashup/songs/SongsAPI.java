package com.alokomkar.mashup.songs;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Alok Omkar on 2017-12-16.
 */

public interface SongsAPI {

    @GET("studio")
    Observable<ArrayList<Songs>> getSongs();
}
