package com.alokomkar.mashup

import android.app.Application
import android.content.Context
import com.alokomkar.mashup.base.CACHE_FILES_COUNT
import com.alokomkar.mashup.base.NetModule
import com.alokomkar.mashup.songs.SongsAPI
import com.danikula.videocache.HttpProxyCacheServer
import android.net.ConnectivityManager
import com.alokomkar.mashup.download.DownloadFileAPI


/**
 * Created by Alok Omkar on 2017-12-16.
 */
class MashUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private var proxy: HttpProxyCacheServer? = null

    fun getProxy(): HttpProxyCacheServer {
        if( proxy == null ) {
            proxy = newProxy()
        }
        return proxy!!
    }

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(CACHE_FILES_COUNT)
                .build()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {

        var songsAPI: SongsAPI?= null
        fun getSongsApI() : SongsAPI? {
            if( songsAPI == null ) songsAPI = NetModule(instance).songsAPI
            return songsAPI
        }

        var downloadFileAPI: DownloadFileAPI?= null
        fun getDownloadFileApi() : DownloadFileAPI? {
            if( downloadFileAPI == null ) downloadFileAPI = NetModule(instance).downloadFileAPI
            return downloadFileAPI
        }

        lateinit var instance: MashUpApplication
            private set
    }
}