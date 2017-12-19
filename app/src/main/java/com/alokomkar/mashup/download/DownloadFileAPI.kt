package com.alokomkar.mashup.download

import io.reactivex.Observable

import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming



/**
 * Created by Alok on 19/12/17.
 */
interface DownloadFileAPI {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<Response<ResponseBody>>

}