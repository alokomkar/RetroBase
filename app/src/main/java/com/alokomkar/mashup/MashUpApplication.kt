package com.alokomkar.mashup

import android.app.Application
import android.content.Context
import com.alokomkar.mashup.base.CACHE_FILES_COUNT
import com.alokomkar.mashup.base.NetModule
import com.alokomkar.mashup.songs.SongsAPI
import com.danikula.videocache.HttpProxyCacheServer



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

    companion object {

        var songsAPI: SongsAPI?= null
        fun getSongsApI() : SongsAPI? {
            if( songsAPI == null ) songsAPI = NetModule(instance).songsAPI
            return songsAPI
        }

        lateinit var instance: MashUpApplication
            private set
    }
}